package com.seed.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Collection;


/**
 * 负责DMS项目中读写的工具类
 * @author _seed
 *
 */
public class IOUtil {
	/**
	 * 将给定的long值以字符串的形式写入到
	 * 给定文件中的第一行
	 * @param l
	 * @param file
	 * @throws Exception 
	 */
	public static void saveLong(long l,File file)
							    throws Exception{
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(file);
			pw.println(l);
		} catch (Exception e) {
			throw e;
		} finally{
			if(pw != null){
				pw.close();
			}
		}
	}
	
	/**
	 * 将给定的集合中的每一个元素的toString返回的
	 * 字符串以行为单位写入到给定的文件中
	 * @param c
	 * @param file
	 * @throws Exception
	 */
	public static void saveCollection(
					  @SuppressWarnings("rawtypes") Collection c,File file)
							throws Exception{
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new FileOutputStream(file,true));
			for(Object o : c){
				pw.println(o);
			}
		} catch (Exception e) {
			throw e;
		} finally{
			if(pw != null){
				pw.close();
			}
		}
	}
	
	/**
	 * 从给定的RandomAccessFile当前位置连续读取
	 * 给定长度的字节量，并转换为一个字符串后返回。
	 * @param raf
	 * @param len
	 * @return
	 * @throws Exception
	 */
	public static String readString(
			RandomAccessFile raf,int len)
						throws Exception{
		try {
			byte[] data = new byte[len];
			raf.read(data);
			String str = new String(
				data,"ISO8859-1"
			);
			return str;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 从给定的文件中读取第一行字符串，然后将其
	 * 转换为一个long值后返回
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static long readLong(File file)
							throws Exception{
		BufferedReader br = null;
		try {
			br = new BufferedReader(
					new InputStreamReader(
						new FileInputStream(
							file	
						)	
					)
				 );
			String line = br.readLine();
			long lon = Long.parseLong(line);
			return lon;
		} catch (Exception e) {
			throw e;
		} finally{
			if(br != null){
				br.close();
			}
		}
	}
}






