package com.example.senva

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.senva.LoginActivity.Global
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

class InicioFragmento : Fragment() {

    private lateinit var tvNombreSaludo: TextView
    private lateinit var viewPager: ViewPager2
    private lateinit var btnDrawerUser: ImageButton
    private lateinit var btnDrawerNotificacion: ImageButton
    private lateinit var tvBadgeNotificacion: TextView
    private lateinit var calendarView: View // Placeholder for the new calendar view
    private lateinit var tvMesAnio: TextView
    private lateinit var btnPrev: ImageButton
    private lateinit var btnNext: ImageButton
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var calendarAdapter: CalendarAdapter
    private var selectedDate: LocalDate = LocalDate.now()
    private var citasFechas: Set<LocalDate> = emptySet()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_inicio_fragmento, container, false)
        tvNombreSaludo = view.findViewById(R.id.tvnombresaludo)
        viewPager = view.findViewById(R.id.viewPagerCampanas)
        btnDrawerUser = view.findViewById(R.id.btnDrawerUser)
        btnDrawerNotificacion = view.findViewById(R.id.btnDrawerNotificacion)
        tvBadgeNotificacion = view.findViewById(R.id.tvBadgeNotificacion)
        calendarView = view.findViewById(R.id.calendarView) // Placeholder for the new calendar view
        tvMesAnio = view.findViewById(R.id.tvMesAnio)
        btnPrev = view.findViewById(R.id.btnPrev)
        btnNext = view.findViewById(R.id.btnNext)
        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView)
        setupCustomCalendar()

        cargarFotoUsuario()
        obtenerNombreDesdeFirestore()
        configurarNotificaciones()
        // Eliminar toda la lógica de calendarView y configuración de calendario
        actualizarBadgeNotificaciones()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listaCampanas = listOf(
            Campana(
                titulo = "Campaña",
                medica = "Medica",
                mensajeFinal = "Solo por este mes",
                imagenResId = R.drawable.mujeranuncio
            ),
            Campana(
                titulo = "Control General",
                medica = "Medicina General",
                mensajeFinal = "Sin costo adicional",
                imagenResId = R.drawable.sliderpag1
            )
        )
        val adapter = CampanaAdapter(listaCampanas) { campana ->
            println("Ver campaña: "+ campana.titulo)
        }
        viewPager.adapter = adapter
        // val btnVerHistorial = view.findViewById<Button>(R.id.btnVerHistorial)
        // btnVerHistorial.setOnClickListener {
        //     val intent = Intent(requireContext(), HistorialCitasActivity::class.java)
        //     startActivity(intent)
        // }

        // ABRIR DRAWER AL PRESIONAR EL ICONO DE USUARIO
        btnDrawerUser.setOnClickListener {
            val drawerLayout = activity?.findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.drawer_layout)
            drawerLayout?.openDrawer(android.view.Gravity.START)
        }
    }

    override fun onResume() {
        super.onResume()
        actualizarBadgeNotificaciones()
    }

    // Eliminar toda la lógica de calendarView y configuración de calendario

    // Eliminar la clase DayViewContainer

    // Función para resetear el flag de notificaciones vistas (llamar cuando se agrega una nueva cita)
    fun resetearNotificacionesVistas() {
        val sharedPreferences = requireActivity().getSharedPreferences(Global.preferencias_compartidas, AppCompatActivity.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("notificaciones_vistas", false).apply()
        actualizarBadgeNotificaciones()
    }

    private fun configurarNotificaciones() {
        btnDrawerNotificacion.setOnClickListener {
            // Marcar que se han visto las notificaciones
            val sharedPreferences = requireActivity().getSharedPreferences(Global.preferencias_compartidas, AppCompatActivity.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("notificaciones_vistas", true).apply()
            
            val intent = Intent(requireContext(), NotificacionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun actualizarBadgeNotificaciones() {
        try {
            val sharedPreferences = requireActivity().getSharedPreferences(Global.preferencias_compartidas, AppCompatActivity.MODE_PRIVATE)
            val notificacionesVistas = sharedPreferences.getBoolean("notificaciones_vistas", false)
            
            // Si ya se vieron las notificaciones, ocultar el badge
            if (notificacionesVistas) {
                tvBadgeNotificacion.visibility = View.GONE
                return
            }
            
            val db = DatabaseHelper(requireContext())
            val citas = db.obtenerTodasLasCitas()
            
            // Contar citas que están completas (tienen todos los datos) y no están completadas
            val citasPendientes = citas.count { cita ->
                cita.estado != "completada" && 
                cita.fechaHora.isNotBlank() && 
                cita.especialidad.isNotBlank() && 
                cita.usuario.isNotBlank() && 
                cita.doctor.isNotBlank()
            }
            
            if (citasPendientes > 0) {
                tvBadgeNotificacion.text = citasPendientes.toString()
                tvBadgeNotificacion.visibility = View.VISIBLE
            } else {
                tvBadgeNotificacion.visibility = View.GONE
            }
        } catch (e: Exception) {
            // En caso de error, ocultar el badge
            tvBadgeNotificacion.visibility = View.GONE
        }
    }

    private fun cargarFotoUsuario() {
        val sharedPreferences = requireActivity().getSharedPreferences(Global.preferencias_compartidas, AppCompatActivity.MODE_PRIVATE)
        val fotoPerfil = sharedPreferences.getString("FotoPerfil", null)
        if (!fotoPerfil.isNullOrEmpty()) {
            Glide.with(this)
                .load(fotoPerfil)
                .placeholder(R.drawable.usuario)
                .into(btnDrawerUser)
        } else {
            btnDrawerUser.setImageResource(R.drawable.usuario)
        }
    }

    private fun obtenerNombreDesdeFirestore() {
        val sharedPreferences = requireActivity().getSharedPreferences(Global.preferencias_compartidas, AppCompatActivity.MODE_PRIVATE)
        val correo = sharedPreferences.getString("Correo", null)

        if (correo != null) {
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("usuarios")
                .whereEqualTo("correo", correo)
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty) {
                        val nombre = result.documents[0].getString("primeronombre") ?: ""
                        tvNombreSaludo.text = nombre
                    } else {
                        tvNombreSaludo.text = "Nombre no encontrado"
                    }
                }
                .addOnFailureListener {
                    tvNombreSaludo.text = "Error al obtener nombre"
                }
        } else {
            tvNombreSaludo.text = "No se encontró el correo"
        }
    }

    private fun setupCustomCalendar() {
        val db = DatabaseHelper(requireContext())
        val citas = db.obtenerTodasLasCitas()
        val formatoEntrada = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        citasFechas = citas.mapNotNull {
            try {
                val date = formatoEntrada.parse(it.fechaHora)
                date?.let { d ->
                    val cal = Calendar.getInstance()
                    cal.time = d
                    LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))
                }
            } catch (e: Exception) { null }
        }.toSet()

        // LOGS DE DEPURACIÓN
        android.util.Log.d("CITAS", "Fechas de citas (raw): " + citas.map { it.fechaHora })
        android.util.Log.d("CITAS", "Fechas parseadas: $citasFechas")

        val yearMonth = YearMonth.now()
        val days = getDaysForMonth(yearMonth)
        calendarAdapter = CalendarAdapter(days, citasFechas, selectedDate) { day ->
            // Acción al seleccionar un día
            selectedDate = day
            calendarAdapter.selectedDate = day
            calendarAdapter.notifyDataSetChanged()
            // Aquí puedes mostrar detalles, abrir diálogo, etc.
        }
        calendarRecyclerView.layoutManager = GridLayoutManager(requireContext(), 7)
        calendarRecyclerView.adapter = calendarAdapter
    }

    private fun getDaysForMonth(yearMonth: YearMonth): List<LocalDate?> {
        val firstDayOfMonth = yearMonth.atDay(1)
        val lastDayOfMonth = yearMonth.atEndOfMonth()
        val days = mutableListOf<LocalDate?>()
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // Lunes=1, Domingo=7
        for (i in 1 until firstDayOfWeek) days.add(null) // Espacios vacíos al inicio
        var current = firstDayOfMonth
        while (!current.isAfter(lastDayOfMonth)) {
            days.add(current)
            current = current.plusDays(1)
        }
        return days
    }

    // Función para agregar una cita y refrescar el calendario
    fun agregarCitaYRefrescarCalendario(fechaHora: String) {
        // Guardar la cita en la base de datos (esto ya lo haces en tu flujo)
        // ...
        val formatoEntrada = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val date = formatoEntrada.parse(fechaHora)
        date?.let {
            val cal = Calendar.getInstance()
            cal.time = it
            val nuevaFecha = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))
            citasFechas = citasFechas + nuevaFecha
        }
        setupCustomCalendar()
    }

    // Adapter para el calendario mensual
    class CalendarAdapter(
        private val days: List<LocalDate?>,
        private val citasFechas: Set<LocalDate>,
        var selectedDate: LocalDate?,
        private val onDayClick: (LocalDate) -> Unit
    ) : RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
            return DayViewHolder(view)
        }
        override fun getItemCount() = days.size
        override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
            val day = days[position]
            holder.bind(day, citasFechas, selectedDate, onDayClick)
        }
        class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val textView: TextView = itemView.findViewById(R.id.tvDayNumber)
            private val marker: View = itemView.findViewById(R.id.viewCitaMarker)
            fun bind(day: LocalDate?, citasFechas: Set<LocalDate>, selectedDate: LocalDate?, onDayClick: (LocalDate) -> Unit) {
                if (day == null) {
                    textView.text = ""
                    marker.visibility = View.INVISIBLE
                    itemView.setBackgroundResource(android.R.color.transparent)
                    itemView.setOnClickListener(null)
                } else {
                    textView.text = day.dayOfMonth.toString()
                    marker.visibility = if (citasFechas.contains(day)) View.VISIBLE else View.INVISIBLE
                    val hoy = LocalDate.now()
                    val esPasado = day.isBefore(hoy)
                    itemView.setBackgroundResource(if (day == selectedDate) R.drawable.bg_cita_marcada else android.R.color.transparent)
                    itemView.isEnabled = !esPasado
                    textView.setTextColor(if (esPasado) 0xFFBBBBBB.toInt() else 0xFF222222.toInt())
                    itemView.alpha = if (esPasado) 0.4f else 1.0f
                    if (!esPasado) {
                        itemView.setOnClickListener { onDayClick(day) }
                    } else {
                        itemView.setOnClickListener(null)
                    }
                }
            }
        }
    }
}
