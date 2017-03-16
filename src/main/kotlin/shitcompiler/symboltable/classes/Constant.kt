package shitcompiler.symboltable.classes

import shitcompiler.symboltable.ObjectClass
import shitcompiler.symboltable.ObjectRecord

data class Constant(val value: Int, val type: ObjectRecord) : ObjectClass