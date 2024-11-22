package com.example.globalsolution.control

import com.example.globalsolution.model.Produto
import com.example.globalsolution.repository.ProdutoRepository

class ProdutoControl {
    private val repository = ProdutoRepository()

    fun salvar(
        produto: Produto,
        onSucesso: () -> Unit,
        onFalha: () -> Unit
    ) {
        repository.salvarProdutoNoFirebase(
            produto,
            sucesso = {
                onSucesso()
            },
            falha = {
                onFalha()
            }
        )
    }
}