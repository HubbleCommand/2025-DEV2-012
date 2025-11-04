# Tic Tac Toe

![Screenshot](images/Screenshot_TicTacToe.jpg?raw=true "Screenshot")

A simple game of Tic Tac Toe made in Jetpack Compose.

## Building & Running

After cloning this repo, you can easily build the app with either Android Studio or IntelliJ IDEA.
Simply open the project with either IDE, sync gradle to get the dependencies,
then build and run the app on either a virtual or physical device.

The final result should look like the screenshot above.

Tests can be run by going to the VM test file, or by adding any of the following gradle run configurations:
- :app:test
- :app:testDebugUnitTest --tests "com.dev2012.tictactoe.ViewModelUnitTest"

## For Reviewers

What was used used:
- Kotlin
- Jetpack Compose
- MVVM with ViewModel and StateFlows
- DI with koin
- CI with GitHub Actions, with test report artifacts

Important files are in:
- /app/src/main/java/com/dev2012/tictactoe
- \app\src\test\java\com\dev2012\tictactoe
