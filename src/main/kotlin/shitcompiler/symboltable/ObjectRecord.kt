package shitcompiler.symboltable

import shitcompiler.symboltable.classes.*
import shitcompiler.symboltable.classes.Nothing

/**
 * Created by NuclearCoder on 06/03/17.
 */

data class ObjectRecord(val name: Int, val kind: Kind, val data: ObjectClass) {

    fun asConstant() = data as Constant
    fun asArrayType() = data as ArrayType
    fun asStructType() = data as StructType
    fun asField() = data as Field
    fun asVariable() = data as VarParam
    fun asParameter() = data as VarParam

    override fun toString() = "[$name: $kind]" + (if (data == Nothing) "" else "($data)")

}