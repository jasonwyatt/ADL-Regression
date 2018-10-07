package db

import org.sqlite.SQLiteConnection
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types.NULL
import java.util.*
import java.util.regex.Pattern

fun connect(dbPath: String, block: Connection.() -> Unit) =
        DriverManager.getConnection("jdbc:sqlite:$dbPath").use { block(it) }

fun Connection.execute(vararg params: Any?, sqlBlock: () -> String) =
        prepareStatement(sqlBlock()).use {
            it.applyParams(*params)
            it.execute()
        }

fun Connection.executeUpdate(vararg params: Any?, sqlBlock: () -> String) =
        prepareStatement(sqlBlock()).use {
            it.applyParams(*params)
            it.executeUpdate()
        }

fun Connection.query(sql: String, vararg params: Any?, block: ResultSet.() -> Unit) =
        prepareStatement(sql).use { statement ->
            statement.applyParams(*params)
            statement.executeQuery().use { block(it) }
        }

fun Connection.query(sql: String, namedParams: Map<String, Any?>, block: ResultSet.() -> Unit) =
        NamedPreparedStatement(this, sql).use { statement ->
            statement.applyParams(namedParams)
            statement.executeQuery().use { block(it) }
        }

fun PreparedStatement.applyParams(vararg params: Any?) {
    params.forEachIndexed { i, param ->
        set(i + 1, param)
    }
}

fun PreparedStatement.set(index: Int, param: Any?) {
    when (param) {
        is String -> setString(index, param)
        is Int -> setInt(index, param)
        is Long -> setLong(index, param)
        is Short -> setShort(index, param)
        is Double -> setDouble(index, param)
        is Boolean -> setInt(index, if (param) 1 else 0)
        is Date -> setLong(index, param.time)
        is Calendar -> setLong(index, param.timeInMillis)
        is Enum<*> -> setInt(index, param.ordinal)
        null -> setNull(index, NULL)
        else -> throw IllegalArgumentException("Param #$index ($param) is of unsupported type: ${param.javaClass.simpleName}")
    }
}

fun ResultSet.getCalendar(colName: String) =
        Calendar.getInstance().apply { timeInMillis = getLong(colName) }

private class NamedPreparedStatement(
        conn: Connection,
        private val namedSqlStatement: String
) : org.sqlite.jdbc4.JDBC4PreparedStatement(conn as SQLiteConnection, prepareSql(namedSqlStatement)) {
    companion object {
        val NAME_PATTERN = Regex(":([a-zA-Z][a-zA-Z0-9]*(_[a-zA-Z0-9]*)*)", RegexOption.MULTILINE)

        fun prepareSql(sql: String) = sql.replace(NAME_PATTERN, "?")
    }

    fun applyParams(params: Map<String, Any?>) {
        NAME_PATTERN.findAll(namedSqlStatement).forEachIndexed { i, match ->
            match.groups[1]?.let {
                set(i + 1, params[it.value])
            }
        }
    }
}