package shitcompiler.visitor.symboltable

import shitcompiler.ast.expression.Atom
import shitcompiler.symboltable.ObjectRecord

fun SymbolTableVisitor.visitInteger(node: Atom.Integer): ObjectRecord {
    return typeInt
}

fun SymbolTableVisitor.visitCharacter(node: Atom.Char): ObjectRecord {
    return typeChar
}