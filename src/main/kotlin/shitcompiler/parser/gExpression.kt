package shitcompiler.parser

import shitcompiler.NAME_FALSE
import shitcompiler.NAME_TRUE
import shitcompiler.ast.expression.*
import shitcompiler.token.Symbol.*

/**
 * Created by NuclearCoder on 02/03/17.
 */

fun Parser.expression(): Expression {
    var node = term()
    while (symbol in EXPRESSION_SYMBOLS) {
        val sym = symbol
        expect(symbol)
        node = BinaryOp(lineNo, sym, node, term())
    }
    return node
}

fun Parser.term(): Expression {
    var node = factor()
    while (symbol in TERM_SYMBOLS) {
        val sym = symbol
        expect(symbol)
        node = BinaryOp(lineNo, sym, node, factor())
    }
    return node
}

fun Parser.factor(): Expression {
    when (symbol) {
        NUM_CONST -> {
            val value = argument
            expect(NUM_CONST)
            return Atom.Integer(lineNo, value)
        }
        CHAR_CONST -> {
            val value = argument
            expect(CHAR_CONST)
            return Atom.Char(lineNo, value)
        }
        TRUE -> {
            expect(TRUE)
            return VariableAccess(lineNo, NAME_TRUE)
        }
        FALSE -> {
            expect(FALSE)
            return VariableAccess(lineNo, NAME_FALSE)
        }
        ID -> {
            val name = argument
            expect(ID)

            // it might be a function call
            if (symbol == LEFT_PARENTHESIS) {
                return functionCall(name, isStatement = false) as Expression
            } else {
                return variableAccess(name)
            }
        }
        in UNARY_SYMBOLS -> {
            val sym = symbol
            expect(symbol)
            return UnaryOp(lineNo, sym, factor())
        }
        LEFT_PARENTHESIS -> {
            expect(LEFT_PARENTHESIS)
            val node = expression()
            expect(RIGHT_PARENTHESIS)
            return node
        }
        else -> {
            syntaxError()
            return Atom.Unknown(lineNo)
        }
    }
}