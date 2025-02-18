package net.k1ra.kotlinmultiplatformpaypal

import androidx.compose.runtime.Composable

expect class PaypalState

@Composable
expect fun RememberPaypalState(resultHandler: PaypalPaymentResultHandler) : PaypalState