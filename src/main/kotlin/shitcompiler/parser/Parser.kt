package shitcompiler.parser

import shitcompiler.ast.AST
import shitcompiler.token.Symbol
import java.util.*
import kotlin.properties.Delegates.notNull

/**
* Created by NuclearCoder on 14/01/2017.
*/

// TODO: error recovery

class Parser(private val symbols: Queue<Int>) {

    private var root by notNull<AST>()

    private var symbol by notNull<Symbol>()

    private var lineNo = 0




}