package ui

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun MenuScreen(
    state: GameState,
    onRowsChanged: (String) -> Unit,
    onColumnsChanged: (String) -> Unit,
    onConnectTargetChanged: (String) -> Unit,
    onButtonClick: () -> Unit
) {
    Div(attrs = {
        style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            alignItems(AlignItems.Center)
        }
    }) {
        Div {
            H1 { Text("Connect 4") }
        }
        Div {
            H2 { Text("Field Size") }
        }
        Div {
            Input(InputType.Number) {
                style {
                    width(30.px)
                }
                value(state.rows)
                onInput { onRowsChanged(it.value?.toString() ?: "") }
            }
            Text("X")
            Input(InputType.Number) {
                style {
                    width(30.px)
                }
                value(state.columns)
                onInput { onColumnsChanged(it.value?.toString() ?: "") }
            }
        }
        Div {
            H2 { Text("Win Condition") }
        }
        Div {
            Text("Connect")
            Input(InputType.Number) {
                style {
                    width(30.px)
                }
                value(state.connectTarget)
                onInput { onConnectTargetChanged(it.value?.toString() ?: "") }
            }
        }
        Div {
            Button(attrs = {
                onClick { onButtonClick() }
            }) {
                Text("Start")
            }
        }
        Div { Text(state.error) }
    }
}