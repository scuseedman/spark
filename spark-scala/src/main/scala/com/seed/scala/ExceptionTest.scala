package com.seed.scala
/**
 * scala异常处理
 * 
 */
import java.io.FileReader
import java.io.FileNotFoundException
import java.io.IOException

object ExceptionTest {
  def main(args:Array[String]){
    println("==========> 程序开始执行，测试scala的异常处理机制")
    try {
      val f = new FileReader("readme.txt")
      
    } catch {
      case ex: FileNotFoundException => {
        ex.printStackTrace() // TODO: handle error
        println("程序执行失败，找不到指定的文件")
      }
      case ex:IOException =>{
        ex.printStackTrace()
        println("程序执行失败，抛出了IOException ！！")
      }
    }finally {
        println("程序必将执行")
      }
  }
}