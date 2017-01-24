package shitcompiler.scanner

import shitcompiler.token.Symbol.*

/**
* Created by NuclearCoder on 14/01/2017.
*/

val ETX = 0x03.toChar()

val SEPARATORS = setOf(' ', '\t') + ('\u0000'..'\u001F')

val KEYWORDS = mapOf(
        "and" to AND,
        "bool" to BOOL,
        "break" to BREAK,
        "char" to CHAR,
        "false" to FALSE,
        "int" to INT,
        "not" to NOT,
        "or" to OR,
        "record" to RECORD,
        "return" to RETURN,
        "static" to STATIC,
        "true" to TRUE,
        "while" to WHILE
)