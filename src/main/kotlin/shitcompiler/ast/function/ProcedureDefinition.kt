package shitcompiler.ast.function

import shitcompiler.ast.statement.BlockStatement

class ProcedureDefinition(name: Int, parameters: List<FunctionParameter>,
                          block: BlockStatement) : FunctionOrProcedureDefinition(name, parameters, block) {

    override fun toString() = "ProcedureDefinition id($name) $parameters { $block }"

}