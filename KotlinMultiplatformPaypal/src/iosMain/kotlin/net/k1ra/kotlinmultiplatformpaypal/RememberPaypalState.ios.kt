package net.k1ra.kotlinmultiplatformpaypal

import androidx.compose.runtime.Composable

actual class PaypalState(val resultHandler: PaypalPaymentResultHandler)

@Composable
actual fun RememberPaypalState(resultHandler: PaypalPaymentResultHandler): PaypalState {
    return PaypalState(resultHandler)
}