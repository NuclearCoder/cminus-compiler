package shitcompiler.symboltable

/**
 * Created by NuclearCoder on 06/03/17.
 */

internal class BlockRecord {

    private val records = mutableListOf<ObjectRecord>()

    fun clear() = records.clear()
    fun firstOrNull(predicate: (ObjectRecord) -> Boolean)
            = records.firstOrNull(predicate)

    fun filter(predicate: (ObjectRecord) -> Boolean)
            = records.filter(predicate)

    fun removeIf(predicate: (ObjectRecord) -> Boolean)
            = records.removeIf(predicate)

    fun find(name: Int): ObjectRecord? {
        return records.firstOrNull { it.name == name }
    }

    fun define(name: Int, kind: Kind, data: ObjectClass): ObjectRecord {
        val obj = ObjectRecord(name, kind, data)
        records.add(obj)
        return obj
    }

}