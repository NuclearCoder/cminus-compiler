package shitcompiler.parser

import shitcompiler.ast.expression.Expression
import shitcompiler.ast.type.ArrayAccess
import shitcompiler.ast.type.FieldAccess
import shitcompiler.ast.type.VariableAccess
import shitcompiler.token.Symbol.*

/**
 * Created by NuclearCoder on 16/03/17.
 */

fun Parser.nameGroup(): List<Int> {
    val names = mutableListOf<Int>()

    names.add(argument)
    expect(ID)
    while (symbol == COMMA) {
        expect(COMMA)
        names.add(argument)
        expect(ID)
    }

    return names
}

fun Parser.variableAccess(): VariableAccess {
    val name = argument
    expect(ID)

    var access = VariableAccess(name)
    while (symbol in SELECTOR_SYMBOLS) {
        if (symbol == LEFT_BRACKET) {
            access = ArrayAccess(access, arraySelector())
        } else {
            access = FieldAccess(access, fieldSelector())
        }
    }

    return access
}

fun Parser.arraySelector(): Expression {
    expect(LEFT_BRACKET)
    val selector = expression()
    expect(RIGHT_BRACKET)

    return selector
}

fun Parser.fieldSelector(): Int {
    expect(PERIOD)
    val field = argument
    expect(ID)

    return field
}