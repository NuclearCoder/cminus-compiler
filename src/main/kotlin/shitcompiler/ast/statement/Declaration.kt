package shitcompiler.ast.statement

import shitcompiler.ast.AST
import shitcompiler.token.Symbol

/**
* Created by NuclearCoder on 03/03/17.
*/

class Declaration(val sym: Symbol, val name: Int): AST {

    override fun toString() = "Declaration {$sym $name}"

}