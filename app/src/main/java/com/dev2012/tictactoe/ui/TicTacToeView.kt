package com.dev2012.tictactoe.ui


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.circle
import androidx.graphics.shapes.toPath
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dev2012.tictactoe.R
import com.dev2012.tictactoe.TAGS
import org.koin.androidx.compose.koinViewModel

@Composable
fun TicTacToeElement(
    modifier: Modifier,
    value: Play,
    //While I'm not a huge fan of callbacks,
    // it's what Compose uses, and it would be worse to pass the VM
    onClick: () -> Unit,
) {
    //While Box is usable, it requires a MutableInteractionSource & other linking
    TextButton(
        onClick = {
            if (value == Play.None) {
                onClick()
            }
        },
        modifier = modifier
            //drawWithCache could be used... but we aren't doing anything heavy here
            .drawBehind {
                if (value == Play.None) {
                    return@drawBehind
                }

                val pth = when (value) {
                    Play.O -> {
                        val circlePolygon = RoundedPolygon.circle(
                            numVertices = 64,
                            radius = size.minDimension / 2f - 12f, centerX = size.width / 2f, centerY = size.height / 2f
                        )
                        circlePolygon.toPath().asComposePath()
                    }
                    Play.X -> Path().apply {
                        moveTo(0f, 0f)
                        lineTo(size.minDimension, size.minDimension)
                        moveTo(size.minDimension, 0f)
                        lineTo(0f, size.minDimension)
                    }
                    else -> null
                }
                pth?.let {
                    drawPath(pth, color = Color.Gray, style = Stroke(width = 12f))
                }
            }
    ) { }
}

@Composable
fun TicTacToeEndDialog(
    text: String,
    onClose: () -> Unit
) {
    Dialog(onDismissRequest = { onClose() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = text,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
            )
            Button(onClick = {
                onClose()
            }) {
                Text(stringResource(R.string.reset))
            }
        }
    }
}

@Composable
fun TicTacToeGameView(modifier: Modifier = Modifier, vm: TicTacToeViewModel = koinViewModel()) {
    val state by vm.uiState.collectAsState()

    if (state.winState != WinState.None) {
        TicTacToeEndDialog("${stringResource(R.string.winner)}: ${state.winState}") {
            vm.reset()
        }
    }

    val itemSize = 100.dp
    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        //As grid is fixed size, don't need lazy anything
        // while it will always be a 3x3 grid, I don't like hard-coding values like that
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            repeat(state.grid.size) { col ->
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(state.grid[col].size) { row ->
                        TicTacToeElement(
                            Modifier
                                .testTag("$col $row")
                                .size(itemSize)
                                .padding(4.dp), value = state.grid[col][row]
                        ) {
                            vm.turn(col, row)
                        }
                        if (row < state.grid[col].size - 1) VerticalDivider(
                            Modifier.height(itemSize), color = Color.Gray, thickness = 1.dp
                        )
                    }
                }
                if (col < state.grid.size - 1) HorizontalDivider(
                    Modifier.width(itemSize * state.grid[0].size), color = Color.Gray, thickness = 1.dp
                )
            }
            Spacer(modifier = Modifier.size(30.dp))
            Text(
                text = "${stringResource(R.string.player)}: ${state.currentPlayer}",
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag(TAGS.PLAYER_TEXT)
            )
        }
    }
}

@Preview
@Composable
fun TicTacToePreview() {
    TicTacToeGameView(modifier = Modifier.fillMaxSize())
}
