package shitcompiler.visitor.symboltable

import shitcompiler.ast.expression.BinaryOp
import shitcompiler.println
import shitcompiler.symboltable.ObjectRecord
import shitcompiler.token.Symbol

fun SymbolTableVisitor.visitBinaryOp(node: BinaryOp): ObjectRecord {
    val typeLeft = visitExpression(node.left)
    val typeRight = visitExpression(node.right)

    return when (node.sym) {
        Symbol.PLUS, Symbol.MINUS, Symbol.ASTERISK, Symbol.DIV, Symbol.MOD -> {
            if (typeLeft == typeInt && typeLeft == typeRight) {
                typeInt
            } else {
                errors.println(node.lineNo, "Expected integer expressions")
                typeUniversal
            }
        }
        Symbol.AND, Symbol.OR -> {
            if (typeLeft == typeBool && typeLeft == typeRight) {
                typeBool
            } else {
                errors.println(node.lineNo, "Expected boolean expressions")
                typeUniversal
            }
        }
        Symbol.EQUAL, Symbol.NOT_EQUAL -> {
            if (typeLeft == typeRight) {
                typeBool
            } else {
                errors.println(node.lineNo, "Expected same-type expressions")
                typeUniversal
            }
        }
        Symbol.GREATER, Symbol.NOT_GREATER, Symbol.LESSER, Symbol.NOT_LESSER -> {
            if (typeLeft == typeInt && typeLeft == typeRight) {
                typeBool
            } else {
                errors.println(node.lineNo, "Expected integer expressions")
                typeUniversal
            }
        }
        else -> {
            errors.println(node.lineNo, "Unexpected operator ${node.sym}")
            typeUniversal
        }
    }
}