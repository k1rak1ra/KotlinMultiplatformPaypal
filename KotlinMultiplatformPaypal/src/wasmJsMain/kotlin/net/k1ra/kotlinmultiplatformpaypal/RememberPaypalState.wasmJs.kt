package net.k1ra.kotlinmultiplatformpaypal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLScriptElement

actual class PaypalState {
    val showPaymentSheet = mutableStateOf(false)
    val htmlHeight = mutableStateOf(0.0)
    lateinit var resultHandler: PaypalPaymentResultHandler
}

var paypalStateInstance = PaypalState()
var addedScript = false

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun RememberPaypalState(resultHandler: PaypalPaymentResultHandler): PaypalState {
    val showSheet = remember { paypalStateInstance.showPaymentSheet }
    val htmlHeight = remember { paypalStateInstance.htmlHeight }

    println("SHOWSHEET VALUE: ${showSheet.value}")

    paypalStateInstance.resultHandler = resultHandler

    if (!addedScript) {
        addedScript = true
        val script = document.createElement("script") as HTMLScriptElement
        script.src = "https://www.paypal.com/sdk/js?client-id=${PaypalConfig.paypalClientId}&components=buttons&enable-funding=venmo"
        script.type = "text/javascript"
        document.body?.appendChild(script)
    }

    if (showSheet.value) {
        //Stupid hack to dynamically get the height of the HTML element, there's probably a better way of doing this
        CoroutineScope(Dispatchers.Main).launch {
            while(paypalStateInstance.showPaymentSheet.value) {
                val element = document.getElementById("paypal-button-container")
                val height = element?.getBoundingClientRect()?.height

                if (height != null)
                    htmlHeight.value = height

                delay(10)
            }
        }

        ModalBottomSheet(
            sheetState = SheetState(
                skipPartiallyExpanded = true,
                density = LocalDensity.current
            ),
            onDismissRequest = {
                resultHandler.onCanceled()
                showSheet.value = false
            }
        ) {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                HtmlView(
                    modifier = Modifier.fillMaxWidth().height((htmlHeight.value).dp).padding(16.dp),
                    factory = {
                        val container = document.createElement("div") as HTMLElement
                        container.innerHTML = "<div id=\"paypal-button-container\"></div>"
                        container
                    }
                )
            }
        }
    }

    return paypalStateInstance
}