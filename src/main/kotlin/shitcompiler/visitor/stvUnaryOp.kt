package shitcompiler.visitor

import shitcompiler.ast.expression.UnaryOp
import shitcompiler.symboltable.ObjectRecord
import shitcompiler.token.Symbol

fun SymbolTableVisitor.visitUnaryOp(node: UnaryOp): ObjectRecord {
    val type = visitExpression(node.operand)
    return when (node.sym) {
        Symbol.PLUS, Symbol.MINUS -> {
            if (type == typeInt) {
                typeInt
            } else {
                error(node.lineNo, "Expected integer expression")
                typeUniversal
            }
        }
        Symbol.NOT -> {
            if (type == typeBool) {
                typeBool
            } else {
                error(node.lineNo, "Expected boolean expression")
                typeUniversal
            }
        }
        else -> {
            error(node.lineNo, "Unexpected operator ${node.sym}")
            typeUniversal
        }
    }
}