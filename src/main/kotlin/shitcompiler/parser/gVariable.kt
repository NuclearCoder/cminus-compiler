package shitcompiler.parser

import shitcompiler.ast.expression.Expression
import shitcompiler.ast.expression.VariableAccess
import shitcompiler.ast.statement.Declaration
import shitcompiler.ast.type.ArrayAccess
import shitcompiler.ast.type.FieldAccess
import shitcompiler.token.Symbol.*

/**
 * Created by NuclearCoder on 16/03/17.
 */

fun Parser.nameGroup(firstName: Int, canAssign: Boolean): List<Declaration.Part> {
    val names = mutableListOf(nameGroupPart(firstName, canAssign))

    while (symbol == COMMA) {
        expect(COMMA)
        val name = argument
        expect(ID)
        names.add(nameGroupPart(name, canAssign))
    }

    return names
}

fun Parser.nameGroupPart(name: Int, canAssign: Boolean): Declaration.Part {
    if (canAssign && symbol == BECOMES) {
        expect(symbol)
        return Declaration.NameAssign(name, expression())
    } else {
        return Declaration.NameOnly(name)
    }
}

fun Parser.variableAccess(name: Int): VariableAccess {
    var access = VariableAccess(lineNo, name)
    while (symbol in SELECTOR_SYMBOLS) {
        if (symbol == LEFT_BRACKET) {
            access = ArrayAccess(lineNo, access, arraySelector())
        } else {
            access = FieldAccess(lineNo, access, fieldSelector())
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