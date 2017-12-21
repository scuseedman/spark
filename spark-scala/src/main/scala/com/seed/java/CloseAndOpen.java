/** 
 * Project Name:spark-hello 
 * File Name:CloseAndOpen.java 
 * Package Name:com.formax.java 
 * Date:2017年12月19日上午9:55:56 
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
package com.seed.java;

import java.util.Scanner;

/** 
 * ClassName:CloseAndOpen
 * Date:     2017年12月19日 上午9:55:56 
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
/**
 * 题目1013：开门人和关门人

题目描述： 
每天第一个到机房的人要把门打开，最后一个离开的人要把门关好。现有一堆杂乱的机房签到、签离记录，请根据记录找出当天开门和关门的人。 
输入： 
测试输入的第一行给出记录的总天数N ( N> 0 )，下面列出了N天的记录。 
每天的记录在第一行给出记录的条目数M (M > 0 )，下面是M行，每行的格式为

证件号码 签到时间 签离时间 

其中时间按“小时:分钟:秒钟”（各占2位）给出，证件号码是长度不超过15的字符串。
1
2
3
4
输出： 
对每一天的记录输出1行，即当天开门和关门人的证件号码，中间用1空格分隔。 
注意：在裁判的标准测试输入中，所有记录保证完整，每个人的签到时间在签离时间之前，且没有多人同时签到或者签离的情况。 
样例输入： 
3 
1 
ME3021112225321 00:00:00 23:59:59 
2 
EE301218 08:05:35 20:56:35 
MA301134 12:35:45 21:40:42 
3 
CS301111 15:30:28 17:00:10 
SC3021234 08:00:00 11:25:25 
CS301133 21:45:00 21:58:40 
样例输出： 
ME3021112225321 ME3021112225321 
EE301218 MA301134 
SC3021234 CS301133 
来源： 
2005年浙江大学计算机及软件工程研究生机试真题
 */
public class CloseAndOpen {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()) {
            int num = scanner.nextInt();
            String[][] mark = new String[num][];
            String[] out = new String [num];
            for(int i=0; i < num ;i++) {
                int numerery = scanner.nextInt();
                scanner.nextLine();
                mark[i] = new String[numerery];
                for(int j=0; j < numerery; j++) {
                    mark[i][j] = scanner.nextLine();
                }
                out[i] = deal(mark[i]);
            }
            for(String str : out) {
                System.out.println(str);
            }
             
        }
        scanner.close();
	}
	public static String deal(String[] data) {
        StringBuilder strBuilder = new StringBuilder();
        int open = 0 ,close =0;
        String[] split = data[0].split("\\s+");
        String strOpen = split[1];
        String strClose = split[2];
        for(int i=1; i < data.length; i++) {
            String[] tempSplit = data[i].split("\\s+");
            if(tempSplit[1].compareTo(strOpen) < 0)
                open = i;
        }
        for(int i=1; i < data.length; i++) {
            String[] tempSplit = data[i].split("\\s+");
            if(tempSplit[2].compareTo(strClose) > 0)
                close = i;
        }
         
        split = data[open].split("\\s+");
        strBuilder.append(split[0]);
        split = data[close].split("\\s+");
        strBuilder.append(" ").append(split[0]);
        return strBuilder.toString();
         
         
    }
}
