package ordonez

import Log
import db.*
import learning.evaluate
import learning.trainOnline
import java.io.File
import java.util.*

fun prepareOrdonezADatabase(dataDir: File, dbFileName: String) {
    connect(File(dataDir, dbFileName).absolutePath) {
        Log.info("Creating tables.")
        execute {
            """
                CREATE TABLE IF NOT EXISTS events (
                    start_time INTEGER NOT NULL,
                    end_time INTEGER NOT NULL,
                    duration INTEGER NOT NULL,
                    start_minute INTEGER NOT NULL,
                    end_minute INTEGER NOT NULL,
                    location INTEGER NOT NULL,
                    type INTEGER NOT NULL,
                    room INTEGER NOT NULL
                )
            """.trimIndent()
        }

        execute {
            """
                CREATE TABLE IF NOT EXISTS labels (
                    start_time INTEGER NOT NULL,
                    end_time INTEGER NOT NULL,
                    label INTEGER NOT NULL
                )
            """.trimIndent()
        }

        execute { "DELETE FROM events" }
        execute { "DELETE FROM labels" }

        Scanner(File(dataDir, "OrdonezA_Sensors.txt")).use {
            it.useDelimiter("(\\s\\s+)|\t")
            it.nextLine()
            it.nextLine()
            var count = 0
            while (it.hasNext()) {
                val startTime = it.next().trim().toCalendar()
                val endTime = it.next().trim().toCalendar()
                val location = it.next().trim().toLocation()
                val type = it.next().trim().toSensorType()
                val room = it.next().trim().toRoom()

                executeUpdate(
                        startTime,
                        endTime,
                        endTime.timeInMillis - startTime.timeInMillis,
                        startTime.minuteInDay,
                        endTime.minuteInDay,
                        location,
                        type,
                        room) {
                    """
                        INSERT INTO events (
                            start_time, end_time, duration, start_minute, end_minute, location, type, room
                        ) VALUES (
                            ?, ?, ?, ?, ?, ?, ?, ?
                        )
                    """.trimIndent()
                }
                count++
            }
            Log.info("Inserted $count sensor readings.")
        }

        Scanner(File(dataDir, "OrdonezA_ADLs.txt")).use {
            it.useDelimiter("(\\s\\s+)|\t")
            it.nextLine()
            it.nextLine()
            var count = 0

            while (it.hasNext()) {
                val startTime = it.next().trim().toCalendar()
                val endTime = it.next().trim().toCalendar()
                val label = it.next().trim().toLabel()

                executeUpdate(startTime, endTime, label) {
                    """
                        INSERT INTO labels (
                            start_time, end_time, label
                        ) VALUES (
                            ?, ?, ?
                        )
                    """.trimIndent()
                }
                count++
            }
            Log.info("Inserted $count labels.")
        }
    }
}

fun extractTrainingData(dataDir: File, dbFileName: String, targetLabel: Label, withLogs: Boolean = false): List<Triple<Array<Double>, Double, Label>> {
    val result = mutableListOf<Triple<Array<Double>, Double, Label>>()

    connect(File(dataDir, dbFileName).absolutePath) {
        val eventQueryStr = """
            SELECT
                start_time,
                end_time,
                location,
                type,
                room
            FROM events
            WHERE
                (start_time >= :start AND start_time <= :end)
                OR
                (end_time >= :start AND end_time <= :end)
        """.trimIndent()

        query("SELECT * FROM labels") {
            while (next()) {
                val labelStart = getCalendar("start_time")
                val labelEnd = getCalendar("end_time")
                val label = getInt("label").toLabel()
                val dataPointBuilder = DataPoint.builder(labelStart, labelEnd)

                query(
                        eventQueryStr,
                        mapOf(
                                "start" to labelStart,
                                "end" to labelEnd
                        )) {

                    while (next()) {
                        dataPointBuilder.addEvent(
                                getCalendar("start_time"),
                                getCalendar("end_time"),
                                getInt("location").toLocation(),
                                getInt("type").toSensorType(),
                                getInt("room").toRoom()
                        )
                    }
                }
                result.add(Triple(dataPointBuilder.build().arrayRepresentationWithBias, if (label == targetLabel) 1.0 else 0.0, label))
            }
        }
    }

    if (withLogs) {
        Log.info("Extracted ${result.size} data points.")
    }

    return result
}

fun trainClassifier(data: List<Triple<Array<Double>, Double, Label>>,
                    iterations: Int = 1,
                    learningRate: Double = 0.1,
                    withLogs: Boolean = false): Array<Double> {

    var parameters = Pair(Array(DataPoint.SIZE) { 1.0 }, Array(DataPoint.SIZE) {1.0})
    for (i in 0 until iterations) {
        data.forEach {
            trainOnline(learningRate, parameters.first, parameters.second, it.first, it.second)
            parameters = Pair(parameters.second, parameters.first)
        }
        if (withLogs) {
            Log.info("Parameters($i): ${Arrays.toString(parameters.second)}")
        }
    }
    return parameters.first
}

fun trainFor(label: Label,
             dataDir: File,
             dbFileName: String,
             shuffleSeed: Random,
             withTest: Boolean = false,
             withDetails: Boolean = false): Triple<Array<Double>, List<Triple<Array<Double>, Double, Label>>, List<Triple<Array<Double>, Double, Label>>> {

    if (withDetails) {
        Log.info("=" * 80)
        Log.info("Training for $label")
        Log.info("=" * 80)
    }
    val data = extractTrainingData(dataDir, dbFileName, label, withDetails).shuffled(shuffleSeed)

    val train = data.subList(0, (data.size * 0.8).toInt())
    val test = data.subList((data.size * 0.8).toInt(), data.size)

    val parameters = trainClassifier(train, withLogs = withDetails)

    if (withTest) {
        var actualPositives = 0
        var actualNegatives = 0
        var falsePositives = 0
        var falseNegatives = 0
        var correct = 0
        test.forEach {
            val estimated = evaluate(parameters, it.first)

            if (it.second == 1.0) {
                actualPositives++
            } else {
                actualNegatives++
            }

            if (estimated) {
                if (it.second == 1.0) {
                    correct++
                } else {
                    falsePositives++
                }
            } else {
                if (it.second == 1.0) {
                    falseNegatives++
                } else {
                    correct++
                }
            }
        }
        Log.info("Results($actualPositives/$actualNegatives): $correct Correct, $falsePositives False Positive, $falseNegatives False Negative")
    }
    if (withDetails) {
        Log.info()
        Log.info()
    }

    return Triple(parameters, train, test)
}
