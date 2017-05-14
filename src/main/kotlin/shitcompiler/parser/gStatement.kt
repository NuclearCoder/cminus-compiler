package shitcompiler.parser

import shitcompiler.ast.function.FunctionCallStatement
import shitcompiler.ast.function.FunctionDefinition
import shitcompiler.ast.statement.Assignment
import shitcompiler.ast.statement.BlockStatement
import shitcompiler.ast.statement.Declaration
import shitcompiler.ast.statement.EmptyStatement
import shitcompiler.ast.statement.Statement
import shitcompiler.ast.type.StructDefinition
import shitcompiler.ast.type.TypeReference
import shitcompiler.println
import shitcompiler.token.Symbol.*
import kotlin.reflect.KClass

/**
 * Created by NuclearCoder on 03/03/17.
 */

fun Parser.statementList(vararg without: KClass<out Statement>, ignore: Boolean): List<Statement> {
    val statements = mutableListOf<Statement>()

    while (symbol in STATEMENT_SYMBOLS) {
        statement().let {
            if (it !is EmptyStatement) {
                if (it::class in without) {
                    errors.println(lineNo, "Illegal statement type ${it::class.simpleName}")
                    if (!ignore)
                        return@statementList statements // stop there
                }
                statements.add(it)
            }
        }
    }

    return statements
}

fun Parser.statement(): Statement {
    return when (symbol) {
        BEGIN -> blockStatement()
        STRUCT -> structStatement()
        INT, CHAR, BOOL, VOID -> declarationOrFunctionStatement()
        ID -> assignmentOrFunctionStatement()
        ASTERISK -> assignmentStatement()
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
        return declarationStatement(name, type, canAssign = true)
    }
}

fun Parser.declarationStatement(canAssign: Boolean): Declaration {
    val type = typeReference()
    val name = argument
    expect(ID)
    return declarationStatement(name, type, canAssign)
}

fun Parser.declarationStatement(firstName: Int, type: TypeReference, canAssign: Boolean): Declaration {
    val names = nameGroup(firstName, canAssign)
    expect(SEMICOLON)
    return Declaration(lineNo, type, names)
}

fun Parser.assignmentOrFunctionStatement(): Statement {
    val name = argument
    expect(ID)

    // it might be a function call
    if (symbol == LEFT_PARENTHESIS) {
        return functionCall(name, isStatement = true) as Statement
    } else {
        return assignmentStatement(name, pointerDepth = 0)
    }
}

fun Parser.assignmentStatement(): Assignment {
    var pointerDepth = 0
    while (symbol == ASTERISK) {
        expect(ASTERISK)
        pointerDepth++
    }

    // TODO: parentheses in variable access

    val name = argument
    expect(ID)

    return assignmentStatement(name, pointerDepth)
}

fun Parser.assignmentStatement(name: Int, pointerDepth: Int): Assignment {
    val access = variableAccess(name, pointerDepth)

    val sym = symbol
    if (symbol in ASSIGN_OP_SYMBOLS) {
        expect(symbol)
    } else {
        syntaxError()
    }

    val value = expression()
    expect(SEMICOLON)
    return Assignment(lineNo, sym, access, value)
}

fun Parser.blockStatement(): BlockStatement {
    expect(BEGIN)

    /* a block is a list of definitions followed by statements */
    val definitions = statementList(
            BlockStatement::class,
            FunctionDefinition::class,
            FunctionCallStatement::class,
            Assignment::class,
            ignore = false)

    val statements = statementList(
            StructDefinition::class,
            Declaration::class,
            FunctionDefinition::class,
            ignore = true)

    expect(END)
    return BlockStatement(lineNo, definitions, statements)
}

fun Parser.emptyStatement(): Statement {
    expect(SEMICOLON)
    return EmptyStatement(lineNo)
}