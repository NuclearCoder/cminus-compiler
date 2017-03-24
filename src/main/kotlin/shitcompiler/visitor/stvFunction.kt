package shitcompiler.visitor

import shitcompiler.ast.AST
import shitcompiler.ast.expression.Expression
import shitcompiler.ast.function.FunctionCallExpression
import shitcompiler.ast.function.FunctionCallStatement
import shitcompiler.ast.function.FunctionDefinition
import shitcompiler.symboltable.Kind
import shitcompiler.symboltable.ObjectRecord
import shitcompiler.symboltable.classes.FunctionR
import shitcompiler.symboltable.classes.VarParam

/**
 * Created by NuclearCoder on 18/03/17.
 */

fun SymbolTableVisitor.visitFunctionDefinition(node: FunctionDefinition) {
    val paramObjs = mutableListOf<ObjectRecord>()

    for (parameter in node.parameters) {
        val name = parameter.name
        val type = parameter.type

        val typeObj = table.findOrDefineType(type)

        paramObjs.add(ObjectRecord(name, Kind.PARAMETER, VarParam(typeObj), 0))
    }

    val returnType = table.findOrDefineType(node.returnType)
    table.define(node.lineNo, node.name, Kind.FUNCTION, FunctionR(returnType, paramObjs))

    visitBlock(node.block) {
        paramObjs.forEach { table.define(node.lineNo, it.name, Kind.VARIABLE, it.data) }
    }
}

fun SymbolTableVisitor.visitFunctionCallStatement(node: FunctionCallStatement) {
    functionCall(node, node.name, node.parameters)
}

fun SymbolTableVisitor.visitFunctionCallExpression(node: FunctionCallExpression): ObjectRecord {
    return functionCall(node, node.name, node.parameters)
}

private fun SymbolTableVisitor.functionCall(node: AST, name: Int, params: List<Expression>): ObjectRecord {
    val obj = table.find(node.lineNo, name)
    return if (obj.kind == Kind.FUNCTION) {
        val formal = obj.asFunction().parameters

        // check one-to-one equality between call parameters and function parameters
        if (formal.size == params.size) {
            for (i in 0..formal.lastIndex) {
                val formalType = formal[i].asParameter().type
                val paramType = visitExpression(params[i])

                if (formalType != paramType) {
                    error(node.lineNo, "Parameter ${i + 1}-th has type $paramType, expected $formalType")
                    return typeUniversal
                }
            }

            obj.asFunction().returnType
        } else {
            error(node.lineNo, "Function parameter count does not match given parameter count")
            typeUniversal
        }
    } else {
        error(node.lineNo, "Function call on '<($name)>' which is not a function")
        typeUniversal
    }
}