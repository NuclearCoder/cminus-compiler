package shitcompiler.symboltable

/**
 * Created by NuclearCoder on 06/03/17.
 */

internal class BlockRecord(val level: Int) {

    internal val records = mutableListOf<ObjectRecord>()

    fun find(name: Int): ObjectRecord? {
        return records.firstOrNull { it.name == name }
    }

    fun define(name: Int, kind: Kind, data: ObjectClass): ObjectRecord {
        val obj = ObjectRecord(name, kind, data, level)
        records.add(obj)
        return obj
    }

    override fun toString() = "BlockRecord $records"

}