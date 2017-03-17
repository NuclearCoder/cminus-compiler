package shitcompiler.ast.function

import shitcompiler.ast.statement.BlockStatement
import shitcompiler.ast.statement.Statement
import shitcompiler.ast.type.TypeReference

class FunctionDefinition(val name: Int, val returnType: TypeReference,
                         val parameters: List<FunctionParameter>,
                         val block: BlockStatement) : Statement {

    override fun toString() = "FunctionDefinition $returnType id($name) $parameters { $block }"

}