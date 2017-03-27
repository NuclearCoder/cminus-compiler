package shitcompiler.ast.access

import shitcompiler.ast.expression.Expression

/**
 * Created by NuclearCoder on 17/03/17.
 */

class ArrayAccess(lineNo: Int, val access: VariableAccess, val selector: Expression) : VariableAccess(lineNo, 0) {

    override fun toString() = "$access[$selector]"

}