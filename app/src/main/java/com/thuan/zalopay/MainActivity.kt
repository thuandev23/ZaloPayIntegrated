package com.thuan.zalopay

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.thuan.zalopay.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonConfirm.setOnClickListener {
            val soLuongString = binding.editTextSoluong.text.toString()
            if (soLuongString.isEmpty()) {
                Toast.makeText(this, "Nhập số lượng muốn mua", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val total = soLuongString.toDouble() * 100000
            Toast.makeText(this, "Tổng tiền: $total", Toast.LENGTH_SHORT).show()
            startActivity(
                Intent(
                    this@MainActivity,
                    OrderPayment::class.java
                ).apply {
                    putExtra("soluong", soLuongString)
                    putExtra("total", total)
                })
        }
    }
}
