package ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import game.Cell
import game.checkWinner
import game.createBoard
import game.dropPiece
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Input
import org.jetbrains.compose.web.dom.Text

const val ROWS_MIN = 4
const val ROWS_MAX = 20

const val COLUMNS_MIN = 4
const val COLUMNS_MAX = 20

const val TARGET_MIN = 4
const val TARGET_MAX = 10



@Composable
fun App() {
    var player by remember{ mutableStateOf(Cell.RED)}
    var board : List<MutableList<Cell>>? by remember { mutableStateOf(null) }
    var isMenu by remember { mutableStateOf(true) }
    var rows by remember{ mutableStateOf("6")}
    var columns by remember{ mutableStateOf("7")}
    var connectTarget by remember{ mutableStateOf("4")}
    var error by remember{ mutableStateOf("")}

    if(isMenu) {
        Div {
            H1 { Text("Connect 4") }
        }
        Div {
            H2 { Text("Field Size") }
        }
        Div {
            Input(InputType.Number){
                value(rows)
                onInput { rows = it.value?.toString() ?: "" }
            }
            Text("X")
            Input(InputType.Number){
                value(columns)
                onInput { columns = it.value?.toString() ?: "" }
            }
        }

        Div {
            H2 { Text("Win Condition") }
        }
        Div {
            Text("Connect")
            Input(InputType.Number){
                value(connectTarget)
                onInput { connectTarget = it.value?.toString() ?: "" }
            }
        }
        Div {
            Button(attrs = {
                onClick {
                    val rowsInt = rows.toIntOrNull() ?: 0
                    val columnsInt = columns.toIntOrNull() ?: 0
                    val connectTargetInt = connectTarget.toIntOrNull() ?: 0


                    if(rowsInt !in ROWS_MIN..ROWS_MAX)
                        error = "Rows cannot be less than $ROWS_MIN and more than $ROWS_MAX"
                    else if(columnsInt !in COLUMNS_MIN..COLUMNS_MAX)
                        error = "Columns cannot be less than $COLUMNS_MIN and more than $COLUMNS_MAX"
                    else if(connectTargetInt !in TARGET_MIN..TARGET_MAX)
                        error = "Target cannot be less than Connect $TARGET_MIN and more than Connect $TARGET_MAX"
                    else if(connectTargetInt > columnsInt && connectTargetInt > rowsInt)
                        error = "Target is impossible to reach"
                    else {
                        error = ""
                        isMenu = false
                        board = createBoard(rowsInt,columnsInt)
                    }
                }
            }) {
                Text("Start")
            }
        }
        Div { Text(error) }
    } else {
        Game(board,player, onColumnClick = { column ->
            board?.let {
                if (dropPiece(it, column, player)) {
                    val winner = checkWinner(it,connectTarget.toInt())
                    if(winner != Cell.EMPTY) isMenu = true
                    if (player == Cell.RED) player = Cell.YELLOW else player = Cell.RED
                }
            }
        })
    }
}

@Composable
private fun Game(board: List<MutableList<Cell>>?, player: Cell,onColumnClick: (Int) -> Unit) {
    Div {
        board?.forEach { row ->
            Div(attrs = {
                style {
                    display(DisplayStyle.Flex)
                }
            }) {
                row.forEachIndexed { columnIndex,cell ->
                    val color: CSSColorValue =
                        if (cell == Cell.EMPTY) Color.gray else (if (cell == Cell.RED) Color.red else Color.yellow)
                    Div(attrs = {
                        style {
                            width(40.px)
                            height(40.px)
                            borderRadius(50.percent)
                            backgroundColor(color)
                        }
                        onClick { onColumnClick(columnIndex)
                        }
                    })
                }
            }
        }
    }
    Div{ Text(player.name)}
}