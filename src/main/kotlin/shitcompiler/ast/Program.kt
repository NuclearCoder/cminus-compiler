package shitcompiler.ast

import shitcompiler.ast.statement.Statement

/**
 * Created by NuclearCoder on 26/01/17.
 */

class Program(lineNo: Int, val statements: List<Statement>) : AST(lineNo) {

    override fun toString() =
            "Program $statements"

}