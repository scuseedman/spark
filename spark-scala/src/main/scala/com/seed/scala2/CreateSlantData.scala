package com.seed.scala2

import java.util.Random
import java.io.PrintWriter
import java.io.File

/**
 * 制造一个倾斜的数据集
 */
object CreateSlantData {
  def main(args:Array[String]):Unit={
    val rand = new Random()
    val writer = new PrintWriter(new File("slantData.txt"))
    
    for(i <- 1 to 900000){
      var num = rand.nextInt(1000)
      if(num < 10){
        writer.write("zhangfei_" + 8 + "\n")
      }else{
    	  writer.write("zhangfei_" + 6  + "\n")
      }
    }
    writer.close()
  }
}