package net.k1ra.kotlinmultiplatformpaypal

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.paypal.android.corepayments.CoreConfig
import com.paypal.android.corepayments.Environment
import com.paypal.android.paypalwebpayments.PayPalPresentAuthChallengeResult
import com.paypal.android.paypalwebpayments.PayPalWebCheckoutClient
import com.paypal.android.paypalwebpayments.PayPalWebCheckoutFinishStartResult
import com.paypal.android.paypalwebpayments.PayPalWebCheckoutRequest
import net.k1ra.sharedprefkmm.SharedPrefKmmInitContentProvider

class PaypalActivity : AppCompatActivity() {
    private val environment = when(PaypalConfig.paypalEnvironment) {
        PaypalEnvironment.SANDBOX -> Environment.SANDBOX
        PaypalEnvironment.LIVE -> Environment.LIVE
    }
    private val config = CoreConfig(PaypalConfig.paypalClientId, environment = environment)
    private val paypalClient = PayPalWebCheckoutClient(SharedPrefKmmInitContentProvider.appContext, config, SharedPrefKmmInitContentProvider.appContext.packageName)
    private var authState: String? = null
    private var launchedBrowser = false

    override fun onResume() {
        super.onResume()
        // Manually attempt auth challenge completion (via host activity intent deep link)
        checkForPayPalAuthCompletion(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // Manually attempt auth challenge completion (via new intent deep link)
        checkForPayPalAuthCompletion(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = globalOrderId
        if (id != null)
            launchPayPalCheckout(id)
    }

    private fun launchPayPalCheckout(orderId: String) {
        launchedBrowser = false
        val checkoutRequest = PayPalWebCheckoutRequest(orderId)
        globalOrderId = null
        when (val result = paypalClient.start(this, checkoutRequest)) {
            is PayPalPresentAuthChallengeResult.Success -> {
                // Capture auth state for balancing call to finishStart() when
                // the merchant application re-enters the foreground
                authState = result.authState
            }
            is PayPalPresentAuthChallengeResult.Failure -> {
                globalResultHandler.onFailed(Exception(result.error.errorDescription))
                finish()
            }
        }
    }

    private fun checkForPayPalAuthCompletion(intent: Intent) = authState?.let { state ->
        // check for checkout completion
        when (val checkoutResult = paypalClient.finishStart(intent, state)) {
           is PayPalWebCheckoutFinishStartResult.Success -> {
               globalResultHandler.onCompleted()
               finish()
               authState = null // Discard auth state when done
           }
           is PayPalWebCheckoutFinishStartResult.Failure -> {
               globalResultHandler.onFailed(Exception(checkoutResult.error.errorDescription))
               finish()
               authState = null // Discard auth state when done
           }

           is PayPalWebCheckoutFinishStartResult.Canceled -> {
               globalResultHandler.onCanceled()
               finish()
               authState = null // Discard auth state when done
           }

            PayPalWebCheckoutFinishStartResult.NoResult -> {
                if (launchedBrowser) {
                    globalResultHandler.onCanceled()
                    finish()
                }

                launchedBrowser = true
            }
        }
    }
}