package game

enum class Cell{
    EMPTY,RED,YELLOW
}

fun createBoard(rows: Int,columns: Int) : List<MutableList<Cell>> = MutableList(rows){MutableList(columns) { Cell.EMPTY } }

fun dropPiece(board: List<MutableList<Cell>>, column: Int, color: Cell): Boolean {
    if (column !in board[0].indices) return false
    for (row in board.lastIndex downTo 0) {
        if (board[row][column] == Cell.EMPTY) {
            board[row][column] = color
            return true
        }
    }
    return false
}

fun checkWinner(board: List<MutableList<Cell>>,requirement : Int) : Cell{
    for(row in board.indices){
        for(column in board[row].indices) {
            val cell = board[row][column]
            if (cell == Cell.EMPTY) continue
            if( hasLine(row,column,0,1,board,requirement) ||
                hasLine(row,column,1,0,board,requirement) ||
                hasLine(row,column,1,1,board,requirement) ||
                hasLine(row,column,1,-1,board,requirement))
                return cell
        }
    }

    return Cell.EMPTY
}

fun hasLine(startRow: Int, startColumn: Int,rowStep: Int,colStep:Int,board: List<MutableList<Cell>>,requirement : Int) : Boolean{
    val value : Cell =  board[startRow][startColumn]
    if(value == Cell.EMPTY)
        return false

    var counter: Int = requirement - 1
    var searchRow = startRow+rowStep
    var searchColumn = startColumn+colStep
    while(counter-- != 0) {
        if(searchRow !in board.indices || searchColumn !in board[searchRow].indices) return false

        val searchValue = board[searchRow][searchColumn]
        if(searchValue == Cell.EMPTY || searchValue != value)
            return false

        searchRow += rowStep
        searchColumn += colStep
    }
    return true
}

fun isDraw(board: List<MutableList<Cell>>) : Boolean{
    return board[0].all { it != Cell.EMPTY }
}