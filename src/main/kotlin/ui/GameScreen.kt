package ui

import androidx.compose.runtime.Composable
import game.Cell
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text


@Composable
fun GameScreen(
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