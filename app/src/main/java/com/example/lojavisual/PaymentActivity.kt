package com.example.lojavisual

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import java.text.NumberFormat
import java.util.Locale

class PaymentActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val name = intent.getStringExtra("name") ?: "Headphone Pro"
        val quantity = intent.getIntExtra("quantity", 1)
        val totalCents = intent.getIntExtra("totalCents", 14990)
        val imageRes = intent.getIntExtra("imageRes", R.drawable.product_headphone)
        val totalFormatted = formatCurrency(totalCents)

        findViewById<TextView>(R.id.paymentProductName).text = name
        findViewById<TextView>(R.id.paymentQuantity).text = "Quantidade: $quantity"
        findViewById<TextView>(R.id.paymentProductPrice).text = totalFormatted
        findViewById<TextView>(R.id.paymentTotal).text = totalFormatted
        findViewById<ImageView>(R.id.paymentProductImage).setImageResource(imageRes)

        findViewById<TextView>(R.id.btnBackPayment).setOnClickListener { finish() }

        val methodGroup = findViewById<RadioGroup>(R.id.paymentMethodGroup)
        val cardFields = findViewById<View>(R.id.cardFields)
        val pixInfo = findViewById<View>(R.id.pixInfo)

        methodGroup.setOnCheckedChangeListener { _, checkedId ->
            val isPix = checkedId == R.id.radioPix
            cardFields.visibility = if (isPix) View.GONE else View.VISIBLE
            pixInfo.visibility = if (isPix) View.VISIBLE else View.GONE
        }

        findViewById<TextView>(R.id.btnFinish).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Pagamento aprovado! ✓")
                .setMessage("Compra demonstrativa finalizada com sucesso.\n\nTotal: $totalFormatted")
                .setPositiveButton("Voltar à loja") { _, _ ->
                    finishAffinity()
                    startActivity(packageManager.getLaunchIntentForPackage(packageName))
                }
                .setNegativeButton("Fechar", null)
                .show()
        }
    }

    private fun formatCurrency(cents: Int): String {
        val locale = Locale("pt", "BR")
        return NumberFormat.getCurrencyInstance(locale).format(cents / 100.0)
    }
}
