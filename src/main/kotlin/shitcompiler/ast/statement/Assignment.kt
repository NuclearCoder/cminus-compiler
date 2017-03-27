package shitcompiler.ast.statement

import shitcompiler.ast.access.VariableAccess
import shitcompiler.ast.expression.Expression
import shitcompiler.token.Symbol

/**
 * Created by NuclearCoder on 03/03/17.
 */

class Assignment(lineNo: Int, val sym: Symbol, val access: VariableAccess, val value: Expression) : Statement(lineNo) {

    override fun toString() = "Assign $access $sym $value"

}