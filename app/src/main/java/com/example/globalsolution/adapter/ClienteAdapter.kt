package com.example.globalsolution.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.globalsolution.R
import com.example.globalsolution.activity.ListaClienteActivity
import com.example.globalsolution.model.Cliente

class ClienteAdapter(
    private val context: Context,
    private val listaClientes: List<Cliente>
) : android.widget.BaseAdapter() {

    override fun getCount(): Int {
        return listaClientes.size
    }

    override fun getItem(position: Int): Any {
        return listaClientes[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val cliente = listaClientes[position]
        val view = LayoutInflater.from(context).inflate(R.layout.item_cliente, parent, false)

        val nomeCliente: TextView = view.findViewById(R.id.nomeCliente)
        val cnpjCliente: TextView = view.findViewById(R.id.cnpjCliente)
        val emailCliente: TextView = view.findViewById(R.id.emailCliente)
        val telefoneCliente: TextView = view.findViewById(R.id.telefoneCliente)
        val enderecoCliente: TextView = view.findViewById(R.id.enderecoCliente)
        val cidadeCliente: TextView = view.findViewById(R.id.cidadeCliente)
        val estadoCliente: TextView = view.findViewById(R.id.estadoCliente)
        val segmentoCliente: TextView = view.findViewById(R.id.segmentoCliente)
        val editarButton: ImageView = view.findViewById(R.id.editarButton)
        val excluirButton: ImageView = view.findViewById(R.id.excluirButton)

        nomeCliente.text = "${cliente.nome}"
        cnpjCliente.text = "CNPJ: ${cliente.cnpj}"
        emailCliente.text = "Email: ${cliente.email}"
        telefoneCliente.text = "Telefone: ${cliente.telefone}"
        enderecoCliente.text = "Endereço: ${cliente.endereco}"
        cidadeCliente.text = "Cidade: ${cliente.cidade}"
        estadoCliente.text = "Estado: ${cliente.estado}"
        segmentoCliente.text = "Segmento: ${cliente.segmento}"

        editarButton.setOnClickListener {
            (context as ListaClienteActivity).editarCliente(cliente)
        }

        excluirButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Excluir Cliente")
                .setMessage("Tem certeza que deseja excluir este cliente?")
                .setPositiveButton("Sim") { _, _ ->
                    val clienteId = cliente.id
                    if (clienteId != null) {
                        (context as ListaClienteActivity).excluirCliente(clienteId)
                    } else {
                        Toast.makeText(context, "ID do cliente não encontrado", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Não", null)
                .show()
        }

        return view
    }
}