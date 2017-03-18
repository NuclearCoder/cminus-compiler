package shitcompiler.visitor.symboltable

import shitcompiler.ast.AST
import shitcompiler.ast.Program
import shitcompiler.ast.function.FunctionCall
import shitcompiler.ast.function.FunctionDefinition
import shitcompiler.ast.function.ProcedureDefinition
import shitcompiler.ast.statement.Assignment
import shitcompiler.ast.statement.BlockStatement
import shitcompiler.ast.statement.Declaration
import shitcompiler.ast.statement.EmptyStatement
import shitcompiler.ast.type.StructDefinition
import shitcompiler.symboltable.SymbolTable
import shitcompiler.visitor.ASTVisitor
import java.io.PrintWriter

/**
 * Created by NuclearCoder on 06/03/17.
 */

class SymbolTableVisitor(internal val errors: PrintWriter) : ASTVisitor {

    internal val table = SymbolTable(errors)

    internal val typeUniversal = table.typeUniversal
    internal val typeInt = table.typeInt
    internal val typeChar = table.typeChar
    internal val typeBool = table.typeBool

    override fun visit(node: AST) {
        when (node) {
            is Program -> visitProgram(node)
            is BlockStatement -> visitBlock(node)
            is Declaration -> visitDeclaration(node)
            is Assignment -> visitAssignment(node)
            is StructDefinition -> visitStructDefinition(node)
            is FunctionDefinition -> visitFunctionDefinition(node)
            is ProcedureDefinition -> visitFunctionDefinition(node)
            is FunctionCall -> visitFunctionCall(node)

            is EmptyStatement -> Unit
            else -> {
                errors.println("Node type not handled: ${node::class.simpleName}")
            }
        }
    }

    private fun visitProgram(node: Program) {
        table.beginBlock()
        for (statement in node.statements) {
            visit(statement)
        }
        table.endBlock()
    }

}