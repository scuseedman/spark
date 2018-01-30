package com.seed.scala2

import java.io.FileReader
import java.io.FileNotFoundException
import java.io.IOException

object Exception {
  def main(args:Array[String]){
    try{
      val f = new FileReader("input.txt")
      throw new IOException
    }catch{
      case ex:FileNotFoundException=>{
        ex.printStackTrace()
        println("fileNotFoundException !!!")
      }
      case ex:IOException=>{
        ex.printStackTrace()
        println("IoException occured ...")
      }
    }finally{
      println("close source !!!")
    }
  }
}