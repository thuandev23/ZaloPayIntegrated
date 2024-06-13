package com.thuan.zalopay


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thuan.zalopay.databinding.ActivityPaymentNotificationBinding


class PaymentNotification : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentNotificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val txtNotification = binding.textViewNotify
        val intent = intent
        txtNotification.text = intent.getStringExtra("result")
    }
}