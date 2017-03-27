package shitcompiler.ast.type

/**
 * Created by NuclearCoder on 3/17/2017.
 */

class PointerTypeReference(lineNo: Int, val elementType: TypeReference) : TypeReference(lineNo, 0) {

    override fun toString() = "$elementType*"

}