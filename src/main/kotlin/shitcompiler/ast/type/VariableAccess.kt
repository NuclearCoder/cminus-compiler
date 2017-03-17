package shitcompiler.ast.type

import shitcompiler.ast.expression.Expression

/**
 * Created by NuclearCoder on 17/03/17.
 */

open class VariableAccess(val name: Int) : Expression {
    protected constructor() : this(0)

    override fun toString() = "Access id($name)"

}