package shitcompiler

import shitcompiler.token.Symbol

/**
* Created by NuclearCoder on 10/03/17.
*/

val ETX = 0x03.toChar()

val SEPARATORS = setOf(' ', '\t', '\n') + ('\u0000'..'\u001F')

val KEYWORDS = mapOf(
        "bool" to Symbol.BOOL,
        "char" to Symbol.CHAR,
        "if" to Symbol.IF,
        "int" to Symbol.INT,
        "else" to Symbol.ELSE,
        "record" to Symbol.RECORD,
        "return" to Symbol.RETURN,
        "while" to Symbol.WHILE
)

val STD_NAMES = listOf("int", "char", "bool", "true", "false")

val NO_NAME = 0