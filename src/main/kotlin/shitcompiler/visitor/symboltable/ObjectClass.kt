package shitcompiler.visitor.symboltable

/**
* Created by NuclearCoder on 10/03/17.
*/

enum class Kind {
    CONSTANT,
    STANDARD_TYPE,
    ARRAY_TYPE,
    RECORD_TYPE,
    FIELD,
    VARIABLE,
    PARAMETER,

    UNDEFINED
}

sealed class ObjectClass

data class Constant(val value: Int, val type: ObjectRecord) : ObjectClass()

data class ArrayType(val length: Int, val indexType: ObjectRecord, val elementType: ObjectRecord) : ObjectClass()

data class RecordType(val fields: List<ObjectRecord>) : ObjectClass()

data class Field(val type: ObjectRecord) : ObjectClass()

data class VarParam(val type: ObjectRecord) : ObjectClass()

object Nothing : ObjectClass() {
    override fun toString() = "Nothing ()"
}