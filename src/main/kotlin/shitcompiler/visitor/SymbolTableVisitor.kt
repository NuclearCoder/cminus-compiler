package shitcompiler.visitor

import shitcompiler.ast.AST
import shitcompiler.ast.Program
import shitcompiler.ast.expression.*
import shitcompiler.ast.function.FunctionCall
import shitcompiler.ast.function.FunctionDefinition
import shitcompiler.ast.function.FunctionOrProcedureDefinition
import shitcompiler.ast.function.ProcedureDefinition
import shitcompiler.ast.statement.Assignment
import shitcompiler.ast.statement.BlockStatement
import shitcompiler.ast.statement.Declaration
import shitcompiler.ast.statement.EmptyStatement
import shitcompiler.ast.type.ArrayAccess
import shitcompiler.ast.type.FieldAccess
import shitcompiler.ast.type.StructDefinition
import shitcompiler.symboltable.Kind
import shitcompiler.symboltable.ObjectRecord
import shitcompiler.symboltable.SymbolTable
import shitcompiler.symboltable.classes.*
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

    private fun visitBlock(node: BlockStatement, before: () -> Unit = {}) {
        table.beginBlock()
        before()

        for (statement in node.statements) {
            visit(statement)
        }

        table.endBlock()
    }

    private fun visitDeclaration(node: Declaration) {
        val names = node.names
        val type = node.type

        val typeObj = table.findOrDefineType(type)

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

            val typeObj = table.findOrDefineType(type)

            names.forEach { fields.add(ObjectRecord(it, Kind.FIELD, Field(typeObj), 0)) }
        }

        table.define(node.name, Kind.STRUCT_TYPE, StructType(fields))
    }

    private fun visitFunctionDefinition(node: FunctionOrProcedureDefinition) {
        val paramObjs = mutableListOf<ObjectRecord>()

        for (parameter in node.parameters) {
            val name = parameter.name
            val type = parameter.type

            val typeObj = table.findOrDefineType(type)

            paramObjs.add(ObjectRecord(name, Kind.PARAMETER, VarParam(typeObj), 0))
        }

        if (node is FunctionDefinition) {
            val returnType = table.findOrDefineType(node.returnType)
            table.define(node.name, Kind.FUNCTION, FunctionR(returnType, paramObjs))
        } else { // node is ProcedureDefinition
            table.define(node.name, Kind.PROCEDURE, Procedure(paramObjs))
        }

        visitBlock(node.block) { paramObjs.forEach({ table.define(it.name, Kind.VARIABLE, it.data) }) }
    }

    private fun visitExpression(node: Expression): ObjectRecord {
        return when (node) {
            is BinaryOp -> visitBinaryOp(node)
            is UnaryOp -> visitUnaryOp(node)
            is VariableAccess -> visitVariableAccess(node)
            is FunctionCall -> visitFunctionCall(node)
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

    private fun visitVariableAccess(node: VariableAccess): ObjectRecord {
        return when (node) {
            is ArrayAccess -> visitArrayAccess(node)
            is FieldAccess -> visitFieldAccess(node)
            else -> {
                // simple identifier access
                val obj = table.find(node.name)
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

    private fun visitFunctionCall(node: FunctionCall): ObjectRecord {
        val name = node.name
        val obj = table.find(name)
        return if (obj.kind == Kind.FUNCTION) {
            val function = obj.asFunction()
            // check one-to-one equality between call parameters and function parameters
            val formal = function.parameters
            val params = node.parameters

            if (formal.size == params.size) {
                for (i in 0..formal.lastIndex) {
                    val formalType = formal[i].asParameter().type
                    val paramType = visitExpression(params[i])

                    if (formalType != paramType) {
                        errors.println("Parameter ${i + 1}-th has type $paramType, expected $formalType")
                        return typeUniversal
                    }
                }
                function.returnType
            } else {
                errors.println("Function parameter count does not match given parameter count")
                typeUniversal
            }
        } else if (obj.kind == Kind.PROCEDURE) {
            typeUniversal
        } else {
            errors.println("Function call on $name which is not a function")
            typeUniversal
        }
    }

    private fun visitInteger(node: Atom.Integer): ObjectRecord {
        return typeInt
    }

    private fun visitCharacter(node: Atom.Char): ObjectRecord {
        return typeChar
    }

}