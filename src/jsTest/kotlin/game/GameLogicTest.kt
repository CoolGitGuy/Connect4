package game

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GameLogicTest {

    @Test
    fun createBoard_builds_empty_board_with_expected_size() {
        val board = createBoard(6, 7)

        assertEquals(6, board.size)
        assertEquals(7, board[0].size)
        assertTrue(board.all { row -> row.all { cell -> cell == Cell.EMPTY } })
    }

    @Test
    fun dropPiece_places_piece_at_bottom_of_column() {
        val board = createBoard(6, 7)

        val placed = dropPiece(board, 3, Cell.RED)

        assertTrue(placed)
        assertEquals(Cell.RED, board[5][3])
    }

    @Test
    fun dropPiece_stacks_pieces_in_same_column() {
        val board = createBoard(6, 7)

        dropPiece(board, 2, Cell.RED)
        dropPiece(board, 2, Cell.YELLOW)

        assertEquals(Cell.RED, board[5][2])
        assertEquals(Cell.YELLOW, board[4][2])
    }

    @Test
    fun dropPiece_returns_false_when_column_is_full() {
        val board = createBoard(2, 2)

        assertTrue(dropPiece(board, 0, Cell.RED))
        assertTrue(dropPiece(board, 0, Cell.YELLOW))
        assertFalse(dropPiece(board, 0, Cell.RED))
    }

    @Test
    fun checkWinner_detects_horizontal_win() {
        val board = createBoard(6, 7)
        board[5][0] = Cell.RED
        board[5][1] = Cell.RED
        board[5][2] = Cell.RED
        board[5][3] = Cell.RED

        assertEquals(Cell.RED, checkWinner(board, 4))
    }

    @Test
    fun checkWinner_detects_vertical_win() {
        val board = createBoard(6, 7)
        board[5][2] = Cell.YELLOW
        board[4][2] = Cell.YELLOW
        board[3][2] = Cell.YELLOW
        board[2][2] = Cell.YELLOW

        assertEquals(Cell.YELLOW, checkWinner(board, 4))
    }

    @Test
    fun checkWinner_detects_diagonal_win() {
        val board = createBoard(6, 7)
        board[5][0] = Cell.RED
        board[4][1] = Cell.RED
        board[3][2] = Cell.RED
        board[2][3] = Cell.RED

        assertEquals(Cell.RED, checkWinner(board, 4))
    }

    @Test
    fun isDraw_returns_true_for_full_top_row() {
        val board = createBoard(2, 3)
        board[0][0] = Cell.RED
        board[0][1] = Cell.YELLOW
        board[0][2] = Cell.RED

        assertTrue(isDraw(board))
    }
}
