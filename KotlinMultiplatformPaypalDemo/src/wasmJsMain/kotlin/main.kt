import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import net.k1ra.kotlinmultiplatformpaypal.LocalLayerContainer

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow(canvasElementId = "ComposeTarget", title = "KotlinMultiplatformPaypalDemo") {
        CompositionLocalProvider(LocalLayerContainer provides document.body!!) {
            App()
        }
    }
}