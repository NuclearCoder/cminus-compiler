package shitcompiler.parser

import shitcompiler.ast.AST
import shitcompiler.token.Symbol
import shitcompiler.token.Symbol.END_TEXT
import shitcompiler.token.Symbol.NEWLINE
import shitcompiler.token.removeSymbol
import java.io.PrintWriter
import java.util.*
import kotlin.properties.Delegates.notNull

/**
* Created by NuclearCoder on 14/01/2017.
*/

// TODO: error recovery

class Parser(private val symbols: Queue<Int>, private val errors: PrintWriter) {

    internal var symbol = Symbol.UNKNOWN
    internal var argument: Int = 0

    private var lineNo = 1

    fun execute(): AST {
        nextSymbol()

        return _program()
    }

    private fun nextSymbol() {
        symbol = symbols.removeSymbol()
        while (symbol == NEWLINE) {
            lineNo++
            symbols.removeSymbol()
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
        else syntaxError()
    }

    private fun _program(): AST {
        val root = blockStatement()

        if (symbol != END_TEXT)
            syntaxError()

        return root
    }

}