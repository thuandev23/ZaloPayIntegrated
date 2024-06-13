package com.thuan.zalopay

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.thuan.zalopay.Api.CreateOrder
import com.thuan.zalopay.databinding.ActivityOrderPaymentBinding
import org.json.JSONObject
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener


class OrderPayment : AppCompatActivity() {
    private lateinit var binding: ActivityOrderPaymentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val txtSoluong = binding.textViewSoluong
        val txtTongTien = binding.textViewTongTien
        val btnThanhToan = binding.buttonThanhToan

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX)

        // lay du lieu tu intent MainActivity
        txtSoluong.text = intent.getStringExtra("soluong")
        val total = intent.getDoubleExtra("total", 0.0)
        val totalString = String.format("%.0f", total)
        txtTongTien.text = total.toString()

        // Xu ly su kien click button thanh toan
        binding.buttonThanhToan.setOnClickListener {
            // tao order
            val orderApi = CreateOrder()
            try {
                val data: JSONObject? = orderApi.createOrder(totalString)
                Log.d("Amount", data.toString())
                // lblZpTransToken.setVisibility(View.VISIBLE) // loading on UI
                val code = data?.getString("return_code")
                Toast.makeText(this@OrderPayment, "return-code: $code", Toast.LENGTH_SHORT).show()
                if(code.equals("1")){
                    val token = data?.getString("zp_trans_token")
                    ZaloPaySDK.getInstance().payOrder(this, token!!,"demozpdk://app", object : PayOrderListener {
                        override fun onPaymentSucceeded(transactionId: String?, transToken: String?, appTransID: String?) {
                            /*runOnUiThread {
                                AlertDialog.Builder(this@OrderPayment)
                                    .setTitle("Payment Success")
                                    .setMessage(
                                        java.lang.String.format(
                                            "TransactionId: %s - TransToken: %s",
                                            transactionId,
                                            transToken
                                        )
                                    )
                                    .setPositiveButton("OK") { dialog, which -> }
                                    .setNegativeButton("Cancel", null).show()
                            }*/
                            startActivity(Intent(this@OrderPayment, PaymentNotification::class.java).also {
                                it.putExtra("result", "Thanh toan thanh cong")
                            })
                            //IsLoading() // loading on UI
                        }

                        override fun onPaymentCanceled(zpTransToken: String?, appTransID: String?) {
                           /* AlertDialog.Builder(this@OrderPayment)
                                .setTitle("User Cancel Payment")
                                .setMessage(
                                    java.lang.String.format(
                                        "zpTransToken: %s \n",
                                        zpTransToken
                                    )
                                )
                                .setPositiveButton("OK") { dialog, which -> }
                                .setNegativeButton("Cancel", null).show()*/
                            startActivity(Intent(this@OrderPayment, PaymentNotification::class.java).also {
                                it.putExtra("result", "Thanh toan bi huy")
                            })
                        }

                        override fun onPaymentError(zaloPayError: ZaloPayError?, zpTransToken: String?, appTransID: String?) {
                            /*AlertDialog.Builder(this@OrderPayment)
                                .setTitle("Payment Fail")
                                .setMessage(
                                    java.lang.String.format(
                                        "ZaloPayErrorCode: %s \nTransToken: %s",
                                        zaloPayError.toString(),
                                        zpTransToken
                                    )
                                )
                                .setPositiveButton("OK") { dialog, which -> }
                                .setNegativeButton("Cancel", null).show()*/
                            startActivity(Intent(this@OrderPayment, PaymentNotification::class.java).also {
                                it.putExtra("result", "Thanh toan that bai")
                            })
                        }

                    })
                }

            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }
}