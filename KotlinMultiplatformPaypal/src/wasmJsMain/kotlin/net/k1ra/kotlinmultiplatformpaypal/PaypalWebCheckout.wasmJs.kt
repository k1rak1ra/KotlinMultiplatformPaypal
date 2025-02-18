package net.k1ra.kotlinmultiplatformpaypal

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

actual object PaypalWebCheckout {
    actual fun showCheckout(orderId: String, paypalState: PaypalState) {
        paypalState.showPaymentSheet.value = true

        CoroutineScope(Dispatchers.Main).launch {
            delay(100)
            makePaypalButtons(
                orderId,
                onApprove = { paypalState.resultHandler.onCompleted() },
                onCancel = { paypalState.resultHandler.onCanceled() },
                onError =  { paypalState.resultHandler.onFailed(Exception()) }
            )
        }
    }
}

fun makePaypalButtons(orderId: String, onApprove: () -> Unit, onCancel: () -> Unit, onError: () -> Unit) {
    js("""
        paypal.Buttons({
                async createOrder() {
                    return orderId;
                },
                async onApprove(data) {
                    onApprove();
                },
                onCancel(data) {
                    onCancel();
                },
                onError(err) {
                    onError();
                }
            }).render('#paypal-button-container');
    """)
}