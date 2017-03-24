package shitcompiler.ast

import shitcompiler.ast.statement.Statement
import shitcompiler.tab

/**
 * Created by NuclearCoder on 26/01/17.
 */

class Program(lineNo: Int, val statements: List<Statement>) : AST(lineNo) {

    override fun toString(): String {
        val sb = StringBuilder("Program {\n")
        statements.forEach {
            sb.append(it.toString().tab())
            sb.append('\n')
        }
        sb.append('}')
        return sb.toString()
    }

}