package shitcompiler.parser

import shitcompiler.ast.statement.*
import shitcompiler.ast.type.TypeReference
import shitcompiler.token.Symbol.*

/**
 * Created by NuclearCoder on 03/03/17.
 */

fun Parser.statementList(): List<Statement> {
    val statements = mutableListOf<Statement>()
    while (symbol in STATEMENT_SYMBOLS) {
        statements.add(statement())
    }
    return statements
}

fun Parser.statement(): Statement {
    return when (symbol) {
        BEGIN -> blockStatement()
        STRUCT -> structStatement()
        INT, CHAR, BOOL -> declarationOrFunctionStatement()
        ID -> assignmentStatement()
        SEMICOLON -> emptyStatement()
        else -> {
            syntaxError()
            emptyStatement()
        }
    }
}

fun Parser.structStatement(): Statement {
    expect(STRUCT)
    return if (symbol == ID) declarationOrFunctionStatement()
    else structTypeDefinition()
}

fun Parser.declarationOrFunctionStatement(): Statement {
    val type = typeReference()

    val name = argument
    expect(ID)

    // it might be a function definition
    if (symbol == LEFT_PARENTHESIS) {
        return functionDefinition(name, type)
    } else {
        return declarationStatement(name, type)
    }
}

fun Parser.declarationStatement(): Declaration {
    val type = typeReference()
    val name = argument
    expect(ID)
    return declarationStatement(name, type)
}

fun Parser.declarationStatement(firstName: Int, type: TypeReference): Declaration {
    val names = nameGroup(firstName)
    expect(SEMICOLON)
    return Declaration(type, names)
}

fun Parser.assignmentStatement(): Assignment {
    val access = variableAccess()

    val sym = symbol
    if (symbol in ASSIGNMENT_SYMBOLS) {
        expect(symbol)
    } else {
        syntaxError()
    }
    val value = expression()
    expect(SEMICOLON)
    return Assignment(sym, access, value)
}

fun Parser.blockStatement(): BlockStatement {
    expect(BEGIN)
    val statements = statementList()
    expect(END)
    return BlockStatement(statements)
}

fun Parser.emptyStatement(): EmptyStatement {
    expect(SEMICOLON)
    return EmptyStatement()
}