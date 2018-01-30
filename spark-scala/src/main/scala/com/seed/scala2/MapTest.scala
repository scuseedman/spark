package com.seed.scala2

object MapTest {
  def main(args:Array[String]){
    mapTest()
  }
  def mapTest(){
    var map = Map("name" -> "zhangfei","age" -> 33,"addr" -> "langzhong")
    println("name ===>>> " + map("name"))
    println("age ===>>> " + map("age"))
    println("addr ===>>> " + map("addr"))
    
    map = Map(("name","尼古拉斯.guanyu"),("age",34),("addr","jingzhou"))
    println(map("addr"))
    println(map.contains("addra"))
    var addres = if(map.contains("addr"))map("addr")else "rhb"
    println("addr : " + addres)
    println("age ===>>> " + map.getOrElse("ag2e", 99))
    println("++++++++ 适当的分割线 ++++++++")
    for((key,value) <- map)println(key + " ===>>> " + value)//遍历key value
    println("++++++++ 适当的分割线 ++++++++")//遍历所有的key
    for(key <- map.keySet)println(" key ===>>> " + key)
    println("++++++++ 适当的分割线 ++++++++")//遍历所有的value
   for(value <- map.values)println("value ===>>> " + value)
    //对map的key value进行反转操作
    println("++++++++ 适当的分割线 ++++++++")//map反转操作
    var map2 = for((key,value) <- map)yield(value,key)
    for((key,value) <- map2)println(key + " >>> " + value)
    
    var tuple = ("尼古拉斯.liubei",22)
    println("name ===>>> " + tuple._1 + " ; age ===>>> " + tuple._2)
    
  }
}