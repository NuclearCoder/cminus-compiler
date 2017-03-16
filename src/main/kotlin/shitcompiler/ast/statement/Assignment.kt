package shitcompiler.ast.statement

import shitcompiler.ast.expression.Expression
import shitcompiler.token.Symbol

/**
 * Created by NuclearCoder on 03/03/17.
 */

class Assignment(val sym: Symbol, val name: Int, val value: Expression) : Statement {

    override fun toString() = "Assignment {id($name) $sym $value}"

}