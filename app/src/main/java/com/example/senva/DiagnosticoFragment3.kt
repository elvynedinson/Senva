package com.example.senva

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.senva.model.Doctor
import com.example.senva.model.Receta
import java.io.File
import java.io.FileOutputStream
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.media.MediaScannerConnection
import androidx.fragment.app.commit
import androidx.fragment.app.replace

class DiagnosticoFragment3 : Fragment() {
    private var doctor: Doctor? = null
    private var receta: Receta? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            doctor = it.getParcelable("doctor")
            receta = it.getParcelable("receta")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diagnostico, container, false)

        // Poblar datos del doctor
        view.findViewById<TextView>(R.id.tvNombreDoctor3)?.text = doctor?.nombre ?: "Nombre no disponible"
        view.findViewById<TextView>(R.id.tvEspecialidadDoctor3)?.text = "Especialidad:"
        view.findViewById<TextView>(R.id.tvEspecialidadValorDoctor3)?.text = doctor?.especialidad ?: "Especialidad"
        view.findViewById<TextView>(R.id.tvFechaDoctor3)?.text = receta?.fecha ?: "Fecha"
        view.findViewById<ImageView>(R.id.ivFotoDoctor3)?.let { imageView ->
            // Si tienes una URL o recurso para la foto del doctor, usa Glide aquí
            // Glide.with(this).load(doctor?.fotoUrl).into(imageView)
            // Por ahora, se deja el drawable por defecto
        }

        // Poblar datos de la receta
        view.findViewById<TextView>(R.id.tvTituloReceta)?.text = "Receta - ${receta?.codigo ?: "001"}"
        view.findViewById<TextView>(R.id.tvFechaReceta)?.text = receta?.fecha ?: "Fecha"

        // Mostrar siempre la misma imagen de receta
        val imageViewReceta = view.findViewById<ImageView>(R.id.ivReceta)
        Glide.with(this)
            .load("file:///android_asset/receta_001.jpg")
            .placeholder(R.drawable.ic_historia_clinica)
            .error(R.drawable.ic_historia_clinica)
            .into(imageViewReceta)

        // Configurar botones
        val codigo = receta?.codigo ?: "001"
        val nombreArchivo = "receta_${codigo}.pdf"

        view.findViewById<Button>(R.id.btnDescargar)?.setOnClickListener {
            checkAndRequestStoragePermission {
                copiarPdfADescargas(nombreArchivo,
                    onSuccess = { outFile ->
                        // Forzar indexación para que aparezca en la app de Descargas
                        MediaScannerConnection.scanFile(
                            context,
                            arrayOf(outFile.absolutePath),
                            arrayOf("application/pdf"),
                            null
                        )
                        Toast.makeText(context, "Receta PDF descargada en Descargas", Toast.LENGTH_SHORT).show()
                    },
                    onError = {
                        Toast.makeText(context, "Error al copiar PDF: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        view.findViewById<Button>(R.id.btnEnviar)?.setOnClickListener {
            checkAndRequestStoragePermission {
                copiarPdfADescargas(nombreArchivo,
                    onSuccess = { pdfFile ->
                        try {
                            val uri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", pdfFile)
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "application/pdf"
                                putExtra(Intent.EXTRA_STREAM, uri)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            startActivity(Intent.createChooser(shareIntent, "Compartir receta PDF"))
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error al compartir: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    },
                    onError = {
                        Toast.makeText(context, "Error al copiar PDF: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ivBack = view.findViewById<View>(R.id.ivBack)
        ivBack?.setOnClickListener {
            doctor?.let {
                (activity as? HomeActivity)?.supportFragmentManager?.commit {
                    replace(R.id.frameContainer, DiagnosticoFragment2.newInstance(it))
                }
            }
        }
    }

    private fun checkAndRequestStoragePermission(onGranted: () -> Unit) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), 1001)
            } else {
                onGranted()
            }
        } else {
            onGranted()
        }
    }

    private fun copiarPdfADescargas(nombreArchivo: String, onSuccess: (File) -> Unit, onError: (Exception) -> Unit) {
        try {
            val inputStream = requireContext().assets.open("receta_generica.pdf")
            val outFile = File(android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS), nombreArchivo)
            val outputStream = FileOutputStream(outFile)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            onSuccess(outFile)
        } catch (e: Exception) {
            onError(e)
        }
    }

    companion object {
        fun newInstance(doctor: Doctor, receta: Receta): DiagnosticoFragment3 {
            return DiagnosticoFragment3().apply {
                arguments = Bundle().apply {
                    putParcelable("doctor", doctor)
                    putParcelable("receta", receta)
                }
            }
        }
    }
} 