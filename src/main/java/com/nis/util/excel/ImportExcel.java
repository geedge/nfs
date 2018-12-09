/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.nis.util.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.protobuf.generated.CellProtos.CellType;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.nis.domain.FunctionRegionDict;
import com.nis.domain.FunctionServiceDict;
import com.nis.domain.configuration.template.ComplexStringAllTemplate;
import com.nis.domain.configuration.template.IpAllTemplate;
import com.nis.domain.configuration.template.StringAllTemplate;
import com.nis.exceptions.ServiceException;
import com.nis.util.DictUtils;
import com.nis.util.Reflections;

/**
 * 导入Excel文件（支持“XLS”和“XLSX”格式）
 * @author ThinkGem
 * @version 2013-03-10
 */
public class ImportExcel {
	
	private static Logger log = LoggerFactory.getLogger(ImportExcel.class);
			
	/**
	 * 工作薄对象
	 */
	private Workbook wb;
	
	/**
	 * 工作表对象
	 */
	private Sheet sheet;
	
	/**
	 * 标题行号
	 */
	private int headerNum;
	
	/**
	 * 构造函数
	 * @param path 导入文件，读取第一个工作表
	 * @param headerNum 标题行号，数据行号=标题行号+1
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public ImportExcel(String fileName, int headerNum) 
			throws InvalidFormatException, IOException {
		this(new File(fileName), headerNum);
	}
	
	/**
	 * 构造函数
	 * @param path 导入文件对象，读取第一个工作表
	 * @param headerNum 标题行号，数据行号=标题行号+1
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public ImportExcel(File file, int headerNum) 
			throws InvalidFormatException, IOException {
		this(file, headerNum, 0);
	}

	/**
	 * 构造函数
	 * @param path 导入文件
	 * @param headerNum 标题行号，数据行号=标题行号+1
	 * @param sheetIndex 工作表编号
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public ImportExcel(String fileName, int headerNum, int sheetIndex) 
			throws InvalidFormatException, IOException {
		this(new File(fileName), headerNum, sheetIndex);
	}
	
	/**
	 * 构造函数
	 * @param path 导入文件对象
	 * @param headerNum 标题行号，数据行号=标题行号+1
	 * @param sheetIndex 工作表编号
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public ImportExcel(File file, int headerNum, int sheetIndex) 
			throws InvalidFormatException, IOException {
		this(file.getName(), new FileInputStream(file), headerNum, sheetIndex);
	}
	
	/**
	 * 构造函数
	 * @param file 导入文件对象
	 * @param headerNum 标题行号，数据行号=标题行号+1
	 * @param sheetIndex 工作表编号
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public ImportExcel(MultipartFile multipartFile, int headerNum, int sheetIndex) 
			throws InvalidFormatException, IOException {
		this(multipartFile.getOriginalFilename(), multipartFile.getInputStream(), headerNum, sheetIndex);
	}

	/**
	 * 构造函数
	 * @param path 导入文件对象
	 * @param headerNum 标题行号，数据行号=标题行号+1
	 * @param sheetIndex 工作表编号
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public ImportExcel(String fileName, InputStream is, int headerNum, int sheetIndex) 
			throws InvalidFormatException, IOException {
		if (StringUtils.isBlank(fileName)){
			throw new RuntimeException("导入文档为空!");
		}else if(fileName.toLowerCase().endsWith("xls")){    
			this.wb = new HSSFWorkbook(is);    
        }else if(fileName.toLowerCase().endsWith("xlsx")){  
        	this.wb = new XSSFWorkbook(is);
        }else{  
        	throw new RuntimeException("文档格式不正确!");
        }  
		if (this.wb.getNumberOfSheets()<sheetIndex){
			throw new RuntimeException("文档中没有工作表!");
		}
		this.sheet = this.wb.getSheetAt(sheetIndex);
		this.headerNum = headerNum;
		log.debug("Initialize success.");
	}
	
	/**
	 * 获取行对象
	 * @param rownum
	 * @return
	 */
	public Row getRow(int rownum){
		return this.sheet.getRow(rownum);
	}

	/**
	 * 获取数据行号
	 * @return
	 */
	public int getDataRowNum(){
		return headerNum+1;
	}
	
	/**
	 * 获取最后一个数据行号
	 * @return
	 */
	public int getLastDataRowNum(){
		if(headerNum==0){
			return this.sheet.getLastRowNum()+getDataRowNum();
		}else{
			return this.sheet.getLastRowNum()+headerNum;
		}
		
	}
	
	/**
	 * 获取最后一个列号
	 * @return
	 */
	public int getLastCellNum(){
		return this.getRow(headerNum).getLastCellNum();
	}
	
	/**
	 * 获取单元格值
	 * @param row 获取的行
	 * @param column 获取单元格列号
	 * @return 单元格值
	 */
	public Object getCellValue(Row row, int column){
		Object val = "";
		try{
			Cell cell = row.getCell(column);
			if (cell != null){
				if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
					val = cell.getNumericCellValue();
				}else if (cell.getCellType() == Cell.CELL_TYPE_STRING){
					val = cell.getStringCellValue();
				}else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA){
					val = cell.getCellFormula();
				}else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){
					val = cell.getBooleanCellValue();
				}else if (cell.getCellType() == Cell.CELL_TYPE_ERROR){
					val = cell.getErrorCellValue();
				}
			}
		}catch (Exception e) {
			return val;
		}
		return val;
	}
	/**
	 * 获取单元格值
	 * @param row 获取的行
	 * @param column 获取单元格列号
	 * @return 单元格值
	 */
	public Object getStringCellValue(Row row, int column){
		Object val = "";
		try{
			Cell cell = row.getCell(column);
			if (cell != null){
				if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
					HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
					val = dataFormatter.formatCellValue(cell);
				}else if (cell.getCellType() == Cell.CELL_TYPE_STRING){
					val = cell.getStringCellValue();
				}else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA){
					val = cell.getCellFormula();
				}else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){
					val = cell.getBooleanCellValue();
				}else if (cell.getCellType() == Cell.CELL_TYPE_ERROR){
					val = cell.getErrorCellValue();
				}
			}
		}catch (Exception e) {
			return val;
		}
		return val;
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
	 * 获取导入数据列表
	 * @param cls 导入对象类型
	 * @param groups 导入分组
	 */
	/*public <E> List<E> getDataList(Class<E> cls,Properties props, int... groups) throws InstantiationException, IllegalAccessException{
		List<Object[]> annotationList = Lists.newArrayList();
		// Get annotation field 
//		Field[] fs = cls.getDeclaredFields();
		List<Field> fs=new ArrayList<Field>();
		// Get annotation field 
		//递归获取cls实体对象及父级对象的属性
		getFields(fs, cls);
		for (Field f : fs){
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
		//log.debug("Import column count:"+annotationList.size());
		// Get excel data
		List<E> dataList = Lists.newArrayList();
		Row header = this.getRow(headerNum);
		List<Object[]> annotationListCopy=Lists.newArrayList();
		for(int i=header.getFirstCellNum();i<=header.getLastCellNum();i++) {
			Object value=this.getCellValue(header, i);
			for(Object[] os:annotationList) {
				ExcelField ef = (ExcelField)os[0];
				String title=ef.title();
				if(props.getProperty(title).equals(value.toString())) {
					annotationListCopy.add(os);
					break;
				}
			}
		}
		if(annotationListCopy.size()==0) {
			throw new ServiceException(props.getProperty("template_error"));
		}
		if(IpAllTemplate.class.isAssignableFrom(cls)||cls.getSimpleName().equals("IpAllTemplate")) {
			boolean has=false;
			for(Object[] os:annotationListCopy) {
				ExcelField ef = (ExcelField)os[0];
				if(ef.title().equals("client_ip")||ef.title().equals("server_ip")) {
					has=true;
					break;
				}
			}
			if(!has) {
				throw new ServiceException(props.getProperty("template_error"));
			}
		}else if(StringAllTemplate.class.isAssignableFrom(cls)||cls.getSimpleName().equals("StringAllTemplate")){
			boolean has=false;
			for(Object[] os:annotationListCopy) {
				ExcelField ef = (ExcelField)os[0];
				if(ef.title().equals("key_word")) {
					has=true;
					break;
				}
			}
			if(!has) {
				throw new ServiceException(props.getProperty("template_error"));
			}
		}else if(ComplexStringAllTemplate.class.isAssignableFrom(cls)||cls.getSimpleName().equals("ComplexStringAllTemplate")){
			boolean has=false;
			for(Object[] os:annotationListCopy) {
				ExcelField ef = (ExcelField)os[0];
				if(ef.title().equals("key_word")) {
					has=true;
					break;
				}
			}
			if(!has) {
				throw new ServiceException(props.getProperty("template_error"));
			}
		}
		// Field sorting
		Collections.sort(annotationList, new Comparator<Object[]>() {
			public int compare(Object[] o1, Object[] o2) {
				return new Integer(((ExcelField)o1[0]).sort()).compareTo(
						new Integer(((ExcelField)o2[0]).sort()));
			};
		});
		//annotationList.clear();
		for (int i = this.getDataRowNum(); i < this.getLastDataRowNum(); i++) {
			E e = (E)cls.newInstance();
			int column = 0;
			Row row = this.getRow(i);
			StringBuilder sb = new StringBuilder();
			for (Object[] os : annotationList){
				Object val = this.getCellValue(row, column++);
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
				}
				sb.append(val+", ");
			}
			dataList.add(e);
			log.debug("Read success: ["+i+"] "+sb.toString());
		}
		return dataList;
	}*/
	/**
	 * 获取导入数据列表
	 * @param cls 导入对象类型
	 * @param groups 导入分组
	 */
	public <E> List<E> getDataList(Class<E> cls,Properties props,FunctionRegionDict regionDict,FunctionServiceDict serviceDict, int... groups) throws InstantiationException, IllegalAccessException{
		if(regionDict==null) {
			throw new RuntimeException("regionDict is null!");
		}
		List<Object[]> annotationList = Lists.newArrayList();
		// Get annotation field 
//		Field[] fs = cls.getDeclaredFields();
		List<Field> fs=new ArrayList<Field>();
		//从region中获取配置信息
		Integer regionType=regionDict.getRegionType();
		//ip相关
		boolean srcIpShow=false;
		boolean destIpShow=false;
		boolean srcPortShow=false;
		boolean destPortShow=false;
		boolean directionShow=false;
		boolean protocolShow=false;
		//字符串公共属性
		boolean matchMethodShow=false;
		boolean hexShow=false;
		boolean isCaseSensitiveShow=false;
		//增强字符串相关
		boolean districtShow=false;
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
		//log.debug("Import column count:"+annotationList.size());
		// Get excel data
		List<E> dataList = Lists.newArrayList();
		/*Row header = this.getRow(headerNum);
		List<Object[]> annotationListCopy=Lists.newArrayList();
		for(int i=header.getFirstCellNum();i<=header.getLastCellNum();i++) {
			Object value=this.getCellValue(header, i);
			for(Object[] os:annotationList) {
				ExcelField ef = (ExcelField)os[0];
				String title=ef.title();
				if(props.getProperty(title).equals(value.toString())) {
					annotationListCopy.add(os);
					break;
				}
			}
		}
		if(annotationListCopy.size()==0) {
			throw new ServiceException(props.getProperty("template_error"));
		}
		if(IpAllTemplate.class.isAssignableFrom(cls)||cls.getSimpleName().equals("IpAllTemplate")) {
			boolean has=false;
			for(Object[] os:annotationListCopy) {
				ExcelField ef = (ExcelField)os[0];
				if(ef.title().equals("client_ip")||ef.title().equals("server_ip")) {
					has=true;
					break;
				}
			}
			if(!has) {
				throw new ServiceException(props.getProperty("template_error"));
			}
		}else if(StringAllTemplate.class.isAssignableFrom(cls)||cls.getSimpleName().equals("StringAllTemplate")){
			boolean has=false;
			for(Object[] os:annotationListCopy) {
				ExcelField ef = (ExcelField)os[0];
				if(ef.title().equals("key_word")) {
					has=true;
					break;
				}
			}
			if(!has) {
				throw new ServiceException(props.getProperty("template_error"));
			}
		}else if(ComplexStringAllTemplate.class.isAssignableFrom(cls)||cls.getSimpleName().equals("ComplexStringAllTemplate")){
			boolean has=false;
			for(Object[] os:annotationListCopy) {
				ExcelField ef = (ExcelField)os[0];
				if(ef.title().equals("key_word")) {
					has=true;
					break;
				}
			}
			if(!has) {
				throw new ServiceException(props.getProperty("template_error"));
			}
		}*/
		// Field sorting
		Collections.sort(annotationList, new Comparator<Object[]>() {
			public int compare(Object[] o1, Object[] o2) {
				return new Integer(((ExcelField)o1[0]).sort()).compareTo(
						new Integer(((ExcelField)o2[0]).sort()));
			};
		});
		//annotationList.clear();
		for (int i = this.getDataRowNum(); i < this.getLastDataRowNum(); i++) {
			E e = (E)cls.newInstance();
			int column = 0;
			Row row = this.getRow(i);
			StringBuilder sb = new StringBuilder();
			for (Object[] os : annotationList){
//				Object val = this.getCellValue(row, column++);
				Object val = this.getStringCellValue(row, column++);
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
				}
				sb.append(val+", ");
			}
			dataList.add(e);
			log.debug("Read success: ["+i+"] "+sb.toString());
		}
		return dataList;
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
