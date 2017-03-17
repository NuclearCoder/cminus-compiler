package shitcompiler.ast.statement

/**
 * Created by NuclearCoder on 03/03/17.
 */

class BlockStatement(val statements: List<Statement>) : Statement {

    override fun toString() = "Block $statements"

}