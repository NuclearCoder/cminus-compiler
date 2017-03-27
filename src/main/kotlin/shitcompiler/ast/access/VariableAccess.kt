package shitcompiler.ast.access

import shitcompiler.ast.expression.Expression

/**
 * Created by NuclearCoder on 17/03/17.
 */

open class VariableAccess(lineNo: Int, val name: Int) : Expression(lineNo) {

    override fun toString() = "<($name)>"

}