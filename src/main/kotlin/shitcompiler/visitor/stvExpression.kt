package shitcompiler.visitor

import shitcompiler.ast.access.VariableAccess
import shitcompiler.ast.expression.Atom
import shitcompiler.ast.expression.BinaryOp
import shitcompiler.ast.expression.Expression
import shitcompiler.ast.expression.UnaryOp
import shitcompiler.ast.function.FunctionCallExpression
import shitcompiler.symboltable.ObjectRecord

/**
 * Created by NuclearCoder on 18/03/17.
 */

fun SymbolTableVisitor.visitExpression(node: Expression): ObjectRecord {
    return when (node) {
        is BinaryOp -> visitBinaryOp(node)
        is UnaryOp -> visitUnaryOp(node)
        is VariableAccess -> visitVariableAccess(node)
        is FunctionCallExpression -> visitFunctionCallExpression(node)
        is Atom.Integer -> visitInteger(node)
        is Atom.Char -> visitCharacter(node)
        else -> {
            error(node.lineNo, "Unhandled expression node ${node::class.simpleName}")
            typeUniversal
        }
    }
}

