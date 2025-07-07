package com.example.senva

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*
import android.widget.CalendarView

class SeleccionarDiaHoraDialogFragment : DialogFragment() {

    private var horaSeleccionada: String? = null
    private var diaSeleccionado: String? = null
    private lateinit var horasAdapter: HorasAdapter

    companion object {
        fun newInstance(nombreDoctor: String): SeleccionarDiaHoraDialogFragment {
            val fragment = SeleccionarDiaHoraDialogFragment()
            val args = Bundle()
            args.putString("NOMBRE_DOCTOR", nombreDoctor)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCancelable(true)
            setCanceledOnTouchOutside(true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_seleccionar_dia_hora, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvTitulo = view.findViewById<TextView>(R.id.tvTitulo)
        val calendarView = view.findViewById<CalendarView>(R.id.calendarView)
        val rvHoras = view.findViewById<RecyclerView>(R.id.rvHoras)
        val btnContinuar = view.findViewById<Button>(R.id.btnContinuar)

        tvTitulo.text = "Días disponibles"

        // Selección de fecha
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        diaSeleccionado = formatoFecha.format(Date(calendarView.date))
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val cal = Calendar.getInstance()
            cal.set(year, month, dayOfMonth)
            diaSeleccionado = formatoFecha.format(cal.time)
        }

        // Configurar RecyclerView para las horas
        rvHoras.layoutManager = GridLayoutManager(context, 3)
        horasAdapter = HorasAdapter { hora ->
            horaSeleccionada = hora
            horasAdapter.setHoraSeleccionada(hora)
        }
        rvHoras.adapter = horasAdapter

        // Configurar botón continuar
        btnContinuar.setOnClickListener {
            if (horaSeleccionada != null && diaSeleccionado != null) {
                // Navegar a ReservarCitaActivity
                val intent = android.content.Intent(context, ReservarCitaActivity::class.java)
                intent.putExtra("hora_seleccionada", horaSeleccionada)
                intent.putExtra("fecha_seleccionada", diaSeleccionado)

                // REENVIAR EL DISTRITO
                val distrito = activity?.intent?.getStringExtra("distrito") ?: ""
                intent.putExtra("distrito", distrito)

                // REENVIAR LA ESPECIALIDAD
                val especialidad = activity?.intent?.getStringExtra("ESPECIALIDAD_SELECCIONADA") ?: ""
                intent.putExtra("especialidad", especialidad)

                // REENVIAR EL NOMBRE DEL DOCTOR
                val nombreDoctor = arguments?.getString("NOMBRE_DOCTOR") ?: ""
                intent.putExtra("doctor", nombreDoctor)

                startActivity(intent)
                dismiss()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.90).toInt(), // 90% del ancho de pantalla
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private inner class HorasAdapter(
        private val onHoraClick: (String) -> Unit
    ) : RecyclerView.Adapter<HorasAdapter.HoraViewHolder>() {

        private val horas = listOf(
            "08:00", "09:00", "10:00", "11:00", "12:00", "13:00",
            "14:00", "15:00", "16:00", "17:00", "18:00", "19:00"
        )
        private var horaSeleccionadaAdapter: String? = null

        fun setHoraSeleccionada(hora: String) {
            horaSeleccionadaAdapter = hora
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoraViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_hora, parent, false)
            return HoraViewHolder(view)
        }

        override fun onBindViewHolder(holder: HoraViewHolder, position: Int) {
            holder.bind(horas[position])
        }

        override fun getItemCount() = horas.size

        inner class HoraViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val btnHora: Button = itemView.findViewById(R.id.btnHora)

            fun bind(hora: String) {
                btnHora.text = hora
                val seleccionado = (hora == horaSeleccionadaAdapter)
                btnHora.isSelected = seleccionado
                btnHora.isActivated = seleccionado
                btnHora.isPressed = seleccionado
                btnHora.setOnClickListener {
                    onHoraClick(hora)
                }
            }
        }
    }
} 