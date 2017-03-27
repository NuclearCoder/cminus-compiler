package shitcompiler.visitor

import shitcompiler.ast.function.FunctionCallStatement
import shitcompiler.ast.function.FunctionDefinition
import shitcompiler.ast.statement.*
import shitcompiler.ast.type.StructDefinition
import shitcompiler.symboltable.Kind
import shitcompiler.symboltable.ObjectRecord
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
        is FunctionCallStatement -> visitFunctionCallStatement(node)
        is EmptyStatement -> Unit
        else -> {
            error(node.lineNo, "Unhandled statement node ${node::class.simpleName}")
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

    val typeObj = table.findOrDefineType(type)
    if (typeObj == typeVoid) {
        error(node.lineNo, "Void variables can not be declared")
    }

    // process assign as they are declared, to respect order of declaration
    names.forEach {
        table.define(node.lineNo, it.name, Kind.VARIABLE, VarParam(typeObj))
        if (it is Declaration.NameAssign) {
            assignment(node.lineNo, simpleAccess(node.lineNo, it.name), visitExpression(it.expr))
        }
    }
}

fun SymbolTableVisitor.visitAssignment(node: Assignment) {
    assignment(node.lineNo, visitVariableAccess(node.access), visitExpression(node.value))
}

fun SymbolTableVisitor.assignment(lineNo: Int, accessType: ObjectRecord, exprType: ObjectRecord) {
    if (exprType == typeVoid) {
        error(lineNo, "Void expressions aren't assignable")
    }
    if (accessType != exprType) {
        error(lineNo, "Trying to assign $exprType to $accessType")
    }
}