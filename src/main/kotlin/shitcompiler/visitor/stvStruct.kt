package shitcompiler.visitor

import shitcompiler.ast.access.FieldAccess
import shitcompiler.ast.type.StructDefinition
import shitcompiler.symboltable.Kind
import shitcompiler.symboltable.ObjectRecord
import shitcompiler.symboltable.classes.Field
import shitcompiler.symboltable.classes.StructType

/**
 * Created by NuclearCoder on 18/03/17.
 */

fun SymbolTableVisitor.visitStructDefinition(node: StructDefinition) {
    val fields = mutableListOf<ObjectRecord>()

    for (declaration in node.fields) {
        val names = declaration.names
        val type = declaration.type

        val typeObj = table.findOrDefineType(type)

        names.forEach { fields.add(ObjectRecord(it.name, Kind.FIELD, Field(typeObj), 0)) }
    }

    table.define(node.lineNo, node.name, Kind.STRUCT_TYPE, StructType(fields))
}

fun SymbolTableVisitor.visitFieldAccess(node: FieldAccess): ObjectRecord {
    // check it's a struct
    val type = visitVariableAccess(node.access)
    return fieldAccess(node.lineNo, type, node.field)
}

fun SymbolTableVisitor.fieldAccess(lineNo: Int, type: ObjectRecord, field: Int): ObjectRecord {
    return if (type.kind == Kind.STRUCT_TYPE) {
        // find the field
        val obj = type.asStructType().fields.firstOrNull { it.name == field }
        if (obj != null) {
            obj.asField().type
        } else {
            error(lineNo, "Undeclared field '<($field)>'")
            typeUniversal
        }
    } else {
        error(lineNo, "Field selector must act on a struct")
        typeUniversal
    }
}