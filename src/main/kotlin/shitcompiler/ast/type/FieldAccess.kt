package shitcompiler.ast.type

/**
 * Created by NuclearCoder on 17/03/17.
 */

class FieldAccess(val access: VariableAccess, val field: Int) : VariableAccess() {

    override fun toString() = "$access.$field"

}