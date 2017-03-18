package shitcompiler.ast.statement

/**
 * Created by NuclearCoder on 03/03/17.
 */

class EmptyStatement(lineNo: Int) : Statement(lineNo) {

    override fun toString() = "Empty"

}