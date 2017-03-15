package shitcompiler.visitor

import shitcompiler.ast.AST
import kotlin.reflect.KClass
import kotlin.reflect.KFunction2

/**
* Created by NuclearCoder on 04/03/17.
*/

@FunctionalInterface interface ASTVisitor {

    fun visit(node: AST)

}