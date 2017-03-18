package shitcompiler

import shitcompiler.token.Symbol
import java.io.PrintWriter
import java.util.*

/**
 * Created by NuclearCoder on 18/03/17.
 */

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