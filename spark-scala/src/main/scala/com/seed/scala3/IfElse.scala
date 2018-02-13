package com.seed.scala3

import java.util.Random

object IfElse {
  def main(args:Array[String]):Unit={
//    testIfElse
  }
  /**
   * if else 判断
   */
  def testIfElse(){
    val rand = new Random()
    for (i <- 1 to 10 ){
    	var num = rand.nextInt(230)
      if(num < 10){
        println(num + " ===>>> 是一个小于10 的数 ")
      }else if(num < 50){
    	  println(num + " ===>>> 是一个小于50 的数 ")
      }else if(num < 100){
    	  println(num + " ===>>> 是一个小于100 的数 ")
      }else if(num < 200){
    	  println(num + " ===>>> 是一个小于200 的数 ")
      }else{
    	  println(num + " ===>>> 是一个小于230 的数 ")
      }
    }
    for(x <- Array.range(1, 20)){
      println(" --->>> " + x)
    }
  }
}