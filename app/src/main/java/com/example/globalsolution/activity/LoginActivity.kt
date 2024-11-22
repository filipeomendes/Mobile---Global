package com.example.globalsolution.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.globalsolution.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsuario: EditText
    private lateinit var etSenha: EditText
    private lateinit var btnEntrar: Button
    private lateinit var tvMensagemErro: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        if (auth.currentUser != null) {
            irParaMenu()
        }

        etUsuario = findViewById(R.id.etUsuario)
        etSenha = findViewById(R.id.etSenha)
        btnEntrar = findViewById(R.id.btnEntrar)
        tvMensagemErro = findViewById(R.id.tvMensagemErro)

        btnEntrar.setOnClickListener {
            validarLogin()
        }
    }

    private fun validarLogin() {
        val email = etUsuario.text.toString().trim()
        val senha = etSenha.text.toString().trim()

        when {
            email.isEmpty() && senha.isEmpty() -> {
                exibirMensagemErro("Por favor, preencha email e senha")
            }
            email.isEmpty() -> {
                exibirMensagemErro("Por favor, preencha o email")
            }
            senha.isEmpty() -> {
                exibirMensagemErro("Por favor, preencha a senha")
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                exibirMensagemErro("Digite um email válido")
            }
            senha.length < 6 -> {
                exibirMensagemErro("A senha deve ter no mínimo 6 caracteres")
            }
            else -> {
                realizarLogin(email, senha)
            }
        }
    }

    private fun realizarLogin(email: String, senha: String) {
        btnEntrar.isEnabled = false

        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    tvMensagemErro.visibility = View.GONE
                    irParaMenu()
                } else {
                    val mensagemErro = when (task.exception) {
                        is FirebaseAuthInvalidUserException -> {
                            "Não existe conta com este email"
                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            "Email ou senha incorretos"
                        }
                        else -> {
                            when (task.exception?.message) {
                                "The email address is badly formatted." ->
                                    "Formato de email inválido"
                                "A network error (such as timeout, interrupted connection or unreachable host) has occurred." ->
                                    "Erro de conexão. Verifique sua internet"
                                "The email address is already in use by another account." ->
                                    "Este email já está sendo usado"
                                "We have blocked all requests from this device due to unusual activity. Try again later." ->
                                    "Muitas tentativas de login. Tente novamente mais tarde"
                                "Access to this account has been temporarily disabled due to many failed login attempts." ->
                                    "Conta temporariamente bloqueada devido a muitas tentativas de login"
                                else -> "Ocorreu um erro ao fazer login. Tente novamente"
                            }
                        }
                    }
                    exibirMensagemErro(mensagemErro)
                }
                btnEntrar.isEnabled = true
            }
    }

    private fun irParaMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun exibirMensagemErro(mensagem: String) {
        tvMensagemErro.text = mensagem
        tvMensagemErro.visibility = View.VISIBLE
    }
}