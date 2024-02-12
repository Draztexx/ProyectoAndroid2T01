package com.example.proyectoandroid2t01

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListAdapter (private var mData:List<ListElement>,private val context: Context): RecyclerView.Adapter<ListAdapter.ViewHolder>(){

    var itemClickListener: ListItemClickListener? = null
    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_element, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(mData[position])
        holder.editButton.setOnClickListener {
            itemClickListener?.onEditButtonClick(position,mData[position].idtareas)
        }

        holder.deleteButton.setOnClickListener {
            itemClickListener?.onDeleteButtonClick(position,mData[position].idtareas)
        }
    }

    fun setItems(items: List<ListElement>) {
        mData = items
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombre: TextView = itemView.findViewById(R.id.nombreTextView)
        private val fecha: TextView = itemView.findViewById(R.id.fechaTextView)
        private val prioridad: TextView = itemView.findViewById(R.id.prioridadTextView)
        private val estado: TextView = itemView.findViewById(R.id.estadoTextView)
        private val descripcion: TextView = itemView.findViewById(R.id.descripcion)
        val editButton: Button = itemView.findViewById(R.id.editarbutton)
        val deleteButton: Button = itemView.findViewById(R.id.eliminarbutton)

        fun bindData(item: ListElement) {
            nombre.text = item.nombre
            fecha.text = item.fecha.toString()
            prioridad.text = item.prioridad
            estado.text = item.estado
            descripcion.text = item.descripcion

        }
    }

    interface ListItemClickListener {
        fun onEditButtonClick(position: Int,idtareas: Int)
        fun onDeleteButtonClick(position: Int,idtareas: Int)
    }


}