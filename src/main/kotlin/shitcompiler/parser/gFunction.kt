package shitcompiler.parser

import shitcompiler.ast.expression.Expression
import shitcompiler.ast.function.FunctionCall
import shitcompiler.token.Symbol.*

/**
 * Created by NuclearCoder on 17/03/17.
 */

fun Parser.functionCall(name: Int): FunctionCall {
    expect(LEFT_PARENTHESIS)

    val parameters = mutableListOf<Expression>()

    parameters.add(expression())
    while (symbol == COMMA) {
        expect(COMMA)
        parameters.add(expression())
    }

    expect(RIGHT_PARENTHESIS)
    return FunctionCall(name, parameters)
}