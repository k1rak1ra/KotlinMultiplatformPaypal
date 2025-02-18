package net.k1ra.kotlinmultiplatformpaypal

import PaypalIosBridge.PaypalBridge
import PaypalIosBridge.PaypalBridgeDelegate
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
var delegate = PaypalBridgeDelegate(completed = {}, canceled =  {}, failed = {})

actual object PaypalWebCheckout {
    @OptIn(ExperimentalForeignApi::class)
    actual fun showCheckout(orderId: String, paypalState: PaypalState) {
        delegate.setCompleted { paypalState.resultHandler.onCompleted() }
        delegate.setCanceled { paypalState.resultHandler.onCanceled() }
        delegate.setFailed { paypalState.resultHandler.onFailed(Exception(it?.description)) }

        val bridge = PaypalBridge(PaypalConfig.paypalEnvironment == PaypalEnvironment.SANDBOX, PaypalConfig.paypalClientId)
        bridge.doCheckoutWithOrderId(orderId, delegate)
    }
}