package shitcompiler.parser

import shitcompiler.ast.expression.Expression
import shitcompiler.ast.function.FunctionCall
import shitcompiler.ast.function.FunctionDefinition
import shitcompiler.ast.function.FunctionParameter
import shitcompiler.ast.type.TypeReference
import shitcompiler.token.Symbol.*

/**
 * Created by NuclearCoder on 17/03/17.
 */

fun Parser.functionCall(name: Int): FunctionCall {
    expect(LEFT_PARENTHESIS)

    val parameters = mutableListOf<Expression>()

    if (symbol != RIGHT_PARENTHESIS) {
        parameters.add(expression())
        while (symbol == COMMA) {
            expect(COMMA)
            parameters.add(expression())
        }
    }

    expect(RIGHT_PARENTHESIS)

    return FunctionCall(name, parameters)
}

fun Parser.functionDefinition(name: Int, returnType: TypeReference): FunctionDefinition {
    expect(LEFT_PARENTHESIS)

    val parameters = mutableListOf<FunctionParameter>()

    if (symbol != RIGHT_PARENTHESIS) {
        parameters.add(functionParameter())
        while (symbol == COMMA) {
            expect(COMMA)
            parameters.add(functionParameter())
        }
    }
    expect(RIGHT_PARENTHESIS)

    return FunctionDefinition(name, returnType, parameters, blockStatement())
}

fun Parser.functionParameter(): FunctionParameter {
    if (symbol == STRUCT)
        expect(STRUCT)

    val type = typeReference()
    val name = argument
    expect(ID)
    return FunctionParameter(type, name)
}