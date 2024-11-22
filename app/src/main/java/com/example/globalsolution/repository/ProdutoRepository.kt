package com.example.globalsolution.repository

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.globalsolution.model.Produto
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class ProdutoRepository {

    private val httpClient = OkHttpClient()
    private val gson = Gson()
    private val URL_BASE = "https://globalsolution-458b6-default-rtdb.firebaseio.com/produtos"

    fun salvarProdutoNoFirebase(
        produto: Produto,
        sucesso: () -> Unit,
        falha: () -> Unit
    ) {
        val produtoJson = gson.toJson(produto)
        val request = Request.Builder()
            .url("$URL_BASE.json")
            .post(
                produtoJson.toRequestBody("application/json".toMediaType())
            )
            .build()

        val callback = object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    Log.i("PRODUTO", "Produto salvo com sucesso")
                    sucesso()
                } else {
                    Log.e("PRODUTO", "Erro ao salvar produto: ${response.message}")
                    falha()
                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("PRODUTO", "Erro ao salvar produto: ${e.message}", e)
                falha()
            }
        }

        httpClient.newCall(request).enqueue(callback)
    }

    fun listarProdutos(
        sucesso: (List<Produto>) -> Unit,
        falha: () -> Unit
    ) {
        val request = Request.Builder()
            .url("$URL_BASE.json")
            .get()
            .build()

        val callback = object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val produtosMap = gson.fromJson(responseBody, Map::class.java) as? Map<String, Any>

                    val produtos = produtosMap?.map { (id, value) ->
                        val produtoJson = gson.toJson(value)
                        val produto = gson.fromJson(produtoJson, Produto::class.java)

                        Produto(
                            id = id,
                            nome = produto.nome,
                            elemento = produto.elemento,
                            litros = produto.litros,
                            preco = produto.preco
                        )
                    } ?: emptyList()

                    Handler(Looper.getMainLooper()).post {
                        sucesso(produtos)
                    }
                } else {
                    Log.e("PRODUTO", "Erro ao listar produtos: ${response.message}")
                    falha()
                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("PRODUTO", "Erro ao listar produtos: ${e.message}", e)
                falha()
            }
        }

        httpClient.newCall(request).enqueue(callback)
    }

    fun excluirProduto(
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
                        sucesso()
                    } else {
                        Log.e("PRODUTO", "Erro ao excluir: ${response.code}")
                        falha()
                    }
                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Handler(Looper.getMainLooper()).post {
                    Log.e("PRODUTO", "Falha na requisição: ${e.message}")
                    falha()
                }
            }
        })
    }

    fun alterarProduto(
        produtoId: String,
        produtoAlterado: Produto,
        sucesso: (Boolean) -> Unit
    ) {
        val produtoJson = gson.toJson(produtoAlterado)
        val request = Request.Builder()
            .url("$URL_BASE/$produtoId.json")
            .put(
                produtoJson.toRequestBody("application/json".toMediaType())
            )
            .build()

        val callback = object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    Log.i("PRODUTO", "Produto alterado com sucesso: ${response.body?.string()}")
                    sucesso(true)
                } else {
                    Log.e("PRODUTO", "Erro ao alterar produto: ${response.message}, Código: ${response.code}")
                    sucesso(false)
                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("PRODUTO", "Erro ao alterar produto: ${e.message}", e)
                sucesso(false)
            }
        }

        httpClient.newCall(request).enqueue(callback)
    }


    fun getProdutoById(
        id: String,
        sucesso: (Produto) -> Unit,
        falha: () -> Unit
    ) {
        val request = Request.Builder()
            .url("$URL_BASE/$id.json")
            .get()
            .build()

        val callback = object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val produto = gson.fromJson(responseBody, Produto::class.java)
                    Handler(Looper.getMainLooper()).post {
                        sucesso(produto)
                    }
                } else {
                    Log.e("PRODUTO", "Erro ao buscar produto: ${response.message}")
                    falha()
                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("PRODUTO", "Erro ao buscar produto: ${e.message}", e)
                falha()
            }
        }

        httpClient.newCall(request).enqueue(callback)
    }
}