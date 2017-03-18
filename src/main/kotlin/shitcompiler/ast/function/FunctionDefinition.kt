package shitcompiler.ast.function

import shitcompiler.ast.statement.BlockStatement
import shitcompiler.ast.type.TypeReference

class FunctionDefinition(lineNo: Int, name: Int,
                         val returnType: TypeReference,
                         parameters: List<FunctionParameter>,
                         block: BlockStatement) : FunctionOrProcedureDefinition(lineNo, name, parameters, block) {

    override fun toString() = "FunctionDefinition $returnType id($name) $parameters { $block }"

}