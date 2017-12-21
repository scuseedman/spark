package com.formax.scala
/**
 * scala 文件操作
 * Scala 进行文件写操作，直接用的都是 java中 的 I/O 类 （java.io.File)：
 */
import java.io._
import scala.io.Source

object FileTest {
  def main(args:Array[String]){
//      readFile("hello.txt")//将内容写入文件
      readFromFile("hello.txt")
    }
  def readFromFile(f:String) ={
    Source.fromFile(f).foreach { 
      print //打印每一行
    }
  }
  /**
   * 写入内容到文件中
   */
  def readFile(f:String) ={
    println("===> scala文件操作演示")
    try {
      var writer = new PrintWriter(new File(f))
      var nums = 1
      while(nums <= 100){
        printf("这是第 %2d 个scala菜鸟\n", nums)
        writer.println("这是第 " + nums + " 个scala菜鸟 ！！")
        nums += 1
      }
      writer.close()
      
    } catch {
      case ex: FileNotFoundException => {
        ex.printStackTrace() // TODO: handle error
        println("程序文件没有找到")
        }
      }
  }
}