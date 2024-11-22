package com.example.globalsolution.control

import com.example.globalsolution.model.Cliente
import com.example.globalsolution.repository.ClienteRepository

class ClienteControl {
    private val repository = ClienteRepository()

    fun salvar(
        cliente: Cliente,
        onSucesso: () -> Unit,
        onFalha: () -> Unit
    ) {
        repository.salvarClienteNoFirebase(
            cliente,
            sucesso = {
                onSucesso()
            },
            falha = {
                onFalha()
            }
        )
    }
}