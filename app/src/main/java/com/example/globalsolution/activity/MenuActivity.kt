package com.example.globalsolution.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.globalsolution.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu)

        val cardCadastrarProdutos: CardView = findViewById(R.id.cardCadastrarProdutos)
        val cardProdutos: CardView = findViewById(R.id.cardProdutos)
        val cardCadastroClientes: CardView = findViewById(R.id.cardCadastro)
        val cardClientesCadastrados: CardView = findViewById(R.id.cardClientesCadastrados)
        val ivLogout: ImageView = findViewById(R.id.ivLogout)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigation)

        cardCadastrarProdutos.setOnClickListener {
            val intent = Intent(this, CadastroProdutoActivity::class.java)
            startActivity(intent)
        }

        cardProdutos.setOnClickListener {
            val intent = Intent(this, ListaProdutoActivity::class.java)
            startActivity(intent)
        }

        cardCadastroClientes.setOnClickListener {
            val intent = Intent(this, CadastroClienteActivity::class.java)
            startActivity(intent)
        }

        cardClientesCadastrados.setOnClickListener {
            val intent = Intent(this, ListaClienteActivity::class.java)
            startActivity(intent)
        }

        ivLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_suporte -> {
                    val intent = Intent(this, SuporteActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }
}