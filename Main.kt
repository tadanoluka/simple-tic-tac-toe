package tictactoe

import kotlin.math.abs

enum class TicTacToeSymbols(val possibleSymbols: List<Char>) {
    X_MARK(listOf('X', 'x', '+')),
    O_MARK(listOf('O', 'o', '0')),
    EMPTY_CELL(listOf(' ', '_', '-')),
    NULL(listOf());

    fun getDefChar() = possibleSymbols[0]
}

enum class GameStates(private val description: String, private val debugString: String) {
    IS_RUN(
        "Neither side has three in a row but the grid still has empty cells.",
        "Game not finished"),
    DRAW(
        "No side has a three in a row and the grid has no empty cells.",
        "Draw"),
    X_WINS("The grid has three X’s in a row (including diagonals).","X wins"),
    O_WINS("The grid has three O’s in a row (including diagonals).","O wins"),
    IMPOSSIBLE("the grid has three X’s in a row as well as three O’s in a row, " +
            "or there are a lot more X's than O's or vice versa.",
        "Impossible"),
    NULL("","");

    fun getOutputString() = debugString
}

class GameGrid(private val grid: MutableList<MutableList<TicTacToeSymbols>>){
    val currentGameState: GameStates = calculateCurrentGameState()

    private var winnerIsX = false
    private var winnerIsO = false

    fun displayGrid() {
        println("""
        ---------
        | ${grid[0][0].getDefChar()} ${grid[0][1].getDefChar()} ${grid[0][2].getDefChar()} |
        | ${grid[1][0].getDefChar()} ${grid[1][1].getDefChar()} ${grid[1][2].getDefChar()} |
        | ${grid[2][0].getDefChar()} ${grid[2][1].getDefChar()} ${grid[2][2].getDefChar()} |
        ---------
    """.trimIndent())
    }

    private fun calculateCurrentGameState(): GameStates {
        winnerIsX = isCharIsAWinner(TicTacToeSymbols.X_MARK)
        winnerIsO = isCharIsAWinner(TicTacToeSymbols.O_MARK)

        return if ((winnerIsO && winnerIsX) || !isDifferenceLegit()) {
            GameStates.IMPOSSIBLE
        } else {
            if (winnerIsX) GameStates.X_WINS
            else if (winnerIsO) GameStates.O_WINS
            else if (!isThereEmptyCell()) GameStates.DRAW
            else GameStates.IS_RUN
        }
    }

    private fun isCharIsAWinner(char: TicTacToeSymbols): Boolean {
        // Check 3 in Horizontals
        for (i in 0..2) {
            if (grid[i][0] == grid[i][1] && grid[i][1] == grid[i][2] && grid[i][1] == char) {
                return true
            }
        }
        // Check 3 in Verticals
        for (i in 0..2) {
            if (grid[0][i] == grid[1][i] && grid[1][i] == grid[2][i] && grid[1][i] == char) {
                return true
            }
        }
        // Check 3 in Diagonals
        if (grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2] && grid[1][1] == char
            || (grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0] && grid[1][1] == char)) {
            return true
        }
        return false
    }

    private fun isDifferenceLegit(): Boolean {
        var xCounter = 0
        var oCounter = 0
        for (row in grid) {
            for (cell in row) {
                if (cell == TicTacToeSymbols.X_MARK) ++xCounter
                else if (cell == TicTacToeSymbols.O_MARK) ++oCounter
            }
        }
        return abs(xCounter - oCounter) <= 1
    }

    private fun isThereEmptyCell(): Boolean {
        for (row in grid) {
            for (cell in row){
                if (cell == TicTacToeSymbols.EMPTY_CELL) {
                    return true
                }
            }
        }
        return false
    }
}

fun main() {
    val inputString = readln()
    if (isInputStringValid(inputString)) {
        val listOfTicTacToeSymbols = parseInputString(inputString)
        val grid = makeGrid(listOfTicTacToeSymbols)
        val gameGrid = GameGrid(grid)
        gameGrid.displayGrid()
        println(gameGrid.currentGameState.getOutputString())
    }

}

fun isInputStringValid(inputString: String): Boolean {
    if (inputString.count() == 9) {
        val possibleCharsList = mutableListOf<Char>()
        for (enum in TicTacToeSymbols.values()) {
            for (char in enum.possibleSymbols) {
                possibleCharsList.add(char)
            }
        }
        for (char in inputString) {
            if (char !in possibleCharsList) {
                println("The input string contains an unknown character - \'$char\'")
                val possibleSymbolsStringList = mutableListOf<String>()
                for (item in possibleCharsList) {
                    possibleSymbolsStringList.add("\'$item\'")
                }
                println("The input string must consist only of the characters: ${possibleSymbolsStringList.joinToString()}")
                return false
            }
        }
    } else {
        println("The length of the input string must be equal to 9 characters.")
        return false
    }
    return true
}

fun parseInputString(inputString: String): MutableList<TicTacToeSymbols> {
    val mListOfTTTSymbols: MutableList<TicTacToeSymbols> = mutableListOf()
    for (char in inputString) {
        when (char) {
            in TicTacToeSymbols.X_MARK.possibleSymbols -> mListOfTTTSymbols.add(TicTacToeSymbols.X_MARK)
            in TicTacToeSymbols.O_MARK.possibleSymbols -> mListOfTTTSymbols.add(TicTacToeSymbols.O_MARK)
            in TicTacToeSymbols.EMPTY_CELL.possibleSymbols -> mListOfTTTSymbols.add(TicTacToeSymbols.EMPTY_CELL)
            else -> mListOfTTTSymbols.add(TicTacToeSymbols.NULL)
        }
    }
    return mListOfTTTSymbols
}

fun makeGrid(inputList: MutableList<TicTacToeSymbols>): MutableList<MutableList<TicTacToeSymbols>> {
    val grid: MutableList<MutableList<TicTacToeSymbols>> = mutableListOf()
    for (i in 0..2) {
        val row = mutableListOf<TicTacToeSymbols>()
        for (j in 0..2) {
            row.add(inputList[j + i * 3])
        }
        grid.add(row)
    }
    return grid
}