package com.example.senva

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class VoucherDialogFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_voucher, container, false)

        val tvSenva = view.findViewById<TextView>(R.id.tvSenva)
        val tvSubSenva = view.findViewById<TextView>(R.id.tvSubSenva)
        val tvNombre = view.findViewById<TextView>(R.id.tvNombre)
        val tvCitaProgramada = view.findViewById<TextView>(R.id.tvCitaProgramada)
        val tvFechaHora = view.findViewById<TextView>(R.id.tvFechaHora)
        val tvDoctorEspecialidad = view.findViewById<TextView>(R.id.tvDoctorEspecialidad)
        val imgQR = view.findViewById<ImageView>(R.id.imgQR)
        val tvUbicacion = view.findViewById<TextView>(R.id.tvUbicacion)
        val tvEsperamos = view.findViewById<TextView>(R.id.tvEsperamos)
        val tvLlegar = view.findViewById<TextView>(R.id.tvLlegar)
        val imgHoja = view.findViewById<ImageView>(R.id.imgHoja)
        val btnCerrar = view.findViewById<Button>(R.id.btnCerrar)

        // Obtener argumentos
        val nombre = arguments?.getString(ARG_NOMBRE) ?: ""
        val fechaHora = arguments?.getString(ARG_FECHA_HORA) ?: ""
        val doctorEspecialidad = arguments?.getString(ARG_DOCTOR_ESPECIALIDAD) ?: ""
        val ubicacion = arguments?.getString(ARG_UBICACION) ?: ""
        val qrResId = arguments?.getInt(ARG_QR_RES_ID) ?: R.drawable.qr

        tvNombre.text = nombre
        tvFechaHora.text = fechaHora
        tvDoctorEspecialidad.text = doctorEspecialidad
        tvUbicacion.text = ubicacion
        imgQR.setImageResource(qrResId)

        btnCerrar.setOnClickListener { dismiss() }

        return view
    }

    companion object {
        private const val ARG_NOMBRE = "nombre"
        private const val ARG_FECHA_HORA = "fecha_hora"
        private const val ARG_DOCTOR_ESPECIALIDAD = "doctor_especialidad"
        private const val ARG_UBICACION = "ubicacion"
        private const val ARG_QR_RES_ID = "qr_res_id"

        fun newInstance(
            nombre: String,
            fechaHora: String,
            doctorEspecialidad: String,
            ubicacion: String,
            qrResId: Int = R.drawable.qr
        ): VoucherDialogFragment {
            val fragment = VoucherDialogFragment()
            val args = Bundle()
            args.putString(ARG_NOMBRE, nombre)
            args.putString(ARG_FECHA_HORA, fechaHora)
            args.putString(ARG_DOCTOR_ESPECIALIDAD, doctorEspecialidad)
            args.putString(ARG_UBICACION, ubicacion)
            args.putInt(ARG_QR_RES_ID, qrResId)
            fragment.arguments = args
            return fragment
        }
    }
} 