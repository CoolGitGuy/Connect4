import org.jetbrains.compose.web.renderComposable
import ui.App

fun main() {
    renderComposable(rootElementId = "root") {
        App()
    }
}

/*@Composable
fun Body() {
    var counter by remember { mutableStateOf(0) }
    Div {
        Text("Clicked: ${counter}")
    }
    Button(
        attrs = {
            onClick { _ ->
                counter++
            }
        }
    ) {
        Text("Click")
    }
}*/
