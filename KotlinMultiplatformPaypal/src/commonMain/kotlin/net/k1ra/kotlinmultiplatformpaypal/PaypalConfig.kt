package net.k1ra.kotlinmultiplatformpaypal

object PaypalConfig {
    internal var paypalClientId = ""
    internal var paypalEnvironment = PaypalEnvironment.SANDBOX

    fun setup(clientId: String, environment: PaypalEnvironment) {
        paypalClientId = clientId
        paypalEnvironment = environment
    }
}