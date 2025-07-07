package com.example.senva

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import android.widget.TextView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NotificacionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notificacion)

        val btnAjustes = findViewById<ImageButton>(R.id.btnAjustesNoti)
        btnAjustes.setOnClickListener {
            val popup = PopupMenu(this, btnAjustes)
            popup.menu.add("Solo hoy")
            popup.menu.add("Últimos 7 días")
            popup.menu.add("Últimos 15 días")
            popup.menu.add("Últimos 30 días")
            popup.menu.add("Últimos 90 días")
            popup.setOnMenuItemClickListener { item ->
                Toast.makeText(this, "Filtro: ${item.title}", Toast.LENGTH_SHORT).show()
                true
            }
            popup.show()
        }

        val btnBack = findViewById<ImageButton>(R.id.btnBackNotificacion)
        btnBack.setOnClickListener {
            finish()
        }

        // Mostrar todas las citas en el RecyclerView
        val db = DatabaseHelper(this)
        val citas = db.obtenerTodasLasCitas()
        val rvNotificaciones = findViewById<RecyclerView>(R.id.rvNotificaciones)
        rvNotificaciones.layoutManager = LinearLayoutManager(this)
        rvNotificaciones.adapter = NotificacionAdapter(citas) { cita ->
            val dialog = VoucherDialogFragment.newInstance(
                nombre = cita.usuario,
                fechaHora = cita.fechaHora,
                doctorEspecialidad = "${cita.doctor} / ${cita.especialidad}",
                ubicacion = cita.direccion,
                qrResId = R.drawable.qr // Puedes cambiar esto si tienes QR dinámico
            )
            dialog.show(supportFragmentManager, "voucherDialog")
        }
    }
} 