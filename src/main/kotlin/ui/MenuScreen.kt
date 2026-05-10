package ui

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
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
        classes("page", "menu-page")
    }) {
        Div(attrs = {
            classes("menu-card")
        }) {
            Div(attrs = {
                classes("menu-title")
            }) {
                H1 { Text("Connect 4") }
            }
            Div {
                H2 { Text("Field Size") }
            }
            Div(attrs = {
                classes("field-row")
            }) {
                Input(InputType.Number) {
                    classes("menu-input")
                    style {
                        width(30.px)
                    }
                    value(state.rows)
                    onInput { onRowsChanged(it.value?.toString() ?: "") }
                }
                Text("X")
                Input(InputType.Number) {
                    classes("menu-input")
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
            Div(
                attrs = {
                    classes("connect-input")
                }
            ) {
                Text("Connect")
                Input(InputType.Number) {
                    classes("menu-input")
                    style {
                        width(30.px)
                    }
                    value(state.connectTarget)
                    onInput { onConnectTargetChanged(it.value?.toString() ?: "") }
                }
            }
            Div(attrs = {
                classes("menu-actions")
            }) {
                Button(attrs = {
                    classes("menu-button")
                    onClick { onButtonClick() }
                }) {
                    Text("Start")
                }
            }
            Div { Text(state.error) }
        }
    }
}
