package shitcompiler.parser

import shitcompiler.ast.expression.Expression
import shitcompiler.ast.expression.VariableAccess
import shitcompiler.ast.type.ArrayAccess
import shitcompiler.ast.type.FieldAccess
import shitcompiler.token.Symbol.*

/**
 * Created by NuclearCoder on 16/03/17.
 */

fun Parser.nameGroup(firstName: Int): List<Int> {
    val names = mutableListOf(firstName)

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
    return variableAccess(name)
}

fun Parser.variableAccess(name: Int): VariableAccess {
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