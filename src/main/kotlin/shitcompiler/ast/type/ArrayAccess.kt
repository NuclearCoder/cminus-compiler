package shitcompiler.ast.type

import shitcompiler.ast.expression.Expression
import shitcompiler.ast.expression.VariableAccess

/**
 * Created by NuclearCoder on 17/03/17.
 */

class ArrayAccess(val access: VariableAccess, val selector: Expression) : VariableAccess(0) {

    override fun toString() = "$access[$selector]"

}