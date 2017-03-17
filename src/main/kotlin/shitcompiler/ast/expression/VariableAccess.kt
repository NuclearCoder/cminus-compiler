package shitcompiler.ast.expression

/**
 * Created by NuclearCoder on 17/03/17.
 */

open class VariableAccess(val name: Int) : Expression {

    override fun toString() = "Access id($name)"

}