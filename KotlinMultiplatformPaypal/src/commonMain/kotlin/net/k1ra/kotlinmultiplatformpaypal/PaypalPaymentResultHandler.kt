package net.k1ra.kotlinmultiplatformpaypal

interface PaypalPaymentResultHandler {
    fun onCompleted()

    fun onCanceled()

    fun onFailed(reason: Throwable)
}