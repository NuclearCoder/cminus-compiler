package shitcompiler.visitor

import shitcompiler.ast.AST

/**
 * Created by NuclearCoder on 04/03/17.
 */

@FunctionalInterface interface ASTVisitor {

    fun visit(node: AST)

}