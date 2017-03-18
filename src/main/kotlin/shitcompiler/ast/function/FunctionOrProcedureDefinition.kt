package shitcompiler.ast.function

import shitcompiler.ast.statement.BlockStatement
import shitcompiler.ast.statement.Statement

/**
 * Created by NuclearCoder on 18/03/17.
 */

abstract class FunctionOrProcedureDefinition(lineNo: Int, val name: Int,
                                             val parameters: List<FunctionParameter>,
                                             val block: BlockStatement) : Statement(lineNo)