package com.example.senva

import android.os.Bundle
import android.widget.TextView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class VoucherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voucher)

        // Recibir los datos del intent
        val nombre = intent.getStringExtra("usuario") ?: ""
        val fechaHora = intent.getStringExtra("fechaHora") ?: ""
        val doctor = intent.getStringExtra("doctor") ?: ""
        val especialidad = intent.getStringExtra("especialidad") ?: ""
        val ubicacion = intent.getStringExtra("ubicacion") ?: ""

        // Mostrar los datos
        findViewById<TextView>(R.id.tvNombre).text = nombre
        findViewById<TextView>(R.id.tvFechaHora).text = fechaHora
        findViewById<TextView>(R.id.tvDoctorEspecialidad).text = "$doctor / $especialidad"
        findViewById<TextView>(R.id.tvUbicacion).text = "(SENVA DE $ubicacion)"

        // Navegación al Home al presionar el icono de la casa
        val homeIcon = findViewById<ImageView>(R.id.homevoucher)
        homeIcon?.setOnClickListener {
            val intent = android.content.Intent(this, HomeActivity::class.java)
            intent.flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or android.content.Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        // Navegación a MiCitaActivity al presionar el icono de nueva cita
        val nuevaCitaIcon = findViewById<ImageView>(R.id.nuevacitavoucher)
        nuevaCitaIcon?.setOnClickListener {
            val intent = android.content.Intent(this, MiCitaActivity::class.java)
            intent.flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or android.content.Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        // No hacer nada para evitar retroceso
    }
} 