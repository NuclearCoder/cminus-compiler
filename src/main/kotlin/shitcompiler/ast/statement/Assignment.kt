package shitcompiler.ast.statement

import shitcompiler.ast.AST
import shitcompiler.ast.expression.Expression
import shitcompiler.token.Symbol

/**
* Created by NuclearCoder on 03/03/17.
*/

class Assignment(val sym: Symbol, val name: Int, val value: Expression): AST {

    override fun toString() = "Assignment {$name $sym $value}"

}