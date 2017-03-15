package shitcompiler.visitor.symboltable

import java.io.PrintWriter

/**
* Created by NuclearCoder on 06/03/17.
*/

internal class BlockRecord(private val errors: PrintWriter) {

    private val records = mutableListOf<ObjectRecord>()

    fun clear() = records.clear()

    fun find(name: Int): ObjectRecord? {
        return records.firstOrNull { it.name == name }
    }

    fun define(name: Int, kind: Kind, data: ObjectClass): ObjectRecord {
        val obj = ObjectRecord(name, kind, data, errors = this.errors)
        records.add(obj)
        return obj
    }

}