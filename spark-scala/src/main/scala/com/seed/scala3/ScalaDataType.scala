package com.seed.scala3

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import scala.io.Source
/**
 * scala 数据类型
 */
object ScalaDataType {
  def main(args:Array[String]):Unit={
    val reader = new BufferedReader(new FileReader("scala-data-type"))//读取失败
    //可以按行读取
//    Source.fromFile("scala-data-type").foreach { x => print(x) }
    //也可以按行读取
//    Source.fromFile("scala-data-type").getLines().foreach { line => println(line) }
//    while(reader.readLine() != null){//读取失败，只能读到一半
//      println(reader.readLine())
//    }
//    reader.close()
    val line = """Byte  8位有符号补码整数。数值区间为 -128 到 127
    		Short 16位有符号补码整数。数值区间为 -32768 到 32767
    		Int 32位有符号补码整数。数值区间为 -2147483648 到 2147483647
    		Long  64位有符号补码整数。数值区间为 -9223372036854775808 到 9223372036854775807
    		Float 32位IEEE754单精度浮点数
    		Double  64位IEEE754单精度浮点数
    		Char  16位无符号Unicode字符, 区间值为 U+0000 到 U+FFFF
    		String  字符序列
    		Boolean true或false
    		Unit  表示无值，和其他语言中void等同。用作不返回任何结果的方法的结果类型。Unit只有一个实例值，写成()。
    		Null  null 或空引用
    		Nothing Nothing类型在Scala的类层级的最低端；它是任何其他类型的子类型。
    		Any Any是所有其他类的超类
    		AnyRef  AnyRef类是Scala里所有引用类(reference class)的基类"""
    val arr = line.split("\\n")
    println("length of arr ==>> " + arr.length)
    for (x <- arr ){
      println(x)
      println(" --------------------- 萌萌的分割线 ----------------------")
    }
  }
}