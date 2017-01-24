package shitcompiler

/**
* Created by NuclearCoder on 14/01/2017.
*/

open class CompilerException(msg: String) : Exception(msg)

class LexerException(msg: String): CompilerException(msg)
