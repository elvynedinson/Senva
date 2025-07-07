package com.example.senva

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import androidx.core.content.ContextCompat

class ListaDoctoresActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctores)

        val recyclerView = findViewById<RecyclerView>(R.id.rvDoctoresDisponibles)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Lista de ejemplo
        val doctores = listOf(
            DoctorDisponible("DRA. RAMÍREZ", "Cardióloga", R.drawable.doctorunos),
            DoctorDisponible("DRA. RAMÍREZ", "Cardióloga", R.drawable.doctorunos),
            DoctorDisponible("DRA. RAMÍREZ", "Cardióloga", R.drawable.doctorunos)
        )

        val adapter = DoctoresDisponiblesAdapter(doctores, supportFragmentManager)
        recyclerView.adapter = adapter

        // Lista de ejemplo para ocupados
        val ocupados = listOf(
            DoctorDisponible("DRA. RAMÍREZ", "Cardióloga", R.drawable.doctorunos),
            DoctorDisponible("DRA. RAMÍREZ", "Cardióloga", R.drawable.doctorunos),
            DoctorDisponible("DRA. RAMÍREZ", "Cardióloga", R.drawable.doctorunos)
        )
        val rvOcupados = findViewById<RecyclerView>(R.id.rvDoctoresOcupados)
        rvOcupados.layoutManager = LinearLayoutManager(this)
        val adapterOcupados = DoctoresDisponiblesAdapter(ocupados, supportFragmentManager)
        rvOcupados.adapter = adapterOcupados

        val btnBack = findViewById<android.widget.ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            // Navegar específicamente a EspecialidadesActivity
            val intent = android.content.Intent(this, EspecialidadesActivity::class.java)
            // Pasar los datos necesarios si los hay
            val citaId = getIntent().getIntExtra("CITA_ID", -1)
            if (citaId != -1) {
                intent.putExtra("CITA_ID", citaId)
            }
            val distrito = getIntent().getStringExtra("distrito")
            if (!distrito.isNullOrEmpty()) {
                intent.putExtra("distrito", distrito)
            }
            startActivity(intent)
            finish()
        }

        // Resaltar el paso 3
        val pasos = listOf(
            findViewById<ImageView>(R.id.imgPaso1),
            findViewById<ImageView>(R.id.imgPaso2),
            findViewById<ImageView>(R.id.imgPaso3),
            findViewById<ImageView>(R.id.imgPaso4),
            findViewById<ImageView>(R.id.imgPaso5)
        )
        for ((index, imageView) in pasos.withIndex()) {
            if (index == 2) {
                // Paso actual (3): verde
                imageView.setColorFilter(ContextCompat.getColor(this, R.color.verde_claro))
            } else {
                // Otros pasos: gris claro
                imageView.setColorFilter(ContextCompat.getColor(this, R.color.gris_claro))
            }
        }
    }
} 