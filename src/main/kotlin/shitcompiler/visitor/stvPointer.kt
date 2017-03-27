package shitcompiler.visitor

import shitcompiler.ast.access.PointerAccess
import shitcompiler.ast.access.PointerFieldAccess
import shitcompiler.symboltable.Kind
import shitcompiler.symboltable.ObjectRecord

/**
 * Created by NuclearCoder on 27/03/17.
 */


fun SymbolTableVisitor.visitPointerAccess(node: PointerAccess): ObjectRecord {
    val ptr = visitVariableAccess(node.access)
    return pointerDereference(node.lineNo, ptr)
}

fun SymbolTableVisitor.visitPointerFieldAccess(node: PointerFieldAccess): ObjectRecord {
    val ptr = visitVariableAccess(node.access)
    val obj = pointerDereference(node.lineNo, ptr)
    return fieldAccess(node.lineNo, obj, node.field)
}

private fun SymbolTableVisitor.pointerDereference(lineNo: Int, obj: ObjectRecord): ObjectRecord {
    return if (obj.kind == Kind.POINTER_TYPE) {
        obj.asPointerType().elementType
    } else {
        error(lineNo, "Dereferencing '<(${obj.name})>' which is not a pointer")
        typeUniversal
    }
}