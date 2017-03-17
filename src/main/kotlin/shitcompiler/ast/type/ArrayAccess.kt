package shitcompiler.ast.type

import shitcompiler.ast.expression.Expression

/**
 * Created by NuclearCoder on 17/03/17.
 */

class ArrayAccess(val access: VariableAccess, val selector: Expression) : VariableAccess() {

    override fun toString() = "$access[$selector]"

}