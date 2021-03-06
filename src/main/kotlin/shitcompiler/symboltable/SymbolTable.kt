package shitcompiler.symboltable

import shitcompiler.UNDEFINED
import shitcompiler.UNNAMED
import shitcompiler.ast.type.ArrayTypeReference
import shitcompiler.ast.type.PointerTypeReference
import shitcompiler.ast.type.TypeReference
import shitcompiler.symboltable.classes.ArrayType
import shitcompiler.symboltable.classes.Constant
import shitcompiler.symboltable.classes.Nothing
import shitcompiler.symboltable.classes.PointerType
import shitcompiler.visitor.SymbolTableVisitor

/**
 * Created by NuclearCoder on 06/03/17.
 */

class SymbolTable(private val visitor: SymbolTableVisitor) {

    val typeUniversal: ObjectRecord
    val typeInt: ObjectRecord
    val typeChar: ObjectRecord
    val typeBool: ObjectRecord
    val typeVoid: ObjectRecord
    val stdTrue: ObjectRecord
    val stdFalse: ObjectRecord

    val typeVoidPtr: ObjectRecord

    private val blocks = arrayListOf(BlockRecord(0))

    // the 0-th level is the standard level
    internal var currentLevel: Int = 0
        private set

    init {
        var i = 0

        typeUniversal = define(0, i++, Kind.STANDARD_TYPE)
        typeInt = define(0, i++, Kind.STANDARD_TYPE)
        typeChar = define(0, i++, Kind.STANDARD_TYPE)
        typeBool = define(0, i++, Kind.STANDARD_TYPE)
        typeVoid = define(0, i++, Kind.STANDARD_TYPE)

        stdTrue = define(0, i++, Kind.CONSTANT, Constant(1, typeBool))
        stdFalse = define(0, i, Kind.CONSTANT, Constant(0, typeBool))

        typeVoidPtr = define(0, UNNAMED, Kind.POINTER_TYPE, PointerType(typeVoid))
    }

    fun findOrDefineType(type: TypeReference): ObjectRecord {
        val lineNo = type.lineNo

        val elementType = if (type is ArrayTypeReference) {
            findOrDefineType(type.elementType)
        } else if (type is PointerTypeReference) {
            findOrDefineType(type.elementType)
        } else {
            find(lineNo, type.name)
        }

        if (elementType.kind != Kind.STANDARD_TYPE
                && elementType.kind != Kind.ARRAY_TYPE
                && elementType.kind != Kind.STRUCT_TYPE
                && elementType.kind != Kind.POINTER_TYPE) {
            visitor.error(lineNo, "Type reference was ${elementType.kind}, expected ${Kind.STANDARD_TYPE}, ${Kind.ARRAY_TYPE} or ${Kind.STRUCT_TYPE}")
            return typeUniversal
        }

        if (type is ArrayTypeReference) {
            if (elementType == typeVoid) {
                // void arrays aren't allowed
                visitor.error(lineNo, "Void array type references aren't allowed")
                return typeVoid
            }

            val elementTypeBlock = blocks[elementType.blockLevel]
            val length = type.length

            // array types are defined in the same level as the element type
            val obj = elementTypeBlock.records.firstOrNull {
                it.name == UNNAMED && it.kind == Kind.ARRAY_TYPE
                        && it.asArrayType().elementType == elementType
                        && it.asArrayType().length == length
            }

            if (obj != null) return obj
            return elementTypeBlock.define(UNNAMED, Kind.ARRAY_TYPE, ArrayType(elementType, length))
        } else if (type is PointerTypeReference) {
            val elementTypeBlock = blocks[elementType.blockLevel]

            // pointer types are defined in the same level as the pointee type
            val obj = elementTypeBlock.records.firstOrNull {
                it.name == UNNAMED && it.kind == Kind.POINTER_TYPE
                        && it.asPointerType().elementType == elementType
            }

            if (obj != null) return obj
            return elementTypeBlock.define(UNNAMED, Kind.POINTER_TYPE, PointerType(elementType))
        } else {
            // if standard type, just return
            return elementType
        }
    }

    fun find(lineNo: Int, name: Int): ObjectRecord {
        (currentLevel downTo 0).forEach {
            val obj = blocks[it].find(name)
            if (obj != null) {
                return obj
            }
        }
        visitor.error(lineNo, "Unknown reference '<($name)>'")
        return define(lineNo, name, Kind.UNDEFINED)
    }

    fun define(lineNo: Int, name: Int, kind: Kind, data: ObjectClass = Nothing): ObjectRecord {
        if (name != UNDEFINED && name != UNNAMED && blocks[currentLevel].find(name) != null) {
            visitor.error(lineNo, "Defined '<($name)>' more than once")
        }
        return blocks[currentLevel].define(name, kind, data)
    }

    fun beginBlock() {
        currentLevel++
        if (currentLevel == blocks.size) {
            blocks.add(BlockRecord(currentLevel))
        }
    }

    fun endBlock() {
        val block = blocks[currentLevel]
        block.records.clear()
        currentLevel--
    }

    /* addressing */
    /*
    fun variableAddressing(variables: List<ObjectRecord>) {
        val lengths = variables.map(this::typeLength)
        var displ = 3 + lengths.sum()
        // variables are stored after the context part which is 3 words long
        for (i in variables.indices) {
            displ -= lengths[i]
            val obj = variables[i].asVariable()
            obj.level = currentLevel
            obj.displ = displ
        }
    }

    fun parameterAddressing(parameters: List<ObjectRecord>) {
        val block = blocks[currentLevel].records

        var varDispl = 3
    }

    fun typeLength(type: ObjectRecord): Int {
        return when (type.kind) {
            Kind.STANDARD_TYPE -> 1
            Kind.ARRAY_TYPE -> type.asArrayType().let { it.length * typeLength(it.elementType) }
            else -> type.asStructType().length
        }
    }
*/


}