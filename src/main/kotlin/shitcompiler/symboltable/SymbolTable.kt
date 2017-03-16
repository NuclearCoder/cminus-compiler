package shitcompiler.symboltable

import shitcompiler.NO_NAME
import shitcompiler.UNNAMED
import shitcompiler.symboltable.classes.ArrayType
import shitcompiler.symboltable.classes.Constant
import shitcompiler.symboltable.classes.Nothing
import java.io.PrintWriter

/**
 * Created by NuclearCoder on 06/03/17.
 */

class SymbolTable(private val errors: PrintWriter) {

    val typeUniversal: ObjectRecord
    val typeInt: ObjectRecord
    val typeChar: ObjectRecord
    val typeBool: ObjectRecord
    val stdTrue: ObjectRecord
    val stdFalse: ObjectRecord

    private val blocks = arrayListOf(BlockRecord())

    // the 0-th level is the standard level
    private var currentLevel: Int = 0

    init {
        var i = 0

        typeUniversal = define(i++, Kind.STANDARD_TYPE)
        typeInt = define(i++, Kind.STANDARD_TYPE)
        typeChar = define(i++, Kind.STANDARD_TYPE)
        typeBool = define(i++, Kind.STANDARD_TYPE)

        stdTrue = define(i++, Kind.CONSTANT, Constant(1, typeBool))
        stdFalse = define(i++, Kind.CONSTANT, Constant(0, typeBool))
    }

    fun findOrDefineType(name: Int, length: Int): ObjectRecord {
        val elementType = find(name)
        if (elementType.kind != Kind.STANDARD_TYPE
                && elementType.kind != Kind.STRUCT_TYPE) {
            errors.println("Wrong kind for $name, got ${elementType.kind}, expected ${Kind.STANDARD_TYPE} or ${Kind.STRUCT_TYPE}")
            return typeUniversal
        }

        // if standard type, just return
        if (length < 0) return elementType

        // array types are always defined in the standard level

        // the standard level might keep references from types higher up after they go out of scope
        // to prevent this, #endBlock will scan the standard block to clean these up
        val obj = blocks[0].firstOrNull {
            it.name == UNNAMED && it.kind == Kind.ARRAY_TYPE
                    && it.asArrayType().elementType == elementType
                    && it.asArrayType().length == length
        }

        if (obj != null) return obj
        return define(UNNAMED, Kind.ARRAY_TYPE, ArrayType(elementType, length))
    }

    fun find(name: Int): ObjectRecord {
        for (level in currentLevel downTo 0) {
            val obj = blocks[level].find(name)
            if (obj != null) {
                return obj
            }
        }
        errors.println("Unknown reference $name")
        return define(name, Kind.UNDEFINED)
    }

    fun define(name: Int, kind: Kind, data: ObjectClass = Nothing): ObjectRecord {
        if (name != NO_NAME && name != UNNAMED && blocks[currentLevel].find(name) != null) {
            errors.println("Defined $name more than once")
        }
        return blocks[currentLevel].define(name, kind, data)
    }

    fun beginBlock() {
        if (++currentLevel == blocks.size) {
            blocks.add(BlockRecord())
        }
    }

    fun endBlock() {
        val block = blocks[currentLevel]

        // remove any dead reference from the standard level
        val deadTypes = block.filter { it.kind == Kind.STRUCT_TYPE }
        blocks[0].removeIf { it.kind == Kind.ARRAY_TYPE && it.asArrayType().elementType in deadTypes }

        block.clear()
        currentLevel--
    }

}