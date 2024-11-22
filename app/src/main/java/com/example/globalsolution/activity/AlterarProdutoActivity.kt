package com.example.globalsolution.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.globalsolution.R
import com.example.globalsolution.repository.ProdutoRepository
import com.example.globalsolution.model.Produto
import com.google.android.material.bottomnavigation.BottomNavigationView

class AlterarProdutoActivity : AppCompatActivity() {
    private lateinit var etNomeProduto: EditText
    private lateinit var etElementoProduto: EditText
    private lateinit var etLitrosProduto: EditText
    private lateinit var etPrecoProduto: EditText
    private lateinit var btnAlterarProduto: Button
    private lateinit var bottomNavigationView: BottomNavigationView

    private var produtoId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alterar_produto)

        etNomeProduto = findViewById(R.id.etNomeProduto)
        etElementoProduto = findViewById(R.id.etElementoProduto)
        etLitrosProduto = findViewById(R.id.etLitrosProduto)
        etPrecoProduto = findViewById(R.id.etPrecoProduto)
        btnAlterarProduto = findViewById(R.id.btnAlterarProduto)
        bottomNavigationView = findViewById(R.id.bottomNavigation)

        produtoId = intent.getStringExtra("produto_id")

        if (produtoId == null) {
            Toast.makeText(this, "Erro: ID do produto não encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        ProdutoRepository().getProdutoById(
            produtoId!!,
            sucesso = { produto ->
                etNomeProduto.setText(produto.nome)
                etElementoProduto.setText(produto.elemento)
                etLitrosProduto.setText(produto.litros.toString())
                etPrecoProduto.setText(produto.preco.toString())
            },
            falha = {
                Toast.makeText(this, "Erro ao carregar produto", Toast.LENGTH_SHORT).show()
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

        btnAlterarProduto.setOnClickListener {
            if (validarCampos()) {
                val produtoAlterado = Produto(
                    id = produtoId!!,
                    nome = etNomeProduto.text.toString(),
                    elemento = etElementoProduto.text.toString(),
                    litros = etLitrosProduto.text.toString().toDouble(),
                    preco = etPrecoProduto.text.toString().toDouble()
                )

                ProdutoRepository().alterarProduto(
                    produtoId = produtoId!!,
                    produtoAlterado = produtoAlterado
                ) { sucesso ->
                    runOnUiThread {
                        if (sucesso) {
                            Toast.makeText(this, "Produto alterado com sucesso!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, "Erro ao alterar produto", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validarCampos(): Boolean {
        return etNomeProduto.text.isNotBlank() &&
                etElementoProduto.text.isNotBlank() &&
                etLitrosProduto.text.isNotBlank() &&
                etPrecoProduto.text.isNotBlank()
    }
}