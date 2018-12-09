package com.nis.web.service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Maps;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.googlecode.ipv6.IPv6Address;
import com.googlecode.ipv6.IPv6Network;
import com.nis.domain.FunctionRegionDict;
import com.nis.domain.FunctionServiceDict;
import com.nis.domain.SysRole;
import com.nis.domain.SysUser;
import com.nis.domain.basics.AsnIpCfg;
import com.nis.domain.callback.InlineIp;
import com.nis.domain.callback.NtcDnsResStrategy;
import com.nis.domain.callback.ProxyObjKeyring;
import com.nis.domain.callback.ProxyObjTrustedCa;
import com.nis.domain.configuration.AppDomainCfg;
import com.nis.domain.configuration.AppHttpCfg;
import com.nis.domain.configuration.AppIpCfg;
import com.nis.domain.configuration.AppPolicyCfg;
import com.nis.domain.configuration.AreaBean;
import com.nis.domain.configuration.AreaIpCfg;
import com.nis.domain.configuration.BaseCfg;
import com.nis.domain.configuration.BaseIpCfg;
import com.nis.domain.configuration.BaseStringCfg;
import com.nis.domain.configuration.CfgIndexInfo;
import com.nis.domain.configuration.ComplexkeywordCfg;
import com.nis.domain.configuration.DdosIpCfg;
import com.nis.domain.configuration.DnsIpCfg;
import com.nis.domain.configuration.DnsResStrategy;
import com.nis.domain.configuration.FileDigestCfg;
import com.nis.domain.configuration.IpReusePolicyCfg;
import com.nis.domain.configuration.PxyObjKeyring;
import com.nis.domain.configuration.PxyObjTrustedCaCert;
import com.nis.domain.configuration.PxyObjTrustedCaCrl;
import com.nis.domain.maat.GroupReuseAddBean;
import com.nis.domain.maat.GroupReuseCfg;
import com.nis.domain.maat.MaatCfg;
import com.nis.domain.maat.MaatCfg.DigestCfg;
import com.nis.domain.maat.MaatCfg.GroupCfg;
import com.nis.domain.maat.MaatCfg.IpCfg;
import com.nis.domain.maat.MaatCfg.NumBoundaryCfg;
import com.nis.domain.maat.MaatCfg.StringCfg;
import com.nis.domain.maat.ToMaatBean;
import com.nis.domain.maat.ToMaatResult;
import com.nis.domain.specific.ConfigGroupInfo;
import com.nis.domain.specific.SpecificServiceCfg;
import com.nis.util.ConfigServiceUtil;
import com.nis.util.Configurations;
import com.nis.util.Constants;
import com.nis.util.IpUtil;
import com.nis.util.Reflections;
import com.nis.util.StringUtil;
import com.nis.util.StringUtils;
import com.nis.web.dao.basics.AsnIpCfgDao;
import com.nis.web.dao.configuration.AppCfgDao;
import com.nis.web.dao.configuration.DdosCfgDao;
import com.nis.web.dao.configuration.DnsIpCfgDao;
import com.nis.web.dao.configuration.DnsResStrategyDao;
import com.nis.web.dao.configuration.IpCfgDao;
import com.nis.web.dao.configuration.StringCfgDao;
import com.nis.web.dao.specific.ConfigGroupInfoDao;
import com.nis.web.dao.specific.SpecificServiceCfgDao;
import com.nis.web.security.UserUtils;
import com.nis.web.service.basics.SysDictInfoService;

/**
 * Service基类
 * 
 * @author ThinkGem
 * @version 2014-05-16
 */
public abstract class BaseService {

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 数据范围过滤
	 * 
	 * @param user
	 *            当前用户对象，通过“entity.getCurrentUser()”获取
	 * @param officeAlias
	 *            机构表别名，多个用“,”逗号隔开。
	 * @param userAlias
	 *            用户表别名，多个用“,”逗号隔开，传递空，忽略此参数
	 * @return 标准连接条件对象
	 */
	public static String dataScopeFilter(SysUser user, String officeAlias, String userAlias) {

		StringBuilder sqlString = new StringBuilder();

		// 进行权限过滤，多个角色权限范围之间为或者关系。
		List<Integer> dataScope = Lists.newArrayList();

		if (StringUtils.isBlank(user.getLoginId())) {
			return "";
		}

		// 超级管理员，跳过权限过滤
		if (user.isAdmin()) {
			boolean isDataScopeAll = isContainsDataScopeAll(user.getUserRoleList());

			for (SysRole r : user.getUserRoleList()) {
				for (String oa : StringUtils.split(officeAlias, ",")) {
					if (!dataScope.contains(r.getDataScope()) && StringUtils.isNotBlank(oa)) {

						sqlString.append(createScopeSql(r.getDataScope(), oa, user, null));
						dataScope.add(r.getDataScope());
					}
				}
			}
			// 如果没有全部数据权限，并设置了用户别名，则当前权限为本人；如果未设置别名，当前无权限为已植入权限
			if (!isDataScopeAll) {
				if (StringUtils.isNotBlank(userAlias)) {
					for (String ua : StringUtils.split(userAlias, ",")) {
						sqlString.append(" OR " + ua + ".id = '" + user.getId() + "'");
					}
				} else {
					for (String oa : StringUtils.split(officeAlias, ",")) {
						// sqlString.append(" OR " + oa + ".id = " + user.getOffice().getId());
						sqlString.append(" OR " + oa + ".id IS NULL");
					}
				}
			} else {
				// 如果包含全部权限，则去掉之前添加的所有条件，并跳出循环。
				sqlString = new StringBuilder();
			}
		}
		if (StringUtils.isNotBlank(sqlString.toString())) {
			return " AND (" + sqlString.substring(4) + ")";
		}
		return "";
	}

	/**
	 * 数据范围过滤
	 * 
	 * @param user
	 *            当前用户对象，通过“entity.getCurrentUser()”获取
	 * @param configAlias
	 *            配置表别名，多个用","逗号隔开，传递空，忽略此参数
	 * @return 标准连接条件对象
	 */
	public static String configScopeFilter(SysUser user, String configAlias) {

		StringBuilder sqlString = new StringBuilder();

		// 进行权限过滤，多个角色权限范围之间为或者关系。
		List<Integer> dataScope = Lists.newArrayList();

		if (StringUtils.isBlank(user.getLoginId())) {
			return "";
		}

		// 超级管理员，跳过权限过滤
		if (!user.isAdmin()) {
			boolean isDataScopeAll = isContainsDataScopeAll(user.getUserRoleList());
			// 如果没有全部数据权限
			if (!isDataScopeAll) {
				for (SysRole r : user.getUserRoleList()) {
					for (String c : StringUtils.split(configAlias, ",")) {
						if (!dataScope.contains(r.getDataScope()) && StringUtils.isNotBlank(c)) {
							sqlString.append(createScopeSql(r.getDataScope(), "", user, c));
							dataScope.add(r.getDataScope());
						}
					}
				}

			} else {
				// 如果包含全部权限，则去掉之前添加的所有条件但增加配置审核取消以及删除的配置，并跳出循环。
				sqlString = new StringBuilder();
				sqlString.append(" OR " + configAlias + ".is_audit !=3");
			}
		}
		if (StringUtils.isNotBlank(sqlString.toString())) {
			return " AND (" + sqlString.substring(4) + ")";
		}
		return "";
	}

	/**
	 * 测试数据是否包含全集
	 * 
	 * @return
	 */
	private static boolean isContainsDataScopeAll(List<SysRole> roleList) {
		boolean isDataScopeAll = false;

		for (SysRole role : roleList) {
			if (SysRole.DATA_SCOPE_ALL.equals(role.getDataScope())) {
				isDataScopeAll = true;
				break;
			}
		}

		return isDataScopeAll;

	}

	/**
	 * 过滤机构信息
	 * 
	 * @param dataScope
	 *            数据范围（1：所有数据；2：所在公司及以下数据；3：所在公司数据；
	 *            4：所在部门及以下数据；5：所在部门数据；6：所在单位及以下数据；7：所在单位数据;
	 *            8:操作员数据；9：审核员数据，10：审计员数据）
	 * @return
	 */
	private static String createScopeSql(int dataScope, String officeAlias, SysUser user, String configAlias) {
		StringBuilder scopeSql = new StringBuilder(1024);

		if (SysRole.DATA_SCOPE_COMPANY_AND_CHILD.equals(dataScope)) {
			scopeSql.append(" OR " + officeAlias + ".id = " + user.getCompany().getId());
			scopeSql.append(" OR " + officeAlias + ".parent_ids LIKE '" + user.getCompany().getParentIds()
					+ user.getCompany().getId() + ",%'");
		} else if (SysRole.DATA_SCOPE_COMPANY.equals(dataScope)) {
			scopeSql.append(" OR " + officeAlias + ".id = " + user.getCompany().getId());
			// 包括本公司下的部门 （type=1:公司；type=2：单位 3.部门）
			scopeSql.append(" OR (" + officeAlias + ".parent_id = '" + user.getCompany().getId() + "' AND "
					+ officeAlias + ".type>1)");
		} else if (SysRole.DATA_SCOPE_OFFICE_AND_CHILD.equals(dataScope)) {
			scopeSql.append(" OR " + officeAlias + ".id = " + user.getOffice().getId());
			scopeSql.append(" OR " + officeAlias + ".parent_ids LIKE '" + user.getOffice().getParentIds()
					+ user.getOffice().getId() + ",%'");
		} else if (SysRole.DATA_SCOPE_OFFICE.equals(dataScope)) {
			scopeSql.append(" OR " + officeAlias + ".id = " + user.getOffice().getId());
		} else if (SysRole.DATA_SCOPE_ENTITY_AND_CHILD.equals(dataScope)) {
			scopeSql.append(" OR " + officeAlias + ".id = " + user.getEntity().getId());
			scopeSql.append(" OR " + officeAlias + ".parent_ids LIKE '" + user.getEntity().getParentIds()
					+ user.getEntity().getId() + ",%'");

		} else if (SysRole.DATA_SCOPE_ENTITY.equals(dataScope)) {
			scopeSql.append(" OR " + officeAlias + ".id = " + user.getEntity().getId());
			// 包括本公司下的部门 （type=1:公司；type=2：单位 3.部门）
			scopeSql.append(" OR (" + officeAlias + ".parent_id = '" + user.getEntity().getId() + "' AND " + officeAlias
					+ ".type>1)");

		} else if (SysRole.DATA_SCOPE_CREATOR.equals(dataScope)) {
			scopeSql.append(" OR " + configAlias + ".is_audit !=3");
		} else if (SysRole.DATA_SCOPE_AUDITOR.equals(dataScope)) {
			scopeSql.append(" OR " + configAlias + ".is_audit = 0 or " + configAlias + ".is_audit = 1");
		} else if (SysRole.DATA_SCOPE_SHOWER.equals(dataScope)) {
			scopeSql.append(" OR " + configAlias + ".is_audit = 1");
		}

		return scopeSql.toString();
	}

	/**
	 * 
	 * getTableName(获取表名对应的Class) (这里描述这个方法适用条件 – 可选)
	 * 
	 * @param clazz
	 * @return String
	 * @exception @since
	 *                1.0.0
	 */
	public String getClassName(String tableName) {
		return Configurations.getStringProperty(tableName, null);
	}

	public static class MapTypeAdapter extends TypeAdapter<Object> {
		@Override
		public Object read(JsonReader in) throws IOException {
			JsonToken token = in.peek();
			switch (token) {
			case BEGIN_ARRAY:
				List<Object> list = new ArrayList<Object>();
				in.beginArray();
				while (in.hasNext()) {
					list.add(read(in));
				}
				in.endArray();
				return list;

			case BEGIN_OBJECT:
				Map<String, Object> map = new LinkedTreeMap<String, Object>();
				in.beginObject();
				while (in.hasNext()) {
					map.put(in.nextName(), read(in));
				}
				in.endObject();
				return map;

			case STRING:
				return in.nextString();

			case NUMBER:
				String temp = in.nextString();
				BigDecimal dbNum = new BigDecimal(temp);
				BigDecimal maxLong = new BigDecimal(Long.MAX_VALUE);
				BigDecimal maxInteger = new BigDecimal(Integer.MAX_VALUE);
				// 数字超过long的最大值，返回BigDecimal类型
				if (dbNum.compareTo(maxLong) == 1) {
					return dbNum;
				} else if (dbNum.compareTo(maxInteger) == 1) {
					long lngNum = Long.parseLong(temp);
					return lngNum;
				} else {
					int lngNum = Integer.parseInt(temp);
					return lngNum;
				}

			case BOOLEAN:
				return in.nextBoolean();

			case NULL:
				in.nextNull();
				return null;

			default:
				throw new IllegalStateException();
			}
		}

		@Override
		public void write(JsonWriter out, Object value) throws IOException {
			// 序列化无需实现
		}

	}

	/**
	 * 转换成字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static <T> String gsonToJson(T obj) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().excludeFieldsWithoutExposeAnnotation()
				.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
					@Override
					public JsonElement serialize(Date src, Type type, JsonSerializationContext context) {
						String format = "yyyy-MM-dd HH:mm:ss";
						long time = ((Date) src).getTime();
						SimpleDateFormat sdf = new SimpleDateFormat(format);
						return new JsonPrimitive(sdf.format(time));
					}
				}).create();
		return gson.toJson(obj);
	}

	/**
	 * 字符串转成对象
	 * 
	 * @param jsonString
	 * @param cls
	 * @return
	 */
	public static <T> T gsonFromJson(String jsonString, Class<T> cls) {
		Gson gson = new GsonBuilder().registerTypeAdapter(new TypeToken<Map>() {
		}.getType(), new MapTypeAdapter()).registerTypeAdapter(new TypeToken<List>() {
		}.getType(), new MapTypeAdapter()).create();
		return gson.fromJson(jsonString, cls);
	}

	/**
	 * 界面配置list转换为服务接口端的list
	 * 
	 * @param <T>
	 * @param <T>
	 * @param dstList
	 * @param srcList
	 * @param cfgType，1为IP类型，2为字符串类型，3为增强字符串，4数值类型，5摘要类,6回调类[但字符串类域配置和增强字符串域配置在接口参数中同属于strRegionList]
	 * @param baseCfg，配置基本信息
	 * @param groupRelationList
	 *            配置分组列表
	 * @return
	 */
	public static <T> Map<String, List> cfgConvert(List dstList, List<T> srcList, Integer cfgType, BaseCfg baseCfg,
			List groupRelationList) {
		Map<String, List> map = new HashMap();
		if (cfgType == 1) {
			List numRegionList = new ArrayList();
			Integer groupId = 0;
			Integer numGroupId = 0;
			List<Integer> regionIdList = ConfigServiceUtil.getId(3, srcList.size());
			for (int i = 0; i < srcList.size(); i++) {
				T srcCfg = srcList.get(i);
				BaseIpCfg baseIpCfg = new BaseIpCfg();
				BeanUtils.copyProperties(srcCfg, baseIpCfg);
				IpCfg cfg = new IpCfg();
				BeanUtils.copyProperties(baseIpCfg, cfg);
				// 区域IP配置，多条IP配置属于同一个分组，其他业务配置IP，一条配置一个分组
				// if(groupId==0 || !cfg.getCfgType().equals(Constants.AREA_REGION)){
				if (groupId == 0) {
					GroupCfg group = new GroupCfg();
					groupId = ConfigServiceUtil.getId(2, 1).get(0);
					group.setGroupId(groupId);
					group.setCompileId(baseCfg.getCompileId());
					group.setAuditTime(baseCfg.getAuditTime());
					group.setIsValid(baseCfg.getIsValid());
					groupRelationList.add(group);
				}
				cfg.setGroupId(groupId);
				cfg.setAuditTime(baseCfg.getAuditTime());
				cfg.setIsValid(baseCfg.getIsValid());
				List<IpCfg> cfgs = ipConvert(cfg, baseIpCfg);
				if (cfgs.size() > 1) {
					List<Integer> ids = ConfigServiceUtil.getId(3, cfgs.size() - 1);
					regionIdList.addAll(ids);
				}
				for (int j = i; j < cfgs.size() + i; j++) {
					cfgs.get(j - i).setRegionId(regionIdList.get(j));
				}
				dstList.addAll(cfgs);

				// 如果protocolId非空非零，需要构造数值型域配置，多条相同协议的IP只需要一条数值域配置（目前没有不同协议IP&情况）
				if (baseIpCfg.getProtocolId() != null && baseIpCfg.getProtocolId() != 0) {
					if (numGroupId == 0) {
						GroupCfg group1 = new GroupCfg();
						group1.setGroupId(ConfigServiceUtil.getId(2, 1).get(0));
						group1.setCompileId(baseIpCfg.getCompileId());
						group1.setAuditTime(baseCfg.getAuditTime());
						group1.setIsValid(baseCfg.getIsValid());
						groupRelationList.add(group1);
						NumBoundaryCfg numCfg = new NumBoundaryCfg();
						numCfg.initDefaultValue();
						numCfg.setLowBoundary(baseIpCfg.getProtocolId());
						numCfg.setUpBoundary(baseIpCfg.getProtocolId());
						numCfg.setRegionId(ConfigServiceUtil.getId(3, 1).get(0));
						numCfg.setAuditTime(baseCfg.getAuditTime());
						numCfg.setGroupId(group1.getGroupId());
						numCfg.setIsValid(baseCfg.getIsValid());
						numRegionList.add(numCfg);
						map.put("numRegionList", numRegionList);
						numGroupId++;
					}
				}
			}

		} else if (cfgType == 2 || cfgType == 3) {
			List<Integer> groupIdList = ConfigServiceUtil.getId(2, srcList.size());
			List<Integer> regionIdList = ConfigServiceUtil.getId(3, srcList.size());
			for (int i = 0; i < srcList.size(); i++) {
				// 一条业务配置创建一个分组
				GroupCfg group = new GroupCfg();
				StringCfg cfg = new StringCfg();
				BeanUtils.copyProperties(srcList.get(i), cfg);
				group.setGroupId(groupIdList.get(i));
				group.setCompileId(baseCfg.getCompileId());
				group.setAuditTime(baseCfg.getAuditTime());
				group.setIsValid(baseCfg.getIsValid());
				groupRelationList.add(group);
				cfg.setGroupId(group.getGroupId());
				cfg.setRegionId(regionIdList.get(i));
				cfg.setAuditTime(baseCfg.getAuditTime());
				cfg.setIsValid(baseCfg.getIsValid());
				// 处理配置关键字转译
				cfg.setCfgKeywords(keywordsEscape(cfg.getCfgKeywords()));
				// 增强字符串转换
				cfg.setDistrict(keywordsEscape(cfg.getDistrict()));
				dstList.add(cfg);
			}
		} else if (cfgType == 4) {
			List<Integer> groupIdList = ConfigServiceUtil.getId(2, srcList.size());
			List<Integer> regionIdList = ConfigServiceUtil.getId(3, srcList.size());
			for (int i = 0; i < srcList.size(); i++) {
				// 一条业务配置创建一个分组
				com.nis.domain.maat.MaatCfg.NumBoundaryCfg cfg = new com.nis.domain.maat.MaatCfg.NumBoundaryCfg();
				BeanUtils.copyProperties(srcList.get(i), cfg);
				GroupCfg group = new GroupCfg();
				group.setGroupId(groupIdList.get(i));
				group.setCompileId(baseCfg.getCompileId());
				group.setAuditTime(baseCfg.getAuditTime());
				group.setIsValid(baseCfg.getIsValid());
				groupRelationList.add(group);
				cfg.setGroupId(group.getGroupId());
				cfg.setRegionId(regionIdList.get(i));
				cfg.setAuditTime(baseCfg.getAuditTime());
				cfg.setIsValid(baseCfg.getIsValid());
				dstList.add(cfg);
			}
		} else if (cfgType == 5) {
			List<Integer> groupIdList = ConfigServiceUtil.getId(2, srcList.size());
			List<Integer> regionIdList = ConfigServiceUtil.getId(3, srcList.size());
			for (int i = 0; i < srcList.size(); i++) {
				// 一条业务配置创建一个分组
				com.nis.domain.maat.MaatCfg.DigestCfg cfg = new com.nis.domain.maat.MaatCfg.DigestCfg();
				BeanUtils.copyProperties(srcList.get(i), cfg);
				GroupCfg group = new GroupCfg();
				group.setGroupId(groupIdList.get(i));
				group.setCompileId(baseCfg.getCompileId());
				group.setAuditTime(baseCfg.getAuditTime());
				group.setIsValid(baseCfg.getIsValid());
				groupRelationList.add(group);
				cfg.setGroupId(group.getGroupId());
				cfg.setRegionId(regionIdList.get(i));
				cfg.setAuditTime(baseCfg.getAuditTime());
				cfg.setIsValid(baseCfg.getIsValid());
				dstList.add(cfg);
			}
		} else {
			dstList.addAll(srcList);
		}
		map.put("groupList", groupRelationList);
		map.put("dstList", dstList);
		return map;
	}
	/**
	 * maatCfg转换,不支持callback,注意除了ip类型的range转换完成之后需要额外获取region id之外，其他需要在src中设置 compileId,regionId,regoup id
	 * 如果ip类型协议id不为空，需要设置numberRegionGroupId，numberRegionRegionId。
	 * @param src 
	 * @param cfgType，1为IP类型，2为字符串类型，3为增强字符串，4数值类型，5摘要类[但字符串类域配置和增强字符串域配置在接口参数中同属于strRegionList]
	 * @return Map<String,List>
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static <T> Map<String,List> cfgConvert(BaseCfg<T> src, Integer cfgType) {
		Map<String,List> map=Maps.newHashMap();
		List groupRelationList=Lists.newArrayList();
		Field compileIdF= Reflections.getAccessibleField(src, "compileId");
		Field groupIdF= Reflections.getAccessibleField(src, "groupId");
		Field regionIdF= Reflections.getAccessibleField(src, "regionId");
		try {
			if(compileIdF==null||compileIdF.get(src)==null) {
				throw new RuntimeException("Please set compileId first");
			}
			if(groupIdF==null||groupIdF.get(src)==null) {
				throw new RuntimeException("Please set groupId first");
			}
			if(regionIdF==null||regionIdF.get(src)==null) {
				throw new RuntimeException("Please set regionId first");
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (cfgType == 1) {
			List<IpCfg> ipcfgs=Lists.newArrayList();
			List numRegionList = new ArrayList();
			BaseIpCfg baseIpCfg = new BaseIpCfg();
			BeanUtils.copyProperties(src, baseIpCfg);
			Integer groupId = baseIpCfg.getGroupId();
			Integer numGroupId = 0;
			IpCfg cfg = new IpCfg();
			BeanUtils.copyProperties(baseIpCfg, cfg);
			// 区域IP配置，多条IP配置属于同一个分组，其他业务配置IP，一条配置一个分组
			// if(groupId==0 || !cfg.getCfgType().equals(Constants.AREA_REGION)){
			GroupCfg group = new GroupCfg();
			if (groupId == 0) {
				groupId = ConfigServiceUtil.getId(2, 1).get(0);
			}
			group.setGroupId(groupId);
			group.setCompileId(baseIpCfg.getCompileId());
			group.setAuditTime(baseIpCfg.getAuditTime());
			group.setIsValid(baseIpCfg.getIsValid());
			groupRelationList.add(group);
			cfg.setGroupId(groupId);
			cfg.setAuditTime(baseIpCfg.getAuditTime());
			cfg.setIsValid(baseIpCfg.getIsValid());
			List<IpCfg> cfgs = ipConvert(cfg, baseIpCfg);
			List<Integer> regionIdList=Lists.newArrayListWithCapacity(cfgs.size() - 1);
			if (cfgs.size() > 1) {
				List<Integer> ids = ConfigServiceUtil.getId(3, cfgs.size() - 1);
				regionIdList.addAll(ids);
			}
			for (int i = 1; i < cfgs.size(); i++) {
				cfgs.get(i).setRegionId(regionIdList.get(i-1));
			}
			ipcfgs.addAll(cfgs);
			map.put("ipRegionList", ipcfgs);
			// 如果protocolId非空非零，需要构造数值型域配置，多条相同协议的IP只需要一条数值域配置（目前没有不同协议IP&情况）
			if (baseIpCfg.getProtocolId() != null && baseIpCfg.getProtocolId() != 0) {
				if (numGroupId == 0) {
					GroupCfg group1 = new GroupCfg();
					group1.setGroupId(groupId);
					group1.setCompileId(baseIpCfg.getCompileId());
					group1.setAuditTime(baseIpCfg.getAuditTime());
					group1.setIsValid(baseIpCfg.getIsValid());
					groupRelationList.add(group1);
					NumBoundaryCfg numCfg = new NumBoundaryCfg();
					numCfg.initDefaultValue();
					numCfg.setLowBoundary(baseIpCfg.getProtocolId());
					numCfg.setUpBoundary(baseIpCfg.getProtocolId());
					if(baseIpCfg.getNumberRegionRegionId()!=null) {
						numCfg.setRegionId(baseIpCfg.getNumberRegionRegionId());
					}else {
						numCfg.setRegionId(ConfigServiceUtil.getId(3, 1).get(0));
					}
					numCfg.setAuditTime(baseIpCfg.getAuditTime());
					numCfg.setGroupId(group1.getGroupId());
					numCfg.setIsValid(baseIpCfg.getIsValid());
					numRegionList.add(numCfg);
					map.put("numRegionList", numRegionList);
					numGroupId++;
				}
			}
		} else if (cfgType == 2 || cfgType == 3) {
				List<StringCfg> stringcfgs=Lists.newArrayList();
				// 一条业务配置创建一个分组
				BaseCfg baseCfg=(BaseCfg)src;
				GroupCfg group = new GroupCfg();
				StringCfg cfg = new StringCfg();
				BeanUtils.copyProperties(src, cfg);
				group.setGroupId(baseCfg.getGroupId());
				group.setCompileId(baseCfg.getCompileId());
				group.setAuditTime(baseCfg.getAuditTime());
				group.setIsValid(baseCfg.getIsValid());
				groupRelationList.add(group);
				cfg.setGroupId(group.getGroupId());
				cfg.setRegionId(baseCfg.getRegionId());
				cfg.setAuditTime(baseCfg.getAuditTime());
				cfg.setIsValid(baseCfg.getIsValid());
				// 处理配置关键字转译
				cfg.setCfgKeywords(keywordsEscape(cfg.getCfgKeywords()));
				// 增强字符串转换
				cfg.setDistrict(keywordsEscape(cfg.getDistrict()));
				stringcfgs.add(cfg);
				map.put("strRegionList", stringcfgs);
		} else if (cfgType == 4) {
			List<com.nis.domain.maat.MaatCfg.NumBoundaryCfg> numCfgs=Lists.newArrayList();
				// 一条业务配置创建一个分组
			BaseCfg baseCfg=(BaseCfg)src;
			com.nis.domain.maat.MaatCfg.NumBoundaryCfg cfg = new com.nis.domain.maat.MaatCfg.NumBoundaryCfg();
			BeanUtils.copyProperties(src, cfg);
			GroupCfg group = new GroupCfg();
			group.setGroupId(baseCfg.getGroupId());
			group.setCompileId(baseCfg.getCompileId());
			group.setAuditTime(baseCfg.getAuditTime());
			group.setIsValid(baseCfg.getIsValid());
			groupRelationList.add(group);
			cfg.setGroupId(group.getGroupId());
			cfg.setRegionId(baseCfg.getRegionId());
			cfg.setAuditTime(baseCfg.getAuditTime());
			cfg.setIsValid(baseCfg.getIsValid());
			numCfgs.add(cfg);
			map.put("numRegionList", numCfgs);
		} else if (cfgType == 5) {
			List<com.nis.domain.maat.MaatCfg.DigestCfg> digestCfgs=Lists.newArrayList();
			BaseCfg baseCfg=(BaseCfg)src;
			// 一条业务配置创建一个分组
			com.nis.domain.maat.MaatCfg.DigestCfg cfg = new com.nis.domain.maat.MaatCfg.DigestCfg();
			BeanUtils.copyProperties(src, cfg);
			GroupCfg group = new GroupCfg();
			group.setGroupId(baseCfg.getGroupId());
			group.setCompileId(baseCfg.getCompileId());
			group.setAuditTime(baseCfg.getAuditTime());
			group.setIsValid(baseCfg.getIsValid());
			groupRelationList.add(group);
			cfg.setGroupId(group.getGroupId());
			cfg.setRegionId(baseCfg.getRegionId());
			cfg.setAuditTime(baseCfg.getAuditTime());
			cfg.setIsValid(baseCfg.getIsValid());
			digestCfgs.add(cfg);
			map.put("digestRegionList", digestCfgs);
		} else {
			throw new RuntimeException("unknown cfgType "+cfgType);
		} 
		map.put("groupRelationList", groupRelationList);
		return map;
	}

	/**
	 * 界面配置list转换为服务接口端的list，用于批量下发时，regionId,groupId已在该方法前被批量获取
	 * 
	 * @param <T>
	 * @param <T>
	 * @param dstList
	 * @param srcList
	 * @param cfgType，1为IP类型，2为字符串类型，3为增强字符串，4数值类型，5摘要类,6回调类[但字符串类域配置和增强字符串域配置在接口参数中同属于strRegionList]
	 * @param baseCfg，配置基本信息
	 * @param groupRelationList
	 *            配置分组列表
	 * @return
	 */
	public static <T> Map<String, List> cfgToMaatConvert(List dstList, List<T> srcList, Integer cfgType,List groupRelationList) {
		Map<String, List> map = new HashMap();
		if (cfgType == 1) {
			List numRegionList = new ArrayList();
			Integer groupId = 0;
			Integer numGroupId = 0;
			for (int i = 0; i < srcList.size(); i++) {
				List<Integer> regionIdList = Lists.newArrayList();
				T srcCfg = srcList.get(i);
				BaseIpCfg baseIpCfg = new BaseIpCfg();
				BeanUtils.copyProperties(srcCfg, baseIpCfg);
				regionIdList.add(baseIpCfg.getRegionId());
				IpCfg cfg = new IpCfg();
				BeanUtils.copyProperties(baseIpCfg, cfg);
				//多条IP配置属于同一个分组
				if (groupId == 0) {
					GroupCfg group = new GroupCfg();
					groupId = baseIpCfg.getGroupId();
					group.setGroupId(groupId);
					group.setCompileId(baseIpCfg.getCompileId());
					group.setAuditTime(baseIpCfg.getAuditTime());
					group.setIsValid(baseIpCfg.getIsValid());
					groupRelationList.add(group);
				}
				cfg.setGroupId(groupId);
				cfg.setAuditTime(baseIpCfg.getAuditTime());
				cfg.setIsValid(baseIpCfg.getIsValid());
				List<IpCfg> cfgs = ipConvert(cfg, baseIpCfg);
				if (cfgs.size() > 1) {
					List<Integer> ids = ConfigServiceUtil.getId(3, cfgs.size() - 1);
					regionIdList.addAll(ids);
				}
				for (int j = i; j < cfgs.size() + i; j++) {
					cfgs.get(j - i).setRegionId(regionIdList.get(j));
				}
				dstList.addAll(cfgs);

				// 如果protocolId非空非零，需要构造数值型域配置，多条相同协议的IP只需要一条数值域配置（目前没有不同协议IP&情况）
				if (baseIpCfg.getProtocolId() != null && baseIpCfg.getProtocolId() != 0) {
					if (numGroupId == 0) {
						GroupCfg group1 = new GroupCfg();
						group1.setGroupId(ConfigServiceUtil.getId(2, 1).get(0));
						group1.setCompileId(baseIpCfg.getCompileId());
						group1.setAuditTime(baseIpCfg.getAuditTime());
						group1.setIsValid(baseIpCfg.getIsValid());
						groupRelationList.add(group1);
						NumBoundaryCfg numCfg = new NumBoundaryCfg();
						numCfg.initDefaultValue();
						numCfg.setLowBoundary(baseIpCfg.getProtocolId());
						numCfg.setUpBoundary(baseIpCfg.getProtocolId());
						numCfg.setRegionId(ConfigServiceUtil.getId(3, 1).get(0));
						numCfg.setAuditTime(baseIpCfg.getAuditTime());
						numCfg.setGroupId(group1.getGroupId());
						numCfg.setIsValid(baseIpCfg.getIsValid());
						numRegionList.add(numCfg);
						map.put("numRegionList", numRegionList);
						numGroupId++;
					}
				}
			}

		} else if (cfgType == 2 || cfgType == 3) {
			for (int i = 0; i < srcList.size(); i++) {
				// 一条业务配置创建一个分组
				BaseCfg baseCfg = new BaseCfg();
				BeanUtils.copyProperties(srcList.get(i), baseCfg);//拷贝公共属性
				GroupCfg group = new GroupCfg();
				StringCfg cfg = new StringCfg();
				BeanUtils.copyProperties(srcList.get(i), cfg);//拷贝公共属性以及私有属性
				group.setGroupId(baseCfg.getGroupId());
				group.setCompileId(baseCfg.getCompileId());
				group.setAuditTime(baseCfg.getAuditTime());
				group.setIsValid(baseCfg.getIsValid());
				groupRelationList.add(group);
				cfg.setGroupId(group.getGroupId());
				cfg.setRegionId(baseCfg.getRegionId());
				cfg.setAuditTime(baseCfg.getAuditTime());
				cfg.setIsValid(baseCfg.getIsValid());
				// 处理配置关键字转译
				cfg.setCfgKeywords(keywordsEscape(cfg.getCfgKeywords()));
				// 增强字符串转换
				cfg.setDistrict(keywordsEscape(cfg.getDistrict()));
				dstList.add(cfg);
			}
		} else if (cfgType == 4) {
			for (int i = 0; i < srcList.size(); i++) {
				BaseCfg baseCfg = new BaseCfg();
				BeanUtils.copyProperties(srcList.get(i), baseCfg);//拷贝公共属性
				// 一条业务配置创建一个分组
				com.nis.domain.maat.MaatCfg.NumBoundaryCfg cfg = new com.nis.domain.maat.MaatCfg.NumBoundaryCfg();
				BeanUtils.copyProperties(srcList.get(i), cfg);
				GroupCfg group = new GroupCfg();
				group.setGroupId(baseCfg.getGroupId());
				group.setCompileId(baseCfg.getCompileId());
				group.setAuditTime(baseCfg.getAuditTime());
				group.setIsValid(baseCfg.getIsValid());
				groupRelationList.add(group);
				cfg.setGroupId(group.getGroupId());
				cfg.setRegionId(baseCfg.getRegionId());
				cfg.setAuditTime(baseCfg.getAuditTime());
				cfg.setIsValid(baseCfg.getIsValid());
				dstList.add(cfg);
			}
		} else if (cfgType == 5) {
			for (int i = 0; i < srcList.size(); i++) {
				BaseCfg baseCfg = new BaseCfg();
				BeanUtils.copyProperties(srcList.get(i), baseCfg);//拷贝公共属性
				// 一条业务配置创建一个分组
				com.nis.domain.maat.MaatCfg.DigestCfg cfg = new com.nis.domain.maat.MaatCfg.DigestCfg();
				BeanUtils.copyProperties(srcList.get(i), cfg);
				GroupCfg group = new GroupCfg();
				group.setGroupId(baseCfg.getGroupId());
				group.setCompileId(baseCfg.getCompileId());
				group.setAuditTime(baseCfg.getAuditTime());
				group.setIsValid(baseCfg.getIsValid());
				groupRelationList.add(group);
				cfg.setGroupId(group.getGroupId());
				cfg.setRegionId(baseCfg.getRegionId());
				cfg.setAuditTime(baseCfg.getAuditTime());
				cfg.setIsValid(baseCfg.getIsValid());
				dstList.add(cfg);
			}
		} else {
			dstList.addAll(srcList);
		}
		map.put("groupList", groupRelationList);
		map.put("dstList", dstList);
		return map;
	}
	/**
	 * 界面IP配置转换为MAAT类或者回调类IP配置
	 * 
	 * @param dstIp
	 * @param srcIp
	 * @return
	 */
	/*
	 * public static IpCfg ipConvert(IpCfg dstIp,BaseIpCfg srcIp){
	 * if(srcIp.getSrcIpAddress()!=null){
	 * if(srcIp.getSrcIpAddress().indexOf("/")!=-1){ if(srcIp.getIpType()==4 ||
	 * srcIp.getIpType()==46){//46表示源ip为ipv4，目的ip为ipv6 Integer srcMaskNum =
	 * Integer.parseInt(srcIp.getSrcIpAddress().split("/")[1]); if(srcMaskNum==0){
	 * dstIp.setSrcIpMask("0.0.0.0"); }else{
	 * dstIp.setSrcIpMask(IpUtil.convertMask(srcMaskNum)); }
	 * 
	 * dstIp.setSrcIp(srcIp.getSrcIpAddress().split("/")[0]); }else
	 * if(srcIp.getIpType()==6|| srcIp.getIpType()==64){//64表示源ip为ipv6，目的ip为ipv4
	 * IPv6Network strangeNetwork = IPv6Network.fromString(srcIp.getSrcIpAddress());
	 * dstIp.setSrcIp(srcIp.getSrcIpAddress().split("/")[0]);
	 * dstIp.setSrcIpMask(strangeNetwork.getNetmask().asAddress().toString()); }else
	 * { Pattern patternV4Subnet=Pattern.compile(Constants.IPV4_IP_SUBNET_REGEXP);
	 * Pattern patternV6Subnet=Pattern.compile(Constants.IPV6_IP_SUBNET_REGEXP);
	 * Matcher matchernV4Subnet=patternV4Subnet.matcher(srcIp.getSrcIpAddress());
	 * Matcher matcherV6Subnet=patternV6Subnet.matcher(srcIp.getSrcIpAddress());
	 * if(matchernV4Subnet.matches()) { Integer srcMaskNum =
	 * Integer.parseInt(srcIp.getSrcIpAddress().split("/")[1]); if(srcMaskNum==0){
	 * dstIp.setSrcIpMask("0.0.0.0"); }else{
	 * dstIp.setSrcIpMask(IpUtil.convertMask(srcMaskNum)); }
	 * dstIp.setSrcIp(srcIp.getSrcIpAddress().split("/")[0]); }else
	 * if(matcherV6Subnet.matches()){ IPv6Network strangeNetwork =
	 * IPv6Network.fromString(srcIp.getSrcIpAddress());
	 * dstIp.setSrcIp(srcIp.getSrcIpAddress().split("/")[0]);
	 * dstIp.setSrcIpMask(strangeNetwork.getNetmask().asAddress().toString()); }else
	 * { throw new RuntimeException("Invalid IP/subnet mask format"); } }
	 * 
	 * }else if(srcIp.getSrcIpAddress().indexOf("-")!=-1){ if(srcIp.getIpType()==4||
	 * srcIp.getIpType()==46){//46表示源ip为ipv4，目的ip为ipv6
	 * dstIp.setSrcIp(srcIp.getSrcIpAddress().split("-")[0]);
	 * dstIp.setSrcIpMask(IpUtil.getMask(srcIp.getSrcIpAddress().split("-")[0],
	 * srcIp.getSrcIpAddress().split("-")[1])); }else if(srcIp.getIpType()==6||
	 * srcIp.getIpType()==64){//64表示源ip为ipv6，目的ip为ipv4 IPv6Address address1 =
	 * IPv6Address.fromString(srcIp.getSrcIpAddress().split("-")[0]); IPv6Address
	 * address2 = IPv6Address.fromString(srcIp.getSrcIpAddress().split("-")[1]);
	 * IPv6Network network = IPv6Network.fromTwoAddresses(address1,address2);
	 * dstIp.setSrcIp(address1.toString());
	 * dstIp.setSrcIpMask(network.getNetmask().asAddress().toString()); }else {
	 * Pattern patternV4Range=Pattern.compile(Constants.IPV4_IP_RANGE_REGEXP);
	 * Pattern patternV6Range=Pattern.compile(Constants.IPV6_IP_RANGE_REGEXP);
	 * Matcher matcherV4Range=patternV4Range.matcher(srcIp.getSrcIpAddress());
	 * Matcher matcherV6Range=patternV6Range.matcher(srcIp.getSrcIpAddress());
	 * if(matcherV4Range.matches()) {
	 * dstIp.setSrcIp(srcIp.getSrcIpAddress().split("-")[0]);
	 * dstIp.setSrcIpMask(IpUtil.getMask(srcIp.getSrcIpAddress().split("-")[0],
	 * srcIp.getSrcIpAddress().split("-")[1])); }else if(matcherV6Range.matches()) {
	 * IPv6Address address1 =
	 * IPv6Address.fromString(srcIp.getSrcIpAddress().split("-")[0]); IPv6Address
	 * address2 = IPv6Address.fromString(srcIp.getSrcIpAddress().split("-")[1]);
	 * IPv6Network network = IPv6Network.fromTwoAddresses(address1,address2);
	 * dstIp.setSrcIp(address1.toString());
	 * dstIp.setSrcIpMask(network.getNetmask().asAddress().toString()); }else {
	 * throw new RuntimeException("Invalid IP range format"); } }
	 * 
	 * }else{ if(srcIp.getIpType()==4||
	 * srcIp.getIpType()==46){//46表示源ip为ipv4，目的ip为ipv6
	 * dstIp.setSrcIp(srcIp.getSrcIpAddress());
	 * dstIp.setSrcIpMask("255.255.255.255"); }else if(srcIp.getIpType()==6||
	 * srcIp.getIpType()==64){//64表示源ip为ipv6，目的ip为ipv4
	 * dstIp.setSrcIp(srcIp.getSrcIpAddress());
	 * dstIp.setSrcIpMask("FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"); }else {//all
	 * Pattern patternV4=Pattern.compile(Constants.IPV4_IP_REGEXP); Pattern
	 * patternV6=Pattern.compile(Constants.IPV6_IP_REGEXP); Matcher
	 * matcherV4=patternV4.matcher(srcIp.getSrcIpAddress()); Matcher
	 * matcherV6=patternV6.matcher(srcIp.getSrcIpAddress()); if(matcherV4.matches())
	 * { dstIp.setSrcIp(srcIp.getSrcIpAddress());
	 * dstIp.setSrcIpMask("255.255.255.255"); }else if(matcherV6.matches()) {
	 * dstIp.setSrcIp(srcIp.getSrcIpAddress());
	 * dstIp.setSrcIpMask("FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"); }else { throw
	 * new RuntimeException("Invalid IP format"); } }
	 * 
	 * } }else{ if(srcIp.getIpType()==4||
	 * srcIp.getIpType()==46){//46表示源ip为ipv4，目的ip为ipv6
	 * dstIp.setSrcIp(srcIp.getSrcIpAddress());
	 * dstIp.setSrcIpMask("255.255.255.255"); }else if(srcIp.getIpType()==6||
	 * srcIp.getIpType()==64){//64表示源ip为ipv6，目的ip为ipv4
	 * dstIp.setSrcIp(srcIp.getSrcIpAddress());
	 * dstIp.setSrcIpMask("FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"); }else {//all
	 * dstIp.setSrcIp(srcIp.getSrcIpAddress());
	 * dstIp.setSrcIpMask("255.255.255.255"); } }
	 * if(srcIp.getDestIpAddress()!=null){
	 * if(srcIp.getDestIpAddress().indexOf("/")!=-1){ if(srcIp.getIpType()==4||
	 * srcIp.getIpType()==64){//64表示源ip为ipv6，目的ip为ipv4 Integer dstMaskNum =
	 * Integer.parseInt(srcIp.getDestIpAddress().split("/")[1]); if(dstMaskNum==0){
	 * dstIp.setDstIpMask("0.0.0.0"); }else{
	 * dstIp.setDstIpMask(IpUtil.convertMask(dstMaskNum));; }
	 * dstIp.setDstIp(srcIp.getDestIpAddress().split("/")[0]); }else
	 * if(srcIp.getIpType()==6|| srcIp.getIpType()==46){//46表示源ip为ipv4，目的ip为ipv6
	 * IPv6Network strangeNetwork =
	 * IPv6Network.fromString(srcIp.getDestIpAddress());
	 * dstIp.setDstIp(srcIp.getDestIpAddress().split("/")[0]);
	 * dstIp.setDstIpMask(strangeNetwork.getNetmask().asAddress().toString()); }else
	 * { Pattern patternV4Subnet=Pattern.compile(Constants.IPV4_IP_SUBNET_REGEXP);
	 * Pattern patternV6Subnet=Pattern.compile(Constants.IPV6_IP_SUBNET_REGEXP);
	 * Matcher matchernV4Subnet=patternV4Subnet.matcher(srcIp.getDestIpAddress());
	 * Matcher matcherV6Subnet=patternV6Subnet.matcher(srcIp.getDestIpAddress());
	 * if(matchernV4Subnet.matches()) { Integer dstMaskNum =
	 * Integer.parseInt(srcIp.getDestIpAddress().split("/")[1]); if(dstMaskNum==0){
	 * dstIp.setDstIpMask("0.0.0.0"); }else{
	 * dstIp.setDstIpMask(IpUtil.convertMask(dstMaskNum));; }
	 * dstIp.setDstIp(srcIp.getDestIpAddress().split("/")[0]); }else
	 * if(matcherV6Subnet.matches()){ IPv6Network strangeNetwork =
	 * IPv6Network.fromString(srcIp.getDestIpAddress());
	 * dstIp.setDstIp(srcIp.getDestIpAddress().split("/")[0]);
	 * dstIp.setDstIpMask(strangeNetwork.getNetmask().asAddress().toString()); }else
	 * { throw new RuntimeException("Invalid IP/subnet mask format"); } }
	 * 
	 * }else if(srcIp.getDestIpAddress().indexOf("-")!=-1){
	 * if(srcIp.getIpType()==4|| srcIp.getIpType()==64){//64表示源ip为ipv6，目的ip为ipv4
	 * dstIp.setDstIp(srcIp.getDestIpAddress().split("-")[0]);
	 * dstIp.setDstIpMask(IpUtil.getMask(srcIp.getDestIpAddress().split("-")[0],
	 * srcIp.getDestIpAddress().split("-")[1])); }else if(srcIp.getIpType()==6||
	 * srcIp.getIpType()==46){//46表示源ip为ipv4，目的ip为ipv6 IPv6Address address1 =
	 * IPv6Address.fromString(srcIp.getDestIpAddress().split("-")[0]); IPv6Address
	 * address2 = IPv6Address.fromString(srcIp.getDestIpAddress().split("-")[1]);
	 * IPv6Network network = IPv6Network.fromTwoAddresses(address1,address2);
	 * dstIp.setDstIp(address1.toString());
	 * dstIp.setDstIpMask(network.getNetmask().asAddress().toString()); }else {
	 * Pattern patternV4Range=Pattern.compile(Constants.IPV4_IP_RANGE_REGEXP);
	 * Pattern patternV6Range=Pattern.compile(Constants.IPV6_IP_RANGE_REGEXP);
	 * Matcher matcherV4Range=patternV4Range.matcher(srcIp.getDestIpAddress());
	 * Matcher matcherV6Range=patternV6Range.matcher(srcIp.getDestIpAddress());
	 * if(matcherV4Range.matches()) {
	 * dstIp.setDstIp(srcIp.getDestIpAddress().split("-")[0]);
	 * dstIp.setDstIpMask(IpUtil.getMask(srcIp.getDestIpAddress().split("-")[0],
	 * srcIp.getDestIpAddress().split("-")[1])); }else if(matcherV6Range.matches())
	 * { IPv6Address address1 =
	 * IPv6Address.fromString(srcIp.getDestIpAddress().split("-")[0]); IPv6Address
	 * address2 = IPv6Address.fromString(srcIp.getDestIpAddress().split("-")[1]);
	 * IPv6Network network = IPv6Network.fromTwoAddresses(address1,address2);
	 * dstIp.setDstIp(address1.toString());
	 * dstIp.setDstIpMask(network.getNetmask().asAddress().toString()); }else {
	 * throw new RuntimeException("Invalid IP range format"); } }
	 * 
	 * }else{ if(srcIp.getIpType()==4||
	 * srcIp.getIpType()==64){//64表示源ip为ipv6，目的ip为ipv4
	 * dstIp.setDstIp(srcIp.getDestIpAddress());
	 * dstIp.setDstIpMask("255.255.255.255"); }else if(srcIp.getIpType()==6||
	 * srcIp.getIpType()==46){//46表示源ip为ipv4，目的ip为ipv6
	 * dstIp.setDstIp(srcIp.getDestIpAddress());
	 * dstIp.setDstIpMask("FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"); }else {//all
	 * Pattern patternV4=Pattern.compile(Constants.IPV4_IP_REGEXP); Pattern
	 * patternV6=Pattern.compile(Constants.IPV6_IP_REGEXP); Matcher
	 * matcherV4=patternV4.matcher(srcIp.getDestIpAddress()); Matcher
	 * matcherV6=patternV6.matcher(srcIp.getDestIpAddress());
	 * if(matcherV4.matches()) { dstIp.setDstIp(srcIp.getDestIpAddress());
	 * dstIp.setDstIpMask("255.255.255.255"); }else if(matcherV6.matches()) {
	 * dstIp.setDstIp(srcIp.getDestIpAddress());
	 * dstIp.setDstIpMask("FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"); }else { throw
	 * new RuntimeException("invalid ip format"); } }
	 * 
	 * } }else{ if(srcIp.getIpType()==4||
	 * srcIp.getIpType()==64){//64表示源ip为ipv6，目的ip为ipv4
	 * dstIp.setDstIp(srcIp.getDestIpAddress());
	 * dstIp.setDstIpMask("255.255.255.255"); }else if(srcIp.getIpType()==6||
	 * srcIp.getIpType()==46){//46表示源ip为ipv4，目的ip为ipv6
	 * dstIp.setDstIp(srcIp.getDestIpAddress());
	 * dstIp.setDstIpMask("FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"); }else {//all
	 * dstIp.setDstIp(srcIp.getDestIpAddress());
	 * dstIp.setDstIpMask("255.255.255.255"); } } if(srcIp.getSrcPort()!=null){
	 * if(srcIp.getSrcPort().indexOf("/")!=-1){ String srcMaskNum =
	 * srcIp.getSrcPort().split("/")[1]; dstIp.setSrcPortMask(srcMaskNum);
	 * dstIp.setSrcPort(srcIp.getSrcPort().split("/")[0]); }else{
	 * dstIp.setSrcPort(srcIp.getSrcPort()); dstIp.setSrcPortMask("65535"); } }else{
	 * dstIp.setSrcPort("0"); dstIp.setSrcPortMask("65535"); }
	 * if(srcIp.getDestPort()!=null){ if(srcIp.getDestPort().indexOf("/")!=-1){
	 * String dstMaskNum = srcIp.getDestPort().split("/")[1];
	 * dstIp.setDstPortMask(dstMaskNum);
	 * dstIp.setDstPort(srcIp.getDestPort().split("/")[0]); }else{
	 * dstIp.setDstPort(srcIp.getDestPort()); dstIp.setDstPortMask("65535"); }
	 * }else{ dstIp.setDstPort("0"); dstIp.setDstPortMask("65535"); } return dstIp;
	 * }
	 */
	/**
	 * 界面IP配置转换为MAAT类或者回调类IP配置
	 * 
	 * @param dstIp
	 * @param srcIp
	 * @return
	 */
	public static List<IpCfg> ipConvert(IpCfg dstIp, BaseIpCfg srcIp) {
		List<IpCfg> ipConvertList = Lists.newArrayList();
		boolean isRange = ((srcIp.getIpPattern() != null && srcIp.getIpPattern() == 2)
				|| (srcIp.getSrcIpAddress() != null && srcIp.getSrcIpAddress().indexOf("-") > -1)
				|| (srcIp.getDestIpAddress() != null && srcIp.getDestIpAddress().indexOf("-") > -1));
		if (isRange) {
			List<IpCfg> tempList = Lists.newArrayList();
			List<IpCfg> tempList1 = Lists.newArrayList();
			if (srcIp.getIpType().intValue() == 4) {
				if (srcIp.getSrcIpAddress() != null) {
					String startIpPart = srcIp.getSrcIpAddress().split("-")[0];
					String endIpPart = srcIp.getSrcIpAddress().split("-")[1];
					Integer startNum = Integer.parseInt(startIpPart.split("\\.")[3]);
					Integer endNum = Integer.parseInt(endIpPart.split("\\.")[3]);
					for (int i = startNum; i <= endNum; i++) {
						IpCfg tempIp = new IpCfg();
						BeanUtils.copyProperties(dstIp, tempIp);
						tempIp.setSrcIp(startIpPart.substring(0, startIpPart.lastIndexOf(".") + 1) + i);
						tempIp.setSrcIpMask("255.255.255.255");
						tempList.add(tempIp);
					}
				} else {
					dstIp.setSrcIp("0.0.0.0");
					dstIp.setSrcIpMask("255.255.255.255");
					tempList.add(dstIp);
				}
				if (srcIp.getDestIpAddress() != null) {
					String startIpPart = srcIp.getDestIpAddress().split("-")[0];
					String endIpPart = srcIp.getDestIpAddress().split("-")[1];
					Integer startNum = Integer.parseInt(startIpPart.split("\\.")[3]);
					Integer endNum = Integer.parseInt(endIpPart.split("\\.")[3]);
					for (IpCfg _cfg : tempList) {
						for (int i = startNum; i <= endNum; i++) {
							IpCfg tempIp = new IpCfg();
							BeanUtils.copyProperties(_cfg, tempIp);
							tempIp.setDstIp(startIpPart.substring(0, startIpPart.lastIndexOf(".") + 1) + i);
							tempIp.setDstIpMask("255.255.255.255");
							// 处理
							convertPortValues(tempIp, srcIp);
							if (!tempIp.getSrcIp().equals(tempIp.getDstIp())) {
								tempList1.add(tempIp);
							}

						}
					}
					tempList.clear();
				} else {
					for (IpCfg _cfg : tempList) {
						_cfg.setDstIp("0.0.0.0");
						_cfg.setSrcIpMask("255.255.255.255");
						convertPortValues(_cfg, srcIp);
					}
				}
				if (tempList1.size() > 0) {
					ipConvertList.addAll(tempList1);
				} else {
					ipConvertList.addAll(tempList);
				}
			} else if (srcIp.getIpType().intValue() == 6) {
				if (srcIp.getSrcIpAddress() != null) {
					IPv6Address address1 = IPv6Address.fromString(srcIp.getSrcIpAddress().split("-")[0]);
					IPv6Address address2 = IPv6Address.fromString(srcIp.getSrcIpAddress().split("-")[1]);
					IPv6Network network = IPv6Network.fromTwoAddresses(address1, address2);
					dstIp.setSrcIp(address1.toString());
					dstIp.setSrcIpMask(network.getNetmask().asAddress().toString());
				} else {
					dstIp.setSrcIp("::");
					dstIp.setSrcIpMask("FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF");
				}
				if (srcIp.getDestIpAddress() != null) {
					IPv6Address address1 = IPv6Address.fromString(srcIp.getDestIpAddress().split("-")[0]);
					IPv6Address address2 = IPv6Address.fromString(srcIp.getDestIpAddress().split("-")[1]);
					IPv6Network network = IPv6Network.fromTwoAddresses(address1, address2);
					dstIp.setDstIp(address1.toString());
					dstIp.setDstIpMask(network.getNetmask().asAddress().toString());
				} else {
					dstIp.setDstIp("::");
					dstIp.setDstIpMask("FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF");
				}

				ipConvertList.add(dstIp);
			} else {
				throw new RuntimeException("Unsupported IP type " + srcIp.getIpType());
			}
		} else {
			if (srcIp.getSrcIpAddress() != null) {
				if (srcIp.getSrcIpAddress().indexOf("/") != -1) {
					if (srcIp.getIpType() == 4 /* || srcIp.getIpType()==46 */) {// 46表示源ip为ipv4，目的ip为ipv6
						Integer srcMaskNum = Integer.parseInt(srcIp.getSrcIpAddress().split("/")[1]);
						if (srcMaskNum == 0) {
							dstIp.setSrcIpMask("0.0.0.0");
						} else {
							dstIp.setSrcIpMask(IpUtil.convertMask(srcMaskNum));
						}

						dstIp.setSrcIp(srcIp.getSrcIpAddress().split("/")[0]);
					} else if (srcIp.getIpType() == 6/* || srcIp.getIpType()==64 */) {// 64表示源ip为ipv6，目的ip为ipv4
						IPv6Network strangeNetwork = IPv6Network.fromString(srcIp.getSrcIpAddress());
						dstIp.setSrcIp(srcIp.getSrcIpAddress().split("/")[0]);
						dstIp.setSrcIpMask(strangeNetwork.getNetmask().asAddress().toString());
					} /*
						 * else { Pattern
						 * patternV4Subnet=Pattern.compile(Constants.IPV4_IP_SUBNET_REGEXP); Pattern
						 * patternV6Subnet=Pattern.compile(Constants.IPV6_IP_SUBNET_REGEXP); Matcher
						 * matchernV4Subnet=patternV4Subnet.matcher(srcIp.getSrcIpAddress()); Matcher
						 * matcherV6Subnet=patternV6Subnet.matcher(srcIp.getSrcIpAddress());
						 * if(matchernV4Subnet.matches()) { Integer srcMaskNum =
						 * Integer.parseInt(srcIp.getSrcIpAddress().split("/")[1]); if(srcMaskNum==0){
						 * dstIp.setSrcIpMask("0.0.0.0"); }else{
						 * dstIp.setSrcIpMask(IpUtil.convertMask(srcMaskNum)); }
						 * dstIp.setSrcIp(srcIp.getSrcIpAddress().split("/")[0]); }else
						 * if(matcherV6Subnet.matches()){ IPv6Network strangeNetwork =
						 * IPv6Network.fromString(srcIp.getSrcIpAddress());
						 * dstIp.setSrcIp(srcIp.getSrcIpAddress().split("/")[0]);
						 * dstIp.setSrcIpMask(strangeNetwork.getNetmask().asAddress().toString()); }else
						 * { throw new RuntimeException("Invalid IP/subnet mask format"); } }
						 */
					else {
						throw new RuntimeException("Unsupported IP type " + srcIp.getIpType());
					}
				} else {
					if (srcIp.getIpType() == 4/* || srcIp.getIpType()==46 */) {// 46表示源ip为ipv4，目的ip为ipv6
						dstIp.setSrcIp(srcIp.getSrcIpAddress());
						dstIp.setSrcIpMask("255.255.255.255");
					} else if (srcIp.getIpType() == 6/* || srcIp.getIpType()==64 */) {// 64表示源ip为ipv6，目的ip为ipv4
						dstIp.setSrcIp(srcIp.getSrcIpAddress());
						dstIp.setSrcIpMask("FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF");
					} /*
						 * else {//all Pattern patternV4=Pattern.compile(Constants.IPV4_IP_REGEXP);
						 * Pattern patternV6=Pattern.compile(Constants.IPV6_IP_REGEXP); Matcher
						 * matcherV4=patternV4.matcher(srcIp.getSrcIpAddress()); Matcher
						 * matcherV6=patternV6.matcher(srcIp.getSrcIpAddress()); if(matcherV4.matches())
						 * { dstIp.setSrcIp(srcIp.getSrcIpAddress());
						 * dstIp.setSrcIpMask("255.255.255.255"); }else if(matcherV6.matches()) {
						 * dstIp.setSrcIp(srcIp.getSrcIpAddress());
						 * dstIp.setSrcIpMask("FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"); }else { throw
						 * new RuntimeException("Invalid IP format"); } }
						 */
					else {
						throw new RuntimeException("Unsupported IP type " + srcIp.getIpType());
					}
				}
			} else {
				if (srcIp.getIpType() == 4/* || srcIp.getIpType()==46 */) {// 46表示源ip为ipv4，目的ip为ipv6
					dstIp.setSrcIp(srcIp.getSrcIpAddress());
					dstIp.setSrcIpMask("255.255.255.255");
				} else if (srcIp.getIpType() == 6/* || srcIp.getIpType()==64 */) {// 64表示源ip为ipv6，目的ip为ipv4
					dstIp.setSrcIp(srcIp.getSrcIpAddress());
					dstIp.setSrcIpMask("FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF");
				} /*
					 * else {//all dstIp.setSrcIp(srcIp.getSrcIpAddress());
					 * dstIp.setSrcIpMask("255.255.255.255"); }
					 */
				else {
					throw new RuntimeException("Unsupported IP type " + srcIp.getIpType());
				}
			}
			if (srcIp.getDestIpAddress() != null) {
				if (srcIp.getDestIpAddress().indexOf("/") != -1) {
					if (srcIp.getIpType() == 4/* || srcIp.getIpType()==64 */) {// 64表示源ip为ipv6，目的ip为ipv4
						Integer dstMaskNum = Integer.parseInt(srcIp.getDestIpAddress().split("/")[1]);
						if (dstMaskNum == 0) {
							dstIp.setDstIpMask("0.0.0.0");
						} else {
							dstIp.setDstIpMask(IpUtil.convertMask(dstMaskNum));
							;
						}
						dstIp.setDstIp(srcIp.getDestIpAddress().split("/")[0]);
					} else if (srcIp.getIpType() == 6/* || srcIp.getIpType()==46 */) {// 46表示源ip为ipv4，目的ip为ipv6
						IPv6Network strangeNetwork = IPv6Network.fromString(srcIp.getDestIpAddress());
						dstIp.setDstIp(srcIp.getDestIpAddress().split("/")[0]);
						dstIp.setDstIpMask(strangeNetwork.getNetmask().asAddress().toString());
					} /*
						 * else { Pattern
						 * patternV4Subnet=Pattern.compile(Constants.IPV4_IP_SUBNET_REGEXP); Pattern
						 * patternV6Subnet=Pattern.compile(Constants.IPV6_IP_SUBNET_REGEXP); Matcher
						 * matchernV4Subnet=patternV4Subnet.matcher(srcIp.getDestIpAddress()); Matcher
						 * matcherV6Subnet=patternV6Subnet.matcher(srcIp.getDestIpAddress());
						 * if(matchernV4Subnet.matches()) { Integer dstMaskNum =
						 * Integer.parseInt(srcIp.getDestIpAddress().split("/")[1]); if(dstMaskNum==0){
						 * dstIp.setDstIpMask("0.0.0.0"); }else{
						 * dstIp.setDstIpMask(IpUtil.convertMask(dstMaskNum));; }
						 * dstIp.setDstIp(srcIp.getDestIpAddress().split("/")[0]); }else
						 * if(matcherV6Subnet.matches()){ IPv6Network strangeNetwork =
						 * IPv6Network.fromString(srcIp.getDestIpAddress());
						 * dstIp.setDstIp(srcIp.getDestIpAddress().split("/")[0]);
						 * dstIp.setDstIpMask(strangeNetwork.getNetmask().asAddress().toString()); }else
						 * { throw new RuntimeException("Invalid IP/subnet mask format"); } }
						 */
					else {
						throw new RuntimeException("Unsupported IP type " + srcIp.getIpType());
					}

				} else {
					if (srcIp.getIpType() == 4/* || srcIp.getIpType()==64 */) {// 64表示源ip为ipv6，目的ip为ipv4
						dstIp.setDstIp(srcIp.getDestIpAddress());
						dstIp.setDstIpMask("255.255.255.255");
					} else if (srcIp.getIpType() == 6/* || srcIp.getIpType()==46 */) {// 46表示源ip为ipv4，目的ip为ipv6
						dstIp.setDstIp(srcIp.getDestIpAddress());
						dstIp.setDstIpMask("FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF");
					} /*
						 * else {//all Pattern patternV4=Pattern.compile(Constants.IPV4_IP_REGEXP);
						 * Pattern patternV6=Pattern.compile(Constants.IPV6_IP_REGEXP); Matcher
						 * matcherV4=patternV4.matcher(srcIp.getDestIpAddress()); Matcher
						 * matcherV6=patternV6.matcher(srcIp.getDestIpAddress());
						 * if(matcherV4.matches()) { dstIp.setDstIp(srcIp.getDestIpAddress());
						 * dstIp.setDstIpMask("255.255.255.255"); }else if(matcherV6.matches()) {
						 * dstIp.setDstIp(srcIp.getDestIpAddress());
						 * dstIp.setDstIpMask("FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"); }else { throw
						 * new RuntimeException("invalid ip format"); } }
						 */
					else {
						throw new RuntimeException("Unsupported IP type " + srcIp.getIpType());
					}
				}
			} else {
				if (srcIp.getIpType() == 4/* || srcIp.getIpType()==64 */) {// 64表示源ip为ipv6，目的ip为ipv4
					dstIp.setDstIp(srcIp.getDestIpAddress());
					dstIp.setDstIpMask("255.255.255.255");
				} else if (srcIp.getIpType() == 6/* || srcIp.getIpType()==46 */) {// 46表示源ip为ipv4，目的ip为ipv6
					dstIp.setDstIp(srcIp.getDestIpAddress());
					dstIp.setDstIpMask("FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF");
				} /*
					 * else {//all dstIp.setDstIp(srcIp.getDestIpAddress());
					 * dstIp.setDstIpMask("255.255.255.255"); }
					 */
				else {
					throw new RuntimeException("Unsupported IP type " + srcIp.getIpType());
				}
			}
			if (srcIp.getSrcPort() != null) {
				if (srcIp.getSrcPort().indexOf("/") != -1) {
					String srcMaskNum = srcIp.getSrcPort().split("/")[1];
					dstIp.setSrcPortMask(srcMaskNum);
					dstIp.setSrcPort(srcIp.getSrcPort().split("/")[0]);
				} else {
					dstIp.setSrcPort(srcIp.getSrcPort());
					dstIp.setSrcPortMask("65535");
				}
			} else {
				dstIp.setSrcPort("0");
				dstIp.setSrcPortMask("65535");
			}
			if (srcIp.getDestPort() != null) {
				if (srcIp.getDestPort().indexOf("/") != -1) {
					String dstMaskNum = srcIp.getDestPort().split("/")[1];
					dstIp.setDstPortMask(dstMaskNum);
					dstIp.setDstPort(srcIp.getDestPort().split("/")[0]);
				} else {
					dstIp.setDstPort(srcIp.getDestPort());
					dstIp.setDstPortMask("65535");
				}
			} else {
				dstIp.setDstPort("0");
				dstIp.setDstPortMask("65535");
			}
			ipConvertList.add(dstIp);
		}
		return ipConvertList;
	}

	/**
	 * 设置端口值
	 * 
	 * @param dstIp
	 * @param srcIp
	 */
	public static void convertPortValues(IpCfg dstIp, BaseIpCfg srcIp) {
		if (srcIp.getSrcPort() != null) {
			if (srcIp.getSrcPort().indexOf("/") != -1) {
				String srcMaskNum = srcIp.getSrcPort().split("/")[1];
				dstIp.setSrcPortMask(srcMaskNum);
				dstIp.setSrcPort(srcIp.getSrcPort().split("/")[0]);
			} else {
				dstIp.setSrcPort(srcIp.getSrcPort());
				dstIp.setSrcPortMask("65535");
			}
		} else {
			dstIp.setSrcPort("0");
			dstIp.setSrcPortMask("65535");
		}
		if (srcIp.getDestPort() != null) {
			if (srcIp.getDestPort().indexOf("/") != -1) {
				String dstMaskNum = srcIp.getDestPort().split("/")[1];
				dstIp.setDstPortMask(dstMaskNum);
				dstIp.setDstPort(srcIp.getDestPort().split("/")[0]);
			} else {
				dstIp.setDstPort(srcIp.getDestPort());
				dstIp.setDstPortMask("65535");
			}
		} else {
			dstIp.setDstPort("0");
			dstIp.setDstPortMask("65535");
		}
	}

	// 拦截策略
	public static ProxyObjKeyring convertCallBackProxyObjKeyring(PxyObjKeyring cfg) {
		ProxyObjKeyring proxyObjKeyring = new ProxyObjKeyring();
		proxyObjKeyring.setId(Long.valueOf(cfg.getCompileId()));
		proxyObjKeyring.setCfgId(cfg.getCompileId());
		proxyObjKeyring.setCrl(cfg.getCrl());
		proxyObjKeyring.setExpireAfter(cfg.getExpireAfter());
		proxyObjKeyring.setKeyringId(cfg.getCompileId());
		proxyObjKeyring.setKeyringName(keywordsEscape(cfg.getCfgDesc()));
		proxyObjKeyring.setKeyringType(cfg.getKeyringType());
		proxyObjKeyring.setPrivateKeyFile(cfg.getPrivateKeyFile());
		proxyObjKeyring.setPublicKeyAlgo(cfg.getPublicKeyAlgo());
		proxyObjKeyring.setPublicKeyFile(cfg.getPublicKeyFile());
		proxyObjKeyring.setService(cfg.getServiceId());
		proxyObjKeyring.setIsValid(cfg.getIsValid());
		proxyObjKeyring.setOpTime(cfg.getAuditTime());
		return proxyObjKeyring;
	}
	// 拦截策略
	public static IpReusePolicyCfg convertCallBackSnatPolicy(IpReusePolicyCfg cfg) {
		if(cfg != null) {
			String areaEffectiveIds=cfg.getAreaEffectiveIds();
			if(StringUtils.isEmpty(areaEffectiveIds)){
				areaEffectiveIds="0";
			}else {
				// 组织区域、运营商配置下发格式
				SysDictInfoService service = SpringContextHolder.getBean(SysDictInfoService.class);
				areaEffectiveIds = service.setEffectiveRange(areaEffectiveIds);
			}
			cfg.setAreaEffectiveIds(areaEffectiveIds);
			cfg.setUserName(keywordsEscape(cfg.getUserName()));
		}
		return cfg;
	}

	// 可信证书
	public static List<ProxyObjTrustedCa> convertCallBackProxyObjTrustedCa(PxyObjTrustedCaCert cfgCert,
			PxyObjTrustedCaCrl cfgCrl) {
		List<ProxyObjTrustedCa> proxyObjTrustedCas = new ArrayList<>();
		if (cfgCert != null && !StringUtil.isEmpty(cfgCert.getCompileId())) {
			ProxyObjTrustedCa proxyObjTrustedCaCert = new ProxyObjTrustedCa();
			proxyObjTrustedCaCert.setId(Long.valueOf(cfgCert.getCompileId()));
			proxyObjTrustedCaCert.setCfgId(cfgCert.getCompileId());
			proxyObjTrustedCaCert.setCertId(cfgCert.getCompileId());
			proxyObjTrustedCaCert.setCertName(keywordsEscape(cfgCert.getCfgDesc()));
			proxyObjTrustedCaCert.setCertFile(cfgCert.getCertFile());
			proxyObjTrustedCaCert.setService(cfgCert.getServiceId());
			proxyObjTrustedCaCert.setIsValid(cfgCert.getIsValid());
			proxyObjTrustedCaCert.setOpTime(cfgCert.getAuditTime());
			proxyObjTrustedCas.add(proxyObjTrustedCaCert);
		}
		if (cfgCrl != null && !StringUtil.isEmpty(cfgCrl.getCompileId())) {
			ProxyObjTrustedCa proxyObjTrustedCaCert = new ProxyObjTrustedCa();
			proxyObjTrustedCaCert.setId(Long.valueOf(cfgCrl.getCompileId()));
			proxyObjTrustedCaCert.setCfgId(cfgCrl.getCompileId());
			proxyObjTrustedCaCert.setCertId(cfgCrl.getCertId());
			proxyObjTrustedCaCert.setService(cfgCrl.getServiceId());
			proxyObjTrustedCaCert.setIsValid(cfgCrl.getIsValid());
			proxyObjTrustedCaCert.setOpTime(cfgCrl.getAuditTime());
			proxyObjTrustedCaCert.setCrlId(cfgCrl.getCompileId());
			proxyObjTrustedCaCert.setCrlFile(cfgCrl.getCrlFile());
			proxyObjTrustedCas.add(proxyObjTrustedCaCert);
		}

		return proxyObjTrustedCas;
	}

	// ip转换为callback用ip
	public static NtcDnsResStrategy convertCallBackDnsResStrategy(DnsResStrategy cfg) {
		NtcDnsResStrategy resStrategy = new NtcDnsResStrategy();
		resStrategy.init();
		resStrategy.setId(Long.valueOf(cfg.getCompileId()));
		resStrategy.setCfgId(cfg.getCompileId());
		resStrategy.setReqStrateId(cfg.getCfgId());
		resStrategy.setStrateName(keywordsEscape(cfg.getCfgDesc()));
		resStrategy.setAction(cfg.getAction());
		resStrategy.setService(cfg.getServiceId());
		resStrategy.setResGroup1Id(cfg.getResGroup1Id());
		resStrategy.setResGroup1Num(cfg.getResGroup1Num());
		resStrategy.setMinTtl(cfg.getMinTtl());
		resStrategy.setMaxTtl(cfg.getMaxTtl());
		resStrategy.setIsValid(cfg.getIsValid());
		resStrategy.setOpTime(cfg.getAuditTime());
		return resStrategy;
	}

	// ip转换为callback用ip
	public static InlineIp convertCallBackIp(BaseIpCfg cfg, Integer policyGroup) {
		List<IpCfg> cfgs = ipConvert(new IpCfg(), cfg);
		if (cfgs.size() > 1) {
			throw new RuntimeException("CallBack IP did not support IP range!");
		}
		IpCfg c = cfgs.get(0);
		InlineIp ip = new InlineIp();
		ip.setId(cfg.getCompileId());
		ip.setCfgId(cfg.getCompileId());
		ip.setAction(cfg.getAction());
		ip.setService(cfg.getServiceId());
		ip.setAddrType(cfg.getIpType());
		ip.setSrcIp(c.getSrcIp());
		ip.setMaskSrcIp(c.getSrcIpMask());
		ip.setDstIp(c.getDstIp());
		ip.setMaskDstIp(c.getDstIpMask());
		ip.setSrcPort(c.getSrcPort());
		ip.setMaskSrcPort(c.getSrcPortMask());
		ip.setDstPort(c.getDstPort());
		ip.setMaskDstPort(c.getDstPortMask());
		ip.setProtocol(cfg.getProtocol());
		ip.setDirection(cfg.getDirection());
		ip.setIsValid(cfg.getIsValid());
		ip.setOpTime(cfg.getAuditTime());
		ip.setPolicyGroup(policyGroup);
		ip.setAreaEffectiveIds(cfg.getAreaEffectiveIds());//添加区域管控
		return ip;
	}

	// 区域IPsetAreaEffectiveIds设置
	public void setAreaEffectiveIds(BaseCfg<?> entity) {
		List<AreaIpCfg> areaCfg = entity.getAreaCfg();
		List<AreaBean> areaIsps = entity.getAreaIsp();
		if (Constants.IS_AREA_EFFECTIVE_NO == entity.getIsAreaEffective()) {
			entity.setAreaEffectiveIds("");
			entity.setAreaType(null);
		} else if (Constants.IS_AREA_EFFECTIVE_YES == entity.getIsAreaEffective()) {
			if (Constants.AREA_EFFECTIVE_TYPE_AREA_ISP == entity.getAreaType() && areaIsps != null
					&& areaIsps.size() > 0) {
				StringBuffer areaEffectiveIds = new StringBuffer();
				for (int i = 0; i < areaIsps.size(); i++) {
					if (StringUtils.isBlank(areaIsps.get(i).getArea())) {
						areaEffectiveIds.append(areaIsps.get(i).getIsp());
					} else if (StringUtils.isBlank(areaIsps.get(i).getIsp())) {
						areaEffectiveIds.append(areaIsps.get(i).getArea());
					} else {
						areaEffectiveIds.append(areaIsps.get(i).getArea() + ":" + areaIsps.get(i).getIsp());
					}
					if (i != areaIsps.size() - 1) {
						areaEffectiveIds.append(",");
					}
				}
				entity.setAreaEffectiveIds(areaEffectiveIds.toString());
			} else if (Constants.AREA_EFFECTIVE_TYPE_AREA_IP == entity.getAreaType() && areaCfg != null
					&& areaCfg.size() > 0) {
				entity.setAreaEffectiveIds("");
			}
		}
	}

	public static String keywordsEscape(String cfgKeywords) {
		if (StringUtils.isNotEmpty(cfgKeywords)) {
			// 不转译特殊字符
			cfgKeywords = cfgKeywords.trim();// 首先去掉首尾空格
			cfgKeywords = StringEscapeUtils.unescapeHtml4(cfgKeywords);
			cfgKeywords = cfgKeywords.replace("\\", "\\\\");
			cfgKeywords = cfgKeywords.replace("&", "\\&");
			cfgKeywords = cfgKeywords.replace(" ", "\\b");
			// ***and***在界面表示多个关键字的与表达式，此特殊字符串在common.js中使用定义，maat端以&表示
			cfgKeywords = cfgKeywords.replace(Constants.KEYWORD_EXPR, "&");

		}
		return cfgKeywords;
	}

	public static String replaceContentEscape(String replaceContent) {
		if (StringUtils.isNotEmpty(replaceContent)) {
			// 不转译特殊字符
			replaceContent = replaceContent.trim();// 首先去掉首尾空格
			replaceContent = replaceContent.replace("/", "\\/");// 首先转意斜杠“/”
		}
		return replaceContent;
	}

	// asn IP 复用转换
	public List<IpCfg> groupReuseCfgAddRemoveConvert(List<? extends BaseCfg<?>> ipCfgList, Integer isValid,
			Integer groupId) {
		logger.warn("convert data start");
		long start = System.currentTimeMillis();
		Date opTime = new Date();
		List<IpCfg> maatIpList = new ArrayList<>();
		if (ipCfgList.size() > 0) {
			// 只用一次instanceof,取代循环中每次都用一次instanceof
			if (ipCfgList.get(0) instanceof AsnIpCfg) {
				for (BaseCfg<?> _cfg : ipCfgList) {
					IpCfg cfg = new IpCfg();
					BaseIpCfg baseIpCfg = new BaseIpCfg();
					AsnIpCfg asnIpCfg = (AsnIpCfg) _cfg;
					BeanUtils.copyProperties(asnIpCfg, baseIpCfg);
					BeanUtils.copyProperties(baseIpCfg, cfg);
					cfg.setGroupId(asnIpCfg.getAsnIpGroup());
					cfg.setRegionId(asnIpCfg.getRegionId());
					String userRegion = "ASN_ID=" + asnIpCfg.getUserRegion1();
					cfg.setUserRegion(userRegion);
					cfg.setIsValid(isValid);
					cfg.setAuditTime(opTime);
					List<IpCfg> cfgs = ipConvert(cfg, baseIpCfg);
					maatIpList.addAll(cfgs);
				}
			} else if (ipCfgList.get(0) instanceof AppIpCfg) {
				for (BaseCfg<?> _cfg : ipCfgList) {
					IpCfg cfg = new IpCfg();
					BaseIpCfg baseIpCfg = new BaseIpCfg();
					AppIpCfg appIpCfg = (AppIpCfg) _cfg;
					BeanUtils.copyProperties(appIpCfg, baseIpCfg);
					BeanUtils.copyProperties(baseIpCfg, cfg);
					cfg.setGroupId(groupId);
					cfg.setRegionId(Integer.parseInt(appIpCfg.getUserRegion1()));
					String userRegion = "APP_ID=" + appIpCfg.getAppCode();
					cfg.setUserRegion(userRegion);
					cfg.setIsValid(isValid);
					cfg.setAuditTime(opTime);
					List<IpCfg> cfgs = ipConvert(cfg, baseIpCfg);
					maatIpList.addAll(cfgs);
				}
			}
		}
		long end = System.currentTimeMillis();
		logger.warn("convert data finish,cost:" + (end - start));
		return maatIpList;
	}
	public MaatCfg convertMaatCfg(BaseCfg _cfg,Integer cfgType) {
		MaatCfg maatCfg = new MaatCfg();
		maatCfg.initDefaultValue();
		BeanUtils.copyProperties(_cfg, maatCfg);
		maatCfg.setAction(_cfg.getAction());
		maatCfg.setAuditTime(_cfg.getAuditTime());
		Map<String,List> regions=cfgConvert(_cfg, cfgType);
		if(regions.get("ipRegionList")!=null) {
			maatCfg.setIpRegionList(regions.get("ipRegionList"));
		}else {
			maatCfg.setIpRegionList(new ArrayList<IpCfg>());
		}
		if(regions.get("strRegionList")!=null) {
			maatCfg.setStrRegionList(regions.get("strRegionList"));
		}else {
			maatCfg.setStrRegionList(new ArrayList<StringCfg>());
		}
		if(regions.get("numRegionList")!=null) {
			maatCfg.setNumRegionList(regions.get("numRegionList"));
		}else {
			maatCfg.setNumRegionList(new ArrayList<NumBoundaryCfg>());
		}
		if(regions.get("digestRegionList")!=null) {
			maatCfg.setDigestRegionList(regions.get("digestRegionList"));
		}else {
			maatCfg.setDigestRegionList(new ArrayList<DigestCfg>());
		}
		if(regions.get("groupRelationList")!=null) {
			maatCfg.setGroupRelationList(regions.get("groupRelationList"));
			maatCfg.setGroupNum(regions.get("groupRelationList").size());
		}else {
			maatCfg.setGroupRelationList(new ArrayList<GroupCfg>());
			maatCfg.setGroupNum(0);
		}
		
		maatCfg.setAreaIpRegionList(new ArrayList<IpCfg>());
		maatCfg.setIsValid(_cfg.getIsValid());
		return maatCfg;
	}
	/**
	 * 分割并保存
	 * @param regionDict
	 * @param serviceDict
	 * @param specificServiceCfg
	 * @param asnNoMap
	 * @param list
	 * @param cfgIndexInfos
	 * @param appPolicyCfgs
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveAndSend(FunctionRegionDict regionDict,FunctionServiceDict serviceDict,
			SpecificServiceCfg specificServiceCfg,
			List<? extends BaseCfg> list,List<CfgIndexInfo> cfgIndexInfos,
			List<AppPolicyCfg> appPolicyCfgs,boolean send){
		if(list!=null&&list.size()>0) {
			String type=null;
			if (list.get(0) instanceof BaseIpCfg) {
				type="BaseIpCfg";
			}else if(list.get(0) instanceof BaseStringCfg) {
				type="BaseStringCfg";
			}else if(list.get(0) instanceof ComplexkeywordCfg) {
				type="ComplexkeywordCfg";
			}else if(list.get(0) instanceof DnsResStrategy) {
				type="DnsResStrategy";
			}else if(list.get(0) instanceof AsnIpCfg) {
				type="AsnIpCfg";
			}
			if (cfgIndexInfos != null && cfgIndexInfos.size() > 0) {
				this.saveCfgIndexOf(cfgIndexInfos);
			}
			if (appPolicyCfgs != null && appPolicyCfgs.size() > 0) {
				this.savePolicyList(appPolicyCfgs);
			}
			if("BaseIpCfg".equals(type)) {
				List<BaseIpCfg> listPage = (List<BaseIpCfg>) list;
				if (specificServiceCfg!=null&&regionDict.getFunctionId().equals(405)) {
					this.saveAppIpCfg((List<BaseIpCfg>)listPage,specificServiceCfg);
				}else {
					this.saveIpBatch(regionDict,serviceDict,(List<BaseIpCfg>)listPage,send);
				}
			}else if("AsnIpCfg".equals(type)) {
				List<AsnIpCfg> listPage = (List<AsnIpCfg>) list;
				this.saveAsnIpBatch(listPage);
			}else if("BaseStringCfg".equals(type)) {
				List<BaseStringCfg<?>> listPage = (List<BaseStringCfg<?>>) list;
				// 调用对应配置的service
				// ip_payload
				if (regionDict.getDictId().intValue() == 167) {
					// interceptCfgService.saveInterceptCfg(listPage);
					this.saveStringCfgBatch(regionDict,serviceDict,listPage, "intercept_pkt_bin",send);
				}else
				// http url
				if (regionDict.getDictId().intValue() == 9||regionDict.getDictId().intValue() == 15 || regionDict.getDictId().intValue() == 596) {
					// websiteCfgService.saveHttpUrlCfg(listPage);
					this.saveStringCfgBatch(regionDict,serviceDict,listPage, "http_url_cfg",send);
				}else
				// FTP url/content
				if (regionDict.getDictId().intValue() == 36||regionDict.getDictId().intValue() == 37) {
					// fileTransferCfgService.saveFtpCfg(listPage);
					this.saveStringCfgBatch(regionDict,serviceDict,listPage, "ftp_keyword_cfg",send);
				}else
				// P2P eMule Keyword
				if(regionDict.getDictId().intValue() == 158){
					this.saveStringCfgBatch(regionDict,serviceDict,listPage, "p2p_keyword_cfg",send);
				}else
				// P2P File Marking
				if(regionDict.getDictId().intValue() == 159){
					// fileTransferCfgService.saveP2pCfg(listPage);
					this.saveStringCfgBatch(regionDict,serviceDict,listPage, "p2p_hash_cfg",send);
				}else
				// BGP AS
				if (regionDict.getFunctionId().equals(61)) {
					// bgpCfgService.saveBgpAsCfg(listPage);
					this.saveStringCfgBatch(regionDict,serviceDict,listPage, "ntc_bgp_as_cfg",send);
				}else
				// SSL SNI/SAN/CN
				if (regionDict.getFunctionId().intValue() == 34) {
					// websiteCfgService.saveSslCfg(listPage);
					this.saveStringCfgBatch(regionDict,serviceDict,listPage, "ssl_keyword_cfg",send);
				}else
				// HTTP 请求/应答内容
				if(regionDict.getDictId().intValue()==12||regionDict.getDictId().intValue()==13||regionDict.getDictId().intValue()==601||regionDict.getDictId().intValue()==602) {
					// websiteCfgService.saveHttpBodyCfg(listPage);
					this.saveStringCfgBatch(regionDict,serviceDict,listPage, "http_body_cfg",send);
				}else
				// 流媒体协议
				if(regionDict.getDictId().intValue()==25){
					// avContentCfgService.saveContUrlCfg(listPage);
					this.saveStringCfgBatch(regionDict,serviceDict,listPage, "av_cont_url_cfg",send);
				}else
				// Voip Account
				if(regionDict.getDictId().intValue()==16){
					// avContentCfgService.saveVoIpAccountCfg(listPage);
					this.saveStringCfgBatch(regionDict,serviceDict,listPage, "av_voip_account_cfg",send);
				}else
				// APP域名特征
				if(regionDict.getDictId().intValue()==64){
					// appCfgService.saveAppDomainCfg(listPage); 
					this.saveStringCfgBatch(regionDict,serviceDict,listPage, AppDomainCfg.getTablename(),send);
				}
			}else if("ComplexkeywordCfg".equals(type)) {
				List<ComplexkeywordCfg> listPage = (List<ComplexkeywordCfg>) list;
				// DNS
				if (regionDict.getDictId().intValue() == 28) {
					// websiteCfgService.saveDnsCfg(listPage);
					this.saveComplexkeywordCfgBatch(regionDict,serviceDict,listPage, "dns_domain_cfg",send);
				}else
				// Mail
				if (regionDict.getDictId().intValue() == 30 || regionDict.getDictId().intValue() == 31||regionDict.getDictId().intValue() == 599) {
					// mailCfgService.saveMailCfg(listPage);
					this.saveComplexkeywordCfgBatch(regionDict,serviceDict,listPage, "mail_keyword_cfg",send);
				}else
				// HTTP请求头域
				if(regionDict.getDictId().intValue()==10) {
					// websiteCfgService.saveHttpReqHeadCfg(listPage);
					this.saveComplexkeywordCfgBatch(regionDict,serviceDict,listPage, "http_req_head_cfg",send);
				}else
				// HTTP响应头域
				if(regionDict.getDictId().intValue()==11) {
					// websiteCfgService.saveHttpResHeadCfg(listPage);
					this.saveComplexkeywordCfgBatch(regionDict,serviceDict,listPage, "http_res_head_cfg",send);
				}else
				// APP http特征
				if(regionDict.getDictId().intValue()==63) {
					// appCfgService.saveAppHttpCfg(listPage);
					this.saveComplexkeywordCfgBatch(regionDict,serviceDict,listPage, AppHttpCfg.getTablename(),send);
				}
			}else if("DnsResStrategy".equals(type)) {
				List<DnsResStrategy> listPage = (List<DnsResStrategy>) list;
				// dnsResStrategyService.saveDnsResStrategies(listPage);
				this.saveDnsResStrategyCfgBatch(listPage);
			}
		}	
	}
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveAsnIpBatch(List<AsnIpCfg> cfgs){
		logger.warn("Start to save IP,size:"+cfgs.size());
		long start=System.currentTimeMillis();
		SqlSessionFactory sqlSessionFactory=SpringContextHolder.getBean(SqlSessionFactory.class);
		SqlSession batchSqlSession = null;
		//需要通过新增域接口新增的ip集合
		List<AsnIpCfg> toAddRegionAsnIpCfgs=Lists.newArrayList();
		try{
			batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
			for(AsnIpCfg cfg:cfgs) {
				((AsnIpCfgDao) batchSqlSession.getMapper(AsnIpCfgDao.class)).insert(cfg);
				if(Constants.VALID_YES==cfg.getIsValid().intValue()) {
					toAddRegionAsnIpCfgs.add(cfg);
				}
			}
			batchSqlSession.commit();
		}finally {
			if(batchSqlSession != null){
				batchSqlSession.close();
			}
		}
		long end=System.currentTimeMillis();
		logger.warn("Save IP finish,cost:"+(end-start));
		if(toAddRegionAsnIpCfgs.size()>0) {
			asnIPRegionSendToMaat(toAddRegionAsnIpCfgs,Constants.VALID_YES);
		}
		cfgs.clear();
		toAddRegionAsnIpCfgs.clear();
		cfgs=null;
		toAddRegionAsnIpCfgs=null;
	}
	/**
	 * 导入配置时数据批量入库
	 * @param data
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveDnsResStrategyCfgBatch(List<DnsResStrategy> data) {
		SqlSessionFactory sqlSessionFactory=SpringContextHolder.getBean(SqlSessionFactory.class);
		SqlSession batchSqlSession = null;
		try{
			batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
			for(int index = 0; index < data.size();index++){
				DnsResStrategy cfg = data.get(index);
				((DnsResStrategyDao) batchSqlSession.getMapper(DnsResStrategyDao.class)).insert(cfg);
			}
			batchSqlSession.commit();
		}finally {
			if(batchSqlSession != null){
				batchSqlSession.close();
			}
		}
		
	}
	/**
	 * 同一个app下的ip批量保存
	 * @param cfgs
	 * @param specificServiceCfg
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = RuntimeException.class)
	public void saveAppIpCfg(List<BaseIpCfg> cfgs,SpecificServiceCfg specificServiceCfg){
		logger.warn("Start to save IP");
		ConfigGroupInfoDao configGroupInfoDao=SpringContextHolder.getBean(ConfigGroupInfoDao.class);
		SpecificServiceCfgDao specificServiceCfgDao=SpringContextHolder.getBean(SpecificServiceCfgDao.class);
		AppCfgDao appCfgDao=SpringContextHolder.getBean(AppCfgDao.class);
		long start=System.currentTimeMillis();
		ConfigGroupInfo configGroupInfo=configGroupInfoDao.getConfigGroupInfoByGroupId(specificServiceCfg.getGroupId());
		if(configGroupInfo==null) {//一般特定服务的组号是新增之后就有的，不会进入这一步。
			Integer groupId = ConfigServiceUtil.getId(2, 1).get(0);
			Integer compileId=ConfigServiceUtil.getId(1, 1).get(0);
			specificServiceCfg.setGroupId(groupId);
			configGroupInfo = new ConfigGroupInfo();
			configGroupInfo.setGroupId(specificServiceCfg.getGroupId());
			configGroupInfo.setGroupName(specificServiceCfg.getSpecServiceName());
			configGroupInfo.setIsIssued(0);
			configGroupInfo.setGroupType(1);
			configGroupInfo.setCompileId(compileId);
			configGroupInfoDao.insertConfigGroupInfo(configGroupInfo);
			//更新group_id
			specificServiceCfg.setOpTime(new Date());
			specificServiceCfgDao.update(specificServiceCfg);
		}
		Integer compileId = configGroupInfo.getCompileId();
//		Varibles maxPacket=asnIpCfgDao.getVaribles("max_allowed_packet");
		List<AppIpCfg> tempList=Lists.newArrayList();
		int len=0;
		for(BaseIpCfg cfg:cfgs) {
			AppIpCfg entity=new AppIpCfg();
			BeanUtils.copyProperties(cfg, entity);
			entity.setCompileId(compileId);
			int tempLen=entity.toString().getBytes(Charset.forName("UTF-8")).length;
			if((len+tempLen)<Constants.MAX_ALLOWED_PACKET) {
				tempList.add(entity);
				len+=tempLen;
			}else {
				appCfgDao.insertAppIpCfgBatch(tempList);
				tempList.clear();
				tempList.add(entity);
				len=tempLen;
			}
		}
		if(tempList.size()>0) {
			logger.warn("save ip size:"+tempList.size());
			appCfgDao.insertAppIpCfgBatch(tempList);
			tempList.clear();
		}
		long end=System.currentTimeMillis();
		logger.warn("Save IP finish,cost:"+(end-start));
	}
	
	/**
	 * 导入配置保存CfgIndex
	 * @param cfgIndexInfos
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveCfgIndexOf(List<CfgIndexInfo> cfgIndexInfos){
		SqlSessionFactory sqlSessionFactory=SpringContextHolder.getBean(SqlSessionFactory.class);
		SqlSession batchSqlSession = null;
		try{
			batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
			for(int index = 0; index < cfgIndexInfos.size();index++){
				CfgIndexInfo cfgIndexInfo = cfgIndexInfos.get(index);
				((IpCfgDao) batchSqlSession.getMapper(IpCfgDao.class)).saveCfgIndexForBatch(cfgIndexInfo);
			}
			batchSqlSession.commit();
		}finally {
			if(batchSqlSession != null){
				batchSqlSession.close();
			}
		}
	}
	/**
	 * 导入配置保存App策略
	 * @param appPolicyCfgs
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void savePolicyList(List<AppPolicyCfg> appPolicyCfgs){
		SqlSessionFactory sqlSessionFactory=SpringContextHolder.getBean(SqlSessionFactory.class);
		SqlSession batchSqlSession = null;
		try{
			batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
			for(int index = 0; index < appPolicyCfgs.size();index++){
				AppPolicyCfg _cfg = appPolicyCfgs.get(index);
				((AppCfgDao) batchSqlSession.getMapper(AppCfgDao.class)).insertAppPolicyCfgForBatch(_cfg);
			}
			batchSqlSession.commit();
		}finally {
			if(batchSqlSession != null){
				batchSqlSession.close();
			}
		}
	}
	/**
	 * 导入配置时数据批量入库（IP配置）
	 * 
	 * @param data
	 * @param tableName
	 */
	@Transactional(readOnly = false, rollbackFor = RuntimeException.class)
	public void saveIpBatch(FunctionRegionDict regionDict, FunctionServiceDict serviceDict,List<BaseIpCfg> cfgs, boolean send) {
		if (regionDict != null) {
			Integer regionType = regionDict.getRegionType();
			if (1 == regionType.intValue()) {
				SqlSessionFactory sqlSessionFactory=SpringContextHolder.getBean(SqlSessionFactory.class);
		        SqlSession batchSqlSession = null;
		        List<MaatCfg> configCompileList = new ArrayList();
		        List<InlineIp> callbackIpList = new ArrayList();
		        try{
		            batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
		            for(int index = 0; index < cfgs.size();index++){
		            	BaseIpCfg _cfg = cfgs.get(index);
		            	if (regionDict.getFunctionId().equals(301)) {
		            		DdosIpCfg ddosIpCfg=new DdosIpCfg();
		    				BeanUtils.copyProperties(_cfg, ddosIpCfg);
		    				//如果insert加入了select last_insert_id，会拖慢6~7倍以上的效率
		    				((DdosCfgDao) batchSqlSession.getMapper(DdosCfgDao.class)).insert(ddosIpCfg);
						}else if (regionDict.getFunctionId().equals(401)) {
							DnsIpCfg dnsIpCfg=new DnsIpCfg();
							BeanUtils.copyProperties(_cfg, dnsIpCfg);
							//如果insert加入了select last_insert_id，会拖慢6~7倍以上的效率
							((DnsIpCfgDao) batchSqlSession.getMapper(DnsIpCfgDao.class)).insert(dnsIpCfg);
						}else {
							batchSqlSession.getMapper(IpCfgDao.class).insertForBatch(_cfg);
						}
		              
		                if(send) {
		                	if(regionDict.getIsMaat().intValue()==1) {
			                	MaatCfg maatCfg=convertMaatCfg(_cfg,1);
			                	//userregion处理
								//限速需要发Droprate=0.001 ，暂不支持Bandwidth=200kbps
								if(_cfg.getAction().equals(Constants.RATELIMIT_ACTION)){
									if(_cfg.getUserRegion1().equals("0")){//丢包率
										_cfg.setUserRegion2(StringUtil.isEmpty(_cfg.getUserRegion2()) ? "":_cfg.getUserRegion2());
										maatCfg.setUserRegion(Constants.INTERCEPT_IP_RATELIMIT_DROPRATE_USER_REGION_KEY+"="+_cfg.getUserRegion2());
									}else if(_cfg.getUserRegion1().equals("1")){//带宽
										_cfg.setUserRegion3(StringUtil.isEmpty(_cfg.getUserRegion3()) ? "":_cfg.getUserRegion3());
										maatCfg.setUserRegion(Constants.INTERCEPT_IP_RATELIMIT_BANDWITH_USER_REGION_KEY+"="+_cfg.getUserRegion3());
									}
								}
			                	configCompileList.add(maatCfg);
		                	}else {
		                		//根据具体情况判断
		                		callbackIpList.add(this.convertCallBackIp(_cfg,_cfg.getDnsStrategyId()));
		                	}
		                }
		            }
		            if (send) {
			        	if(configCompileList.size() > 0) {
			        		ToMaatBean maatBean = new ToMaatBean();
							maatBean.setConfigCompileList(configCompileList);
							maatBean.setAuditTime(new Date());
							maatBean.setCreatorName(UserUtils.getUser().getName());
							maatBean.setVersion(Constants.MAAT_VERSION);
							maatBean.setOpAction(Constants.INSERT_ACTION);
							// 调用服务接口下发配置数据
							String json = BaseService.gsonToJson(maatBean);
							if(configCompileList.size()>100) {
								logger.info("IP 配置下发配置条数：" + configCompileList.size());
							}else {
								logger.info("IP 配置下发配置参数：" + json);
							}
							// 调用服务接口下发配置
							ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
							logger.info("IP 配置下发响应信息：" + result.getMsg());
			        	}else {
			        		//调用服务接口下发配置数据
							String json=gsonToJson(callbackIpList);
							logger.info("IP配置下发配置参数："+json);
							//调用服务接口下发配置
							try {
								ToMaatResult result = ConfigServiceUtil.postCallbackCfg(json);
								if(result!=null){
									logger.info("IP配置配置下发响应信息："+result.getMsg());
								}
							} catch (Exception e) {
								logger.error("IP配置配置下发失败",e);
								throw e;
							}
			        	}
					}
		            batchSqlSession.commit();
		        }finally {
		            if(batchSqlSession != null){
		                batchSqlSession.close();
		            }
		        }
			}
		}
        
    }
	/**
	 * 导入配置时数据批量入库（字符串配置）
	 * 
	 * @param data
	 * @param tableName
	 */
	@Transactional(readOnly = false, rollbackFor = RuntimeException.class)
	public void saveStringCfgBatch(FunctionRegionDict regionDict, FunctionServiceDict serviceDict,
			List<BaseStringCfg<?>> data, String tableName, boolean send) {
		if (regionDict != null) {
			Integer regionType = regionDict.getRegionType();
			if (2 == regionType.intValue()) {
				SqlSessionFactory sqlSessionFactory = SpringContextHolder.getBean(SqlSessionFactory.class);
				List<MaatCfg> configCompileList = new ArrayList();
				SqlSession batchSqlSession = null;
				try {
					batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
					for (int index = 0; index < data.size(); index++) {
						BaseStringCfg<?> _cfg = data.get(index);
						_cfg.setTableName(tableName);
						if(regionDict.getDictId().intValue()==64){
							AppDomainCfg cfg=new AppDomainCfg();
							BeanUtils.copyProperties(_cfg, cfg);
							((AppCfgDao) batchSqlSession.getMapper(AppCfgDao.class)).insertAppDomainCfgBatch(cfg);
						}else {
							((StringCfgDao) batchSqlSession.getMapper(StringCfgDao.class)).saveStringCfgBatch(_cfg);
						}
						if (send) {
							if(regionDict.getIsMaat().intValue()==1) {
								configCompileList.add(convertMaatCfg(_cfg,2));
								//userregion处理
							}
							
						}
					}
					if (send && configCompileList.size() > 0) {
						ToMaatBean maatBean = new ToMaatBean();
						maatBean.setConfigCompileList(configCompileList);
						maatBean.setAuditTime(new Date());
						maatBean.setCreatorName(UserUtils.getUser().getName());
						maatBean.setVersion(Constants.MAAT_VERSION);
						maatBean.setOpAction(Constants.INSERT_ACTION);
						// 调用服务接口下发配置数据
						String json = BaseService.gsonToJson(maatBean);
						if(configCompileList.size()>100) {
							logger.info("字符串配置下发配置条数：" + configCompileList.size());
						}else {
							logger.info("字符串配置下发配置参数：" + json);
						}
						// 调用服务接口下发配置
						ToMaatResult result = ConfigServiceUtil.postMaatCfg(json);
						logger.info("字符串配置下发响应信息：" + result.getMsg());

					}
					batchSqlSession.commit();
				} finally {
					if (batchSqlSession != null) {
						batchSqlSession.close();
					}
				}
			} else {
				throw new RuntimeException("Region type must be 2!");
			}
		}
	}
	/**
	 * 导入配置时数据批量入库（增强字符串配置）
	 * @param data
	 * @param tableName
	 */
	@Transactional(readOnly=false,rollbackFor=RuntimeException.class)
	public void saveComplexkeywordCfgBatch(FunctionRegionDict regionDict, FunctionServiceDict serviceDict,List<ComplexkeywordCfg> data, String tableName,boolean send) {
		SqlSessionFactory sqlSessionFactory=SpringContextHolder.getBean(SqlSessionFactory.class);
		SqlSession batchSqlSession = null;
		try{
			batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
			for(int index = 0; index < data.size();index++){
				ComplexkeywordCfg cfg = data.get(index);
                cfg.setTableName(tableName);
                if(regionDict.getDictId().intValue()==63) {
                	 AppHttpCfg _cfg=new AppHttpCfg();
     				BeanUtils.copyProperties(cfg, _cfg);
     				((AppCfgDao) batchSqlSession.getMapper(AppCfgDao.class)).insertAppHttpCfgBatch(_cfg);
                }else {
                	((StringCfgDao) batchSqlSession.getMapper(StringCfgDao.class)).saveComplexkeywordCfgBatch(cfg);
                }
               
				
			}
			batchSqlSession.commit();
		}finally {
			if(batchSqlSession != null){
				batchSqlSession.close();
			}
		}
	}
	/**
	 * asn ip分组复用新增
	 * @param asnIpCfgs
	 */
	public void asnIPRegionSendToMaat(List<AsnIpCfg> asnIpCfgs,Integer valid) {
		logger.warn("asnIPRegionSendToMaat start");
		long start=System.currentTimeMillis();
		GroupReuseAddBean maatBean = new GroupReuseAddBean();		
		List<GroupReuseCfg> groupReuseList=new ArrayList<>();
		GroupReuseCfg groupReuseCfg=new GroupReuseCfg();
		List<IpCfg> ipRegionList = groupReuseCfgAddRemoveConvert(asnIpCfgs,valid,null);
		List<StringCfg> strRegionList = new ArrayList<>();
		List<NumBoundaryCfg> numRegionList = new ArrayList<>();
		groupReuseCfg.setIpRegionList(ipRegionList);
		groupReuseCfg.setStrRegionList(strRegionList);
		groupReuseCfg.setNumRegionList(numRegionList);
		groupReuseList.add(groupReuseCfg);
		maatBean.setGroupReuseCfgList(groupReuseList);
		maatBean.setAuditTime(new Date());
		maatBean.setCreatorName(UserUtils.getUser().getName());
		maatBean.setVersion(Constants.MAAT_VERSION);
		if(valid==Constants.VALID_YES) {
			maatBean.setOpAction(Constants.INSERT_ACTION);
			//调用服务接口下发配置数据
			String json=gsonToJson(maatBean);
			if(asnIpCfgs.size()<=100) {
				logger.info("asn ip复用域新增配置下发配置参数："+json);
			}else {
				logger.info("asn ip复用域新增配置下发region条数："+asnIpCfgs.size());
			}
			//调用服务接口下发配置
			ToMaatResult result =ConfigServiceUtil.postGroupReuseSources(json);
			logger.info("asn ip复用域新增配置响应信息："+result.getMsg());
		}else {
			maatBean.setOpAction(Constants.UPDATE_ACTION);
			//调用服务接口下发配置数据
			String json=gsonToJson(maatBean);
			if(asnIpCfgs.size()<=100) {
				logger.info("asn ip复用域删除配置下发配置参数："+json);
			}else {
				logger.info("asn ip复用域删除配置下发region条数："+asnIpCfgs.size());
				
			}
			//调用服务接口下发配置
			ToMaatResult result = ConfigServiceUtil.put(json,3);
			logger.info("asn ip复用域删除配置响应信息："+result.getMsg());
		}
		long end=System.currentTimeMillis();
		logger.warn("asnIPRegionSendToMaat finish,cost:"+(end-start));
	}
}
