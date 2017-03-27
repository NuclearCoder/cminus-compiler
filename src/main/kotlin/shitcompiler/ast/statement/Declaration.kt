package shitcompiler.ast.statement

import shitcompiler.ast.expression.Expression
import shitcompiler.ast.type.TypeReference
import shitcompiler.tab

/**
 * Created by NuclearCoder on 03/03/17.
 */

class Declaration(lineNo: Int, val type: TypeReference, val names: List<Part>) : Statement(lineNo) {

    override fun toString() =
            if (names.size == 1)
                "Declaration $type ${names[0]}"
            else
                "Declaration $type\n${names.map { it.toString() }.joinToString(",\n").tab()}"

    abstract class Part(val name: Int)

    class NameOnly(name: Int) : Part(name) {

        override fun toString() = "<($name)>"

    }

    class NameAssign(name: Int, val expr: Expression) : Part(name) {

        override fun toString() = "<($name)> = $expr"

    }

}