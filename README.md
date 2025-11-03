# Tic Tac Toe

![Tic Tac Toe](Kata_TicTacToe.png?raw=true "Tic Tac Toe")

A simple game of Tic Tac Toe made in Jetpack Compose.

## Building & Running

This project can easily be run with either Android Studio or IntelliJ IDEA.
Simply open the project with either IDE, sync gradle,
and run the app on either a virtual or physical device.

Tests can be run by going to the VM test file, or by adding any of the following (gradle) configurations:
- app:test
- :app:testDebugUnitTest --tests "com.dev2012.tictactoe.ViewModelUnitTest"

## For Reviewers

What was used used:
- Kotlin
- Jetpack Compose
- MVVM with ViewModel and StateFlows
- DI with koin
- CI with GitHub Actions

Important files are in:
- /app/src/main/java/com/dev2012/tictactoe
- \app\src\test\java\com\dev2012\tictactoe
