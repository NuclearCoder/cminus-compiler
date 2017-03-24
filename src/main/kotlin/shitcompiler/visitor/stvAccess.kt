package shitcompiler.visitor

import shitcompiler.ast.expression.VariableAccess
import shitcompiler.ast.type.ArrayAccess
import shitcompiler.ast.type.FieldAccess
import shitcompiler.symboltable.Kind
import shitcompiler.symboltable.ObjectRecord

fun SymbolTableVisitor.visitVariableAccess(node: VariableAccess): ObjectRecord {
    return when (node) {
        is ArrayAccess -> visitArrayAccess(node)
        is FieldAccess -> visitFieldAccess(node)
        else -> {
            // simple identifier access
            val obj = table.find(node.lineNo, node.name)
            when (obj.kind) {
                Kind.CONSTANT -> obj.asConstant().type
                Kind.VARIABLE -> obj.asVariable().type
                Kind.PARAMETER -> obj.asParameter().type
                else -> {
                    error(node.lineNo, "Identifier '<(${node.name})>' must refer to a constant, variable or parameter")
                    typeUniversal
                }
            }
        }
    }
}

fun SymbolTableVisitor.visitArrayAccess(node: ArrayAccess): ObjectRecord {
    // check it's an array
    val type = visitVariableAccess(node.access)
    return if (type.kind == Kind.ARRAY_TYPE) {
        val selectorType = visitExpression(node.selector)
        if (selectorType == typeInt) {
            type.asArrayType().elementType
        } else {
            error(node.lineNo, "Indexed selector must be an integer expression")
            typeUniversal
        }
    } else {
        error(node.lineNo, "Indexed selector must act on an array")
        typeUniversal
    }
}