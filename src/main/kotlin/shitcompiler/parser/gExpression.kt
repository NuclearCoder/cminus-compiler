package shitcompiler.parser

import shitcompiler.NAME_FALSE
import shitcompiler.NAME_TRUE
import shitcompiler.ast.access.VariableAccess
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
    return when (symbol) {
        NUM_CONST -> atomNum()
        CHAR_CONST -> atomChar()
        TRUE, FALSE -> factorBool()
        ID -> functionOrVariable()
        ASTERISK -> factorVariable()
        LEFT_PARENTHESIS -> {
            expect(LEFT_PARENTHESIS)
            val node = expression()
            expect(RIGHT_PARENTHESIS)
            node
        }
        in UNARY_SYMBOLS -> factorUnary()
        else -> {
            syntaxError()
            Atom.Unknown(lineNo)
        }
    }
}

fun Parser.atomNum(): Atom {
    val value = argument
    expect(NUM_CONST)
    return Atom.Integer(lineNo, value)
}

fun Parser.atomChar(): Atom {
    val value = argument
    expect(CHAR_CONST)
    return Atom.Char(lineNo, value)
}

fun Parser.factorBool(): VariableAccess {
    return if (symbol == TRUE) {
        expect(TRUE)
        VariableAccess(lineNo, NAME_TRUE)
    } else {
        expect(FALSE)
        VariableAccess(lineNo, NAME_FALSE)
    }
}

fun Parser.functionOrVariable(): Expression {
    val name = argument
    expect(ID)

    // it might be a function call
    if (symbol == LEFT_PARENTHESIS) {
        return functionCall(name, isStatement = false) as Expression
    } else {
        return variableAccess(name, pointerDepth = 0)
    }
}

fun Parser.factorVariable(): Expression {
    var pointerDepth = 0
    while (symbol == ASTERISK) {
        expect(ASTERISK)
        pointerDepth++
    }

    // TODO: parentheses in variable access

    val name = argument
    expect(ID)

    return variableAccess(name, pointerDepth)
}

fun Parser.factorUnary(): UnaryOp {
    val sym = symbol
    expect(symbol)
    return UnaryOp(lineNo, sym, factor())
}