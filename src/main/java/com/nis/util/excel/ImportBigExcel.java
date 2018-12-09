/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.nis.util.excel;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.google.common.collect.Lists;
import com.nis.domain.FunctionRegionDict;
import com.nis.domain.FunctionServiceDict;
import com.nis.util.DictUtils;
import com.nis.util.Reflections;

/**
 * 导入Excel文件（支持“XLS”和“XLSX”格式）
 * @author ThinkGem
 * @version 2013-03-10
 */
public class ImportBigExcel extends XLSXCovertCSVReader{
	
	private static Logger log = LoggerFactory.getLogger(ImportBigExcel.class);
	/**
	 * 标题行号
	 */
	private Integer headerNum;
	private int sheetIndex;
	private Properties props;
	private File uploadFile;
	private FunctionRegionDict regionDict;
	private FunctionServiceDict serviceDict;
	//ip相关
	private boolean srcIpShow=false;
	private boolean destIpShow=false;
	private boolean srcPortShow=false;
	private boolean destPortShow=false;
	private boolean directionShow=false;
	private boolean protocolShow=false;
	//字符串公共属性
	private boolean matchMethodShow=false;
	private boolean hexShow=false;
	private boolean isCaseSensitiveShow=false;
	//增强字符串相关
	private boolean districtShow=false;
	private List<Object[]> annotationList;
	private List<List<Object>> dataList=Lists.newArrayList();
	/**
	 * 构造函数
	 * @param file 导入文件对象
	 * @param headerNum 标题行号，数据行号=标题行号+1
	 * @param sheetIndex 工作表编号
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public ImportBigExcel(MultipartFile multipartFile, Integer headerNum, int sheetIndex) 
			throws InvalidFormatException, IOException {
		File upFile=new File(UUID.randomUUID()+"_"+multipartFile.getOriginalFilename());
		upFile.mkdirs();
		multipartFile.transferTo(upFile);
		if (!upFile.exists()){
			throw new RuntimeException("导入文档为空!");
		}else if(upFile.getName().toLowerCase().endsWith("xls")){
			throw new RuntimeException("xls not supported");
        }else if(upFile.getName().toLowerCase().endsWith("xlsx")){  
        }else{  
        	throw new RuntimeException("文档格式不正确!");
        }  
		this.sheetIndex = sheetIndex;
		this.headerNum = headerNum;
		this.uploadFile=upFile;
		log.debug("Initialize success.");
	}
	/**
	 * //递归获取cls实体对象及父级对象的属性
	 * wx:修改，子类覆盖父类的同名方法
	 * @param list
	 * @param cls
	 */
	public void getFields(List<Field> list,Class<?> cls) {
		Field[] fields=cls.getDeclaredFields();
		if(fields != null && fields.length > 0){
			List<Field> tempList=new ArrayList<>();
			for (Field field : fields) {
				if(list.size()==0) {
					tempList.add(field);
				}else {
					boolean has=false;
					for(Field checkF:list) {
						if(checkF.getName().equals(field.getName())) {
							has=true;
							break;
						}
					}
					if(!has) {
						tempList.add(field);
					}
				}
			}
			list.addAll(tempList);
		}
		if(cls.getSuperclass() != null){
			getFields(list,cls.getSuperclass());
		}
	}
	/**
	 * //递归获取cls实体对象及父级对象的method
	 * @param list
	 * @param cls
	 */
	public void getMethods(List<Method> list,Class<?> cls) {
		Method[] methods=cls.getDeclaredMethods();
		if(methods != null && methods.length > 0){
			List<Method> tempList=new ArrayList<>();
			for (Method method : methods) {
				if(list.size()==0) {
					tempList.add(method);
				}else {
					boolean has=false;
					for(Method checkM:list) {
						if(checkM.getName().equals(method.getName())) {
							has=true;
							break;
						}
					}
					if(!has) {
						tempList.add(method);
					}
				}
			}
			list.addAll(tempList);
		}
		if(cls.getSuperclass() != null){
			getMethods(list,cls.getSuperclass());
		}
	}
	
	/**
	 * 加载region,service,注解
	 * @param cls
	 * @param props
	 * @param regionDict
	 * @param serviceDict
	 * @param groups
	 */
	public void loadInitParams(Class cls,Properties props,FunctionRegionDict regionDict,FunctionServiceDict serviceDict, int... groups) {
		this.props=props;
		this.regionDict=regionDict;
		this.serviceDict=serviceDict;
		//从region中获取配置信息
		Integer regionType=regionDict.getRegionType();
		
		if(regionType==1) {
			String ipPortShow=regionDict.getConfigIpPortShow();
			if(StringUtils.isNotBlank(ipPortShow)) {
				if(ipPortShow.indexOf("1")>-1) {
					srcIpShow=true;
				}
				if(ipPortShow.indexOf("2")>-1) {
					srcPortShow=true;
				}
				if(ipPortShow.indexOf("3")>-1) {
					destIpShow=true;
				}
				if(ipPortShow.indexOf("4")>-1) {
					destPortShow=true;
				}
			};
			//协议方向等，如果只有一个值，就 不需要输入了
			String direction=regionDict.getConfigDirection();
			if(StringUtils.isNotBlank(direction)&&direction.indexOf(",")>-1) {
				directionShow=true;
			}
			String protocol=regionDict.getConfigProtocol();
			if(StringUtils.isNotBlank(protocol)&&protocol.indexOf(",")>-1) {
				protocolShow=true;
			}
			//packet ip reject
			if(regionDict.getFunctionId().equals(5)&&serviceDict!=null&&serviceDict.getServiceId().equals(16)) {
				protocolShow=false;
			}
		}else if(regionType==2||regionType==3){
			String matchMethod= regionDict.getConfigMatchMethod();
			if(StringUtils.isNotBlank(matchMethod)&&matchMethod.indexOf(",")>-1) {
				matchMethodShow=true;
			}
			String hex= regionDict.getConfigHex();
			if(StringUtils.isNotBlank(hex)&&hex.indexOf(",")>-1) {
				hexShow=true;
				isCaseSensitiveShow=true;
			}
			String district=regionDict.getConfigDistrict();
			if(StringUtils.isNotBlank(district)&&district.indexOf(",")>-1) {
				districtShow=true;
			}
		}
		List<Object[]> annotationList = Lists.newArrayList();
		List<Field> fs=new ArrayList<Field>();
		// Get annotation field 
		//递归获取cls实体对象及父级对象的属性
		getFields(fs, cls);
		for (Field f : fs){
			//排除不需要导出的字段
			if(!srcIpShow) {
				if(f.getName().equalsIgnoreCase("srcIpAddress")) {
					continue;
				}
			}
			if(!destIpShow) {
				if(f.getName().equalsIgnoreCase("destIpAddress")) {
					continue;
				}
			}
			if(!srcPortShow) {
				if(f.getName().equalsIgnoreCase("srcPort")) {
					continue;
				}
			}
			if(!destPortShow) {
				if(f.getName().equalsIgnoreCase("destPort")) {
					continue;
				}
			}
			if(!directionShow) {
				if(f.getName().equalsIgnoreCase("direction")) {
					continue;
				}
			}
			if(!protocolShow) {
				if(f.getName().equalsIgnoreCase("protocol")) {
					continue;
				}
			}
			if(!matchMethodShow) {
				if(f.getName().equalsIgnoreCase("matchMethod")) {
					continue;
				}
			}
			if(!hexShow) {
				if(f.getName().equalsIgnoreCase("isHex")) {
					continue;
				}
			}
			if(!isCaseSensitiveShow) {
				if(f.getName().equalsIgnoreCase("isCaseInsenstive")) {
					continue;
				}
			}
			if(!districtShow) {
				if(f.getName().equalsIgnoreCase("district")) {
					continue;
				}
			}
			ExcelField ef = f.getAnnotation(ExcelField.class);
			if (ef != null && (ef.type()==0 || ef.type()==2)){
				if (groups!=null && groups.length>0){
					boolean inGroup = false;
					for (int g : groups){
						if (inGroup){
							break;
						}
						for (int efg : ef.groups()){
							if (g == efg){
								inGroup = true;
								annotationList.add(new Object[]{ef, f});
								break;
							}
						}
					}
				}else{
					annotationList.add(new Object[]{ef, f});
				}
			}
		}
		// Get annotation method
//		Method[] ms = cls.getDeclaredMethods();
		List<Method> ms=new ArrayList<Method>();
		// Get annotation method
		//递归获取cls实体对象及父级对象的属性
		getMethods(ms, cls);
		for (Method m : ms){
			//排除不需要导出的字段
			if(!srcIpShow) {
				if(m.getName().equalsIgnoreCase("get"+"srcIpAddress")||m.getName().equalsIgnoreCase("set"+"srcIpAddress")) {
					continue;
				}
			}
			if(!destIpShow) {
				if(m.getName().equalsIgnoreCase("get"+"destIpAddress")||m.getName().equalsIgnoreCase("set"+"destIpAddress")) {
					continue;
				}
			}
			if(!srcPortShow) {
				if(m.getName().equalsIgnoreCase("get"+"srcPort")||m.getName().equalsIgnoreCase("set"+"srcPort")) {
					continue;
				}
			}
			if(!destPortShow) {
				if(m.getName().equalsIgnoreCase("get"+"destPort")||m.getName().equalsIgnoreCase("set"+"destPort")) {
					continue;
				}
			}
			if(!directionShow) {
				if(m.getName().equalsIgnoreCase("get"+"direction")||m.getName().equalsIgnoreCase("set"+"direction")) {
					continue;
				}
			}
			if(!protocolShow) {
				if(m.getName().equalsIgnoreCase("get"+"protocol")||m.getName().equalsIgnoreCase("set"+"protocol")) {
					continue;
				}
			}
			if(!matchMethodShow) {
				if(m.getName().equalsIgnoreCase("get"+"matchMethod")||m.getName().equalsIgnoreCase("set"+"matchMethod")) {
					continue;
				}
			}
			if(!hexShow) {
				if(m.getName().equalsIgnoreCase("get"+"isHex")||m.getName().equalsIgnoreCase("set"+"isHex")) {
					continue;
				}
			}
			if(!isCaseSensitiveShow) {
				if(m.getName().equalsIgnoreCase("get"+"isCaseInsenstive")||m.getName().equalsIgnoreCase("set"+"isCaseInsenstive")) {
					continue;
				}
			}
			if(!districtShow) {
				if(m.getName().equalsIgnoreCase("get"+"district")||m.getName().equalsIgnoreCase("set"+"district")) {
					continue;
				}
			}
			ExcelField ef = m.getAnnotation(ExcelField.class);
			if (ef != null && (ef.type()==0 || ef.type()==2)){
				if (groups!=null && groups.length>0){
					boolean inGroup = false;
					for (int g : groups){
						if (inGroup){
							break;
						}
						for (int efg : ef.groups()){
							if (g == efg){
								inGroup = true;
								annotationList.add(new Object[]{ef, m});
								break;
							}
						}
					}
				}else{
					annotationList.add(new Object[]{ef, m});
				}
			}
		}
		// Field sorting
		Collections.sort(annotationList, new Comparator<Object[]>() {
			public int compare(Object[] o1, Object[] o2) {
				return new Integer(((ExcelField)o1[0]).sort()).compareTo(
						new Integer(((ExcelField)o2[0]).sort()));
			};
		});
		this.annotationList=annotationList;
	}
	@Override
	public List<Object> optRows(int sheetIndex, int curRow, List<Object> rowlist) {
		// TODO Auto-generated method stub
		List<Object> rowlistCopy=Lists.newArrayList();
		rowlistCopy.addAll(rowlist);
		dataList.add(rowlistCopy);
		return null;
	}
	/**
	 * 获取导入数据列表
	 * @param cls 导入对象类型
	 * @param groups 导入分组
	 * @throws SQLException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws OpenXML4JException 
	 * @throws IOException
	 */
	public <E> BlockingQueue<E> getDataList(Class<E> cls) throws InstantiationException, IllegalAccessException, IOException, OpenXML4JException, ParserConfigurationException, SAXException, SQLException{
		log.warn("start to load data...");
		this.processOneSheet(uploadFile, this.sheetIndex);
		long start=System.currentTimeMillis();
		if(regionDict==null) {
			throw new RuntimeException("regionDict is null!");
		}
		// Get excel data
		BlockingQueue<E> _dataList =new ArrayBlockingQueue(dataList.size(),true);
		for (int i = 0; i < dataList.size(); i++) {
			E e = (E)cls.newInstance();
			if(i<=headerNum) {
				continue;
			}
			int column = 0;
			List<Object> row=dataList.get(i);
			StringBuilder sb = new StringBuilder();
			for (Object[] os : annotationList){
				Object val=row.get(column);
				column++;
				if (val != null){
					ExcelField ef = (ExcelField)os[0];
					// If is dict type, get dict value
					if (StringUtils.isNotBlank(ef.dictType())){
						Object val1 = DictUtils.getDictCode(ef.dictType(), val.toString(), "");
						//没有获取到字典值的话会影响验证判断
						if(val1!=null&&StringUtils.isNotBlank(val1.toString())) {
							val=val1;
						}
						//log.debug("Dictionary type value: ["+i+","+colunm+"] " + val);
					}
					// Get param type and type cast
					Class<?> valType = Class.class;
					if (os[1] instanceof Field){
						valType = ((Field)os[1]).getType();
					}else if (os[1] instanceof Method){
						Method method = ((Method)os[1]);
						if ("get".equals(method.getName().substring(0, 3))){
							valType = method.getReturnType();
						}else if("set".equals(method.getName().substring(0, 3))){
							valType = ((Method)os[1]).getParameterTypes()[0];
						}
					}
					//log.debug("Import value type: ["+i+","+column+"] " + valType);
					try {
						if (valType == String.class){
							String s = String.valueOf(val.toString());
							//0.0.0.0表示任意IP的含义
							if(StringUtils.endsWith(s, ".0") && !s.endsWith("0.0.0.0")){
								val = StringUtils.substringBefore(s, ".0");
							}else{
								val = String.valueOf(val.toString());
							}
						}else if (valType == Integer.class){
							val = Double.valueOf(val.toString()).intValue();
						}else if (valType == Long.class){
							val = Double.valueOf(val.toString()).longValue();
						}else if (valType == Double.class){
							val = Double.valueOf(val.toString());
						}else if (valType == Float.class){
							val = Float.valueOf(val.toString());
						}else if (valType == Date.class){
							val = DateUtil.getJavaDate((Double)val);
						}else{
							if (ef.fieldType() != Class.class){
								val = ef.fieldType().getMethod("getValue", String.class).invoke(null, val.toString());
							}else{
								val = Class.forName(this.getClass().getName().replaceAll(this.getClass().getSimpleName(), 
										"fieldtype."+valType.getSimpleName()+"Type")).getMethod("getValue", String.class).invoke(null, val.toString());
							}
						}
					} catch (Exception ex) {
						log.info("Get cell value ["+i+","+column+"] error: " + ex.toString());
						val = null;
					}
					// set entity value
					if (os[1] instanceof Field){
						Reflections.invokeSetter(e, ((Field)os[1]).getName(), val);
					}else if (os[1] instanceof Method){
						String mthodName = ((Method)os[1]).getName();
						if ("get".equals(mthodName.substring(0, 3))){
							mthodName = "set"+StringUtils.substringAfter(mthodName, "get");
						}
						Reflections.invokeMethod(e, mthodName, new Class[] {valType}, new Object[] {val});
					}
					//设置索引值
					Reflections.invokeSetter(e, "index", (i+1));
				}
				sb.append(val+", ");
			}
			_dataList.offer(e);
			row.clear();
			log.debug("Read success: ["+i+"] "+sb.toString());
		}
		long end=System.currentTimeMillis();
		log.warn(" load data finish,size:"+_dataList.size()+",cost:"+(end-start));
		return _dataList;
	}
	public File getUploadFile() {
		return uploadFile;
	}
	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

//	/**
//	 * 导入测试
//	 */
//	public static void main(String[] args) throws Throwable {
//		
//		ImportExcel ei = new ImportExcel("target/export.xlsx", 1);
//		
//		for (int i = ei.getDataRowNum(); i < ei.getLastDataRowNum(); i++) {
//			Row row = ei.getRow(i);
//			for (int j = 0; j < ei.getLastCellNum(); j++) {
//				Object val = ei.getCellValue(row, j);
//				System.out.print(val+", ");
//			}
//			System.out.print("\n");
//		}
//		
//	}
	
}
