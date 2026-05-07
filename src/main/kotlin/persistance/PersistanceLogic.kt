package persistance

import game.Cell
import kotlinx.browser.localStorage


data class SavedGame(
    val rows: String,
    val columns: String,
    val connectTarget: String,
    val player: Cell,
    val winner: Cell,
    val draw: Boolean,
    val isMenu: Boolean,
    val board: List<MutableList<Cell>>?
)

fun saveGame(save: SavedGame) {
    val boardString = save.board
        ?.joinToString("|") { row ->
            row.joinToString(",") { cell ->
                cell.name
            }
        } ?: ""

    val saveString = listOf(
        save.rows,
        save.columns,
        save.connectTarget,
        save.player.name,
        save.winner.name,
        save.draw.toString(),
        save.isMenu.toString(),
        boardString
    ).joinToString(";")

    localStorage.setItem("connect4-save", saveString)
}

fun loadGame(): SavedGame? {
    val saved = localStorage.getItem("connect4-save") ?: return null
    val parts = saved.split(";")

    val boardString = parts[7]
    val board = boardString.split("|").map { rowString ->
        rowString.split(",").map { cellString ->
            Cell.valueOf(cellString)
        }.toMutableList()
    }

    return SavedGame(
        rows = parts[0],
        columns = parts[1],
        connectTarget = parts[2],
        player = Cell.valueOf(parts[3]),
        winner = Cell.valueOf(parts[4]),
        draw = parts[5].toBoolean(),
        isMenu = parts[6].toBoolean(),
        board = board
    )
}

fun clearSavedGame(name: String){
    localStorage.removeItem(name)
}
