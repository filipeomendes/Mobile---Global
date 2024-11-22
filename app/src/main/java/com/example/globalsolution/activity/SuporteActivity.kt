package com.example.globalsolution.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.globalsolution.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class SuporteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suporte)

        val imageView: ImageView = findViewById(R.id.ivRelogio)
        val textView: TextView = findViewById(R.id.tvMensagem)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigation)

        textView.text = "Estamos fora do horário de atendimento, volte mais tarde! Pedimos desculpas pelo transtorno."

        val btnVoltar: Button = findViewById(R.id.btnVoltar)

        btnVoltar.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, MenuActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }
}