package shitcompiler

import shitcompiler.parser.Parser
import shitcompiler.scanner.Scanner
import shitcompiler.token.toPrettyString
import shitcompiler.visitor.symboltable.SymbolTableVisitor
import java.io.PrintWriter
import java.io.StringWriter

/**
* Created by NuclearCoder on 14/01/2017.
*/

fun main(args: Array<String>) {
    while (true) {
        print(">>> ")
        val input = readMultiLine()
        if (input.isEmpty())
            return

        val errors = StringWriter()
        val ps = PrintWriter(errors)

        val tokens = run { Scanner(input, ps).execute() }
        println("Tokens:\n  ${tokens.toPrettyString()}")

        val root = run { Parser(tokens, ps).execute() }
        println("\nAST:\n  $root\n")

        val symTableVisitor = SymbolTableVisitor(ps)
        symTableVisitor.visit(root)

        println(errors.toString())
    }
}

fun readMultiLine(): String {
    val sb = StringBuilder()
    do {
        val line = readLine()
        if (line != null && line.isNotEmpty()) {
            sb.append(line)
            sb.append('\n')
        }
    } while (line != null && line.isNotEmpty())
    return sb.toString()
}