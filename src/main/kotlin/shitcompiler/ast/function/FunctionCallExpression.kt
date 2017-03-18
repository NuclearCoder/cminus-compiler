package shitcompiler.ast.function

import shitcompiler.ast.expression.Expression

/**
 * Created by NuclearCoder on 17/03/17.
 */

class FunctionCallExpression(lineNo: Int, val name: Int, val parameters: List<Expression>) : Expression(lineNo) {

    override fun toString() = "FunctionCall id($name) $parameters"

}