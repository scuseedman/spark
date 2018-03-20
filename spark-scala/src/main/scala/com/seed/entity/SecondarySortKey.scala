package com.seed.entity

class SecondarySortKey (val money:Int, val hands :Int) extends Ordered[SecondarySortKey] with Serializable {
  def compare(that: SecondarySortKey): Int = {
    if(this.money - that.money != 0){
      this.money - that.money
    }else{
      this.hands - that.hands
    }
  }
}