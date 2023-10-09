package com.example.gpayintegration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.gpayintegration.PaymentsUtil.allowedPaymentMethods
import com.example.gpayintegration.databinding.ActivityGpayBinding
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wallet.IsReadyToPayRequest
import com.google.android.gms.wallet.button.ButtonOptions
import com.google.android.gms.wallet.button.PayButton

class GPayActivity : AppCompatActivity() {
    private lateinit var layout: ActivityGpayBinding
    private lateinit var googlePayButton: PayButton
    private lateinit var addToGoogleWalletButton: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = ActivityGpayBinding.inflate(layoutInflater)
        setContentView(layout.root)


    }
}
