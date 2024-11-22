package com.example.globalsolution.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import com.example.globalsolution.R
import com.example.globalsolution.adapter.ProdutoAdapter
import com.example.globalsolution.repository.ProdutoRepository
import com.example.globalsolution.model.Produto
import com.google.android.material.bottomnavigation.BottomNavigationView

class ListaProdutoActivity : Activity() {

    private lateinit var listaProdutos: ListView
    private lateinit var btnCadastrarProduto: Button
    private lateinit var produtoRepository: ProdutoRepository
    private lateinit var bottomNavigationView: BottomNavigationView
    private var produtosList: MutableList<Produto> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_produto)

        listaProdutos = findViewById(R.id.listaProdutos)
        btnCadastrarProduto = findViewById(R.id.btnCadastrarProduto)
        bottomNavigationView = findViewById(R.id.bottomNavigation)
        produtoRepository = ProdutoRepository()

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

        carregarProdutos()

        btnCadastrarProduto.setOnClickListener {
            val intent = Intent(this, CadastroProdutoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun carregarProdutos() {
        produtoRepository.listarProdutos(
            sucesso = { produtos ->
                produtosList.clear()
                produtosList.addAll(produtos)
                listaProdutos.adapter = ProdutoAdapter(this, produtosList)
            },
            falha = {
                Toast.makeText(this, "Erro ao carregar produtos!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    fun excluirProduto(id: String) {
        produtoRepository.excluirProduto(
            id = id,
            sucesso = {
                Toast.makeText(this, "Produto excluído com sucesso!", Toast.LENGTH_SHORT).show()
                carregarProdutos()
            },
            falha = {
                Toast.makeText(this, "Erro ao excluir produto!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    fun editarProduto(produto: Produto) {
        val intent = Intent(this, AlterarProdutoActivity::class.java)
        intent.putExtra("produto_id", produto.id)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        carregarProdutos()
    }
}