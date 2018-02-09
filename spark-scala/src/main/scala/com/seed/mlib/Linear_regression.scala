package com.seed.mlib

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression._
import org.apache.spark.mllib.linalg.DenseVector
/**
 * 线必回归测试
 */
object Linear_regression {
  def main(args:Array[String]):Unit={
    val conf = new SparkConf().setAppName("linear_regression").setMaster("local[2]")
    val sc = new SparkContext(conf)
    sc.setLogLevel("WARN")
    val data = sc.textFile("mlib.txt", 2)
    println(data.count())
    println(data.first())
    val trainSet = data.map {line=>
    val parts = line.split(',')
    LabeledPoint(parts(0).toDouble,Vectors.dense(parts(1).split(' ').map(_.toDouble)))}
    trainSet.take(2).foreach(println)
    val numIterations = 100//迭代次数
    val stepSize = 1 // 每次迭代步长
    val minBatchFraction = 1.0 // 样本参与迭代比例
//    val model = LinearRegressionWithSGD.train(trainSet, numIterations,stepSize,minBatchFraction)
    val model = LinearRegressionWithSGD.train(trainSet, 100,0.0001)
//    println(" ======================================>>>>>>")
    println(model.weights)
    println(model.intercept)
    println("===>>> " + model.predict(new DenseVector(Array(175,5))))//48平 3房
    
    
  }
}