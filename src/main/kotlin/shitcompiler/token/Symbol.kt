package shitcompiler.token

import shitcompiler.parser.LONG_SYMBOLS
import java.util.*

/**
* Created by NuclearCoder on 14/01/2017.
*/

enum class Symbol(private val s: String) {
    ID("id"),

    NUM_CONST("[int]"),
    CHAR_CONST("[char]"),
    TRUE("true"),
    FALSE("false"),


    INT("int"),
    BOOL("bool"),
    CHAR("char"),
    RECORD("record"),

    BEGIN("{"),
    END("}"),

    LEFT_PARENTHESIS("("),
    RIGHT_PARENTHESIS(")"),

    LEFT_BRACKET("["),
    RIGHT_BRACKET("]"),

    IF("if"),
    ELSE("else"),
    WHILE("while"),
    RETURN("return"),
    //BREAK("break"),

    BECOMES("="),
    BECOMES_PLUS("+="),
    BECOMES_MINUS("-="),
    BECOMES_TIMES("*="),
    BECOMES_DIV("/="),
    BECOMES_MOD("%="),

    DOUBLE_PLUS("++"),
    DOUBLE_MINUS("--"),

    PLUS("+"),
    MINUS("-"),
    ASTERISK("*"),
    DIV("/"),
    MOD("%"),

    LESSER("<"),
    GREATER(">"),
    NOT_LESSER(">="),
    NOT_GREATER("<="),
    EQUAL("=="),
    NOT_EQUAL("!="),

    OR("||"),
    AND("&&"),
    NOT("!"),

    PERIOD("."),
    COMMA(","),
    SEMICOLON(";"),

    NEWLINE("\\n"),
    END_TEXT("[ETX]"),
    UNKNOWN("[unknown]");

    override fun toString() = s
}

inline fun Int.toSymbol() = Symbol.values()[this]
inline fun Queue<Int>.removeSymbol() = remove().toSymbol()

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