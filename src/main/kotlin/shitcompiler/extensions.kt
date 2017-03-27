package shitcompiler

import shitcompiler.token.Symbol
import java.io.PrintWriter
import java.util.*

/**
 * Created by NuclearCoder on 18/03/17.
 */

// matches "<(n)>"
private val ERROR_ARG_REGEX = "<\\((\\d+)\\)>".toPattern()


fun readMultiLine(): String {
    val sb = StringBuilder()
    do {
        val line = readLine()
        if (line != null && line.isNotEmpty()) {
            sb.append(line)
            sb.append('\n')
        }
    } while (line != null && line.isNotEmpty())
    return sb.toString()
}

fun PrintWriter.println(lineNo: Int, msg: String) {
    this.println("(line $lineNo) $msg")
}

fun PrintWriter.println(names: Map<Int, String>, lineNo: Int, msg: String) {
    this.println(lineNo, format(names, msg))
}

fun format(names: Map<Int, String>, msg: String): String {
    val matcher = ERROR_ARG_REGEX.matcher(msg)
    val sb = StringBuffer(msg.length)
    while (matcher.find()) {
        val index = matcher.group(1).toInt()
        matcher.appendReplacement(sb, names[index] ?: "<no-name>")
    }
    matcher.appendTail(sb)
    return sb.toString()
}

fun String.tab(): String {
    return this.split('\n').map { PRINT_TAB + it }.joinToString("\n")
}

fun Int.toSymbol() = Symbol.values()[this]
fun Queue<Int>.removeSymbol() = remove().toSymbol()

fun Queue<Int>.toPrettyString(): String {
    val it = this.iterator()
    val sb = StringBuilder()
    while (it.hasNext()) {
        val symbol = it.next().toSymbol()
        sb.append(symbol.toString())
        if (symbol in LONG_SYMBOLS) {
            sb.append('(')
            sb.append(it.next())
            sb.append(')')
        }
        if (symbol != Symbol.END_TEXT) {
            sb.append(' ')
        }
    }
    return sb.toString()
}