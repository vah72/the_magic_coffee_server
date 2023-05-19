package com.example.demo.order.service;

//import com.example.demo.order.model.Payment;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

public interface PayWithPaypalService {
    Payment pay(Double total, String currency, String method, String intent,
                String cancelUrl, String successUrl ) throws PayPalRESTException;

    Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;
}
