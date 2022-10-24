package tictactoe

import kotlin.math.abs

enum class TicTacToeSymbols(private val possibleSymbols: List<Char>) {
    X_MARK(listOf('X', 'x', '+')),
    O_MARK(listOf('O', 'o', '0')),
    EMPTY_CELL(listOf(' ', '_', '-'));

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
        "Impossible");

    fun getOutputString() = debugString
}

class GameGrid(private val grid: MutableList<MutableList<TicTacToeSymbols>>){
    var currentGameState: GameStates = calculateCurrentGameState()

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

    fun calculateCurrentGameState(): GameStates {
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

    fun isCellEmpty(row: Int, column: Int): Boolean {
        return grid[row][column] == TicTacToeSymbols.EMPTY_CELL
    }
    fun addPlayerMove(row: Int, column: Int, ticTacToeSymbol: TicTacToeSymbols) {
        grid[row][column] = ticTacToeSymbol
    }
}

fun main() {
    val grid = makeEmptyGrid()
    val gameGrid = GameGrid(grid)
    gameGrid.displayGrid()
    var turnSide: TicTacToeSymbols = TicTacToeSymbols.X_MARK
    while (true) {
        if (gameGrid.calculateCurrentGameState() == GameStates.IS_RUN) {
            userMakeMove(gameGrid, turnSide)
            gameGrid.displayGrid()
            turnSide = if (turnSide == TicTacToeSymbols.X_MARK) {
                TicTacToeSymbols.O_MARK
            } else {
                TicTacToeSymbols.X_MARK
            }
        } else {
            println(gameGrid.calculateCurrentGameState().getOutputString())
            break
        }
    }

}

fun userMakeMove(gameGrid: GameGrid, userSide: TicTacToeSymbols) {
    var userMadeAMove = false
    while (!userMadeAMove) {
        val userInputForMove = readln()
        if (isUserInputForMoveValid(userInputForMove)){
            val moveCoords = getCoordinatesFromString(userInputForMove)
            val row = moveCoords[0]
            val column = moveCoords[1]
            if (gameGrid.isCellEmpty(row, column)) {
                gameGrid.addPlayerMove(row, column, userSide)
                userMadeAMove = true
            } else println("This cell is occupied! Choose another one!")
        }
    }
}

fun makeEmptyGrid(): MutableList<MutableList<TicTacToeSymbols>> {
    val grid: MutableList<MutableList<TicTacToeSymbols>> = mutableListOf()
    for (i in 0..2) {
        val row = mutableListOf<TicTacToeSymbols>()
        for (j in 0..2) {
            row.add(TicTacToeSymbols.EMPTY_CELL)
        }
        grid.add(row)
    }
    return grid
}

fun isUserInputForMoveValid(inputStringForMove: String): Boolean {
    val trimmedString = inputStringForMove.trimIndent()
    return if (trimmedString.length == 3) {
        val row = trimmedString.toCharArray()[0]
        val column = trimmedString.toCharArray()[2]
        if (row.isDigit() && column.isDigit()) {
            if (row.digitToInt() in 1..3 && column.digitToInt() in 1..3) {
                true
            } else {
                println("Coordinates should be from 1 to 3!")
                false
            }
        } else {
            println("You should enter numbers!")
            false
        }
    } else false
}

fun getCoordinatesFromString(inputString: String): List<Int> {
    val trimmedString = inputString.trimIndent()
    val row = trimmedString.toCharArray()[0].digitToInt() - 1
    val column = trimmedString.toCharArray()[2].digitToInt() - 1
    return listOf(row, column)
}