package com.seedman.app

import java.io.FileWriter
import java.io.File
import java.util.Random

object CreateFile {
  def main(args:Array[String]):Unit={
    val writer = new FileWriter(new File("./peoples_ages"))
    val rand = new Random()
    for( i <- 1 to 1000000){
      writer.write( i + " " + rand.nextInt(100))
      writer.write(System.getProperty("line.separator"))
    }
    writer.flush()
    writer.close()
  }
}