import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import net.k1ra.kotlinmultiplatformpaypal.PaypalConfig
import net.k1ra.kotlinmultiplatformpaypal.PaypalEnvironment
import net.k1ra.kotlinmultiplatformpaypal.PaypalPaymentResultHandler
import net.k1ra.kotlinmultiplatformpaypal.PaypalWebCheckout
import net.k1ra.kotlinmultiplatformpaypal.RememberPaypalState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val resultHandler = object : PaypalPaymentResultHandler {
        override fun onCompleted() {
            println("COMPLETED")
        }

        override fun onCanceled() {
            println("CANCELLED")
        }

        override fun onFailed(reason: Throwable) {
            println("FAILED ${reason.message}")
        }

    }

    PaypalConfig.setup("", PaypalEnvironment.SANDBOX)

    val paypalState = RememberPaypalState(resultHandler)

    MaterialTheme {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                PaypalWebCheckout.showCheckout("", paypalState)
            }) {
                Text("Test checkout")
            }
        }
    }
}