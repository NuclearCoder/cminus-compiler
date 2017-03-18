package shitcompiler.parser

import shitcompiler.NAME_BOOL
import shitcompiler.NAME_CHAR
import shitcompiler.NAME_INT
import shitcompiler.NO_NAME
import shitcompiler.ast.statement.Declaration
import shitcompiler.ast.type.ArrayTypeReference
import shitcompiler.ast.type.StructDefinition
import shitcompiler.ast.type.TypeReference
import shitcompiler.token.Symbol.*

/**
 * Created by NuclearCoder on 16/03/17.
 */

fun Parser.typeName(): Int {
    return when (symbol) {
        INT -> {
            expect(INT)
            NAME_INT
        }
        CHAR -> {
            expect(CHAR)
            NAME_CHAR
        }
        BOOL -> {
            expect(BOOL)
            NAME_BOOL
        }
        ID -> {
            val arg = argument
            expect(ID)
            arg
        }
        else -> {
            syntaxError()
            NO_NAME
        }
    }
}

fun Parser.typeReference(): TypeReference {
    /* a type is either a type or an array type*/
    var reference = TypeReference(lineNo, typeName())

    while (symbol == LEFT_BRACKET) {
        expect(LEFT_BRACKET)
        val length = argument
        expect(NUM_CONST)
        expect(RIGHT_BRACKET)

        reference = ArrayTypeReference(lineNo, reference, length)
    }

    return reference
}

fun Parser.structTypeDefinition(): StructDefinition {
    expect(BEGIN)

    val fields = mutableListOf<Declaration>()
    while (symbol in DECLARATION_SYMBOLS) {
        fields.add(declarationStatement())
    }

    expect(END)
    val name = argument
    expect(ID)
    expect(SEMICOLON)

    return StructDefinition(lineNo, name, fields)
}