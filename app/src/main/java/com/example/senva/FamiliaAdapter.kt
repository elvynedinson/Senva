package com.example.senva

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.senva.model.Familiar

class FamiliaAdapter(
    private val familiares: List<Familiar>,
    private val onEditar: (Familiar, Int) -> Unit,
    private val onEliminar: (Int) -> Unit
) : RecyclerView.Adapter<FamiliaAdapter.FamiliarViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamiliarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_familiar, parent, false)
        return FamiliarViewHolder(view)
    }
    override fun getItemCount() = familiares.size
    override fun onBindViewHolder(holder: FamiliarViewHolder, position: Int) {
        holder.bind(familiares[position], position)
    }
    inner class FamiliarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombreFamiliar)
        private val tvDni: TextView = itemView.findViewById(R.id.tvDniFamiliar)
        private val tvApodo: TextView = itemView.findViewById(R.id.tvApodoFamiliar)
        private val btnEditar: ImageButton = itemView.findViewById(R.id.btnEditarFamiliar)
        private val btnEliminar: ImageButton = itemView.findViewById(R.id.btnEliminarFamiliar)
        fun bind(familiar: Familiar, pos: Int) {
            val nombreCompleto = listOf(
                familiar.primerNombre,
                familiar.segundoNombre,
                familiar.primerApellido,
                familiar.segundoApellido
            ).filter { it.isNotBlank() }.joinToString(" ")
            tvNombre.text = nombreCompleto
            tvDni.text = "DNI: ${familiar.dni}"
            tvApodo.text = "Apodo: ${familiar.apodo}"
            btnEditar.setOnClickListener { onEditar(familiar, pos) }
            btnEliminar.setOnClickListener { onEliminar(pos) }
        }
    }
} 