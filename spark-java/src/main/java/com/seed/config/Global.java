/** 
 * Project Name:spark-java 
 * File Name:Global.java 
 * Package Name:com.seed.config 
 * Date:2017年12月25日下午5:32:54 
 * Copyright (c) 2017,  All Rights Reserved. 
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
package com.seed.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;



/** 
 * ClassName:Global
 * Date:     2017年12月25日 下午5:32:54 
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class Global {

    private static Properties prop;
//    private static ClassLoader classloader;
    private static InputStream in;
    
    static {
        prop = new Properties();
        
        String sysName = System.getProperty("os.name").toLowerCase();
        String path = sysName.contains("windows")?"E:\\formax_workspace\\spark\\spark-java\\src\\main\\resources\\config\\config.properties"
        		:"/data/formax_data/data_test/shell-scripts/config.properties";
        //如果是这样加载方式，文件需要在集群的每台服务器上都存在。否则有些excutor会报空指针
        
//       String path = Global.class.getResource("/resources/config/config.properties").getPath();
        System.out.println("path ===> " + path);
        
        Global.initConfig(path);
//        classloader = Thread.currentThread().getContextClassLoader();
//        in = classloader.getResourceAsStream("/config/config.properties");
        try {
//        	prop.load(new FileInputStream(path));
//        	prop.load(Global.class.getResourceAsStream("config/config.properties"));
//            prop.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 如果指定了配置文件路径，则使用指定配置文件初始化，否则用默认的
     * 
     * @param confPath
     */
    public static void initConfig(String confPath) {
        try {
            in = new FileInputStream(confPath);
            prop.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 从配置文件中获取配置
     * 
     * @param key
     * @return String
     */
    public static String getConfVal(String key) {
        return prop.getProperty(key);
    }


}
