package shitcompiler.visitor.symboltable

import java.io.PrintWriter

/**
* Created by NuclearCoder on 06/03/17.
*/

data class ObjectRecord(val name: Int, val kind: Kind, val data: ObjectClass, private val errors: PrintWriter) {

    fun asConstant() = data as Constant
    fun asArrayType() = data as ArrayType
    fun asRecordType() = data as RecordType
    fun asField() = data as Field
    fun asVariable() = data as VarParam
    fun asParameter() = data as VarParam

}