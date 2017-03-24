package shitcompiler.ast.type

import shitcompiler.ast.statement.Declaration
import shitcompiler.ast.statement.Statement
import shitcompiler.tab

/**
 * Created by NuclearCoder on 16/03/17.
 */

class StructDefinition(lineNo: Int, val name: Int, val fields: List<Declaration>) : Statement(lineNo) {

    override fun toString(): String {
        val sb = StringBuilder("Struct ")
        sb.append("<(")
        sb.append(name)
        sb.append(")> {\n")
        sb.append(fields.map { it.toString().tab() }.joinToString(",\n"))
        sb.append("\n}")
        return sb.toString()
    }

}