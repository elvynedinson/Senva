package com.example.senva

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import androidx.core.content.ContextCompat

class ConfirmacionActivity : AppCompatActivity() {
    private var citaConfirmada = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmacion)
        
        // Recibir los datos del Intent
        val usuario = intent.getStringExtra("usuario") ?: ""
        val ubicacion = intent.getStringExtra("ubicacion") ?: ""
        val especialidad = intent.getStringExtra("especialidad") ?: ""
        val doctor = intent.getStringExtra("doctor") ?: ""
        val fechaHora = intent.getStringExtra("fechaHora") ?: ""
        val motivo = intent.getStringExtra("motivo") ?: ""
        
        // Mostrar los datos en los TextView
        findViewById<TextView>(R.id.tvUsuario).text = usuario
        findViewById<TextView>(R.id.tvUbicacion).text = ubicacion
        findViewById<TextView>(R.id.tvEspecialidad).text = especialidad
        findViewById<TextView>(R.id.tvDoctor).text = doctor
        findViewById<TextView>(R.id.tvFechaHora).text = fechaHora
        findViewById<TextView>(R.id.tvMotivo).text = motivo
        
        // Configurar el botón "Confirmar Cita"
        findViewById<Button>(R.id.btnConfirmarCita).setOnClickListener {
            // Guardar la cita en SQLite
            val db = DatabaseHelper(this)
            db.insertarCita(
                direccion = findViewById<TextView>(R.id.tvUbicacion).text.toString(),
                provincia = "",
                distrito = findViewById<TextView>(R.id.tvUbicacion).text.toString(),
                latitud = 0.0,
                longitud = 0.0,
                especialidad = findViewById<TextView>(R.id.tvEspecialidad).text.toString(),
                usuario = findViewById<TextView>(R.id.tvUsuario).text.toString(),
                doctor = findViewById<TextView>(R.id.tvDoctor).text.toString(),
                fechaHora = findViewById<TextView>(R.id.tvFechaHora).text.toString(),
                motivo = findViewById<TextView>(R.id.tvMotivo).text.toString()
            )
            citaConfirmada = true
            
            // Resetear el flag de notificaciones vistas para mostrar el badge
            val sharedPreferences = getSharedPreferences(LoginActivity.Global.preferencias_compartidas, MODE_PRIVATE)
            sharedPreferences.edit().putBoolean("notificaciones_vistas", false).apply()
            
            // Navegar a VoucherActivity
            val intent = android.content.Intent(this, VoucherActivity::class.java)
            intent.putExtra("usuario", findViewById<TextView>(R.id.tvUsuario).text.toString())
            intent.putExtra("fechaHora", findViewById<TextView>(R.id.tvFechaHora).text.toString())
            intent.putExtra("doctor", findViewById<TextView>(R.id.tvDoctor).text.toString())
            intent.putExtra("especialidad", findViewById<TextView>(R.id.tvEspecialidad).text.toString())
            intent.putExtra("ubicacion", findViewById<TextView>(R.id.tvUbicacion).text.toString())
            startActivity(intent)
            finish()
        }
        val btnBack = findViewById<android.widget.ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            if (!citaConfirmada) {
                onBackPressedDispatcher.onBackPressed()
            }
        }

        // Resaltar el paso 5
        val pasos = listOf(
            findViewById<ImageView>(R.id.imgPaso1),
            findViewById<ImageView>(R.id.imgPaso2),
            findViewById<ImageView>(R.id.imgPaso3),
            findViewById<ImageView>(R.id.imgPaso4),
            findViewById<ImageView>(R.id.imgPaso5)
        )
        for ((index, imageView) in pasos.withIndex()) {
            if (index == 4) {
                // Paso actual (5): verde
                imageView.setColorFilter(ContextCompat.getColor(this, R.color.verde_claro))
            } else {
                // Otros pasos: gris claro
                imageView.setColorFilter(ContextCompat.getColor(this, R.color.gris_claro))
            }
        }
    }

    override fun onBackPressed() {
        if (!citaConfirmada) {
            super.onBackPressed()
        }
        // Si la cita ya fue confirmada, no hace nada
    }
} 