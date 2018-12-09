package com.nis.web.controller.configuration;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.util.DateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.beust.jcommander.internal.Maps;
import com.nis.domain.FunctionServiceDict;
import com.nis.domain.Page;
import com.nis.domain.SysDataDictionaryItem;
import com.nis.domain.SysDataDictionaryName;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.maat.ToMaatResult;
import com.nis.exceptions.MaatConvertException;
import com.nis.util.CacheUtils;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Constants;
import com.nis.util.DateUtils;
import com.nis.util.DictUtils;
import com.nis.util.StringUtil;
import com.nis.web.controller.BaseController;
import com.nis.web.service.BaseService;

/**
 * 配置全量同步
 * @author zhangwei
 *
 */
@Controller
@RequestMapping("${adminPath}/config/synchronization")
public class ConfigSynchronizationController  extends BaseController {

	@RequestMapping(value = {"configSyncInfo"})
	public String configSyncInfo(Model model){
		return "/sys/configSyncInfo";
	}
	/**
	 * @param cmd 0为取消同步，1为同步
	 * 下发同步命令
	 */
	@RequestMapping(value = {"configSync"})
	public String configSync(Model model,Integer cmd,HttpServletRequest request,HttpServletResponse response){
		Map<String,Integer> map = new HashMap();
		map.put("syncStatus", cmd);
		JSONObject result = ConfigServiceUtil.configSyncCmd(BaseService.gsonToJson(map));
		if(result.get("status").equals(201)){//响应成功
			if(cmd.equals(1)){//同步
				SysDataDictionaryName sysDictName = new SysDataDictionaryName();
				sysDictName.setMark("currrent_sync_status");
				List<SysDataDictionaryName> dictList = dictService.findDicByName(sysDictName);
				if(!StringUtil.isEmpty(dictList)){
					SysDataDictionaryName dict = dictList.get(0);
					List<SysDataDictionaryItem> itemList = dict.getDictItemList();
					if(!StringUtil.isEmpty(itemList)){
						SysDataDictionaryItem item =  itemList.get(0);
						item.setDictionaryId(dict.getId().intValue());
						item.setItemValue("0");//0为开始同步
						item.setItemDesc("最近一次同步时间："+DateUtils.formatDateTime(new Date()));//记录本次同步时间
						dictService.updateDictItem(item);
						//删除字典缓存
						CacheUtils.remove(Constants.CACHE_DICT_MAP);
					}
				}
				send(model,request,response);
			}else if(cmd.equals(0)){//取消
				
			}
		}
		return "/sys/configSyncInfo";
	}
	
	/**
	 * 下发同步配置
	 */
	@RequestMapping(value = {"send"})
	public void send(Model model,HttpServletRequest request,HttpServletResponse response){
		int status = -2;
		try {
			configSynchronizationService.send(request,response,null);
			status = 1;
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch(MaatConvertException e){
			e.printStackTrace();
			status = -1;
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(status!=1){
				SysDataDictionaryName sysDictName = new SysDataDictionaryName();
				sysDictName.setMark("currrent_sync_status");
				List<SysDataDictionaryName> dictList = dictService.findDicByName(sysDictName);
				if(!StringUtil.isEmpty(dictList)){
					SysDataDictionaryName dict = dictList.get(0);
					List<SysDataDictionaryItem> itemList = dict.getDictItemList();
					if(!StringUtil.isEmpty(itemList)){
						SysDataDictionaryItem item =  itemList.get(0);
						item.setDictionaryId(dict.getId().intValue());
						item.setItemValue(String.valueOf(status));//-2界面组织数据失败，-1服务端返回的失败
						dictService.updateDictItem(item);
						//删除字典缓存
						CacheUtils.remove(Constants.CACHE_DICT_MAP);
					}
				}
				Map<String,Integer> map = new HashMap();
				map.put("syncStatus", 0);
				JSONObject result = ConfigServiceUtil.configSyncCmd(BaseService.gsonToJson(map));//取消同步
			}else{
				boolean isFinished = true;
				while(isFinished){
					JSONObject result = ConfigServiceUtil.configSyncStatus();//获取同步状态
					if(result.get("status").equals(200)){
						List<Map<String,Object>> list = result.getJSONArray("data");
						if(!StringUtil.isEmpty(list)){
							Map<String,Object> map = list.get(0);
							String serverStatus = map.get("status").toString();
							if(serverStatus.equals("-1")||serverStatus.equals("3")){
								isFinished = false;
								SysDataDictionaryName sysDictName = new SysDataDictionaryName();
								sysDictName.setMark("currrent_sync_status");
								List<SysDataDictionaryName> dictList = dictService.findDicByName(sysDictName);
								if(!StringUtil.isEmpty(dictList)){
									SysDataDictionaryName dict = dictList.get(0);
									List<SysDataDictionaryItem> itemList =dict.getDictItemList();
									if(!StringUtil.isEmpty(itemList)){
										SysDataDictionaryItem item =  itemList.get(0);
										if(!serverStatus.equals(item.getItemValue())){//如果状态不同，则更新状态
											item.setDictionaryId(dict.getId().intValue());
											item.setItemValue(serverStatus);
											dictService.updateDictItem(item);
											//删除字典缓存
											CacheUtils.remove(Constants.CACHE_DICT_MAP);
										}
										
									}
								}
							}
							
						}
					}
					try{
			            Thread.sleep(60000);
			        } catch (InterruptedException e) {
			            e.printStackTrace(); 
			        }
				}
				
			}
		}
	}
	/**
	 * 同步状态获取
	 */
	@RequestMapping(value = {"getStatus"})
	@ResponseBody
	public Map<String,Object> getStatus(Model model){
		String currentStatus = DictUtils.getDictLabel("currrent_sync_status", "status","0");
		String successStatus = DictUtils.getDictCode("config_sync_status", "finish","3");
		String serverFailStatus = DictUtils.getDictCode("config_sync_status", "serverFail","-1");
		String failStatus = DictUtils.getDictCode("config_sync_status", "fail","-2");
		Map<String,Object> map = Maps.newHashMap();
		if(!currentStatus.equals(successStatus) && !currentStatus.equals(failStatus) && !currentStatus.equals(serverFailStatus)){
			JSONObject result = ConfigServiceUtil.configSyncStatus();//获取同步状态
			if(result.get("status").equals(200)){
				List<Map<String,Object>> list = result.getJSONArray("data");
				if(!StringUtil.isEmpty(list)){
					map = list.get(0);
					String status = map.get("status").toString();
					SysDataDictionaryName sysDictName = new SysDataDictionaryName();
					sysDictName.setMark("currrent_sync_status");
					List<SysDataDictionaryName> dictList = dictService.findDicByName(sysDictName);
					if(!StringUtil.isEmpty(dictList)){
						SysDataDictionaryName dict = dictList.get(0);
						List<SysDataDictionaryItem> itemList =dict.getDictItemList();
						if(!StringUtil.isEmpty(itemList)){
							SysDataDictionaryItem item =  itemList.get(0);
							if(!status.equals(item.getItemValue())){//如果状态不同，则更新状态
								item.setDictionaryId(dict.getId().intValue());
								item.setItemValue(status);
								dictService.updateDictItem(item);
								//删除字典缓存
								CacheUtils.remove(Constants.CACHE_DICT_MAP);
							}
							
						}
					}
				}
			}
		}else{
			map.put("status",currentStatus);
		}
		return map;
	}
}
