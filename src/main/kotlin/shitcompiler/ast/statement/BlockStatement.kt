package shitcompiler.ast.statement

/**
 * Created by NuclearCoder on 03/03/17.
 */

class BlockStatement(lineNo: Int, val statements: List<Statement>) : Statement(lineNo) {

    override fun toString() = "Block $statements"

}