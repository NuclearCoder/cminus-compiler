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

    override fun toString() = "FunctionDefinition $returnType id($name) $parameters { $block }"

}