package com.seed.scala3

object KeepKeyWords {
  def main(args:Array[String]):Unit={
    println(" ===>>> scala 程序开始")
    println(" 本程序将要输出所有scala程序的保留关键字 ...")
    /**
     * 所有scala程序中使用的关键字
     */
    val keywords = "abstract  case  catch class def do  else  extends false final finally for forSome if  implicit  import lazy match new null object override  package private protected return  sealed  super this  throw trait try true  type  val var while with  yield  -  : = => <- <:  <%  >: #  @   "
    val arr = keywords.replaceAll("  ", " ").split(" ")
    println(" length of arr ===>>> " + arr.length)
    //遍历所有关键字
    for(x <- arr){
      println(" keep key words ===>>> ::: " + x)
    }
  }
}