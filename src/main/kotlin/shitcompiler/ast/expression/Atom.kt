package shitcompiler.ast.expression


/**
* Created by NuclearCoder on 02/03/17.
*/

sealed class Atom<out T>(open val value: T) : Expression {

    data class Integer(override val value: Int) : Atom<Int>(value)
    data class Char(override val value: Int) : Atom<Int>(value)
    data class Identifier(override val value: Int) : Atom<Int>(value)

    object Unknown: Atom<Unit>(Unit) {
        override fun toString() = "Unknown"
    }

}