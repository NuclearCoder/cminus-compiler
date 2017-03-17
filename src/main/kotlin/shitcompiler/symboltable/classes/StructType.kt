package shitcompiler.symboltable.classes

import shitcompiler.symboltable.ObjectClass
import shitcompiler.symboltable.ObjectRecord

data class StructType(val fields: List<ObjectRecord>) : ObjectClass