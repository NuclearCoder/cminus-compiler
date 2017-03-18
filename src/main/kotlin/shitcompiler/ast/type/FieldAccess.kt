package shitcompiler.ast.type

import shitcompiler.ast.expression.VariableAccess

/**
 * Created by NuclearCoder on 17/03/17.
 */

class FieldAccess(lineNo: Int, val access: VariableAccess, val field: Int) : VariableAccess(lineNo, 0) {

    override fun toString() = "$access.$field"

}