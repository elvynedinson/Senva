package com.example.senva

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

class MiCitaActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mapView: MapView
    private var googleMap: GoogleMap? = null
    private var citaLat: Double = -12.027
    private var citaLng: Double = -77.012
    private var citaDireccion: String = "Dpto: Lima\nProv: Lima\nDist: San Juan de Lurigancho"
    private var marker: Marker? = null
    private lateinit var databaseHelper: DatabaseHelper
    
    // Variables para almacenar la información de ubicación
    private var direccionSeleccionada: String = ""
    private var provinciaSeleccionada: String = ""
    private var distritoSeleccionado: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mi_cita)
        
        // Inicializar la base de datos
        databaseHelper = DatabaseHelper(this)

        mapView = findViewById(R.id.mapViewCita)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        val fabCambiarUbicacion = findViewById<FloatingActionButton>(R.id.fabCambiarUbicacion)
        val btnContinuar = findViewById<Button>(R.id.btnContinuar)
        val tvUbicacionActual = findViewById<TextView>(R.id.tvUbicacionActual)
        val tvDpto = findViewById<TextView>(R.id.tvDpto)
        val tvProv = findViewById<TextView>(R.id.tvProv)
        val tvDist = findViewById<TextView>(R.id.tvDist)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        // Inicializar dirección
        tvDpto.text = "Dpto: Lima"
        tvProv.text = "Prov: Lima"
        tvDist.text = "Dist: San Juan de Lurigancho"

        fabCambiarUbicacion.setOnClickListener {
            Snackbar.make(fabCambiarUbicacion, "Toca el mapa para seleccionar una nueva ubicación", Snackbar.LENGTH_SHORT).show()
        }

        btnContinuar.setOnClickListener {
            // Guardar la información de ubicación en la base de datos
            if (direccionSeleccionada.isNotEmpty() && provinciaSeleccionada.isNotEmpty() && distritoSeleccionado.isNotEmpty()) {
                val citaId = databaseHelper.insertarCita(
                    direccion = direccionSeleccionada,
                    provincia = provinciaSeleccionada,
                    distrito = distritoSeleccionado,
                    latitud = citaLat,
                    longitud = citaLng,
                    especialidad = "", // Se llenará en la siguiente pantalla
                    usuario = "",
                    doctor = "",
                    fechaHora = "",
                    motivo = ""
                )
                
                if (citaId != -1L) {
                    // Resetear el flag de notificaciones vistas para mostrar el badge
                    val sharedPreferences = getSharedPreferences(LoginActivity.Global.preferencias_compartidas, MODE_PRIVATE)
                    sharedPreferences.edit().putBoolean("notificaciones_vistas", false).apply()
                    
                    // Pasar el ID de la cita a la siguiente actividad
                    val tvDist = findViewById<TextView>(R.id.tvDist)
                    val distritoCompleto = tvDist.text.toString()
                    val distrito = distritoCompleto.replace("Dist: ", "").trim()
                    val intent = Intent(this, EspecialidadesActivity::class.java)
                    intent.putExtra("CITA_ID", citaId.toInt())
                    intent.putExtra("distrito", distrito)
                    startActivity(intent)
                } else {
                    Snackbar.make(btnContinuar, "Error al guardar la ubicación", Snackbar.LENGTH_SHORT).show()
                }
            } else {
                Snackbar.make(btnContinuar, "Por favor selecciona una ubicación en el mapa", Snackbar.LENGTH_SHORT).show()
            }
        }

        btnBack.setOnClickListener {
            finish()
        }

        // Resaltar el paso 1
        val pasos = listOf(
            findViewById<ImageView>(R.id.imgPaso1),
            findViewById<ImageView>(R.id.imgPaso2),
            findViewById<ImageView>(R.id.imgPaso3),
            findViewById<ImageView>(R.id.imgPaso4),
            findViewById<ImageView>(R.id.imgPaso5)
        )
        for ((index, imageView) in pasos.withIndex()) {
            if (index == 0) {
                // Paso actual (1): verde
                imageView.setColorFilter(ContextCompat.getColor(this, R.color.verde_claro))
            } else {
                // Otros pasos: gris claro
                imageView.setColorFilter(ContextCompat.getColor(this, R.color.gris_claro))
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val citaLatLng = LatLng(citaLat, citaLng)
        marker = googleMap?.addMarker(MarkerOptions().position(citaLatLng).title("Lugar de la cita"))
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(citaLatLng, 15f))
        marker?.showInfoWindow()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap?.isMyLocationEnabled = true
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val userLatLng = LatLng(location.latitude, location.longitude)
                    googleMap?.addPolyline(
                        PolylineOptions()
                            .add(userLatLng, citaLatLng)
                            .color(ContextCompat.getColor(this, R.color.verde_claro))
                            .width(8f)
                    )
                    val bounds = LatLngBounds.builder()
                        .include(userLatLng)
                        .include(citaLatLng)
                        .build()
                    googleMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
                    // Mostrar badge si está cerca (menos de 500m)
                    val results = FloatArray(1)
                    android.location.Location.distanceBetween(
                        userLatLng.latitude, userLatLng.longitude,
                        citaLatLng.latitude, citaLatLng.longitude,
                        results
                    )
                    val tvUbicacionActual = findViewById<TextView>(R.id.tvUbicacionActual)
                    if (results[0] < 500) {
                        tvUbicacionActual?.visibility = View.VISIBLE
                    } else {
                        tvUbicacionActual?.visibility = View.GONE
                    }
                }
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        // Permitir al usuario seleccionar la ubicación tocando el mapa
        googleMap?.setOnMapClickListener { latLng ->
            marker?.remove()
            marker = googleMap?.addMarker(MarkerOptions().position(latLng).title("Nueva ubicación de la cita"))
            marker?.showInfoWindow()
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
            citaLat = latLng.latitude
            citaLng = latLng.longitude
            // Actualizar dirección usando Geocoder en un hilo secundario
            Thread {
                try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocation(citaLat, citaLng, 1)
                    runOnUiThread {
            val tvDpto = findViewById<TextView>(R.id.tvDpto)
            val tvProv = findViewById<TextView>(R.id.tvProv)
            val tvDist = findViewById<TextView>(R.id.tvDist)
            if (!addresses.isNullOrEmpty()) {
                val addr = addresses[0]
                val dpto = addr.adminArea ?: "-"
                val prov = addr.subAdminArea ?: "-"
                val dist = addr.locality ?: addr.subLocality ?: "-"
                tvDpto?.text = "Dpto: $dpto"
                tvProv?.text = "Prov: $prov"
                tvDist?.text = "Dist: $dist"
                direccionSeleccionada = addr.getAddressLine(0) ?: ""
                provinciaSeleccionada = prov
                distritoSeleccionado = dist
            } else {
                tvDpto?.text = "Dpto: -"
                tvProv?.text = "Prov: -"
                tvDist?.text = "Dist: -"
                direccionSeleccionada = ""
                provinciaSeleccionada = ""
                distritoSeleccionado = ""
            }
            Snackbar.make(findViewById(R.id.mapViewCita), "Ubicación de la cita actualizada", Snackbar.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Snackbar.make(findViewById(R.id.mapViewCita), "No se pudo obtener la dirección", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }.start()
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }
    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
} 