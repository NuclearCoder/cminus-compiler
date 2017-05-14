package shitcompiler.ast.statement

import shitcompiler.ast.expression.Expression
import shitcompiler.ast.type.TypeReference

/**
 * Created by NuclearCoder on 03/03/17.
 */

class Declaration(lineNo: Int, val type: TypeReference, val names: List<Part>) : Statement(lineNo) {

    override fun toString() =
            "Declaration <$type> ${names.map(Part::toString).joinToString(", ")}"

    abstract class Part(val name: Int)

    class NameOnly(name: Int) : Part(name) {

        override fun toString() = "<($name)>"

    }

    class NameAssign(name: Int, val expr: Expression) : Part(name) {

        override fun toString() = "<($name)> = $expr"

    }

}