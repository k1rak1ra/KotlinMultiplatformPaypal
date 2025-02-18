package net.k1ra.kotlinmultiplatformpaypal

expect object PaypalWebCheckout {
    fun showCheckout(orderId: String, paypalState: PaypalState)
}