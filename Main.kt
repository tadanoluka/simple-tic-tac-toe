package tictactoe

fun main() {
    displayGridFromString(readln())
}

fun displayGridFromString(inputString: String) {
    if (isStringCanBeGrid(inputString)) {
        println("""
            ---------
            | ${inputString[0]} ${inputString[1]} ${inputString[2]} |
            | ${inputString[3]} ${inputString[4]} ${inputString[5]} |
            | ${inputString[6]} ${inputString[7]} ${inputString[8]} |
            ---------
        """.trimIndent())
    }
}

fun isStringCanBeGrid(inputString: String): Boolean {
    if (inputString.count() == 9) {
        for (char in inputString) {
            if (char != 'O' && char != 'X' && char != '_') {
                println("The input string must consist only of the characters \'O\', \'X\' and \'_\'.")
                return false
            }
        }
    } else {
        println("The length of the input string must be equal to 9 characters.")
        return false
    }
    return true
}