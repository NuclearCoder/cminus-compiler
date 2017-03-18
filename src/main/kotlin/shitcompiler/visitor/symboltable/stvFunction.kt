package shitcompiler.visitor.symboltable

import shitcompiler.ast.AST
import shitcompiler.ast.expression.Expression
import shitcompiler.ast.function.FunctionCallExpression
import shitcompiler.ast.function.FunctionCallStatement
import shitcompiler.ast.function.FunctionDefinition
import shitcompiler.ast.function.FunctionOrProcedureDefinition
import shitcompiler.println
import shitcompiler.symboltable.Kind
import shitcompiler.symboltable.ObjectRecord
import shitcompiler.symboltable.classes.FunctionR
import shitcompiler.symboltable.classes.Procedure
import shitcompiler.symboltable.classes.VarParam

/**
 * Created by NuclearCoder on 18/03/17.
 */

fun SymbolTableVisitor.visitFunctionDefinition(node: FunctionOrProcedureDefinition) {
    val paramObjs = mutableListOf<ObjectRecord>()

    for (parameter in node.parameters) {
        val name = parameter.name
        val type = parameter.type

        val typeObj = table.findOrDefineType(node.lineNo, type)

        paramObjs.add(ObjectRecord(name, Kind.PARAMETER, VarParam(typeObj), 0))
    }

    if (node is FunctionDefinition) {
        val returnType = table.findOrDefineType(node.lineNo, node.returnType)
        table.define(node.lineNo, node.name, Kind.FUNCTION, FunctionR(returnType, paramObjs))
    } else { // node is ProcedureDefinition
        table.define(node.lineNo, node.name, Kind.PROCEDURE, Procedure(paramObjs))
    }

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
    return if (obj.kind == Kind.FUNCTION || obj.kind == Kind.PROCEDURE) {
        val formal: List<ObjectRecord>

        if (obj.kind == Kind.FUNCTION) {
            formal = obj.asFunction().parameters
        } else {
            formal = obj.asProcedure().parameters
        }

        // check one-to-one equality between call parameters and function parameters
        if (formal.size == params.size) {
            for (i in 0..formal.lastIndex) {
                val formalType = formal[i].asParameter().type
                val paramType = visitExpression(params[i])

                if (formalType != paramType) {
                    errors.println(node.lineNo, "Parameter ${i + 1}-th has type $paramType, expected $formalType")
                    return typeUniversal
                }
            }

            if (obj.kind == Kind.FUNCTION) {
                obj.asFunction().returnType
            } else {
                typeUniversal
            }
        } else {
            errors.println(node.lineNo, "Function parameter count does not match given parameter count")
            typeUniversal
        }
    } else {
        errors.println(node.lineNo, "Function call on $name which is not a function")
        typeUniversal
    }
}