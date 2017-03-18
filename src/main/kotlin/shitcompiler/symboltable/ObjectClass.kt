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

    FUNCTION,
    PROCEDURE,

    FIELD,
    VARIABLE,
    PARAMETER,

    UNDEFINED
}
