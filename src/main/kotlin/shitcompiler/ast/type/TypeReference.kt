package shitcompiler.ast.type

import shitcompiler.ast.AST

/**
 * Created by NuclearCoder on 16/03/17.
 */

// if length is negative, the type is not considered an array
class TypeReference(val name: Int, val length: Int) : AST {

    override fun toString() = "type($name)" + (if (length < 0) "" else "[$length]")

}