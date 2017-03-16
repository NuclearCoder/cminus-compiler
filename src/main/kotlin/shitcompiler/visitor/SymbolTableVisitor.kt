package shitcompiler.visitor

import shitcompiler.ast.AST
import shitcompiler.ast.expression.Atom
import shitcompiler.ast.expression.BinaryOp
import shitcompiler.ast.expression.Expression
import shitcompiler.ast.expression.UnaryOp
import shitcompiler.ast.statement.Assignment
import shitcompiler.ast.statement.BlockStatement
import shitcompiler.ast.statement.Declaration
import shitcompiler.ast.type.StructDefinition
import shitcompiler.symboltable.Kind
import shitcompiler.symboltable.ObjectRecord
import shitcompiler.symboltable.SymbolTable
import shitcompiler.symboltable.classes.Field
import shitcompiler.symboltable.classes.RecordType
import shitcompiler.symboltable.classes.VarParam
import shitcompiler.token.Symbol.*
import java.io.PrintWriter

/**
 * Created by NuclearCoder on 06/03/17.
 */

class SymbolTableVisitor(private val errors: PrintWriter) : ASTVisitor {

    val table = SymbolTable(errors)

    private val typeUniversal = table.typeUniversal
    private val typeInt = table.typeInt
    private val typeChar = table.typeChar
    private val typeBool = table.typeBool

    override fun visit(node: AST) {
        when (node) {
            is BlockStatement -> visitBlock(node)
            is Declaration -> visitDeclaration(node)
            is Assignment -> visitAssignment(node)
            is StructDefinition -> visitStructDefinition(node)
            else -> {
                errors.println("Node type not handled: ${node::class.simpleName}")
            }
        }
    }

    private fun visitBlock(node: BlockStatement) {
        table.beginBlock()
        for (statement in node.statements) {
            visit(statement)
        }
        table.endBlock()
    }

    private fun visitDeclaration(node: Declaration) {
        val name = node.name
        val type = node.type

        val typeObj = table.findOrDefineType(type.name, type.length)

        table.define(name, Kind.VARIABLE, VarParam(typeObj))
    }

    private fun visitAssignment(node: Assignment) {
        val name = node.name
        val type = visitExpression(node.value)

        val obj = table.find(name)
        when (obj.kind) {
            Kind.VARIABLE -> {
                val varType = obj.asVariable().type
                if (varType != type) {
                    errors.println("Trying to assign $type to $varType")
                }
            }
            else -> errors.println("Trying to assign a value to kind ${obj.kind}")
        }
    }

    private fun visitStructDefinition(node: StructDefinition) {
        val fields = mutableListOf<ObjectRecord>()

        for (declaration in node.fields) {
            val name = declaration.name
            val type = declaration.type

            val typeObj = table.findOrDefineType(type.name, type.length)

            fields.add(ObjectRecord(name, Kind.VARIABLE, Field(typeObj)))
        }

        table.define(node.name, Kind.STRUCT_TYPE, RecordType(fields))
    }

    private fun visitExpression(node: Expression): ObjectRecord {
        return when (node) {
            is BinaryOp -> visitBinaryOp(node)
            is UnaryOp -> visitUnaryOp(node)
            is Atom.Identifier -> visitIdentifier(node)
            is Atom.Integer -> visitInteger(node)
            is Atom.Char -> visitCharacter(node)
            else -> {
                errors.println("Unexpected expression ${node::class.simpleName}")
                typeUniversal
            }
        }
    }

    private fun visitBinaryOp(node: BinaryOp): ObjectRecord {
        val typeLeft = visitExpression(node.left)
        val typeRight = visitExpression(node.right)

        return when (node.sym) {
            PLUS, MINUS, ASTERISK, DIV, MOD -> {
                if (typeLeft == typeInt && typeLeft == typeRight) {
                    typeInt
                } else {
                    errors.println("Expected integer expressions")
                    typeUniversal
                }
            }
            AND, OR -> {
                if (typeLeft == typeBool && typeLeft == typeRight) {
                    typeBool
                } else {
                    errors.println("Expected boolean expressions")
                    typeUniversal
                }
            }
            EQUAL, NOT_EQUAL -> {
                if (typeLeft == typeRight) {
                    typeBool
                } else {
                    errors.println("Expected same-type expressions")
                    typeUniversal
                }
            }
            GREATER, NOT_GREATER, LESSER, NOT_LESSER -> {
                if (typeLeft == typeInt && typeLeft == typeRight) {
                    typeBool
                } else {
                    errors.println("Expected integer expressions")
                    typeUniversal
                }
            }
            else -> {
                errors.println("Unexpected operator ${node.sym}")
                typeUniversal
            }
        }
    }

    private fun visitUnaryOp(node: UnaryOp): ObjectRecord {
        val type = visitExpression(node.operand)
        return when (node.sym) {
            PLUS, MINUS -> {
                if (type == table.typeInt) {
                    table.typeInt
                } else {
                    errors.println("Expected integer expression")
                    table.typeUniversal
                }
            }
            NOT -> {
                if (type == table.typeBool) {
                    table.typeBool
                } else {
                    errors.println("Expected boolean expression")
                    table.typeUniversal
                }
            }
            else -> {
                errors.println("Unexpected operator ${node.sym}")
                table.typeUniversal
            }
        }
    }

    private fun visitIdentifier(node: Atom.Identifier): ObjectRecord {
        val obj = table.find(node.value)
        return when (obj.kind) {
            Kind.CONSTANT -> obj.asConstant().type
            Kind.VARIABLE -> obj.asVariable().type
            Kind.PARAMETER -> obj.asParameter().type
            else -> {
                errors.println("Identifier must refer to a constant, variable or parameter")
                typeUniversal
            }
        }
    }

    private fun visitInteger(node: Atom.Integer): ObjectRecord {
        return table.typeInt
    }

    private fun visitCharacter(node: Atom.Char): ObjectRecord {
        return table.typeChar
    }

}