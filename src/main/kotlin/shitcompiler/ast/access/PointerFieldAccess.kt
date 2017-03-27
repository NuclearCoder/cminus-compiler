package shitcompiler.ast.access

/**
 * Created by NuclearCoder on 27/03/17.
 */

class PointerFieldAccess(lineNo: Int, val access: VariableAccess, val field: Int) : VariableAccess(lineNo, 0) {

    override fun toString() = "$access-><($field)>"

}
