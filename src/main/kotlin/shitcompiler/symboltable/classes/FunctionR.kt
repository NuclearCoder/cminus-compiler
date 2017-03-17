package shitcompiler.symboltable.classes

import shitcompiler.symboltable.ObjectClass
import shitcompiler.symboltable.ObjectRecord

/**
 * Created by NuclearCoder on 17/03/17.
 */

data class FunctionR(val returnType: ObjectRecord, val parameters: List<ObjectRecord>) : ObjectClass