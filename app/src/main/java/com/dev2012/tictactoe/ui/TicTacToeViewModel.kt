package com.dev2012.tictactoe.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


enum class Player {
    X, O
}

enum class WinState {
    X, O, Draw, None
}

enum class Play {
    X, O, None
}

data class TicTacToeUiState(
    val winState: WinState = WinState.None,
    val currentPlayer: Player = Player.X,
    val grid: List<List<Play>> = List(3) { List(3) { Play.None } }
)

/**
 * ViewModel that manages the state of a Tic Tac Toe game.
 *
 * All you have to do is call [turn],
 * this VM then manages setting the correct token as well as changing the player.
 *
 * Note that once the game reaches a WinState other than None (ie Draw),
 * you must call [reset] yourself
 */
class TicTacToeViewModel : ViewModel() {
    //While each of these could be split into separate StateFlows
    // having one as a snapshot makes the View code cleaner for such a simple UI
    private val _uiState = MutableStateFlow(TicTacToeUiState())
    val uiState: StateFlow<TicTacToeUiState> = _uiState.asStateFlow()

    fun reset() {
        _uiState.value = TicTacToeUiState()
    }

    fun turn(x: Int, y: Int) {
        val currentState = _uiState.value.winState

        if (currentState != WinState.None) {
            return
        }

        val grid = _uiState.value.grid.map { it.toMutableList() }.toMutableList()
        var player = _uiState.value.currentPlayer

        if (grid[x][y] == Play.None) {
            grid[x][y] = if (player == Player.X) Play.X else Play.O
            player = if (player == Player.X) Player.O else Player.X

            //Resetting will be done by the user with confirming
            val state = checkWinConditions(grid)

            //State only changes if we can change the cell
            _uiState.value = TicTacToeUiState(
                currentPlayer = player, grid = grid, winState = state
            )
        }
    }

    private fun checkSet(set: List<Play>): WinState? {
        try {
            val expected = set.first { it != Play.None } //NOT set.first() for .None
            if (set.all { cell -> cell == expected }) {
                return if (expected == Play.X) WinState.X
                else WinState.O
            }
        //Catch & ignore if set.first fails
        } catch (e: NoSuchElementException) {}

        return null
    }

    private fun checkWinConditions(grid: List<List<Play>>): WinState {
        //Check row
        grid.forEach { row ->
            checkSet(row)?.let { return it }
        }

        //Check column
        for (i in 0..<grid.size) {
            val col = grid.map { it[i] }
            checkSet(col)?.let { return it }
        }

        //Check diagonals
        val diagonal1 = List(grid.size) { i -> grid[i][i] }
        checkSet(diagonal1)?.let { return it }

        val diagonal2 = List(grid.size) { i -> grid[i][grid.size - 1 - i] }
        checkSet(diagonal2)?.let { return it }

        //Check if full
        if (grid.all { row -> row.all { cell -> cell != Play.None } }) {
            return WinState.Draw
        }
        return WinState.None
    }
}