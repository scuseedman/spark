package com.seed.mlib
import org.apache.spark.SparkContext
import org.apache.spark.mllib.classification.SVMWithSGD
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.{Matrices, Vectors, Vector}
import org.apache.spark.SparkConf

/**
 * 案例：导入训练数据集，然后在训练集上执行训练算法，最后在所得模型上进行预测并计算训练误差。
 */
object SparkStudy {
  def main(args:Array[String]):Unit={
     val conf = new SparkConf().setAppName("mlib demo").setMaster("local[2]")
		  // 加载和解析数据文件
     val sc = new SparkContext(conf)
     sc.setLogLevel("WARN")
     //1 0 1 1 1
     //0 1 0 0 1
     val data = sc.textFile("peoples_ages")

      val parsedData = data.map { line =>
         val parts = line.split(' ')
         LabeledPoint(parts(0).toDouble, Vectors.dense(parts.tail.map(x => x.toDouble)))
        }
    // 设置迭代次数并进行进行训练

      val numIterations = 20

      val model = SVMWithSGD.train(parsedData, numIterations)

      // 统计分类错误的样本比例

      val labelAndPreds = parsedData.map { point =>
        val prediction = model.predict(point.features)
        (point.label, prediction)
        }

      val trainErr = labelAndPreds.filter(r => r._1 != r._2).count.toDouble / parsedData.count

      println("Training Error = " + trainErr)
  }
}