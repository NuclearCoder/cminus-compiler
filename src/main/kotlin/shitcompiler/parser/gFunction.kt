package shitcompiler.parser

import shitcompiler.ast.expression.Expression
import shitcompiler.ast.function.FunctionCall
import shitcompiler.ast.function.FunctionDefinition
import shitcompiler.ast.function.FunctionParameter
import shitcompiler.ast.function.ProcedureDefinition
import shitcompiler.ast.statement.Statement
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

fun Parser.functionDefinition(name: Int, returnType: TypeReference?): Statement {
    val parameters = mutableListOf<FunctionParameter>()

    expect(LEFT_PARENTHESIS)
    if (symbol != RIGHT_PARENTHESIS) {
        if (symbol != VOID) {
            parameters.add(functionParameter())
            while (symbol == COMMA) {
                expect(COMMA)
                parameters.add(functionParameter())
            }
        } else { // void parameters
            expect(VOID)
        }
    }
    expect(RIGHT_PARENTHESIS)

    val block = blockStatement()

    // procedure / function
    return if (returnType != null)
        FunctionDefinition(name, returnType, parameters, block)
    else
        ProcedureDefinition(name, parameters, block)
}

fun Parser.functionParameter(): FunctionParameter {
    if (symbol == STRUCT) // skip STRUCT if there is - only there for distinction
        expect(STRUCT)

    val type = typeReference()
    val name = argument
    expect(ID)
    return FunctionParameter(type, name)
}