package shitcompiler.parser

import shitcompiler.*
import shitcompiler.ast.statement.Declaration
import shitcompiler.ast.type.ArrayTypeReference
import shitcompiler.ast.type.PointerTypeReference
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
        VOID -> {
            expect(VOID)
            NAME_VOID
        }
        ID, STRUCT -> {
            if (symbol == STRUCT)
                expect(STRUCT)

            val arg = argument
            expect(ID)
            arg
        }
        else -> {
            syntaxError()
            UNDEFINED
        }
    }
}

fun Parser.typeReference(): TypeReference {
    /* a type is either a type, a pointer or an array type */
    var reference = TypeReference(lineNo, typeName())

    while (symbol in TYPE_QUALIFIER_SYMBOLS) {
        if (symbol == LEFT_BRACKET) {
            expect(LEFT_BRACKET)
            val length = argument
            expect(NUM_CONST)
            expect(RIGHT_BRACKET)

            reference = ArrayTypeReference(lineNo, reference, length)
        } else {
            expect(ASTERISK)
            reference = PointerTypeReference(lineNo, reference)
        }
    }

    return reference
}

fun Parser.structTypeDefinition(): StructDefinition {
    expect(BEGIN)

    val fields = mutableListOf<Declaration>()
    while (symbol in DECLARATION_SYMBOLS) {
        fields.add(declarationStatement(canAssign = false))
    }

    expect(END)
    val name = argument
    expect(ID)
    expect(SEMICOLON)

    return StructDefinition(lineNo, name, fields)
}