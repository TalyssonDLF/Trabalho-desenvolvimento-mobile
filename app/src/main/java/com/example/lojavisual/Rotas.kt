package com.example.lojavisual

object Rotas {
    const val PRODUTOS = "produtos"
    const val CUPONS = "cupons"
    const val CARRINHO = "carrinho"
    const val SOBRE = "sobre"
    const val PAGAMENTO = "pagamento"

    const val PRODUTO_DETALHE = "produto/{produtoId}"
    const val CUPOM_DETALHE = "cupom/{cupomId}"

    fun produtoDetalhe(produtoId: Int) = "produto/$produtoId"
    fun cupomDetalhe(cupomId: Int) = "cupom/$cupomId"
}
