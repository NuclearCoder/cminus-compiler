package shitcompiler.parser

import shitcompiler.LONG_SYMBOLS
import shitcompiler.ast.Program
import shitcompiler.println
import shitcompiler.removeSymbol
import shitcompiler.token.Symbol
import java.io.PrintWriter
import java.util.*

/**
 * Created by NuclearCoder on 14/01/2017.
 */

class Parser(private val symbols: Queue<Int>, internal val errors: PrintWriter) {

    internal var symbol = Symbol.UNKNOWN
    internal var argument: Int = 0

    internal var lineNo = 1
        private set

    fun execute(): Program {
        nextSymbol()

        val root = program()

        if (symbol != Symbol.END_TEXT)
            syntaxError()

        return root
    }

    private fun nextSymbol() {
        symbol = symbols.removeSymbol()
        while (symbol == Symbol.NEWLINE) {
            lineNo++
            symbol = symbols.removeSymbol()
        }
        if (symbol in LONG_SYMBOLS) {
            argument = symbols.remove()
        }
    }

    internal fun syntaxError() {
        errors.println(lineNo, "Unexpected $symbol")
    }

    internal fun expect(expected: Symbol) {
        if (symbol == expected) nextSymbol()
        else {
            syntaxError()
            nextSymbol()
        }
    }

}