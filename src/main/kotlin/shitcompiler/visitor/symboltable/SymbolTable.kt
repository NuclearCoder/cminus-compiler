package shitcompiler.visitor.symboltable

import shitcompiler.NO_NAME
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

    private val blocks = arrayListOf(BlockRecord(errors))

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
        if (name != NO_NAME && blocks[currentLevel].find(name) != null) {
            errors.println("Defined $name more than once")
        }
        return blocks[currentLevel].define(name, kind, data)
    }

    fun beginBlock() {
        if (++currentLevel == blocks.size) {
            blocks.add(BlockRecord(errors))
        }
    }

    fun endBlock() {
        blocks[currentLevel--].clear()
    }

}