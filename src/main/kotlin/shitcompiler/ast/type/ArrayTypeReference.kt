package shitcompiler.ast.type

/**
 * Created by NuclearCoder on 3/17/2017.
 */

class ArrayTypeReference(val elementType: TypeReference, val length: Int) : TypeReference(0) {

    override fun toString() = "$elementType[$length]"

}