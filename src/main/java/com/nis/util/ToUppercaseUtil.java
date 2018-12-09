/**
 *@Title: ToUppercaseUtil.java
 *@Package com.nis.util 
 *@Description TODO
 *@author dell
 *@date 2018年6月28日 下午1:39:46
 *@version  版本号
 */
package com.nis.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: ToUppercaseUtil.java 
 * @Description: 替换message_zh_CN.properties的ascii符号由小写至大写
 * @author (wx)
 * @date 2018年6月28日 下午1:39:46
 * @version V1.0
 */
public class ToUppercaseUtil {
	public static void main(String[] args) {
		File file=new File("C:\\Users\\Administrator\\git\\NFS\\src\\main\\resources\\messages\\message_zh_CN.properties");
		try {
			RandomAccessFile raf=new RandomAccessFile(file, "rw");
			long lastPoint=0;
			String line=null;
			while((line=raf.readLine())!=null){
				if(line.startsWith("#")){
					lastPoint=raf.getFilePointer();
					continue;
				}else{
					if(StringUtils.isNotBlank(line)){
						String oldline=line;
						String[] split=line.split("=");
						String expression="\\\\u[0-9a-f]{4}";
						Pattern p=Pattern.compile(expression);
						Matcher matcher=p.matcher(split[1]);
						while(matcher.find()){
							String ascii=matcher.group();
							String newAscii="\\u"+ascii.substring(2).toUpperCase();
							if(!ascii.equals(newAscii)){
								line=line.replace(ascii, newAscii);
							}
							
						}
						final long point=raf.getFilePointer();
						if(line.equals(oldline)){
							lastPoint=point;
							continue;
						}else{
							raf.seek(lastPoint);
							System.out.println("replace "+oldline+" with "+line);
							raf.write(line.getBytes());
							lastPoint=point;
						}
						
					}else{
						lastPoint=raf.getFilePointer();
						continue;
					}
				}
			}
			
			raf.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
