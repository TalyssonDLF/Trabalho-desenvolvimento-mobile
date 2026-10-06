package com.example.lojavisual

data class Produto(
    val id: Int,
    val nome: String,
    val precoCentavos: Int,
    val categoria: String,
    val descricao: String,
    val imageRes: Int
)

data class Cupom(
    val id: Int,
    val codigo: String,
    val percentual: Int,
    val descricao: String,
    val ativo: Boolean = true
)

data class CarrinhoItem(
    val produtoId: Int,
    val nome: String,
    val precoUnitarioCentavos: Int,
    val quantidade: Int,
    val imageRes: Int
) {
    val subtotalCentavos: Int
        get() = precoUnitarioCentavos * quantidade
}
