@file:JvmName("Main")
package shitcompiler

import shitcompiler.scanner.Scanner

/**
* Created by NuclearCoder on 14/01/2017.
*/

fun main(args: Array<String>) {

    do {
        print(">>> ")
        val input = (readLine() ?: "").trim()

        val scanner = Scanner(input)

        val tokens = scanner.execute()

        println(tokens.reversed()) // for printing

    } while (input.isNotEmpty())

}