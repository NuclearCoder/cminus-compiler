package shitcompiler.parser

import shitcompiler.ast.AST
import shitcompiler.ast.expression.Expression
import shitcompiler.ast.function.FunctionCallExpression
import shitcompiler.ast.function.FunctionCallStatement
import shitcompiler.ast.function.FunctionDefinition
import shitcompiler.ast.function.FunctionParameter
import shitcompiler.ast.statement.Statement
import shitcompiler.ast.type.TypeReference
import shitcompiler.token.Symbol.*

/**
 * Created by NuclearCoder on 17/03/17.
 */

fun Parser.functionCall(name: Int, isStatement: Boolean): AST {
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

    return if (isStatement)
        FunctionCallStatement(lineNo, name, parameters)
    else
        FunctionCallExpression(lineNo, name, parameters)
}

fun Parser.functionDefinition(name: Int, returnType: TypeReference): Statement {
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

    return FunctionDefinition(lineNo, name, returnType, parameters, block)
}

fun Parser.functionParameter(): FunctionParameter {
    if (symbol == STRUCT) // skip STRUCT if there is - only there for distinction
        expect(STRUCT)

    val type = typeReference()
    val name = argument
    expect(ID)
    return FunctionParameter(type, name)
}