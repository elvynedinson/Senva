package com.example.senva

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import android.text.InputFilter
import com.example.senva.model.Familiar

class AgregarFamiliarDialogFragment(
    private val familiarAEditar:
    Familiar? = null,
    private val onFamiliarAgregado: (String, String, String, String, String, String) -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCancelable(true)
            setCanceledOnTouchOutside(true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_agregar_familiar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinnerDocumento = view.findViewById<Spinner>(R.id.spinnerDocumento)
        val etNumeroDocumento = view.findViewById<EditText>(R.id.etNumeroDocumento)
        // Limitar a 8 dígitos
        etNumeroDocumento.filters = arrayOf(InputFilter.LengthFilter(8))
        etNumeroDocumento.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        val etApodo = view.findViewById<EditText>(R.id.etApodo)
        val btnAgregar = view.findViewById<Button>(R.id.btnAgregarFamiliar)

        // Opciones del spinner
        val opciones = listOf("Documento", "DNI", "Carnet de extranjería", "Pasaporte")
        val adapter = object : ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, opciones) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0 // Deshabilitar la opción 'Documento'
            }
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDocumento.adapter = adapter

        // Ocultar campo de número al inicio
        etNumeroDocumento.visibility = View.GONE

        spinnerDocumento.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    etNumeroDocumento.visibility = View.GONE
                } else {
                    etNumeroDocumento.visibility = View.VISIBLE
                    when (opciones[position]) {
                        "DNI" -> etNumeroDocumento.hint = "Número de DNI"
                        "Carnet de extranjería" -> etNumeroDocumento.hint = "Número de carnet"
                        "Pasaporte" -> etNumeroDocumento.hint = "Número de pasaporte"
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                etNumeroDocumento.visibility = View.GONE
            }
        }

        btnAgregar.setOnClickListener {
            val apodo = etApodo.text.toString().trim()
            val dni = etNumeroDocumento.text.toString().trim()
            val primerNombre = view.findViewById<EditText>(R.id.etPrimerNombre).text.toString().trim()
            val segundoNombre = view.findViewById<EditText>(R.id.etSegundoNombre).text.toString().trim()
            val primerApellido = view.findViewById<EditText>(R.id.etPrimerApellido).text.toString().trim()
            val segundoApellido = view.findViewById<EditText>(R.id.etSegundoApellido).text.toString().trim()
            if (apodo.isNotEmpty() && dni.length == 8 && primerNombre.isNotEmpty() && primerApellido.isNotEmpty()) {
                onFamiliarAgregado(apodo, dni, primerNombre, segundoNombre, primerApellido, segundoApellido)
                dismiss()
            } else {
                Toast.makeText(requireContext(), "Completa todos los campos obligatorios y un DNI válido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.90).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
} 