package shitcompiler.ast.expression

import shitcompiler.token.Symbol

/**
 * Created by NuclearCoder on 02/03/17.
 */

class BinaryOp(lineNo: Int, val sym: Symbol, val left: Expression, val right: Expression) : Expression(lineNo) {

    override fun toString() = "BinaryOp $left $sym $right"

}