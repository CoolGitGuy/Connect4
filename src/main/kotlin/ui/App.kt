package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import game.Cell
import game.checkWinner
import game.createBoard
import game.dropPiece
import game.isDraw
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexDirection
import org.jetbrains.compose.web.css.JustifyContent
import org.jetbrains.compose.web.css.alignItems
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.flexDirection
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.justifyContent
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Text
import persistance.SavedGame
import persistance.clearSavedGame
import persistance.loadGame
import persistance.saveGame

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

    fun currentSave() = SavedGame(state.rows,state.columns,state.connectTarget,state.player,state.winner,state.draw,state.isMenu,state.board)

    fun resetGame() {
        state = state.copy(isMenu = true,
        winner = Cell.EMPTY,
        draw = false,
        player = Cell.RED,
        board = null,
        rows = "6",
        columns = "7",
        connectTarget = "4",
        error = "")
        clearSavedGame("connect4-save")
    }



    LaunchedEffect(Unit) {
        val savedGame = loadGame() ?: return@LaunchedEffect

        state = state.copy(rows = savedGame.rows,
        columns = savedGame.columns,
        connectTarget = savedGame.connectTarget,
        player = savedGame.player,
        winner = savedGame.winner,
        draw = savedGame.draw,
        isMenu = savedGame.isMenu,
        board = savedGame.board)
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
        Div(attrs = {
            style {
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Column)
                alignItems(AlignItems.Center)
            }
        }) {
            Div {
                H1 { Text("Connect 4") }
            }
            Div {
                H2 { Text("Field Size") }
            }
            Div {
                Input(InputType.Number) {
                    style {
                        width(30.px)
                    }
                    value(state.rows)
                    onInput { state = state.copy(rows = it.value?.toString() ?: "") }
                }
                Text("X")
                Input(InputType.Number) {
                    style {
                        width(30.px)
                    }
                    value(state.columns)
                    onInput { state = state.copy(columns = it.value?.toString() ?: "") }
                }
            }
            Div {
                H2 { Text("Win Condition") }
            }
            Div {
                Text("Connect")
                Input(InputType.Number) {
                    style {
                        width(30.px)
                    }
                    value(state.connectTarget)
                    onInput { state = state.copy(connectTarget = it.value?.toString() ?: "") }
                }
            }
            Div {
                Button(attrs = {
                    onClick {
                        saveGame(currentSave())
                        val rowsInt = state.rows.toIntOrNull() ?: 0
                        val columnsInt = state.columns.toIntOrNull() ?: 0
                        val connectTargetInt = state.connectTarget.toIntOrNull() ?: 0


                        if (rowsInt !in ROWS_MIN..ROWS_MAX)
                            state = state.copy(error = "Rows cannot be less than $ROWS_MIN and more than $ROWS_MAX")
                        else if (columnsInt !in COLUMNS_MIN..COLUMNS_MAX)
                            state = state.copy(error = "Columns cannot be less than $COLUMNS_MIN and more than $COLUMNS_MAX")
                        else if (connectTargetInt !in TARGET_MIN..TARGET_MAX)
                            state = state.copy(error = "Target cannot be less than Connect $TARGET_MIN and more than Connect $TARGET_MAX")
                        else if (connectTargetInt > columnsInt && connectTargetInt > rowsInt)
                            state = state.copy(error = "Target is impossible to reach")
                        else {
                            state = state.copy(error = "",
                                isMenu = false,
                                board = createBoard(rowsInt, columnsInt))
                        }
                    }
                }) {
                    Text("Start")
                }
            }
            Div { Text(state.error) }
        }
    } else {
        Game(state.board, state.player, onButtonClick = { resetGame() }, onColumnClick = { column ->
            if (state.draw || state.winner != Cell.EMPTY) return@Game

            state.board?.let {
                val nextBoard =
                    it.map { row -> row.toMutableList() } // assigned a new board reference so compose picks up the change
                if (dropPiece(nextBoard, column, state.player)) {
                    state = state.copy(winner = checkWinner(nextBoard, state.connectTarget.toInt()))
                    if (state.winner != Cell.EMPTY) {
                        for (row in nextBoard.indices) {
                            for (column in nextBoard[row].indices) {
                                nextBoard[row][column] = state.winner
                            }
                        }
                    }
                    state = state.copy(board = nextBoard)
                    if (state.winner == Cell.EMPTY)
                        state = state.copy(player = if (state.player == Cell.RED) Cell.YELLOW else Cell.RED)
                }
                if (isDraw(nextBoard) && state.winner == Cell.EMPTY) {
                    for (row in nextBoard.indices) {
                        for (column in nextBoard[row].indices) {
                            nextBoard[row][column] = Cell.EMPTY
                        }
                    }
                    state = state.copy(draw = true)
                }
            }

            saveGame(currentSave())
        }, draw = state.draw)
    }
}



@Composable
private fun Game(
    board: List<MutableList<Cell>>?,
    player: Cell,
    onColumnClick: (Int) -> Unit,
    onButtonClick: () -> Unit,
    draw: Boolean
) {
    Div {
        Button(attrs = {
            style {
                width(100.percent)
            }
            onClick { onButtonClick() }
        }) { Text("Back To Main Menu") }
    }

    Div(attrs = {
        style {
            width(100.percent)
            property("overflow-x", "auto")
        }
    }) {
        board?.forEach { row ->
            Div(attrs = {
                style {
                    display(DisplayStyle.Flex)
                    justifyContent(JustifyContent.Center)
                }
            }) {
                row.forEachIndexed { columnIndex, cell ->
                    val color = when (cell) {
                        Cell.EMPTY -> Color.gray
                        Cell.RED -> Color.red
                        else -> Color.yellow
                    }
                    Div(attrs = {
                        style {
                            width(70.px)
                            height(70.px)
                            borderRadius(50.percent)
                            backgroundColor(color)
                        }
                        onClick {
                            onColumnClick(columnIndex)
                        }
                    })
                }
            }
        }
    }

    Div(attrs = {
        style {
            display(DisplayStyle.Flex)
            justifyContent(JustifyContent.Center)
        }
    }) { Text(if (draw) "Game ended in a draw" else "Current player: ${player.name}") }
}