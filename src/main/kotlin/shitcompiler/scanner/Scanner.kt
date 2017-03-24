package shitcompiler.scanner

import shitcompiler.ETX
import shitcompiler.SEPARATORS
import shitcompiler.println
import shitcompiler.token.Symbol
import shitcompiler.token.Symbol.*
import java.io.PrintWriter
import java.util.*
import kotlin.properties.Delegates.notNull

/**
 * Created by NuclearCoder on 14/01/2017.
 */

class Scanner(private val input: String, private val errors: PrintWriter) {

    private val symbols = LinkedList<Int>()

    private var lineNo: Int = 1
    private var position: Int = -1
    private var currentChar by notNull<Char>()

    private val nameTable = WordTable()

    fun execute(): Queue<Int> {
        nextChar()
        while (ETX != currentChar)
            nextSymbol()
        emit(END_TEXT)
        return symbols
    }

    // utility methods

    private fun nextChar() {
        currentChar = if (++position < input.length) input[position] else ETX
    }

    private fun emit(argument: Int) {
        symbols.addLast(argument)
    }

    private fun emit(symbol: Symbol) {
        symbols.addLast(symbol.ordinal)
    }

    /** moves the cursor and emits the Symbol */
    private fun nextEmit(symbol: Symbol) {
        nextChar()
        emit(symbol)
    }

    private fun skipSeparators() {
        while (currentChar != ETX && currentChar in SEPARATORS) {
            if (currentChar == '\n') {
                emit(NEWLINE)
                lineNo++
            }
            nextChar()
        }
    }

    private fun skipLine() {
        while (currentChar != '\n' && currentChar != ETX)
            nextChar()
    }

    // scanner

    private fun nextSymbol() {
        skipSeparators()

        if (currentChar.isLetter()) scanWord()
        else if (currentChar.isDigit()) scanNumeral()
        else if (currentChar == '\'') scanCharacter()
        else when (currentChar) {
            '{' -> nextEmit(BEGIN)
            '}' -> nextEmit(END)
            '(' -> nextEmit(LEFT_PARENTHESIS)
            ')' -> nextEmit(RIGHT_PARENTHESIS)
            '[' -> nextEmit(LEFT_BRACKET)
            ']' -> nextEmit(RIGHT_BRACKET)
            ';' -> nextEmit(SEMICOLON)
            ',' -> nextEmit(COMMA)
            '.' -> nextEmit(PERIOD)
            '=' -> scanEqual()
            '+' -> scanPlus()
            '-' -> scanMinus()
            '*' -> scanAsterisk()
            '/' -> scanSlash()
            '<' -> scanLesser()
            '>' -> scanGreater()
            '!' -> scanNot()
            '&' -> scanAmp()
            '|' -> scanPipe()
            '%' -> nextEmit(MOD)
            ETX -> {
            }
            else -> nextEmit(UNKNOWN)
        }
    }

    private fun scanWord() {
        val sb = StringBuilder()
        while (currentChar.isLetterOrDigit()) {
            sb.append(currentChar)
            nextChar()
        }

        val rec = nameTable.search(sb.toString())
        if (rec.isName) {
            emit(ID)
        }
        emit(rec.index)
    }

    private fun scanNumeral() {
        var value: Int = 0
        while (currentChar.isDigit()) {
            val digit = (currentChar - '0')
            if (value <= (Int.MAX_VALUE - digit) / 10) {
                value = 10 * value + digit
                nextChar()
            } else {
                errors.println(lineNo, "An int constant is outside the range 0..${Int.MAX_VALUE}")
                // error recovery:
                emit(NUM_CONST)
                emit(0)
                return
            }
        }
        emit(NUM_CONST)
        emit(value)
    }

    private fun scanCharacter() {
        val character: Char
        nextChar()
        if (currentChar == '\\') {
            nextChar()
            when (currentChar) {
                'n' -> character = '\n'  // line break
                '0' -> character = '\u0000'  // NUL character
                else -> character = currentChar  // otherwise, it's the following character
            }
        } else {
            character = currentChar
        }
        nextChar()
        if (currentChar != '\'') {
            errors.println(lineNo, "A char constant is missing a closing quote")
            // error recovery:
            emit(CHAR_CONST)
            emit(0)
            return
        }
        nextChar()
        emit(CHAR_CONST)
        emit(character.toInt())
    }

    private fun scanEqual() {
        nextChar()
        if (currentChar == '=') nextEmit(EQUAL)
        else emit(BECOMES)
    }

    private fun scanPlus() {
        nextChar()
        when (currentChar) {
            '=' -> nextEmit(BECOMES_PLUS)
            '+' -> nextEmit(DOUBLE_PLUS)
            else -> emit(PLUS)
        }
    }

    private fun scanMinus() {
        nextChar()
        when (currentChar) {
            '=' -> nextEmit(BECOMES_MINUS)
            '-' -> nextEmit(DOUBLE_MINUS)
            else -> emit(MINUS)
        }
    }

    private fun scanAsterisk() {
        nextChar()
        if (currentChar == '=') nextEmit(BECOMES_TIMES)
        else emit(ASTERISK)
    }

    private fun scanSlash() {
        nextChar()
        when (currentChar) {
            '/' -> skipLine()
            '*' -> skipLongComment()
            '=' -> nextEmit(BECOMES_DIV)
            else -> emit(DIV)
        }
    }

    private fun scanLesser() {
        nextChar()
        if (currentChar == '=') nextEmit(NOT_GREATER)
        else emit(LESSER)
    }

    private fun scanGreater() {
        nextChar()
        if (currentChar == '=') nextEmit(NOT_LESSER)
        else emit(GREATER)
    }

    private fun scanNot() {
        nextChar()
        if (currentChar == '=') nextEmit(NOT_EQUAL)
        else emit(NOT)
    }

    private fun scanAmp() {
        nextChar()
        if (currentChar == '&') nextEmit(AND)
    }

    private fun scanPipe() {
        nextChar()
        if (currentChar == '|') nextEmit(OR)
    }

    private fun skipLongComment() {
        // support nested comments
        var level = 1
        lgcom@ while (ETX != currentChar && level > 0) {
            val chr = currentChar
            nextChar()
            when (chr) {
                '/' -> {
                    if (currentChar == '*')
                        level++
                    else continue@lgcom
                }
                '*' -> {
                    if (currentChar == '/')
                        level--
                    else continue@lgcom
                }
            }
        }
    }

}