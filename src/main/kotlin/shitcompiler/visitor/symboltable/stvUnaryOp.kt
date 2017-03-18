package shitcompiler.visitor.symboltable

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
                errors.println("Expected integer expression")
                typeUniversal
            }
        }
        Symbol.NOT -> {
            if (type == typeBool) {
                typeBool
            } else {
                errors.println("Expected boolean expression")
                typeUniversal
            }
        }
        else -> {
            errors.println("Unexpected operator ${node.sym}")
            typeUniversal
        }
    }
}