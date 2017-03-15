package shitcompiler.parser

import shitcompiler.ast.AST
import shitcompiler.ast.statement.Assignment
import shitcompiler.ast.statement.BlockStatement
import shitcompiler.ast.statement.Declaration
import shitcompiler.ast.statement.EmptyStatement
import shitcompiler.token.Symbol.*

/**
* Created by NuclearCoder on 03/03/17.
*/

private fun Parser.statementList(): List<AST> {
    val statements = mutableListOf<AST>()
    while (symbol in STATEMENT_SYMBOLS) {
        statements.add(statement())
    }
    return statements
}

fun Parser.statement(): AST {
    return when (symbol) {
        BEGIN -> blockStatement()
        INT, BOOL, CHAR -> declarationStatement()
        ID -> assignmentStatement()
        SEMICOLON -> emptyStatement()
        else -> {
            syntaxError()
            emptyStatement()
        }
    }
}

fun Parser.declarationStatement(): AST {
    val sym = symbol
    expect(symbol)
    val name = argument
    expect(ID)
    expect(SEMICOLON)
    return Declaration(sym, name)
}

fun Parser.assignmentStatement(): AST {
    val name = argument
    expect(ID)
    val sym = symbol
    if (symbol in ASSIGNMENT_SYMBOLS) {
        expect(symbol)
    } else {
        syntaxError()
    }
    val value = expression()
    expect(SEMICOLON)
    return Assignment(sym, name, value)
}

fun Parser.blockStatement(): AST {
    expect(BEGIN)
    val statements = statementList()
    expect(END)
    return BlockStatement(statements)
}

fun Parser.emptyStatement(): AST {
    expect(SEMICOLON)
    return EmptyStatement()
}