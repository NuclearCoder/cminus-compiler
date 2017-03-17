package shitcompiler.parser

import shitcompiler.LONG_SYMBOLS
import shitcompiler.ast.AST
import shitcompiler.token.Symbol
import shitcompiler.token.Symbol.END_TEXT
import shitcompiler.token.Symbol.NEWLINE
import shitcompiler.token.removeSymbol
import java.io.PrintWriter
import java.util.*

/**
 * Created by NuclearCoder on 14/01/2017.
 */

class Parser(private val symbols: Queue<Int>, val errors: PrintWriter) {

    internal var symbol = Symbol.UNKNOWN
    internal var argument: Int = 0

    private var lineNo = 1

    fun execute(): AST {
        nextSymbol()

        val root = program()

        if (symbol != END_TEXT)
            syntaxError()

        return root
    }

    private fun nextSymbol() {
        symbol = symbols.removeSymbol()
        while (symbol == NEWLINE) {
            lineNo++
            symbol = symbols.removeSymbol()
        }
        if (symbol in LONG_SYMBOLS) {
            argument = symbols.remove()
        }
    }

    internal fun syntaxError() {
        errors.println("Invalid syntax on symbol $symbol, line #$lineNo")
    }

    internal fun expect(expected: Symbol) {
        if (symbol == expected) nextSymbol()
        else {
            if (symbol == Symbol.UNKNOWN) nextSymbol()
            syntaxError()
        }
    }

}