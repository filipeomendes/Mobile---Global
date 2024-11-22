package com.example.globalsolution.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.example.globalsolution.R
import com.example.globalsolution.adapter.ClienteAdapter
import com.example.globalsolution.repository.ClienteRepository
import com.example.globalsolution.model.Cliente
import com.google.android.material.bottomnavigation.BottomNavigationView

class ListaClienteActivity : Activity() {

    private lateinit var listaClientes: ListView
    private lateinit var btnCadastrarCliente: Button
    private lateinit var clienteRepository: ClienteRepository
    private lateinit var bottomNavigationView: BottomNavigationView
    private var clientesList: MutableList<Cliente> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_cliente)

        listaClientes = findViewById(R.id.listaClientes)
        btnCadastrarCliente = findViewById(R.id.btnCadastrarCliente)
        bottomNavigationView = findViewById(R.id.bottomNavigation)
        clienteRepository = ClienteRepository()

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

        carregarClientes()

        btnCadastrarCliente.setOnClickListener {
            val intent = Intent(this, CadastroClienteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun carregarClientes() {
        clienteRepository.listarClientes(
            sucesso = { clientes ->
                clientesList.clear()
                clientesList.addAll(clientes)
                listaClientes.adapter = ClienteAdapter(this, clientesList)
            },
            falha = {
                Toast.makeText(this, "Erro ao carregar clientes!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    fun excluirCliente(id: String) {
        clienteRepository.excluirCliente(
            id = id,
            sucesso = {
                Toast.makeText(this, "Cliente excluído com sucesso!", Toast.LENGTH_SHORT).show()
                carregarClientes()
            },
            falha = {
                Toast.makeText(this, "Erro ao excluir cliente!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    fun editarCliente(cliente: Cliente) {
        val intent = Intent(this, AlterarClienteActivity::class.java)
        intent.putExtra("cliente_id", cliente.id)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        carregarClientes()
    }
}