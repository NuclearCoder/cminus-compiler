package shitcompiler.parser

import shitcompiler.ast.Program
import shitcompiler.ast.function.FunctionCallStatement
import shitcompiler.ast.statement.Assignment
import shitcompiler.ast.statement.BlockStatement

/**
 * Created by NuclearCoder on 26/01/17.
 */

fun Parser.program(): Program {
    // a program is a collection of functions and struct declarations

    val statements = statementList(
            BlockStatement::class,
            FunctionCallStatement::class,
            Assignment::class,
            ignore = true)

    return Program(lineNo, statements)
}