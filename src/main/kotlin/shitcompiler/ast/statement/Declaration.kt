package shitcompiler.ast.statement

import shitcompiler.ast.type.TypeReference

/**
 * Created by NuclearCoder on 03/03/17.
 */

class Declaration(val type: TypeReference, val names: List<Int>) : Statement {

    override fun toString() = "Declaration $type${names.map { "id($it)" }}"

}