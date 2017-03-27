package shitcompiler.ast.expression


/**
 * Created by NuclearCoder on 02/03/17.
 */

sealed class Atom(lineNo: Int, val value: Int) : Expression(lineNo) {

    class Integer(lineNo: Int, value: Int) : Atom(lineNo, value) {
        override fun toString() = "$value"
    }

    class Char(lineNo: Int, value: Int) : Atom(lineNo, value) {
        override fun toString() = "'$value'"
    }

    class Unknown(lineNo: Int) : Atom(lineNo, 0) {
        override fun toString() = "Unknown"
    }

}