package com.dev2012.tictactoe

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.dev2012.tictactoe.ui.TicTacToeGameView
import com.dev2012.tictactoe.ui.TicTacToeViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.dsl.module

class TTTInstrumentedTest {
    //region setup
    @get:Rule
    val rule = createComposeRule()

    private val testModule = module {
        factory { TicTacToeViewModel() }
    }

    @Before
    fun setup() {
        loadKoinModules(testModule)
    }

    @After
    fun tearDown() {
        stopKoin()
    }
    //endregion


    @Test
    fun testPlayerText() {
        rule.setContent{ TicTacToeGameView() }

        rule.onNodeWithTag(TAGS.PLAYER_TEXT).assertTextEquals("Player: X")
        rule.onNodeWithTag("0 0").performClick()
        rule.onNodeWithTag(TAGS.PLAYER_TEXT).assertTextEquals("Player: O")
    }
}