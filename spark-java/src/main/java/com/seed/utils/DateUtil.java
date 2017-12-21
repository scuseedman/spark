package com.seed.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {

    /**
     * 根据unix时间戳，返回秒
     * 
     * @param timestamp
     * @return String  yyyyMMddHHmmss
     */
    public static String uTdStrSeconds(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date(timestamp * 1000L));
    }
    
    /**
     * 根据unix时间戳，返回秒
     * 
     * @param timestamp
     * @return String  yyyyMMddHHmmss
     */
    public static String uTdStrSeconds(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date(Long.parseLong(timestamp) * 1000L));
    }
    
    /**
     * 根据unix时间戳，返回时间
     * 格式   yyyy-MM-dd hh:mm:ss
     */
    public static String uTdStrTime(String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdf.format(new Date(Long.parseLong(timestamp) * 1000L));
    }
    
    /**
     * 获取距timestamp时间戳before秒的yyyyMMddHHmmss时间的List集合
     * 
     * @param timestamp 时间戳
     * @param before    往前推几秒
     * @return
     */
    public static List<Long> getBeforeSeconds(String timestamp, int before) {
        List<Long> seconds = new ArrayList<>();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        
        for (int i = before; i >= 0; i--) {
            calendar.setTime(new Date(Long.parseLong(timestamp) * 1000L));
            calendar.add(Calendar.SECOND, -i);
            seconds.add(Long.parseLong(sdf.format(calendar.getTime())));
        }
        
        return seconds;
    }
}
