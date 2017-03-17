package shitcompiler.parser

import shitcompiler.ast.Program
import shitcompiler.ast.function.FunctionCall
import shitcompiler.ast.statement.Assignment
import shitcompiler.ast.statement.BlockStatement
import shitcompiler.ast.statement.EmptyStatement
import shitcompiler.ast.statement.Statement

/**
 * Created by NuclearCoder on 26/01/17.
 */

fun Parser.program(): Program {
    // a program is a collection of functions and struct declarations

    val statements = mutableListOf<Statement>()

    while (symbol in DECLARATION_SYMBOLS) {
        statement().let filter@ {
            if (it is EmptyStatement) // skip empty statements
                return@filter
            if (it is BlockStatement
                    || it is Assignment
                    || it is FunctionCall) {
                errors.println("Illegal statement type at the program level ${it::class.simpleName}")
                return@filter
            }
            statements.add(it)
        }
    }

    return Program(statements)
}