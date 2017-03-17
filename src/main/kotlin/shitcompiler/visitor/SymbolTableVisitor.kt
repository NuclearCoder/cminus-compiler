package shitcompiler.visitor

import shitcompiler.ast.AST
import shitcompiler.ast.expression.Atom
import shitcompiler.ast.expression.BinaryOp
import shitcompiler.ast.expression.Expression
import shitcompiler.ast.expression.UnaryOp
import shitcompiler.ast.statement.Assignment
import shitcompiler.ast.statement.BlockStatement
import shitcompiler.ast.statement.Declaration
import shitcompiler.ast.type.ArrayAccess
import shitcompiler.ast.type.FieldAccess
import shitcompiler.ast.type.StructDefinition
import shitcompiler.ast.type.VariableAccess
import shitcompiler.symboltable.Kind
import shitcompiler.symboltable.ObjectRecord
import shitcompiler.symboltable.SymbolTable
import shitcompiler.symboltable.classes.Field
import shitcompiler.symboltable.classes.StructType
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
        val names = node.names
        val type = node.type

        val typeObj = table.findOrDefineType(type.name, type.length)

        names.forEach { table.define(it, Kind.VARIABLE, VarParam(typeObj)) }
    }

    private fun visitAssignment(node: Assignment) {
        val accessType = visitVariableAccess(node.access)
        val exprType = visitExpression(node.value)

        if (accessType != exprType) {
            errors.println("Trying to assign $exprType to $accessType")
        }
    }

    private fun visitStructDefinition(node: StructDefinition) {
        val fields = mutableListOf<ObjectRecord>()

        for (declaration in node.fields) {
            val names = declaration.names
            val type = declaration.type

            val typeObj = table.findOrDefineType(type.name, type.length)

            names.forEach { fields.add(ObjectRecord(it, Kind.VARIABLE, Field(typeObj))) }
        }

        table.define(node.name, Kind.STRUCT_TYPE, StructType(fields))
    }

    private fun visitExpression(node: Expression): ObjectRecord {
        return when (node) {
            is BinaryOp -> visitBinaryOp(node)
            is UnaryOp -> visitUnaryOp(node)
            is VariableAccess -> visitVariableAccess(node)
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
                if (type == typeInt) {
                    typeInt
                } else {
                    errors.println("Expected integer expression")
                    typeUniversal
                }
            }
            NOT -> {
                if (type == typeBool) {
                    typeBool
                } else {
                    errors.println("Expected boolean expression")
                    typeUniversal
                }
            }
            else -> {
                errors.println("Unexpected operator ${node.sym}")
                typeUniversal
            }
        }
    }

    private fun visitInteger(node: Atom.Integer): ObjectRecord {
        return typeInt
    }

    private fun visitCharacter(node: Atom.Char): ObjectRecord {
        return typeChar
    }

    private fun visitVariableAccess(node: VariableAccess): ObjectRecord {
        return when (node) {
            is ArrayAccess -> visitArrayAccess(node)
            is FieldAccess -> visitFieldAccess(node)
            else -> {
                // simple identifier access
                val obj = table.find(node.id)
                when (obj.kind) {
                    Kind.CONSTANT -> obj.asConstant().type
                    Kind.VARIABLE -> obj.asVariable().type
                    Kind.PARAMETER -> obj.asParameter().type
                    else -> {
                        errors.println("Identifier must refer to a constant, variable or parameter")
                        typeUniversal
                    }
                }
            }
        }
    }

    private fun visitArrayAccess(node: ArrayAccess): ObjectRecord {
        // check it's an array
        val type = visitVariableAccess(node.access)
        return if (type.kind == Kind.ARRAY_TYPE) {
            val selectorType = visitExpression(node.selector)
            if (selectorType == typeInt) {
                type.asArrayType().elementType
            } else {
                errors.println("Indexed selector must be an integer expression")
                typeUniversal
            }
        } else {
            errors.println("Indexed selector must act on an array")
            typeUniversal
        }
    }

    private fun visitFieldAccess(node: FieldAccess): ObjectRecord {
        // check it's a struct
        val type = visitVariableAccess(node.access)
        return if (type.kind == Kind.STRUCT_TYPE) {
            // find the field
            val field = node.field
            val obj = type.asStructType().fields.firstOrNull { it.name == field }
            if (obj != null) {
                obj.asField().type
            } else {
                errors.println("Undeclared field $field")
                typeUniversal
            }
        } else {
            errors.println("Field selector must act on a struct")
            typeUniversal
        }
    }

}