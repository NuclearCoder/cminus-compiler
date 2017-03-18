package shitcompiler.ast.function

import shitcompiler.ast.statement.BlockStatement

class ProcedureDefinition(lineNo: Int, name: Int,
                          parameters: List<FunctionParameter>,
                          block: BlockStatement) : FunctionOrProcedureDefinition(lineNo, name, parameters, block) {

    override fun toString() = "ProcedureDefinition id($name) $parameters { $block }"

}