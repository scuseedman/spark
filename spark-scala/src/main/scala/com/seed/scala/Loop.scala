package com.seed.scala
/**
 * scala循环表达式
 * while
 * do...while
 * for 
 */
object Loop {
  def main(args: Array[String]): Unit = {
    var a = 10
    while(a < 30){
      println("====> a : " + a )
      a += 1
    }
    
    var b = 0
    do{
      println("=====> b = " + b)
      b += 1
    }while(b < 20)
      
      for(c <- 1 to 10){
        println("=====>  c = " + c )
      }
    
    var z = List(1,2,3,4,5,6,7)
    for (d <- z){
      println("====> z = " + d)
    }
    
  }
}