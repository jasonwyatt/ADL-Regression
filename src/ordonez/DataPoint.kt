package ordonez

import java.util.*
import kotlin.math.PI
import kotlin.math.sin

data class DataPoint(
        val startTimePeriodic: Double,
        val endTimePeriodic: Double,
        // minutes at location
        val minutesAtShower: Double,
        val minutesAtBasin: Double,
        val minutesAtCooktop: Double,
        val minutesAtMaindoor: Double,
        val minutesAtFridge: Double,
        val minutesAtCabinet: Double,
        val minutesAtCupboard: Double,
        val minutesAtToilet: Double,
        val minutesAtSeat: Double,
        val minutesAtBed: Double,
        val minutesAtMicrowave: Double,
        val minutesAtToaster: Double,
        // minutes of sensor data
        val minutesOfPressure: Double,
        val minutesOfMagnetic: Double,
        val minutesOfPIR: Double,
        val minutesOfFlush: Double,
        val minutesOfElectric: Double,
        // minutes in room
        val minutesInBedroom: Double,
        val minutesInBathroom: Double,
        val minutesInKitchen: Double,
        val minutesInLiving: Double,
        val minutesInEntrance: Double
) {
    companion object {
        fun builder(startTime: Calendar, endTime: Calendar) = Builder(startTime, endTime)

        val SIZE: Int = builder(Calendar.getInstance(), Calendar.getInstance()).build().arrayRepresentationWithBias.size
    }

    @Transient
    val arrayRepresentationWithBias = arrayOf(
            1.0,
            //startTimePeriodic,
            //endTimePeriodic,
            // minutes at location
            minutesAtShower,
            minutesAtBasin,
            minutesAtCooktop,
            minutesAtMaindoor,
            minutesAtFridge,
            minutesAtCabinet,
            minutesAtCupboard,
            minutesAtToilet,
            minutesAtSeat,
            minutesAtBed,
            minutesAtMicrowave,
            minutesAtToaster,
            // minutes of sensor data
            minutesOfPressure,
            minutesOfMagnetic,
            minutesOfPIR,
            minutesOfFlush,
            minutesOfElectric,
            // minutes in room
            minutesInBedroom,
            minutesInBathroom,
            minutesInKitchen,
            minutesInLiving,
            minutesInEntrance
    )

    class Builder(
            val startTime: Calendar,
            val endTime: Calendar
    ) {
        private val timeRange = LongRange(startTime.timeInMillis, endTime.timeInMillis)
        private var minutesAtShower = 0.0
        private var minutesAtBasin = 0.0
        private var minutesAtCooktop = 0.0
        private var minutesAtMaindoor = 0.0
        private var minutesAtFridge = 0.0
        private var minutesAtCabinet = 0.0
        private var minutesAtCupboard = 0.0
        private var minutesAtToilet = 0.0
        private var minutesAtSeat = 0.0
        private var minutesAtBed = 0.0
        private var minutesAtMicrowave = 0.0
        private var minutesAtToaster = 0.0
        // minutes of sensor data
        private var minutesOfPressure = 0.0
        private var minutesOfMagnetic = 0.0
        private var minutesOfPIR = 0.0
        private var minutesOfFlush = 0.0
        private var minutesOfElectric = 0.0
        // minutes in room
        private var minutesInBedroom = 0.0
        private var minutesInBathroom = 0.0
        private var minutesInKitchen = 0.0
        private var minutesInLiving = 0.0
        private var minutesInEntrance = 0.0

        fun addEvent(startTime: Calendar,
                     endTime: Calendar,
                     location: Location,
                     sensor: SensorType,
                     room: Room) = apply {
            val timeRange = LongRange(startTime.timeInMillis, endTime.timeInMillis)
            val durationMinutes = Math.max(this.timeRange.overlap(timeRange) / (60*1000), 1).toInt()

            when (location) {
                Location.Shower -> addShowerTime(durationMinutes)
                Location.Basin -> addBasinTime(durationMinutes)
                Location.Cooktop -> addCooktopTime(durationMinutes)
                Location.Maindoor -> addMaindoorTime(durationMinutes)
                Location.Fridge -> addFridgeTime(durationMinutes)
                Location.Cabinet -> addCabinetTime(durationMinutes)
                Location.Cupboard -> addCupboardTime(durationMinutes)
                Location.Toilet -> addToiletTime(durationMinutes)
                Location.Seat -> addSeatTime(durationMinutes)
                Location.Bed -> addBedTime(durationMinutes)
                Location.Microwave -> addMicrowaveTime(durationMinutes)
                Location.Toaster -> addToasterTime(durationMinutes)
            }

            when (sensor) {
                SensorType.PIR -> addPIRTime(durationMinutes)
                SensorType.Magnetic -> addMagneticTime(durationMinutes)
                SensorType.Flush -> addFlushTime(durationMinutes)
                SensorType.Pressure -> addPresesureTime(durationMinutes)
                SensorType.Electric -> addElectricTime(durationMinutes)
            }

            when (room) {
                Room.Bedroom -> addBedroomTime(durationMinutes)
                Room.Bathroom -> addBathroomTime(durationMinutes)
                Room.Kitchen -> addKitchenTime(durationMinutes)
                Room.Living -> addLivingTime(durationMinutes)
                Room.Entrance -> addEntranceTime(durationMinutes)
            }
        }

        fun addShowerTime(minutes: Int) = apply { minutesAtShower += minutes }
        fun addBasinTime(minutes: Int) = apply { minutesAtBasin += minutes }
        fun addCooktopTime(minutes: Int) = apply { minutesAtCooktop += minutes }
        fun addMaindoorTime(minutes: Int) = apply { minutesAtMaindoor += minutes }
        fun addFridgeTime(minutes: Int) = apply { minutesAtFridge += minutes }
        fun addCabinetTime(minutes: Int) = apply { minutesAtCabinet += minutes }
        fun addCupboardTime(minutes: Int) = apply { minutesAtCupboard += minutes }
        fun addToiletTime(minutes: Int) = apply { minutesAtToilet += minutes }
        fun addSeatTime(minutes: Int) = apply { minutesAtSeat += minutes }
        fun addBedTime(minutes: Int) = apply { minutesAtBed += minutes }
        fun addMicrowaveTime(minutes: Int) = apply { minutesAtMicrowave += minutes }
        fun addToasterTime(minutes: Int) = apply { minutesAtToaster += minutes }

        fun addPresesureTime(minutes: Int) = apply { minutesOfPressure += minutes }
        fun addMagneticTime(minutes: Int) = apply { minutesOfMagnetic += minutes }
        fun addPIRTime(minutes: Int) = apply { minutesOfPIR += minutes }
        fun addFlushTime(minutes: Int) = apply { minutesOfFlush += minutes }
        fun addElectricTime(minutes: Int) = apply { minutesOfElectric += minutes }

        fun addBedroomTime(minutes: Int) = apply { minutesInBedroom += minutes }
        fun addBathroomTime(minutes: Int) = apply { minutesInBathroom += minutes }
        fun addKitchenTime(minutes: Int) = apply { minutesInKitchen += minutes }
        fun addLivingTime(minutes: Int) = apply { minutesInLiving += minutes }
        fun addEntranceTime(minutes: Int) = apply { minutesInEntrance += minutes }

        fun build(): DataPoint =
                DataPoint(
                        sin(2.toDouble() * PI * startTime.minuteInDay.toDouble() / (24.0 * 60.0)),
                        sin(2.toDouble() * PI * endTime.minuteInDay.toDouble() / (24.0 * 60.0)),
                        minutesAtShower, minutesAtBasin, minutesAtCooktop, minutesAtMaindoor,
                        minutesAtFridge, minutesAtCabinet, minutesAtCupboard, minutesAtToilet,
                        minutesAtSeat, minutesAtBed, minutesAtMicrowave, minutesAtToaster,
                        minutesOfPressure, minutesOfMagnetic, minutesOfPIR, minutesOfFlush,
                        minutesOfElectric, minutesInBedroom, minutesInBathroom, minutesInKitchen,
                        minutesInLiving, minutesInEntrance
                )
    }
}

