package com.example.lojavisual

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import kotlin.math.roundToInt

class ProductActivity : Activity() {

    private var quantity = 1
    private var unitPriceCents = 14990

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        val name = intent.getStringExtra("name") ?: "Headphone Pro"
        val price = intent.getStringExtra("price") ?: "R$ 149,90"
        val category = intent.getStringExtra("category") ?: "Áudio"
        val description = intent.getStringExtra("description") ?: "Produto demonstrativo."
        val imageRes = intent.getIntExtra("imageRes", R.drawable.product_headphone)

        unitPriceCents = priceToCents(price)

        findViewById<TextView>(R.id.productName).text = name
        findViewById<TextView>(R.id.productPrice).text = price
        findViewById<TextView>(R.id.productCategory).text = category.uppercase()
        findViewById<TextView>(R.id.productDescription).text = description
        findViewById<ImageView>(R.id.productImage).setImageResource(imageRes)

        val quantityText = findViewById<TextView>(R.id.quantityText)

        findViewById<TextView>(R.id.btnBack).setOnClickListener { finish() }

        findViewById<TextView>(R.id.btnMinus).setOnClickListener {
            if (quantity > 1) {
                quantity--
                quantityText.text = quantity.toString()
            }
        }

        findViewById<TextView>(R.id.btnPlus).setOnClickListener {
            if (quantity < 9) {
                quantity++
                quantityText.text = quantity.toString()
            }
        }

        findViewById<TextView>(R.id.btnBuy).setOnClickListener {
            val totalCents = unitPriceCents * quantity
            val paymentIntent = Intent(this, PaymentActivity::class.java).apply {
                putExtra("name", name)
                putExtra("quantity", quantity)
                putExtra("totalCents", totalCents)
                putExtra("imageRes", imageRes)
            }
            startActivity(paymentIntent)
        }
    }

    private fun priceToCents(price: String): Int {
        val numeric = price
            .replace("R$", "")
            .replace(".", "")
            .replace(",", ".")
            .trim()
        return ((numeric.toDoubleOrNull() ?: 149.90) * 100).roundToInt()
    }
}
