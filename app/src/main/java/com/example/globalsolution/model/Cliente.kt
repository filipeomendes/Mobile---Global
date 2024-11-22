package com.example.globalsolution.model

data class Cliente(
    var id: String? = null,
    var nome: String = "",
    var cnpj: String = "",
    var senha: String = "",
    var email: String = "",
    var telefone: String = "",
    var endereco: String = "",
    var cidade: String = "",
    var estado: String = "",
    var segmento: String = ""
)