package com.example.globalsolution.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.globalsolution.R
import com.example.globalsolution.activity.ListaProdutoActivity
import com.example.globalsolution.model.Produto

class ProdutoAdapter(
    private val context: Context,
    private val listaProdutos: List<Produto>
) : android.widget.BaseAdapter() {

    override fun getCount(): Int {
        return listaProdutos.size
    }

    override fun getItem(position: Int): Any {
        return listaProdutos[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val produto = listaProdutos[position]
        val view = LayoutInflater.from(context).inflate(R.layout.item_produto, parent, false)

        val nomeProduto: TextView = view.findViewById(R.id.nomeProduto)
        val elementoProduto: TextView = view.findViewById(R.id.elementoProduto)
        val litrosProduto: TextView = view.findViewById(R.id.litrosProduto)
        val precoProduto: TextView = view.findViewById(R.id.precoProduto)
        val editarButton: ImageView = view.findViewById(R.id.editarButton)
        val excluirButton: ImageView = view.findViewById(R.id.excluirButton)

        nomeProduto.text = produto.nome
        elementoProduto.text = "Elemento Químico: ${produto.elemento}"
        litrosProduto.text = "Litros: ${produto.litros}"
        precoProduto.text = "R$ ${produto.preco}"

        editarButton.setOnClickListener {
            (context as ListaProdutoActivity).editarProduto(produto)
        }

        excluirButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Excluir Produto")
                .setMessage("Tem certeza que deseja excluir este produto?")
                .setPositiveButton("Sim") { _, _ ->
                    (context as ListaProdutoActivity).excluirProduto(produto.id)
                }
                .setNegativeButton("Não", null)
                .show()
        }

        return view
    }
}