package shitcompiler.ast.function

import shitcompiler.ast.expression.Expression
import shitcompiler.ast.statement.Statement

/**
 * Created by NuclearCoder on 17/03/17.
 */

class FunctionCallStatement(lineNo: Int, val name: Int, val parameters: List<Expression>) : Statement(lineNo) {

    override fun toString() = "FunctionCall id(<($name)>) $parameters"

}