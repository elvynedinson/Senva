package com.example.senva

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.ImageView
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.senva.model.Familiar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.content.Context
import com.example.senva.FamiliaUtils

class ReservarCitaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservarcita)

        val spinner = findViewById<Spinner>(R.id.spinnerQuienAtiende)
        val btnReservarMas = findViewById<ImageView>(R.id.reservarmas)
        val etDni = findViewById<EditText>(R.id.etDni)
        val etNombres = findViewById<EditText>(R.id.etNombres)
        val opciones = mutableListOf("Seleccione", "Yo")
        val familiares = FamiliaUtils.cargarFamiliares(this)
        val familiaresData = mutableMapOf<String, Familiar>()
        // Datos fijos
        // familiaresData["Hija"] = Pair("12345678", "Ana María")
        // familiaresData["Papá"] = Pair("87654321", "Juan Carlos")
        for (familiar in familiares) {
            opciones.add(familiar.apodo)
            familiaresData[familiar.apodo] = familiar
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opciones)
        spinner.adapter = adapter

        var yoDni: String? = null
        var yoNombres: String? = null
        // Obtener datos de usuario una sola vez
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("usuarios")
                .whereEqualTo("correo", user.email)
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty) {
                        val doc = result.documents[0]
                        yoDni = doc.getString("dni") ?: ""
                        yoNombres = (doc.getString("primeronombre") ?: "") + " " + (doc.getString("segundonombre") ?: "")
                    }
                }
        }

        // Listener para el spinner
        spinner.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>, view: android.view.View?, position: Int, id: Long) {
                val seleccionado = opciones[position]
                when (seleccionado) {
                    "Yo" -> {
                        etDni.setText(yoDni ?: "")
                        etNombres.setText(yoNombres ?: "")
                    }
                    else -> {
                        val familiar = familiaresData[seleccionado]
                        etDni.setText(familiar?.dni ?: "")
                        etNombres.setText(listOf(familiar?.primerNombre, familiar?.segundoNombre, familiar?.primerApellido, familiar?.segundoApellido).filter { !it.isNullOrBlank() }.joinToString(" "))
                    }
                }
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {
                etDni.setText("")
                etNombres.setText("")
            }
        })

        btnReservarMas.setOnClickListener {
            if (familiares.size >= 3) {
                Toast.makeText(this, "Solo se permiten 4 personas como máximo (incluyéndote a ti).", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val dialog = AgregarFamiliarDialogFragment { apodo, dni, primerNombre, segundoNombre, primerApellido, segundoApellido ->
                val nuevoFamiliar = Familiar(apodo, dni, primerNombre, segundoNombre, primerApellido, segundoApellido)
                familiares.add(nuevoFamiliar)
                FamiliaUtils.guardarFamiliares(this, familiares)
                opciones.add(apodo)
                familiaresData[apodo] = nuevoFamiliar
                adapter.notifyDataSetChanged()
                spinner.setSelection(opciones.size - 1)
            }
            dialog.show(supportFragmentManager, "AgregarFamiliarDialog")
        }

        // Configurar el botón Continuar
        val btnContinuar = findViewById<Button>(R.id.btnContinuar)
        btnContinuar.setOnClickListener {
            // Validar que no esté en "Seleccione"
            if (spinner.selectedItemPosition == 0) {
                Toast.makeText(this, "Por favor, selecciona quién se atiende.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Obtener el nombre completo mostrado en etNombres
            val nombreCompleto = etNombres.text.toString()
            val motivo = findViewById<EditText>(R.id.etObservaciones).text.toString()

            // Obtener datos del intent anterior (doctor, especialidad, etc.)
            val doctor = intent.getStringExtra("doctor") ?: "Dr. No especificado"
            val especialidad = intent.getStringExtra("especialidad") ?: "Especialidad no especificada"
            val ubicacion = intent.getStringExtra("distrito") ?: "Ubicación no especificada"

            // Obtener fecha y hora seleccionadas
            val hora = intent.getStringExtra("hora_seleccionada") ?: ""
            val fecha = intent.getStringExtra("fecha_seleccionada") ?: ""
            // Unificar formato a dd/MM/yyyy HH:mm
            val fechaHora = if (fecha.isNotEmpty() && hora.isNotEmpty()) {
                try {
                    val formatoEntrada = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
                    val formatoFecha = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                    val formatoHora = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
                    val fechaDate = formatoFecha.parse(fecha)
                    val horaDate = formatoHora.parse(hora)
                    // Combinar fecha y hora
                    val cal = java.util.Calendar.getInstance()
                    cal.time = fechaDate!!
                    val horaCal = java.util.Calendar.getInstance()
                    horaCal.time = horaDate!!
                    cal.set(java.util.Calendar.HOUR_OF_DAY, horaCal.get(java.util.Calendar.HOUR_OF_DAY))
                    cal.set(java.util.Calendar.MINUTE, horaCal.get(java.util.Calendar.MINUTE))
                    formatoEntrada.format(cal.time)
                } catch (e: Exception) {
                    "$fecha $hora"
                }
            } else "Fecha/Hora no especificada"

            // Crear el Intent para ir a ConfirmacionActivity
            val intentConfirmacion = Intent(this, ConfirmacionActivity::class.java)
            intentConfirmacion.putExtra("usuario", nombreCompleto)
            intentConfirmacion.putExtra("doctor", doctor)
            intentConfirmacion.putExtra("especialidad", especialidad)
            intentConfirmacion.putExtra("fechaHora", fechaHora)
            intentConfirmacion.putExtra("motivo", motivo)
            intentConfirmacion.putExtra("ubicacion", ubicacion)

            // Navegar a ConfirmacionActivity
            startActivity(intentConfirmacion)
        }
        val btnBack = findViewById<android.widget.ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Resaltar el paso 4
        val pasos = listOf(
            findViewById<ImageView>(R.id.imgPaso1),
            findViewById<ImageView>(R.id.imgPaso2),
            findViewById<ImageView>(R.id.imgPaso3),
            findViewById<ImageView>(R.id.imgPaso4),
            findViewById<ImageView>(R.id.imgPaso5)
        )
        for ((index, imageView) in pasos.withIndex()) {
            if (index == 3) {
                // Paso actual (4): verde
                imageView.setColorFilter(ContextCompat.getColor(this, R.color.verde_claro))
            } else {
                // Otros pasos: gris claro
                imageView.setColorFilter(ContextCompat.getColor(this, R.color.gris_claro))
            }
        }
    }
} 