package shitcompiler.ast.type

/**
 * Created by NuclearCoder on 3/17/2017.
 */

class ArrayTypeReference(lineNo: Int, val elementType: TypeReference, val length: Int) : TypeReference(lineNo, 0) {

    override fun toString() = "$elementType<$length>"

}