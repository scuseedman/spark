/** 
 * Project Name:spark-java 
 * File Name:HbaseUtil.java 
 * Date:2017年12月8日下午3:39:44 
 * 
* 　　　　　　　　┏┓　　　┏┓
 * 　　　　　　　┏┛┻━━━┛┻┓
 * 　　　　　　　┃　　　　　　　┃ 　
 * 　　　　　　　┃　　　━　　　┃
 * 　　　　　　　┃　＞　　　＜　┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃...　⌒　... ┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃　　　　　　　　　　
 * 　　　　　　　　　┃　　　┃   神兽无影，BUG无踪！
 * 　　　　　　　　　┃　　　┃　　　　　　　　　　　
 * 　　　　　　　　　┃　　　┃  　　　　　　
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　　　　　　　　　　
 * 　　　　　　　　　┃　　　┗━━━┓
 * 　　　　　　　　　┃　　　　　　　┣┓
 * 　　　　　　　　　┃　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛
*
*
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛Code is far away from bug with the animal protecting
 * 　　　　┃　　　┃    神兽无影，BUG无踪！
 * 　　　　┃　　　┃
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 *
 * ━━━━━━感觉萌萌哒━━━━━━
*/ 
package com.seed.utils;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/** 
 * ClassName:HbaseUtil
 * Date:     2017年12月8日 下午3:39:44 
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class HbaseUtil {
	static Configuration conf = null;
	static{
		/**
		 * the attribute in the file hbase/conf/hbase-site.xml for the setting hbase.zookeeper.quorum
		 */
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", "hadoop01:2181,hadoop02:2181,hadoop03:2181");
	}
	public static boolean put(String tableName, String rowkey, String columnFamily, String qualifier, String value) {
	    try {
	        HTable table = new HTable(conf, Bytes.toBytes(tableName));
	        Put put = new Put(Bytes.toBytes(rowkey));
	        put.add(columnFamily.getBytes(), qualifier.getBytes(), value.getBytes());
	        put.add(Bytes.toBytes(columnFamily),  Bytes.toBytes(qualifier), Bytes.toBytes(value));
	        table.put(put);
	        System.out.println("put successfully！ " + rowkey + "," + columnFamily + "," + qualifier + "," + value);
	        table.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	    return true;
	}
	
	public static Result getResult(String tableName, String rowKey)  
            throws IOException {  
        Get get = new Get(Bytes.toBytes(rowKey));  
        HTable table = new HTable(conf, Bytes.toBytes(tableName));
        Result result = table.get(get);  
        System.out.println("the size of the result is : " + result.rawCells().length);
    for (Cell rowKV : result.rawCells()) {
    	System.out.println("RowKey: " + new String(CellUtil.cloneRow(rowKV)));
    	System.out.println("Timestamp: " + rowKV.getTimestamp() + " ");
        System.out.println("column Family: " + new String(CellUtil.cloneFamily(rowKV)) + " ");
        System.out.println("column Name:  " + new String(CellUtil.cloneQualifier(rowKV)) + " ");
        System.out.println("Value: " + new String(CellUtil.cloneValue(rowKV)) + " ");
        System.out.println("-------------------------------------------");  
    }
    table.close();
    return result;  
    }  
	
	public static Result getResultByColumn(String tableName, String rowKey,  
            String familyName, String columnName) throws IOException {  
        HTable table = new HTable(conf, Bytes.toBytes(tableName));  
        Get get = new Get(Bytes.toBytes(rowKey));  
        get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName)); 
        Result result = table.get(get);  
        System.out.println("the size of the result is : " + result.rawCells().length);
        for (Cell rowKV : result.rawCells()) {  
            System.out.println("family:" + Bytes.toString(CellUtil.cloneFamily(rowKV)));  
            System.out.println("qualifier:" + Bytes.toString(CellUtil.cloneQualifier(rowKV)));  
            System.out.println("value:" + Bytes.toString(CellUtil.cloneValue(rowKV)));  
            System.out.println("Timestamp:" + rowKV.getTimestamp());  
            System.out.println("-------------------------------------------");  
        }  
        table.close();
        return result;
    } 
	
	public static boolean put_finance(String tableName, String rowkey, String columnFamily, String qualifier, String value) {
	    try {
	    	Integer v2 = 0;
	    	Result res = getResultByColumn("test","counter","cf",qualifier);//先获取结果，有则累加，无则直接put
	    	Integer v1 = 0;
	    	if (null != res){
	    		for (Cell rowKV : res.rawCells()) {
	    			String s = Bytes.toString(CellUtil.cloneValue(rowKV));
	    			System.out.println("the string of get is : " + s);
	    			v1 = Integer.valueOf(s);
	    			System.out.println("the v1 is : " + v1);
	    	    }
	    	} 
	    	v2 = v1 + Integer.valueOf(value).intValue();
	    	System.out.println("the value of v2 is : " + v2);
	    	put(tableName,rowkey,columnFamily,qualifier,v2 + "");//put the data into hbase !!
	    }catch (IOException e) {
		        e.printStackTrace();
		        return false;
		    }
		    return true;
		}
	public static void main(String[] args) throws IOException {
//		put("test","counter","cf","zhangfei","1");
		put_finance("test","counter","cf","zhangfei","1");
//		getResult("test","counter");
//		getResultByColumn("test","counter","cf","zhangfei");
	}
}
