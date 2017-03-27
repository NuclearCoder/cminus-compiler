package shitcompiler.ast.expression

import shitcompiler.token.Symbol

/**
 * Created by NuclearCoder on 03/03/17.
 */

class UnaryOp(lineNo: Int, val sym: Symbol, val operand: Expression) : Expression(lineNo) {

    override fun toString() = "$sym $operand"
}
