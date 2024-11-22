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
import com.example.globalsolution.control.ProdutoControl
import com.example.globalsolution.model.Produto
import com.google.android.material.bottomnavigation.BottomNavigationView

class CadastroProdutoActivity : Activity() {
    private val control = ProdutoControl()

    private lateinit var etIdProduto: EditText
    private lateinit var etNome: EditText
    private lateinit var etElemento: EditText
    private lateinit var etLitros: EditText
    private lateinit var etPreco: EditText
    private lateinit var btnCadastrarProduto: Button
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_produto)

        etIdProduto = findViewById(R.id.etIdProduto)
        etNome = findViewById(R.id.etNomeProduto)
        etElemento = findViewById(R.id.etElemento)
        etLitros = findViewById(R.id.etLitros)
        etPreco = findViewById(R.id.etPreco)
        btnCadastrarProduto = findViewById(R.id.btnCadastrarProduto)
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

        btnCadastrarProduto.setOnClickListener {
            if (validarCampos()) {
                val produto = gerarProduto()
                control.salvar(
                    produto,
                    onSucesso = {
                        runOnUiThread {
                            Toast.makeText(
                                this@CadastroProdutoActivity,
                                "Produto cadastrado com sucesso",
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }
                    },
                    onFalha = {
                        runOnUiThread {
                            Toast.makeText(
                                this@CadastroProdutoActivity,
                                "Erro ao cadastrar produto",
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
        return etIdProduto.text.isNotBlank() &&
                etNome.text.isNotBlank() &&
                etElemento.text.isNotBlank() &&
                etLitros.text.isNotBlank() &&
                etPreco.text.isNotBlank()
    }

    private fun gerarProduto(): Produto {
        return Produto(
            id = etIdProduto.text.toString(),
            nome = etNome.text.toString(),
            elemento = etElemento.text.toString(),
            litros = etLitros.text.toString().toDouble(),
            preco = etPreco.text.toString().toDouble()
        )
    }

    private fun atualizarEstadoBotao() {
        val camposPreenchidos = etIdProduto.text.isNotBlank() &&
                etNome.text.isNotBlank() &&
                etElemento.text.isNotBlank() &&
                etLitros.text.isNotBlank() &&
                etPreco.text.isNotBlank()

        btnCadastrarProduto.isEnabled = camposPreenchidos

        if (camposPreenchidos) {
            btnCadastrarProduto.setBackgroundColor(resources.getColor(R.color.botao_ativo))
        } else {
            btnCadastrarProduto.setBackgroundColor(resources.getColor(R.color.botao_inativo))
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