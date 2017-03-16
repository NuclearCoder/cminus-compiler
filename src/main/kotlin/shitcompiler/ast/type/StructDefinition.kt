package shitcompiler.ast.type

import shitcompiler.ast.statement.Declaration
import shitcompiler.ast.statement.Statement

/**
 * Created by NuclearCoder on 16/03/17.
 */

class StructDefinition(val name: Int, val fields: List<Declaration>) : Statement {

    override fun toString() = "Struct (id($name), $fields)"

}