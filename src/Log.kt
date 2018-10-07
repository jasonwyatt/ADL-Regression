import java.util.logging.Level

object Log {
    var level: Level = Level.ALL

    private fun isEnabled(level: Level) = level.intValue() >= this.level.intValue()

    private fun print(level: Level, message: String, throwable: Throwable? = null) {
        if (isEnabled(level)) {
            println("${level.name}: $message")
            throwable?.let {
                it.printStackTrace()
            }
        }
    }

    fun info(message: String = "", throwable: Throwable? = null) =
            print(Level.INFO, message, throwable = throwable)
    fun warn(message: String = "", throwable: Throwable? = null) =
            print(Level.WARNING, message, throwable = throwable)
    fun error(message: String = "", throwable: Throwable? = null) =
            print(Level.SEVERE, message, throwable = throwable)
}
