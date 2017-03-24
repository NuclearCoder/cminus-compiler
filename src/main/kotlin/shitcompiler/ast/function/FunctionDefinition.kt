package shitcompiler.ast.function

import shitcompiler.ast.statement.BlockStatement
import shitcompiler.ast.statement.Statement
import shitcompiler.ast.type.TypeReference

/**
 * Created by NuclearCoder on 18/03/17.
 */

class FunctionDefinition(lineNo: Int, val name: Int,
                         val returnType: TypeReference,
                         val parameters: List<FunctionParameter>,
                         val block: BlockStatement) : Statement(lineNo) {

    override fun toString(): String {
        val sb = StringBuilder("Function ")
        sb.append(returnType)
        sb.append(" <(")
        sb.append(name)
        sb.append(")> (")
        sb.append(parameters.map(FunctionParameter::toString).joinToString(", "))
        sb.append(") ")
        sb.append(block.toString())
        return sb.toString()
    }

}