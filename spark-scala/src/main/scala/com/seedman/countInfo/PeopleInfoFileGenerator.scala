package com.seedman.countInfo

import java.io.File
import java.io.FileWriter
import java.util.Random

object PeopleInfoFileGenerator {
  def main(args:Array[String]):Unit={
    val writer = new FileWriter(new File("./some_people_info"))
    val rand = new Random()
//    println(System.getProperty("line.separator"))
    for(i <- 1 to 1000000){
      var height = rand.nextInt(220)
      if(height < 50) height += 50
      var gender = getRandomGender()
      if(height < 100 && gender == "M"){
        height = height + 100
      }else if(height < 100 && gender == "F"){
    	  height = height + 50
      }
      
      writer.write(i + " " + gender + " " + height)
      writer.write(System.getProperty("line.separator"))
    }
    writer.flush()
    writer.close()
    println("People Information File generated successfully.")
  }
  def getRandomGender():String={
    val rand =  new Random()
    var num = rand.nextInt(2) + 1
    if(num % 2 == 0){
      "M"
    }else{
      "F"
    }
  }
}  