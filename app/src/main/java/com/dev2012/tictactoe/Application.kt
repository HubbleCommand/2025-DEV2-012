package com.dev2012.tictactoe

import android.app.Application
import com.dev2012.tictactoe.ui.TicTacToeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { TicTacToeViewModel() }
}

class TicTacToeApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TicTacToeApplication)
            modules(appModule)
        }
    }
}