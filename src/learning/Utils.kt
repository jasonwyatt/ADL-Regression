package learning

import kotlin.math.E
import kotlin.math.pow


fun sigmoid(parameters: Array<Double>, features: Array<Double>): Double = 1.0 / (1 + E.pow(-(parameters * features)))

fun trainOnline(learningRate: Double,
                parametersIn: Array<Double>,
                parametersOut: Array<Double>,
                features: Array<Double>,
                correctAnswer: Double) {
    val adjustment = learningRate * (sigmoid(parametersIn, features) - correctAnswer)
    for (i in 0 until parametersIn.size) {
        parametersOut[i] = parametersIn[i] - adjustment * features[i]
    }
}

fun evaluate(parameters: Array<Double>, features: Array<Double>, threshold: Double = 0.5) =
        sigmoid(parameters, features) >= threshold

@Suppress("UNCHECKED_CAST")
operator fun <T: Number> Array<T>.times(rhs: Array<T>): T = zip(rhs) { l, r -> l.toDouble() * r.toDouble() }.sum() as T
