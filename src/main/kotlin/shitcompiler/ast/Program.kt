package shitcompiler.ast

import shitcompiler.ast.statement.Statement

/**
 * Created by NuclearCoder on 26/01/17.
 */

class Program(val statements: List<Statement>) : AST {

    override fun toString() =
            "Program $statements"

}