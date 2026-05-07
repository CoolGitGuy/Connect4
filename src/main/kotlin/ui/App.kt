package ui

import androidx.compose.runtime.*
import game.*
import kotlinx.coroutines.delay
import persistence.clearSavedGame
import persistence.loadState
import persistence.saveState

const val ROWS_MIN = 4
const val ROWS_MAX = 20

const val COLUMNS_MIN = 4
const val COLUMNS_MAX = 20

const val TARGET_MIN = 4
const val TARGET_MAX = 10

data class GameState(
    val board: List<MutableList<Cell>>?,
    val player: Cell,
    val winner: Cell,
    val draw: Boolean,
    val rows: String,
    val columns: String,
    val connectTarget: String,
    val isMenu: Boolean,
    val error: String
)

@Composable
fun App() {
    var state by remember {
        mutableStateOf(
            GameState(
                board = null,
                player = Cell.RED,
                winner = Cell.EMPTY,
                draw = false,
                rows = "6",
                columns = "7",
                connectTarget = "4",
                isMenu = true,
                error = ""
            )
        )
    }

    fun handleColumnClick(column: Int) {
        state = applyMove(state, column)
        saveState(state)
    }

    fun resetGame() {
        state = state.copy(
            isMenu = true,
            winner = Cell.EMPTY,
            draw = false,
            player = Cell.RED,
            board = null,
            rows = "6",
            columns = "7",
            connectTarget = "4",
            error = ""
        )
        clearSavedGame()
    }

    LaunchedEffect(Unit) {
        val savedGame = loadState()
        state = savedGame
    }

    if (state.winner != Cell.EMPTY) {
        LaunchedEffect(state.winner) {
            delay(3000)
            resetGame()
        }
    } else if (state.draw) {
        LaunchedEffect(state.draw) {
            delay(3000)
            resetGame()
        }
    }

    if (state.isMenu) {
        MenuScreen(
            state,
            onRowsChanged = { state = state.copy(rows = it) },
            onColumnsChanged = { state = state.copy(columns = it) },
            onConnectTargetChanged = { state = state.copy(connectTarget = it) },
            onButtonClick = {
                saveState(state)
                val rowsInt = state.rows.toIntOrNull() ?: 0
                val columnsInt = state.columns.toIntOrNull() ?: 0

                val error = getValidationError(state)
                state = state.copy(error = error)
                if (error.isEmpty()) state =
                    state.copy(error = "", isMenu = false, board = createBoard(rowsInt, columnsInt))
            },
        )
    } else {
        GameScreen(
            board = state.board,
            player = state.player,
            onButtonClick = { resetGame() },
            onColumnClick = { handleColumnClick(it) },
            draw = state.draw
        )
    }
}

private fun getValidationError(state: GameState): String {
    val rowsInt = state.rows.toIntOrNull() ?: 0
    val columnsInt = state.columns.toIntOrNull() ?: 0
    val connectTargetInt = state.connectTarget.toIntOrNull() ?: 0

    return if (rowsInt !in ROWS_MIN..ROWS_MAX) "Rows cannot be less than $ROWS_MIN and more than $ROWS_MAX"
    else if (columnsInt !in COLUMNS_MIN..COLUMNS_MAX) "Columns cannot be less than $COLUMNS_MIN and more than $COLUMNS_MAX"
    else if (connectTargetInt !in TARGET_MIN..TARGET_MAX) "Target cannot be less than Connect $TARGET_MIN and more than Connect $TARGET_MAX"
    else if (connectTargetInt > columnsInt && connectTargetInt > rowsInt) "Target is impossible to reach"
    else ""
}

private fun applyMove(state: GameState, column: Int): GameState {
    if (state.draw || state.winner != Cell.EMPTY) return state

    val board = state.board ?: return state
    val nextBoard = board.map { row -> row.toMutableList() }

    if (!dropPiece(nextBoard, column, state.player)) return state

    val winner = checkWinner(nextBoard, state.connectTarget.toInt())
    if (winner != Cell.EMPTY) {
        for (row in nextBoard.indices) {
            for (col in nextBoard[row].indices) {
                nextBoard[row][col] = winner
            }
        }
        return state.copy(board = nextBoard, winner = winner)
    }

    if (isDraw(nextBoard)) {
        for (row in nextBoard.indices) {
            for (col in nextBoard[row].indices) {
                nextBoard[row][col] = Cell.EMPTY
            }
        }
        return state.copy(board = nextBoard, draw = true)
    }

    val nextPlayer = if (state.player == Cell.RED) Cell.YELLOW else Cell.RED
    return state.copy(board = nextBoard, player = nextPlayer)
}