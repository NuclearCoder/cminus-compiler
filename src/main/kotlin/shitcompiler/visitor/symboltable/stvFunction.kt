package shitcompiler.visitor.symboltable

import shitcompiler.ast.function.FunctionCall
import shitcompiler.ast.function.FunctionDefinition
import shitcompiler.ast.function.FunctionOrProcedureDefinition
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

        val typeObj = table.findOrDefineType(type)

        paramObjs.add(ObjectRecord(name, Kind.PARAMETER, VarParam(typeObj), 0))
    }

    if (node is FunctionDefinition) {
        val returnType = table.findOrDefineType(node.returnType)
        table.define(node.name, Kind.FUNCTION, FunctionR(returnType, paramObjs))
    } else { // node is ProcedureDefinition
        table.define(node.name, Kind.PROCEDURE, Procedure(paramObjs))
    }

    visitBlock(node.block) { paramObjs.forEach({ table.define(it.name, Kind.VARIABLE, it.data) }) }
}

fun SymbolTableVisitor.visitFunctionCall(node: FunctionCall): ObjectRecord {
    val name = node.name
    val obj = table.find(name)
    return if (obj.kind == Kind.FUNCTION) {
        val function = obj.asFunction()
        // check one-to-one equality between call parameters and function parameters
        val formal = function.parameters
        val params = node.parameters

        if (formal.size == params.size) {
            for (i in 0..formal.lastIndex) {
                val formalType = formal[i].asParameter().type
                val paramType = visitExpression(params[i])

                if (formalType != paramType) {
                    errors.println("Parameter ${i + 1}-th has type $paramType, expected $formalType")
                    return typeUniversal
                }
            }
            function.returnType
        } else {
            errors.println("Function parameter count does not match given parameter count")
            typeUniversal
        }
    } else if (obj.kind == Kind.PROCEDURE) {
        typeUniversal
    } else {
        errors.println("Function call on $name which is not a function")
        typeUniversal
    }
}