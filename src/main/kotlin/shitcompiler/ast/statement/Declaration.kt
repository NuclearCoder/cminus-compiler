package shitcompiler.ast.statement

import shitcompiler.ast.type.TypeReference

/**
 * Created by NuclearCoder on 03/03/17.
 */

class Declaration(val type: TypeReference, val name: Int) : Statement {

    override fun toString() = "Declaration {$type id($name)}"

}