package shitcompiler.visitor.symboltable

import shitcompiler.ast.statement.Assignment
import shitcompiler.ast.statement.BlockStatement
import shitcompiler.ast.statement.Declaration
import shitcompiler.symboltable.Kind
import shitcompiler.symboltable.classes.VarParam

/**
 * Created by NuclearCoder on 18/03/17.
 */

fun SymbolTableVisitor.visitBlock(node: BlockStatement, before: () -> Unit = {}) {
    table.beginBlock()
    before()

    for (statement in node.statements) {
        visit(statement)
    }

    table.endBlock()
}

fun SymbolTableVisitor.visitDeclaration(node: Declaration) {
    val names = node.names
    val type = node.type

    val typeObj = table.findOrDefineType(type)

    names.forEach { table.define(it, Kind.VARIABLE, VarParam(typeObj)) }
}

fun SymbolTableVisitor.visitAssignment(node: Assignment) {
    val accessType = visitVariableAccess(node.access)
    val exprType = visitExpression(node.value)

    if (accessType != exprType) {
        errors.println("Trying to assign $exprType to $accessType")
    }
}