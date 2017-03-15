package shitcompiler.ast.statement

import shitcompiler.ast.AST

/**
* Created by NuclearCoder on 03/03/17.
*/

class BlockStatement(val statements: List<AST>) : AST {

    override fun toString() = "Block {$statements}"

}