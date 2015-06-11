import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.classification.{NaiveBayes, NaiveBayesModel}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.evaluation.MulticlassMetrics
import org.apache.spark.mllib.linalg.{ SparseVector => SV }
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.mllib.feature.IDF

/* Preparation:
 *  cd maildir
 *  mkdir ../all-mail
 *  find * -type f | awk '{ str=$0; gsub(/\//, "!", str); print "mv " $0 " ../all-mail/" str }' | bash
 *  cd ../all-mail
 *  ls > ../mail-index
 *  cd ..
 *  shuf mail-index | head -n 1024 > trainset
 *  mkdir train-mail
 *  for i in `cat trainset`; do cp all-mail/$i train-mail/; done
 *  then manually sort
 */

val datapath="all-mail/"
 /* trainpath has {spam, ham} directories */
val trainpath="train-mail/*"
/* separate based on if spam or ham */
val train_rdd = sc.wholeTextFiles(trainpath)
val trainLabels = train_rdd.map { case (file, text) =>
  file.split("/").takeRight(2).head
}

def tokenize(content: String) : Seq[String] = {
  // split by word, but include internal punctuation
  // so we can have domain "enron.com", etc.
  // but strip ending punctuation
  val pure_numbers="""[^0-9]*""".r
  return content.split("""\W+""")
    .map(_.toLowerCase)
    .filter(token => pure_numbers.pattern.matcher(token).matches)
    .toSeq
}

// train the filter ...
val trainingMap =
  trainLabels.distinct.collect().zipWithIndex.toMap

/* construct tf-idf vector */
val rdd = train_rdd
val dim=math.pow(2,19).toInt
val hashingTF = new HashingTF(dim)
val rdd_tf = rdd.map { case (file,text) =>
  hashingTF.transform(tokenize(text)) }
val idf = new IDF().fit(rdd_tf)
val rdd_tfidf = idf.transform(rdd_tf)
val train_tfidf = rdd_tfidf

val zipped = trainLabels.zip(train_tfidf)
val train = zipped.map {case (label, vector) =>
  LabeledPoint(trainingMap(label), vector) }
train.cache

// train it
val model = NaiveBayes.train(train, lambda=0.1)

// run it on the training data to test
val prediction = train.map(p => (model.predict(p.features), p.label))
val accuracy = 1.0 * prediction.filter(x=>x._1 == x._2).count() / train.count()
print(accuracy)

//todo: get tf-idf of whole set, run naive bayes on whole set
//val data_rdd=sc.wholeTextFiles(path)
