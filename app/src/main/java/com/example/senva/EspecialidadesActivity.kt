package com.example.senva

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.ImageView
import androidx.core.content.ContextCompat

class EspecialidadesActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private var citaId: Int = -1
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_especialidades)
        
        // Inicializar la base de datos
        databaseHelper = DatabaseHelper(this)
        
        // Obtener el ID de la cita de la actividad anterior
        citaId = intent.getIntExtra("CITA_ID", -1)

        val recyclerView = findViewById<RecyclerView>(R.id.rvEspecialidades)
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        val especialidades = listOf(
            Especialidad("Medicina G.", R.drawable.medicinageneral),
            Especialidad("Nutrición", R.drawable.nutricion),
            Especialidad("Pediatría", R.drawable.pediatria),
            Especialidad("Cardiología", R.drawable.cardiologia),
            Especialidad("Dermatología", R.drawable.dermatologia),
            Especialidad("Traumatología", R.drawable.traumatologia),
            Especialidad("Oftalmología", R.drawable.oftalmologia),
            Especialidad("Ginecología", R.drawable.ginecologia),
            Especialidad("Neumología", R.drawable.neumologia),
            Especialidad("Endocrinología", R.drawable.endocrinologia),
            Especialidad("Psicología", R.drawable.psicologia),
            Especialidad("Gastro", R.drawable.gastro),
            Especialidad("Reumatología", R.drawable.reumatologia),
            Especialidad("Nefrología", R.drawable.nefrologia),
            Especialidad("Geriatría", R.drawable.geriatria)
        )

        val adapter = EspecialidadesAdapter(especialidades) { especialidad ->
            // Actualizar la especialidad en la base de datos
            if (citaId != -1) {
                val filasActualizadas = databaseHelper.actualizarEspecialidadCita(citaId, especialidad.nombre)
                
                if (filasActualizadas > 0) {
                    // Navegar a la pantalla de doctores y pasar la especialidad seleccionada
                    val distrito = intent.getStringExtra("distrito") ?: ""
                    val intent = Intent(this, ListaDoctoresActivity::class.java)
                    intent.putExtra("ESPECIALIDAD_SELECCIONADA", especialidad.nombre)
                    intent.putExtra("distrito", distrito)
                    startActivity(intent)
                    finish()
                } else {
                    android.widget.Toast.makeText(
                        this,
                        "Error al guardar la especialidad",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        recyclerView.adapter = adapter

        // Resaltar el paso 2
        val pasos = listOf(
            findViewById<ImageView>(R.id.imgPaso1),
            findViewById<ImageView>(R.id.imgPaso2),
            findViewById<ImageView>(R.id.imgPaso3),
            findViewById<ImageView>(R.id.imgPaso4),
            findViewById<ImageView>(R.id.imgPaso5)
        )
        for ((index, imageView) in pasos.withIndex()) {
            if (index == 1) {
                // Paso actual (2): verde
                imageView.setColorFilter(ContextCompat.getColor(this, R.color.verde_claro))
            } else {
                // Otros pasos: gris claro
                imageView.setColorFilter(ContextCompat.getColor(this, R.color.gris_claro))
            }
        }

        val btnBack = findViewById<android.widget.ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            // Navegar a MiCitaActivity
            val intent = Intent(this, MiCitaActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
} 