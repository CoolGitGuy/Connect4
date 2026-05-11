package ui

import androidx.compose.runtime.Composable
import game.Cell
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text


@Composable
fun GameScreen(
    board: List<MutableList<Cell>>?,
    player: Cell,
    onColumnClick: (Int) -> Unit,
    onButtonClick: () -> Unit,
    draw: Boolean,
    lastPlayedRow: Int?,
    lastPlayedColumn: Int?
) {
    Div(attrs = {
        classes("page", "game-page")
    }) {
        Div(
            attrs = {
                classes("game-card")
            }
        ) {
            Div(attrs = {
                classes("game-actions")
            }) {
                Button(attrs = {
                    classes("menu-button")
                    onClick { onButtonClick() }
                }) { Text("Back To Main Menu") }
            }
            Div(attrs = {
                classes("board-scroll")
            }) {
                Div(
                    attrs = {
                        classes("connect-board")
                    }
                ) {
                    board?.forEachIndexed { rowIndex, row ->
                        Div(attrs = {
                            classes("board-row")
                        }) {
                            row.forEachIndexed { columnIndex, cell ->
                                val cellClass = when (cell) {
                                    Cell.EMPTY -> "cell-empty"
                                    Cell.RED -> "cell-red"
                                    else -> "cell-yellow"
                                }
                                Div(attrs = {
                                    classes("board-cell", cellClass)
                                    if (rowIndex == lastPlayedRow && columnIndex == lastPlayedColumn) {
                                        classes("cell-drop")
                                    }
                                    onClick {
                                        onColumnClick(columnIndex)
                                    }
                                })
                            }
                        }
                    }
                }
            }
            Div(attrs = {
                classes("game-status")
            }) { Text(if (draw) "Game ended in a draw" else "Current player: ${player.name}") }
        }
    }
}
