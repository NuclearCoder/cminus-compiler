package shitcompiler.ast.statement

import shitcompiler.ast.type.TypeReference

/**
 * Created by NuclearCoder on 03/03/17.
 */

class Declaration(lineNo: Int, val type: TypeReference, val names: List<Int>) : Statement(lineNo) {

    override fun toString() =
            "Declaration $type ${names.map { "<($it)>" }.joinToString(", ")}"

}