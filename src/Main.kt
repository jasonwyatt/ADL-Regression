import learning.evaluate
import ordonez.*
import java.io.File
import java.util.*

fun evaluatePerformance(dataDir: File, dbFile: String, withResultPrinting: Boolean = false): Double {
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
    // from the possible set of activities from a set of data not used during training.
    var correct = 0
    var incorrect = 0
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

    val accuracy = correct.toDouble() / (correct + incorrect).toDouble()

    if (withResultPrinting) {
        Log.info("=" * 80)
        Log.info("=" * 80)
        Log.info("Multinomial Regression Results:")
        Log.info("${accuracy * 100.0}% Accuracy ($correct / ${correct + incorrect} correctly identified)")
        Log.info("=" * 80)
        Log.info("=" * 80)
        Log.info()
        Log.info()
    }

    return accuracy
}

fun main(args: Array<String>) {
    // prepare database from raw data
    val dataDir = File("./data")
    val dbFile = "ordonez.db"
    prepareOrdonezADatabase(dataDir, dbFile)

    val runs = 100
    var accuracySum = 0.0
    for (i in 0 until runs) {
        accuracySum += evaluatePerformance(dataDir, dbFile)
    }

    Log.info("=" * 80)
    Log.info("=" * 80)
    Log.info("Multinomial Regression Average Results:")
    Log.info("${(accuracySum / runs) * 100.0}% average accuracy over $runs runs.")
    Log.info("=" * 80)
    Log.info("=" * 80)
}
