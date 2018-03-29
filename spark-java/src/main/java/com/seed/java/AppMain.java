/** 
 * Project Name:spark-java 
 * File Name:AppMain.java 
 * Package Name:com.seed.java 
 * Date:2018年3月19日下午2:10:52 
 * Copyright (c) 2018,  All Rights Reserved. 
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
 * 　　　　　　　　　┃　　　┃　欣赏一个人,始于颜值,敬于智慧,久于善良,终于人品.　　　　　　　　　　
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
 * 　　　　┃　　　┃    欣赏一个人,始于颜值,敬于智慧,久于善良,终于人品.
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
/** 
 * ClassName:AppMain
 * Date:     2018年3月19日 下午2:10:52 
 * DESC:	
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class AppMain {
	public   static   void  main(String[] args)  //main 方法本身放入方法区。    
	{    
		Student test1 = new  Student( " 赵云1 " );   //test1是引用，所以放到栈区里， Student是自定义对象应该放到堆里面    
		Student test2 = new  Student( " 赵云2 " );    
		
		test1.printName();    
		test2.printName();    
	}    
}  
class  Student        //运行时, jvm 把appmain的信息都放入方法区    
{    
	/** 范例名称 */    
	private String   name;      //new Student实例后， name 引用放入栈区里，  name 对象放入堆里    
	
	/** 构造方法 */    
	public  Student(String name)    
	{    
		this .name = name;    
	}    
	
	/** 输出 */    
	public   void  printName()   //print方法本身放入 方法区里。    
	{    
		System.out.println(name);    
	}    
}   
