package shitcompiler.visitor

import shitcompiler.ast.Program
import shitcompiler.println
import shitcompiler.symboltable.SymbolTable
import java.io.PrintWriter

/**
 * Created by NuclearCoder on 06/03/17.
 */

class SymbolTableVisitor(private val names: Map<Int, String>, private val errors: PrintWriter) {

    internal val table = SymbolTable(this)

    internal val typeUniversal = table.typeUniversal
    internal val typeInt = table.typeInt
    internal val typeChar = table.typeChar
    internal val typeBool = table.typeBool
    internal val typeVoid = table.typeVoid

    /*
    private val ops = LinkedList<Int>()

    internal fun emit(i: Int) {
        ops.addLast(i)
    }

    internal fun emit(op: OpCode) {
        emit(op.ordinal)
    }

    internal fun emit(op: OpCode, vararg args: Int) {
        emit(op)
        for (arg in args) {
            emit(arg)
        }
    }*/

    fun visit(node: Program) {
        table.beginBlock()
        for (statement in node.statements) {
            visitStatement(statement)
        }
        table.endBlock()
    }

    fun error(lineNo: Int, msg: String) {
        errors.println(names, lineNo, msg)
    }

}