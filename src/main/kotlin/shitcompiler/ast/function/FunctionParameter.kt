package shitcompiler.ast.function

import shitcompiler.ast.type.TypeReference

/**
 * Created by NuclearCoder on 17/03/17.
 */

class FunctionParameter(val type: TypeReference, val name: Int) {

    override fun toString() = "$type <($name)>"

}