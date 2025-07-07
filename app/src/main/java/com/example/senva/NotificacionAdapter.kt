package com.example.senva

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

sealed class NotificacionItem {
    data class FechaHeader(val fecha: String) : NotificacionItem()
    data class CitaItem(val cita: Cita) : NotificacionItem()
}

class NotificacionAdapter(
    citas: List<Cita>,
    private val onCitaClick: (Cita) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items: List<NotificacionItem>

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_CITA = 1
    }

    init {
        // Filtrar solo citas que no están completadas (pendientes o canceladas)
        val citasFiltradas = citas.filter { 
            it.fechaHora.isNotBlank() && 
            it.especialidad.isNotBlank() && 
            it.estado != "completada"
        }
        items = agruparPorFecha(citasFiltradas)
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is NotificacionItem.FechaHeader -> TYPE_HEADER
            is NotificacionItem.CitaItem -> TYPE_CITA
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notificacion, parent, false)
            CitaViewHolder(view, onCitaClick)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is NotificacionItem.FechaHeader -> {
                (holder as HeaderViewHolder).bind(item.fecha)
            }
            is NotificacionItem.CitaItem -> {
                (holder as CitaViewHolder).bind(item.cita)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvFechaHeader: TextView = itemView.findViewById(android.R.id.text1)
        fun bind(fecha: String) {
            tvFechaHeader.text = fecha
            tvFechaHeader.setTextColor(0xFF333333.toInt())
            tvFechaHeader.textSize = 16f
            tvFechaHeader.setPadding(16, 24, 16, 8)
        }
    }

    class CitaViewHolder(itemView: View, val onCitaClick: (Cita) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val tvEspecialidad: TextView = itemView.findViewById(R.id.tvEspecialidadNoti)
        private val tvFecha: TextView = itemView.findViewById(R.id.tvFechaNoti)
        private val tvHora: TextView = itemView.findViewById(R.id.tvHoraNoti)
        private var currentCita: Cita? = null
        init {
            itemView.setOnClickListener {
                currentCita?.let { onCitaClick(it) }
            }
        }
        fun bind(cita: Cita) {
            currentCita = cita
            tvEspecialidad.text = cita.especialidad
            val partes = cita.fechaHora.split(" ")
            if (partes.size >= 2) {
                tvFecha.text = partes[0]
                tvHora.text = partes.drop(1).joinToString(" ")
            } else {
                tvFecha.text = cita.fechaHora
                tvHora.text = ""
            }
        }
    }

    private fun agruparPorFecha(citas: List<Cita>): List<NotificacionItem> {
        val items = mutableListOf<NotificacionItem>()
        val formatoEntrada = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formatoSalida = SimpleDateFormat("EEEE dd MMM, yyyy", Locale("es"))
        var fechaActual: String? = null
        for (cita in citas) {
            val fechaCreacion = try {
                formatoEntrada.parse(cita.fechaCreacion)
            } catch (e: Exception) {
                null
            }
            val fechaTexto = fechaCreacion?.let { formatoSalida.format(it).replaceFirstChar { c -> c.uppercase() } } ?: cita.fechaCreacion
            if (fechaTexto != fechaActual) {
                items.add(NotificacionItem.FechaHeader(fechaTexto))
                fechaActual = fechaTexto
            }
            items.add(NotificacionItem.CitaItem(cita))
        }
        return items
    }
} 