package shitcompiler.ast.access

/**
 * Created by NuclearCoder on 27/03/17.
 */

class PointerAccess(lineNo: Int, val access: VariableAccess) : VariableAccess(lineNo, 0) {

    override fun toString() = "*$access"

}
