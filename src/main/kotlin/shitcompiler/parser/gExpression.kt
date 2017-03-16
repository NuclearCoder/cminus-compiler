package shitcompiler.parser

import shitcompiler.ast.expression.Atom
import shitcompiler.ast.expression.BinaryOp
import shitcompiler.ast.expression.Expression
import shitcompiler.ast.expression.UnaryOp
import shitcompiler.token.Symbol.*

/**
 * Created by NuclearCoder on 02/03/17.
 */

fun Parser.expression(): Expression {
    var node = term()
    while (symbol in ADD_SYMBOLS) {
        val sym = symbol
        expect(symbol)
        node = BinaryOp(sym, node, term())
    }
    return node
}

fun Parser.term(): Expression {
    var node = factor()
    while (symbol in MULTIPLY_SYMBOLS) {
        val sym = symbol
        expect(symbol)
        node = BinaryOp(sym, node, factor())
    }
    return node
}

fun Parser.factor(): Expression {
    when (symbol) {
        NUM_CONST -> {
            val value = argument
            expect(NUM_CONST)
            return Atom.Integer(value)
        }
        CHAR_CONST -> {
            val value = argument
            expect(CHAR_CONST)
            return Atom.Char(value)
        }
        ID -> {
            val value = argument
            expect(ID)
            return Atom.Identifier(value)
        }
        in UNARY_SYMBOLS -> {
            val sym = symbol
            expect(symbol)
            return UnaryOp(sym, expression())
        }
        LEFT_PARENTHESIS -> {
            expect(LEFT_PARENTHESIS)
            val node = expression()
            expect(RIGHT_PARENTHESIS)
            return node
        }
        else -> {
            syntaxError()
            return Atom.Unknown
        }
    }
}