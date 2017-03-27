package shitcompiler.visitor

import shitcompiler.ast.function.FunctionCallStatement
import shitcompiler.ast.function.FunctionDefinition
import shitcompiler.ast.statement.*
import shitcompiler.ast.type.StructDefinition
import shitcompiler.parser.ASSIGN_INT_SYMBOLS
import shitcompiler.symboltable.Kind
import shitcompiler.symboltable.ObjectRecord
import shitcompiler.symboltable.classes.VarParam
import kotlin.properties.Delegates.notNull

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
        // check the expression before the declaration, to avoid potential recursive assignment
        var expr: ObjectRecord by notNull()
        if (it is Declaration.NameAssign) {
            expr = visitExpression(it.expr)
        }
        table.define(node.lineNo, it.name, Kind.VARIABLE, VarParam(typeObj))
        if (it is Declaration.NameAssign) {
            val access = simpleAccess(node.lineNo, it.name)
            assignment(node.lineNo, access, expr)
        }
    }
}

fun SymbolTableVisitor.visitAssignment(node: Assignment) {
    val access = visitVariableAccess(node.access)
    val expr = visitExpression(node.value)

    if (node.sym in ASSIGN_INT_SYMBOLS && (access != typeInt || expr != typeInt)) {
        error(node.lineNo, "Trying to use assignment operators on non-integer types")
    } else {
        assignment(node.lineNo, access, expr)
    }
}

fun SymbolTableVisitor.assignment(lineNo: Int, access: ObjectRecord, expr: ObjectRecord) {
    if (expr == typeVoid) {
        error(lineNo, "Void expressions aren't assignable")
    }
    if (access != expr) {
        error(lineNo, "Trying to assign $expr to $access")
    }
}