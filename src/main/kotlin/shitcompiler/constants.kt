package shitcompiler

import shitcompiler.token.Symbol.*

/**
 * Created by NuclearCoder on 10/03/17.
 */

val ETX = 0x03.toChar()

val SEPARATORS = setOf(' ', '\t', '\n') + ('\u0000'..'\u001F')

val KEYWORDS = mapOf(
        "bool" to BOOL,
        "char" to CHAR,
        "if" to IF,
        "int" to INT,
        "else" to ELSE,
        "return" to RETURN,
        "struct" to STRUCT,
        "while" to WHILE
)

val NO_NAME = -1 // used when the reference is undefined
val UNNAMED = 0 // used when the reference should not be named
val NAME_INT = 1
val NAME_CHAR = 2
val NAME_BOOL = 3
val NAME_TRUE = 4
val NAME_FALSE = 5

val STD_NAMES = mapOf(
        NAME_INT to "int",
        NAME_CHAR to "char",
        NAME_BOOL to "bool",
        NAME_TRUE to "true",
        NAME_FALSE to "false"
)

val LONG_SYMBOLS = setOf(ID, NUM_CONST, CHAR_CONST)
