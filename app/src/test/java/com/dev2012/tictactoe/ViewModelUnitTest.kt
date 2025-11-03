package com.dev2012.tictactoe

import com.dev2012.tictactoe.ui.Play
import com.dev2012.tictactoe.ui.Player
import com.dev2012.tictactoe.ui.TicTacToeViewModel
import com.dev2012.tictactoe.ui.WinState
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.getKoin

class ViewModelUnitTest {

    //region setup
    private val testModule = module {
        factory { TicTacToeViewModel() }
    }

    @Before
    fun setup() {
        startKoin {
            modules(testModule)
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }
    //endregion

    @Test
    fun initial_state() {
        val viewModel: TicTacToeViewModel = getKoin().get()
        assert(viewModel.uiState.value.grid.all { row -> row.all { cell -> cell == Play.None } })
        assertEquals(Player.X, viewModel.uiState.value.currentPlayer)
    }

    @Test
    fun first_player_x() {
        val viewModel: TicTacToeViewModel = getKoin().get()
        assertEquals(Player.X, viewModel.uiState.value.currentPlayer)
        viewModel.turn(0, 0)
        assertEquals(Play.X, viewModel.uiState.value.grid[0][0])

        //Verify that the player is then switched to O
        assertEquals(Player.O, viewModel.uiState.value.currentPlayer)
    }

    @Test
    fun reset() {
        val viewModel: TicTacToeViewModel = getKoin().get()
        viewModel.turn(0, 0)
        viewModel.reset()

        //Assert that every cell is Play.None
        assert(viewModel.uiState.value.grid.all { row -> row.all { cell -> cell == Play.None } })
    }

    /**
     * Test that a player cannot play on a cell that has already been used
     */
    @Test
    fun double_play() {
        val viewModel: TicTacToeViewModel = getKoin().get()
        viewModel.turn(0, 0) //X
        viewModel.turn(0, 0) //O, but doesn't set & player doesn't change

        //Calling turn on the same grid position should always give the first play, in this case X
        assertEquals(Play.X, viewModel.uiState.value.grid[0][0])

        viewModel.turn(0, 1) //O
        viewModel.turn(0, 1) //X, but doesn't set & player doesn't change

        //Calling turn on the same grid position should always give the first play, in this case O
        assertEquals(Play.O, viewModel.uiState.value.grid[0][1])
    }

    //region win tests
    @Test
    fun win_row() {
        val viewModel: TicTacToeViewModel = getKoin().get()
        viewModel.turn(0, 0) //X
        viewModel.turn(0, 1) //O
        viewModel.turn(1, 0) //X
        viewModel.turn(1, 1) //O
        viewModel.turn(2, 0) //X

        assertEquals(WinState.X, viewModel.uiState.value.winState)
    }

    @Test
    fun win_column() {
        val viewModel: TicTacToeViewModel = getKoin().get()
        viewModel.turn(2, 1) //X
        viewModel.turn(1, 0) //O
        viewModel.turn(0, 1) //X
        viewModel.turn(1, 1) //O
        viewModel.turn(2, 2) //X
        viewModel.turn(1, 2) //O

        assertEquals(WinState.O, viewModel.uiState.value.winState)
    }

    @Test
    fun win_diagonal() {
        val viewModel: TicTacToeViewModel = getKoin().get()
        viewModel.turn(2, 2) //X
        viewModel.turn(1, 0) //O
        viewModel.turn(1, 1) //X
        viewModel.turn(1, 2) //O
        viewModel.turn(0, 0) //X

        val grid = viewModel.uiState.value.grid
        val diagonal2 = List(grid.size) { i -> grid[i][i] }
        assertEquals(List(3) { Play.X }, diagonal2)

        assertEquals(WinState.X, viewModel.uiState.value.winState)
    }

    @Test
    fun draw() {
        val viewModel: TicTacToeViewModel = getKoin().get()
        viewModel.turn(0, 0) //X    X - -
        viewModel.turn(0, 1) //O    O - -
        viewModel.turn(0, 2) //X    X - -

        viewModel.turn(1, 2) //O    X - O
        viewModel.turn(1, 1) //X    O X -
        viewModel.turn(2, 0) //O    X O -

        viewModel.turn(1, 0) //X    X X O
        viewModel.turn(2, 2) //O    O X X
        viewModel.turn(2, 1) //X    X O O

        assertEquals(WinState.Draw, viewModel.uiState.value.winState)
    }
    //endregion
}