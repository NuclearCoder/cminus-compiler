package shitcompiler.symboltable

/**
 * Created by NuclearCoder on 10/03/17.
 */

interface ObjectClass

enum class Kind {
    CONSTANT,

    STANDARD_TYPE,
    ARRAY_TYPE,
    STRUCT_TYPE,
    POINTER_TYPE,

    FUNCTION,

    FIELD,
    VARIABLE,
    PARAMETER,

    UNDEFINED
}
