package net.k1ra.kotlinmultiplatformpaypal

import android.content.Intent
import net.k1ra.sharedprefkmm.SharedPrefKmmInitContentProvider

var globalResultHandler = object : PaypalPaymentResultHandler {
    override fun onCompleted() {}
    override fun onCanceled() {}
    override fun onFailed(reason: Throwable) {}
}
var globalOrderId: String? = null

actual object PaypalWebCheckout {
    actual fun showCheckout(orderId: String, paypalState: PaypalState) {
        globalResultHandler = paypalState.resultHandler
        globalOrderId = orderId

        val intent = Intent(paypalState.activity, PaypalActivity::class.java)

        paypalState.activity.startActivity(intent)
    }
}