package shitcompiler.ast.expression


/**
 * Created by NuclearCoder on 02/03/17.
 */

sealed class Atom(val value: Int) : Expression {

    class Integer(value: Int) : Atom(value) {
        override fun toString() = "int($value)"
    }

    class Char(value: Int) : Atom(value) {
        override fun toString() = "char($value)"
    }

    object Unknown : Atom(0) {
        override fun toString() = "Unknown"
    }

}