package net.k1ra.kotlinmultiplatformpaypal

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

actual class PaypalState(val activity: Activity, val resultHandler: PaypalPaymentResultHandler)

@Composable
actual fun RememberPaypalState(resultHandler: PaypalPaymentResultHandler): PaypalState {
    return PaypalState(LocalContext.current.getActivity(), resultHandler)
}

fun Context.getActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }

    throw Exception("Could not get Activity")
}
