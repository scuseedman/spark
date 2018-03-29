/** 
 * Project Name:spark-java 
 * File Name:RDD2DataFrameReflection.java 
 * Package Name:com.seed.sparkSQL 
 * Date:2018年3月19日下午3:56:09 
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
package com.seed.sparkSQL;

import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import com.seed.entity.Student;


/** 
 * ClassName:RDD2DataFrameReflection
 * Date:     2018年3月19日 下午3:56:09 
 * DESC:	
 * @author   lwd 
 * @version   
 * @since    JDK 1.7
 * @see       
 */
public class RDD2DataFrameReflection {
	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("reflection rdd");
		JavaSparkContext jsc = new JavaSparkContext(conf);
		jsc.setLogLevel("WARN");
		
		SQLContext sqlContext = new SQLContext(jsc);
		
		JavaRDD<String> lines = jsc.textFile("students").filter(new Function<String, Boolean>() {
			private static final long serialVersionUID = 1L;
			public Boolean call(String line) throws Exception {
				return line.split(",").length == 3;
			}
		});
		JavaRDD<Student> students = lines.map(new Function<String, Student>() {
			private static final long serialVersionUID = 1L;
			public Student call(String line) throws Exception {
				return new Student(line);
			}
		});
		
		DataFrame stuDF = sqlContext.createDataFrame(students, Student.class);
		stuDF.registerTempTable("students");
		DataFrame teen_res = sqlContext.sql("select id,name,age from students where age > 20");
		System.out.println(teen_res.first());
		JavaRDD<Row> teenagerRDD = teen_res.javaRDD();
		JavaRDD<Student> reverse_students = teenagerRDD.map(new Function<Row, Student>() {
			private static final long serialVersionUID = 1L;
			public Student call(Row row) throws Exception {
				Student stu = new Student();
				stu.setAge(row.getInt(0));
				stu.setName(row.getString(1));
				stu.setAge(row.getInt(2));
				return stu;
			}
			
		});
		
		List<Student> student_lists = reverse_students.collect();
		for(Student stu: student_lists){
			System.out.println("name ===>>> " + stu.getName() + " ; age ==>> " + stu.getAge());
		}
		jsc.close();
	}
}
