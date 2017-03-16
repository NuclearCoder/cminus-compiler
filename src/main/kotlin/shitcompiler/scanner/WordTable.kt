package shitcompiler.scanner

import shitcompiler.KEYWORDS
import shitcompiler.NAME_FALSE
import shitcompiler.STD_NAMES

/**
 * Created by NuclearCoder on 10/03/17.
 */

class WordTable {

    private val table = mutableMapOf<String, WordRecord>()

    private var nameIndex: Int

    init {
        for ((index, word) in STD_NAMES)
            define(true, word, index)
        nameIndex = NAME_FALSE

        for ((word, symbol) in KEYWORDS)
            define(false, word, symbol.ordinal)
    }

    fun define(isName: Boolean, text: String, index: Int) {
        table[text] = WordRecord(isName, index)
    }

    fun search(text: String): WordRecord {
        return table.computeIfAbsent(text, { WordRecord(true, ++nameIndex) })
    }

}