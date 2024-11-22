package com.example.globalsolution.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.globalsolution.R
import com.example.globalsolution.repository.ClienteRepository
import com.example.globalsolution.model.Cliente
import com.google.android.material.bottomnavigation.BottomNavigationView

class AlterarClienteActivity : AppCompatActivity() {
    private lateinit var etNome: EditText
    private lateinit var etCnpj: EditText
    private lateinit var etEmail: EditText
    private lateinit var etTelefone: EditText
    private lateinit var etEndereco: EditText
    private lateinit var etCidade: EditText
    private lateinit var etEstado: EditText
    private lateinit var etSegmento: EditText
    private lateinit var btnAlterarCliente: Button
    private lateinit var bottomNavigationView: BottomNavigationView

    private var clienteId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alterar_cliente)

        etNome = findViewById(R.id.etNome)
        etCnpj = findViewById(R.id.etCNPJ)
        etEmail = findViewById(R.id.etEmail)
        etTelefone = findViewById(R.id.etTelefone)
        etEndereco = findViewById(R.id.etEndereco)
        etCidade = findViewById(R.id.etCidade)
        etEstado = findViewById(R.id.etEstado)
        etSegmento = findViewById(R.id.etSegmento)
        btnAlterarCliente = findViewById(R.id.btnAlterarCliente)
        bottomNavigationView = findViewById(R.id.bottomNavigation)

        clienteId = intent.getStringExtra("cliente_id")

        if (clienteId == null) {
            Toast.makeText(this, "Erro: ID do cliente não encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        ClienteRepository().getClienteById(
            clienteId!!,
            sucesso = { cliente ->
                etNome.setText(cliente.nome)
                etCnpj.setText(cliente.cnpj)
                etEmail.setText(cliente.email)
                etTelefone.setText(cliente.telefone)
                etEndereco.setText(cliente.endereco)
                etCidade.setText(cliente.cidade)
                etEstado.setText(cliente.estado)
                etSegmento.setText(cliente.segmento)
            },
            falha = {
                Toast.makeText(this, "Erro ao carregar cliente", Toast.LENGTH_SHORT).show()
                finish()
            }
        )

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

        btnAlterarCliente.setOnClickListener {
            if (validarCampos()) {
                val clienteAlterado = Cliente(
                    id = clienteId!!,
                    nome = etNome.text.toString(),
                    cnpj = etCnpj.text.toString(),
                    email = etEmail.text.toString(),
                    telefone = etTelefone.text.toString(),
                    endereco = etEndereco.text.toString(),
                    cidade = etCidade.text.toString(),
                    estado = etEstado.text.toString(),
                    segmento = etSegmento.text.toString()
                )

                ClienteRepository().alterarCliente(
                    clienteId = clienteId!!,
                    clienteAlterado = clienteAlterado
                ) { sucesso ->
                    runOnUiThread {
                        if (sucesso) {
                            Toast.makeText(this, "Cliente alterado com sucesso!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, "Erro ao alterar cliente", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show()
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
}