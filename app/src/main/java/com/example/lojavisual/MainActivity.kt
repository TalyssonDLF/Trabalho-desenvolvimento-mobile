package com.example.lojavisual

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindProduct(
            R.id.cardHeadphone,
            "Headphone Pro",
            "R$ 149,90",
            "Áudio",
            "Headphone sem fio com design confortável, acabamento premium e bateria para acompanhar música, estudos e jogos durante todo o dia.",
            R.drawable.product_headphone
        )

        bindProduct(
            R.id.cardWatch,
            "Smartwatch Neo",
            "R$ 199,90",
            "Tecnologia",
            "Smartwatch leve e moderno com mostrador digital, acompanhamento da rotina e visual versátil para usar em qualquer ocasião.",
            R.drawable.product_watch
        )

        bindProduct(
            R.id.cardSneaker,
            "Tênis Urban",
            "R$ 299,90",
            "Moda",
            "Tênis casual com solado confortável e estilo urbano, ideal para o dia a dia e combinações modernas.",
            R.drawable.product_sneaker
        )

        bindProduct(
            R.id.cardKeyboard,
            "Teclado Mini",
            "R$ 189,90",
            "Acessórios",
            "Teclado compacto para produtividade e jogos, com visual minimalista e tamanho perfeito para mesas menores.",
            R.drawable.product_keyboard
        )
    }

    private fun bindProduct(
        viewId: Int,
        name: String,
        price: String,
        category: String,
        description: String,
        imageRes: Int
    ) {
        findViewById<View>(viewId).setOnClickListener {
            val intent = Intent(this, ProductActivity::class.java).apply {
                putExtra("name", name)
                putExtra("price", price)
                putExtra("category", category)
                putExtra("description", description)
                putExtra("imageRes", imageRes)
            }
            startActivity(intent)
        }
    }
}
