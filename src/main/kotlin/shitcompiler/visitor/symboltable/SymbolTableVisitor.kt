package shitcompiler.visitor.symboltable

import shitcompiler.ast.Program
import shitcompiler.symboltable.SymbolTable
import java.io.PrintWriter

/**
 * Created by NuclearCoder on 06/03/17.
 */

class SymbolTableVisitor(internal val errors: PrintWriter) {

    internal val table = SymbolTable(errors)

    internal val typeUniversal = table.typeUniversal
    internal val typeInt = table.typeInt
    internal val typeChar = table.typeChar
    internal val typeBool = table.typeBool
    internal val typeVoid = table.typeVoid

    fun visit(node: Program) {
        table.beginBlock()
        for (statement in node.statements) {
            visitStatement(statement)
        }
        table.endBlock()
    }

}