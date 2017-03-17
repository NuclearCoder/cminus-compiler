package shitcompiler.ast.expression

import shitcompiler.token.Symbol

/**
 * Created by NuclearCoder on 03/03/17.
 */

class UnaryOp(val sym: Symbol, val operand: Expression) : Expression {

    override fun toString() = "UnaryOp $sym $operand"
}
