package shitcompiler

import shitcompiler.parser.Parser
import shitcompiler.scanner.Scanner
import shitcompiler.visitor.SymbolTableVisitor
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

        val names = mutableMapOf<Int, String>()

        val tokens = Scanner(input, names, ps).execute()
        println("  Tokens:\n${format(names, tokens.toPrettyString())}")

        val root = Parser(tokens, ps).execute()
        println("\n  AST:\n${format(names, root.toString())}\n")

        val symTableVisitor = SymbolTableVisitor(names, ps)
        symTableVisitor.visit(root)

        println(errors.toString())
    }
}