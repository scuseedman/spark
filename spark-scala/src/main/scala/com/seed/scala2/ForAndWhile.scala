package com.seed.scala2
import scala.util.control.Breaks._
object ForAndWhile {
  def main(args:Array[String]):Unit={
    println("the demo for the while and for ")
    var n = 10
    for( i<-6 to n){
      printf("i --->>> %d \n" ,i)
    }
    
    for(c<- "hello world"){
//      if(n == 5)break;
      println("c ===>>> " + c)
      n -= 1
    }
    
    for(i <- 100 to 1 if i % 2 == 0)println("i ===>>>  " + i )
    
    println(" ======================")
    for(i <- 1 until 9; j <- 1 until 9){
      if(j == 9 ){
        println(i * j )
      }else{
        println(i * j + " ")
      }
    }
  }
}