package shitcompiler.ast.function

import shitcompiler.ast.statement.BlockStatement
import shitcompiler.ast.type.TypeReference

class FunctionDefinition(name: Int, val returnType: TypeReference,
                         parameters: List<FunctionParameter>,
                         block: BlockStatement) : FunctionOrProcedureDefinition(name, parameters, block) {

    override fun toString() = "FunctionDefinition $returnType id($name) $parameters { $block }"

}