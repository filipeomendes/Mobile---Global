package com.example.globalsolution.repository

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.globalsolution.model.Cliente
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class ClienteRepository {

    private val httpClient = OkHttpClient()
    private val gson = Gson()
    private val URL_BASE = "https://globalsolution-458b6-default-rtdb.firebaseio.com/clientes"

    fun salvarClienteNoFirebase(
        cliente: Cliente,
        sucesso: (String) -> Unit,
        falha: () -> Unit
    ) {
        val clienteJson = gson.toJson(cliente)
        val request = Request.Builder()
            .url("$URL_BASE.json")
            .post(
                clienteJson.toRequestBody("application/json".toMediaType())
            )
            .build()

        httpClient.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val idGerado = gson.fromJson(responseBody, Map::class.java)["name"] as? String
                    if (idGerado != null) {
                        Log.i("CLIENTE", "Cliente salvo com ID: $idGerado")
                        Handler(Looper.getMainLooper()).post {
                            sucesso(idGerado)
                        }
                    } else {
                        Log.e("CLIENTE", "Erro ao obter ID gerado")
                        falha()
                    }
                } else {
                    Log.e("CLIENTE", "Erro ao salvar cliente: ${response.message}")
                    falha()
                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("CLIENTE", "Erro ao salvar cliente: ${e.message}", e)
                falha()
            }
        })
    }

    fun listarClientes(
        sucesso: (List<Cliente>) -> Unit,
        falha: () -> Unit
    ) {
        val request = Request.Builder()
            .url("$URL_BASE.json")
            .get()
            .build()

        httpClient.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val clientesMap = gson.fromJson(responseBody, Map::class.java) as? Map<String, Any>

                    val clientes = clientesMap?.map { (id, value) ->
                        val clienteJson = gson.toJson(value)
                        val cliente = gson.fromJson(clienteJson, Cliente::class.java)
                        cliente.id = id
                        cliente
                    } ?: emptyList()

                    Handler(Looper.getMainLooper()).post {
                        sucesso(clientes)
                    }
                } else {
                    Log.e("CLIENTE", "Erro ao listar clientes: ${response.message}")
                    falha()
                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("CLIENTE", "Erro ao listar clientes: ${e.message}", e)
                falha()
            }
        })
    }

    fun excluirCliente(
        id: String,
        sucesso: () -> Unit,
        falha: () -> Unit
    ) {
        val request = Request.Builder()
            .url("$URL_BASE/$id.json")
            .delete()
            .build()

        httpClient.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                Handler(Looper.getMainLooper()).post {
                    if (response.isSuccessful) {
                        Log.i("CLIENTE", "Cliente excluído com sucesso")
                        sucesso()
                    } else {
                        Log.e("CLIENTE", "Erro ao excluir cliente: ${response.code}")
                        falha()
                    }
                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Handler(Looper.getMainLooper()).post {
                    Log.e("CLIENTE", "Falha na requisição: ${e.message}")
                    falha()
                }
            }
        })
    }

    fun alterarCliente(
        clienteId: String,
        clienteAlterado: Cliente,
        sucesso: (Boolean) -> Unit
    ) {
        val clienteJson = gson.toJson(clienteAlterado)
        val request = Request.Builder()
            .url("$URL_BASE/$clienteId.json")
            .put(
                clienteJson.toRequestBody("application/json".toMediaType())
            )
            .build()

        httpClient.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    Log.i("CLIENTE", "Cliente alterado com sucesso")
                    sucesso(true)
                } else {
                    Log.e("CLIENTE", "Erro ao alterar cliente: ${response.message}")
                    sucesso(false)
                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("CLIENTE", "Erro ao alterar cliente: ${e.message}", e)
                sucesso(false)
            }
        })
    }

    fun getClienteById(
        id: String,
        sucesso: (Cliente) -> Unit,
        falha: () -> Unit
    ) {
        val request = Request.Builder()
            .url("$URL_BASE/$id.json")
            .get()
            .build()

        httpClient.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val cliente = gson.fromJson(responseBody, Cliente::class.java)
                    cliente.id = id
                    Handler(Looper.getMainLooper()).post {
                        sucesso(cliente)
                    }
                } else {
                    Log.e("CLIENTE", "Erro ao buscar cliente: ${response.message}")
                    falha()
                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("CLIENTE", "Erro ao buscar cliente: ${e.message}", e)
                falha()
            }
        })
    }
}