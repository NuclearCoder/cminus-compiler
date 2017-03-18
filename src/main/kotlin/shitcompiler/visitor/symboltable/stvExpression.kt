package shitcompiler.visitor.symboltable

import shitcompiler.ast.expression.*
import shitcompiler.ast.function.FunctionCallExpression
import shitcompiler.println
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
            errors.println(node.lineNo, "Unhandled expression node ${node::class.simpleName}")
            typeUniversal
        }
    }
}

