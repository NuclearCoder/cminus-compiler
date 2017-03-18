package shitcompiler.visitor.symboltable

import shitcompiler.ast.function.FunctionCallStatement
import shitcompiler.ast.function.FunctionDefinition
import shitcompiler.ast.function.ProcedureDefinition
import shitcompiler.ast.statement.*
import shitcompiler.ast.type.StructDefinition
import shitcompiler.println
import shitcompiler.symboltable.Kind
import shitcompiler.symboltable.classes.VarParam

/**
 * Created by NuclearCoder on 18/03/17.
 */

fun SymbolTableVisitor.visitStatement(node: Statement) {
    when (node) {
        is BlockStatement -> visitBlock(node)
        is Declaration -> visitDeclaration(node)
        is Assignment -> visitAssignment(node)
        is StructDefinition -> visitStructDefinition(node)
        is FunctionDefinition -> visitFunctionDefinition(node)
        is ProcedureDefinition -> visitFunctionDefinition(node)
        is FunctionCallStatement -> visitFunctionCallStatement(node)
        is EmptyStatement -> Unit
        else -> {
            errors.println(node.lineNo, "Unhandled statement node ${node::class.simpleName}")
        }
    }
}

fun SymbolTableVisitor.visitBlock(node: BlockStatement, before: () -> Unit = {}) {
    table.beginBlock()
    before()

    for (statement in node.statements) {
        visitStatement(statement)
    }

    table.endBlock()
}

fun SymbolTableVisitor.visitDeclaration(node: Declaration) {
    val names = node.names
    val type = node.type

    val typeObj = table.findOrDefineType(node.lineNo, type)

    names.forEach { table.define(node.lineNo, it, Kind.VARIABLE, VarParam(typeObj)) }
}

fun SymbolTableVisitor.visitAssignment(node: Assignment) {
    val accessType = visitVariableAccess(node.access)
    val exprType = visitExpression(node.value)

    if (accessType != exprType) {
        errors.println(node.lineNo, "Trying to assign $exprType to $accessType")
    }
}