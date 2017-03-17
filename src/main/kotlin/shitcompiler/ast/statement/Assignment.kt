package shitcompiler.ast.statement

import shitcompiler.ast.expression.Expression
import shitcompiler.ast.type.VariableAccess
import shitcompiler.token.Symbol

/**
 * Created by NuclearCoder on 03/03/17.
 */

class Assignment(val sym: Symbol, val access: VariableAccess, val value: Expression) : Statement {

    override fun toString() = "Assignment {$access $sym $value}"

}