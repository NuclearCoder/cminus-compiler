package shitcompiler.ast.function

import shitcompiler.ast.expression.Expression
import shitcompiler.ast.statement.Statement

/**
 * Created by NuclearCoder on 17/03/17.
 */

class FunctionCall(val name: Int, val parameters: List<Expression>) : Expression, Statement {

    override fun toString() = "FunctionCall id($name) $parameters"

}