package com.seed.scala2

object HelloScala {
  def main(args:Array[String]):Unit={
    println("hello scala")
    var nums = 10
    println("nums ===>>> " + nums)
    nums = 11
    println("nums ===>>> " + nums)
    if(nums > 12 ){
    	println("nums >>>> 12")
    	println("unt ytd ")
    }else{
    	println("nums <<<< 12")
    	println("unt ytd ")
    }
    
    var name = "zhangfei"
    var age = 12
    printf("my name is : %s , and my age is : %d \n" ,name,age)
    
    name = readLine("please enter your name here : ")
    age = Integer.valueOf(readLine("please enter your age here : "))
    printf("my name is : %s , and my age is : %d \n" ,name,age)
    if(age < 18){
      printf("sorry %s , your age is %d less 18 , you cann't in !!! \n",name,age)
      System.exit(1);
    }else{
    	printf("welcome %s , your age is %d more 18 , have a good day in it  !!! \n",name,age)
    }
    
    
    
    
     
    
    
    
  }
}