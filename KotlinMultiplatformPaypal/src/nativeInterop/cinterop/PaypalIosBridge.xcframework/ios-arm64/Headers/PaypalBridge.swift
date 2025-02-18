//
//  PaypalBridge.swift
//  PaypalIosBridge
//
//  Created by Oliver Gaus on 2025-02-17.
//

import Foundation
import CorePayments
import PayPalWebPayments

@objcMembers
@objc
public class PaypalBridge : NSObject {
    public var isSandbox: Bool
    public var clientId: String
    
    public init(isSandbox: Bool, clientId: String) {
        self.isSandbox = isSandbox
        self.clientId = clientId
    }
    
    public func doCheckout(
        orderId: String,
        delegate: PaypalBridgeDelegate
    ) {
        var environment = Environment.live
        if (isSandbox) {
            environment = Environment.sandbox
        }
        
        let config = CoreConfig(clientID: clientId, environment: environment)
        let payPalClient = PayPalWebCheckoutClient(config: config)
        let payPalWebRequest = PayPalWebCheckoutRequest(orderID:orderId, fundingSource:.paypal)
        
        payPalClient.delegate = delegate
        payPalClient.start(request: payPalWebRequest)
    }
}

@objcMembers
@objc
public class PaypalBridgeDelegate : NSObject, PayPalWebCheckoutDelegate {
    public var completed: () -> Void
    public var canceled: () -> Void
    public var failed: (Error) -> Void
    
    public init(completed: @escaping () -> Void, canceled: @escaping () -> Void, failed: @escaping (Error) -> Void) {
        self.completed = completed
        self.canceled = canceled
        self.failed = failed
    }
    
    
    public func payPal(_ payPalClient: PayPalWebPayments.PayPalWebCheckoutClient, didFinishWithResult result: PayPalWebPayments.PayPalWebCheckoutResult) {
        completed()
    }
    
    public func payPal(_ payPalClient: PayPalWebPayments.PayPalWebCheckoutClient, didFinishWithError error: CorePayments.CoreSDKError) {
        failed(error)
    }
    
    public func payPalDidCancel(_ payPalClient: PayPalWebPayments.PayPalWebCheckoutClient) {
        canceled()
    }
}
