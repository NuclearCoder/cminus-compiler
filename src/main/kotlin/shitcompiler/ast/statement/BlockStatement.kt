package shitcompiler.ast.statement

import shitcompiler.tab

/**
 * Created by NuclearCoder on 03/03/17.
 */

class BlockStatement(lineNo: Int, val definitions: List<Statement>, val statements: List<Statement>) : Statement(lineNo) {

    override fun toString(): String {
        val sb = StringBuilder("Block {\n")
        definitions.forEach {
            sb.append(it.toString().tab())
            sb.append('\n')
        }
        statements.forEach {
            sb.append(it.toString().tab())
            sb.append('\n')
        }
        sb.append('}')
        return sb.toString()
    }

}