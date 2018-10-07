import learning.evaluate
import ordonez.*
import java.io.File
import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

fun evaluatePerformance(dataDir: File, dbFile: String, withResultPrinting: Boolean = false): Pair<Double, Double> {
    val shuffleSeed = Random(System.currentTimeMillis())
    val classifiers = arrayOfNulls<Array<Double>>(Label.values().size)
    var trainingData: List<Triple<Array<Double>, Double, Label>> = listOf()
    var testData: List<Triple<Array<Double>, Double, Label>> = listOf()

    // for each label, train a classifier
    for (label in Label.values()) {
        val result = trainFor(label, dataDir, dbFile, shuffleSeed)
        // stash the classifier in our matrix
        classifiers[label.ordinal] = result.first
        trainingData = result.second
        testData = result.third
    }

    // Evalulate the one-vs-all classifier to see if we pick the right activity
    // from the possible set of activities from the set of data used during training.
    var correct = 0
    var incorrect = 0
    trainingData.forEach { (features, _, label) ->
        val topLabelOrdinal = classifiers
                .requireNoNulls()
                .mapIndexed { i, classifier -> Pair(i, evaluate(classifier, features)) }
                .maxBy { it.second }!!
                .first
        val topLabel = Label.values()[topLabelOrdinal]

        if (topLabel == label) {
            correct++
        } else {
            incorrect++
        }
    }

    val trainingAccuracy = correct.toDouble() / (correct + incorrect).toDouble()

    // Evalulate the one-vs-all classifier to see if we pick the right activity
    // from the possible set of activities from a set of data not used during training.
    correct = 0
    incorrect = 0
    testData.forEach { (features, _, label) ->
        val topLabelOrdinal = classifiers
                .requireNoNulls()
                .mapIndexed { i, classifier -> Pair(i, evaluate(classifier, features)) }
                .maxBy { it.second }!!
                .first
        val topLabel = Label.values()[topLabelOrdinal]

        if (topLabel == label) {
            correct++
        } else {
            incorrect++
        }
    }

    val testAccuracy = correct.toDouble() / (correct + incorrect).toDouble()

    if (withResultPrinting) {
        Log.info("=" * 80)
        Log.info("=" * 80)
        Log.info("Multinomial Regression Results:")
        Log.info("Training Accuracy: ${((trainingAccuracy * 100.0)*100.0).roundToInt() / 100.0}%")
        Log.info("Test Accuracy: ${((testAccuracy * 100.0)*100.0).roundToInt() / 100.0}%")
        Log.info("=" * 80)
        Log.info("=" * 80)
        Log.info()
        Log.info()
    }

    return Pair(testAccuracy, trainingAccuracy)
}

fun main(args: Array<String>) {
    // prepare database from raw data
    val dataDir = File("./data")
    val dbFile = "ordonez.db"
    prepareOrdonezADatabase(dataDir, dbFile)

    val runs = 100
    var testAccuracySum = 0.0
    var testAccuracyMax = 0.0
    var testAccuracyMin = 100.0
    var trainingAccuracySum = 0.0
    var trainingAccuracyMax = 0.0
    var trainingAccuracyMin = 100.0
    for (i in 0 until runs) {
        val result = evaluatePerformance(dataDir, dbFile)
        testAccuracySum += result.first
        trainingAccuracySum += result.second
        testAccuracyMax = max(testAccuracyMax, result.first)
        testAccuracyMin = min(testAccuracyMin, result.first)
        trainingAccuracyMax = max(trainingAccuracyMax, result.second)
        trainingAccuracyMin = min(trainingAccuracyMin, result.second)
    }

    Log.info("=" * 80)
    Log.info("=" * 80)
    Log.info("Multinomial Regression Average Results (over $runs runs):")
    Log.info("Training Data Accuracy:")
    Log.info("    Average: ${((trainingAccuracySum / runs) * 10000.0).roundToInt() / 100.0}%")
    Log.info("    Min: ${(trainingAccuracyMin * 10000.0).roundToInt() / 100.0}%")
    Log.info("    Max: ${(trainingAccuracyMax * 10000.0).roundToInt() / 100.0}%")
    Log.info("Test Data Accuracy:")
    Log.info("    Average: ${((testAccuracySum / runs) * 10000.0).roundToInt() / 100.0}%")
    Log.info("    Min: ${(testAccuracyMin * 10000.0).roundToInt() / 100.0}%")
    Log.info("    Max: ${(testAccuracyMax * 10000.0).roundToInt() / 100.0}%")
    Log.info("=" * 80)
    Log.info("=" * 80)
}
