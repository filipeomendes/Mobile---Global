package com.example.globalsolution.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.globalsolution.R
import com.example.globalsolution.control.ClienteControl
import com.example.globalsolution.model.Cliente
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.UUID

class CadastroClienteActivity : Activity() {
    private val control = ClienteControl()

    private lateinit var etNome: EditText
    private lateinit var etCnpj: EditText
    private lateinit var etEmail: EditText
    private lateinit var etTelefone: EditText
    private lateinit var etEndereco: EditText
    private lateinit var etCidade: EditText
    private lateinit var etEstado: EditText
    private lateinit var etSegmento: EditText
    private lateinit var btnCadastrarCliente: Button
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_cliente)

        etNome = findViewById(R.id.etNome)
        etCnpj = findViewById(R.id.etCNPJ)
        etEmail = findViewById(R.id.etEmail)
        etTelefone = findViewById(R.id.etTelefone)
        etEndereco = findViewById(R.id.etEndereco)
        etCidade = findViewById(R.id.etCidade)
        etEstado = findViewById(R.id.etEstado)
        etSegmento = findViewById(R.id.etSegmento)
        btnCadastrarCliente = findViewById(R.id.btnCadastrarCliente)
        bottomNavigationView = findViewById(R.id.bottomNavigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MenuActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_suporte -> {
                    val intent = Intent(this, SuporteActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        btnCadastrarCliente.setOnClickListener {
            if (validarCampos()) {
                val cliente = gerarCliente()
                control.salvar(
                    cliente,
                    onSucesso = {
                        runOnUiThread {
                            Toast.makeText(
                                this@CadastroClienteActivity,
                                "Cliente cadastrado com sucesso",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }
                    },
                    onFalha = {
                        runOnUiThread {
                            Toast.makeText(
                                this@CadastroClienteActivity,
                                "Erro ao cadastrar cliente",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                )
            } else {
                Toast.makeText(
                    this,
                    "Preencha todos os campos antes de cadastrar",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun validarCampos(): Boolean {
        return etNome.text.isNotBlank() &&
                etCnpj.text.isNotBlank() &&
                etEmail.text.isNotBlank() &&
                etTelefone.text.isNotBlank() &&
                etEndereco.text.isNotBlank() &&
                etCidade.text.isNotBlank() &&
                etEstado.text.isNotBlank() &&
                etSegmento.text.isNotBlank()
    }

    private fun gerarCliente(): Cliente {

        val idCliente = UUID.randomUUID().toString()

        return Cliente(
            id = idCliente,
            nome = etNome.text.toString(),
            cnpj = etCnpj.text.toString(),
            email = etEmail.text.toString(),
            telefone = etTelefone.text.toString(),
            endereco = etEndereco.text.toString(),
            cidade = etCidade.text.toString(),
            estado = etEstado.text.toString(),
            segmento = etSegmento.text.toString()
        )
    }

    private fun atualizarEstadoBotao() {
        val camposPreenchidos = etNome.text.isNotBlank() &&
                etCnpj.text.isNotBlank() &&
                etEmail.text.isNotBlank() &&
                etTelefone.text.isNotBlank() &&
                etEndereco.text.isNotBlank() &&
                etCidade.text.isNotBlank() &&
                etEstado.text.isNotBlank() &&
                etSegmento.text.isNotBlank()

        btnCadastrarCliente.isEnabled = camposPreenchidos

        if (camposPreenchidos) {
            btnCadastrarCliente.setBackgroundColor(resources.getColor(R.color.botao_ativo))
        } else {
            btnCadastrarCliente.setBackgroundColor(resources.getColor(R.color.botao_inativo))
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            atualizarEstadoBotao()
        }

        override fun afterTextChanged(s: Editable?) {}
    }
}