package shitcompiler.symboltable.classes

import shitcompiler.symboltable.ObjectClass
import shitcompiler.symboltable.ObjectRecord

data class ArrayType(val elementType: ObjectRecord, val length: Int) : ObjectClass