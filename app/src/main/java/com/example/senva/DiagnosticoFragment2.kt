package com.example.senva

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.senva.model.Doctor
import com.example.senva.model.Receta

class DiagnosticoFragment2 : Fragment() {
    private lateinit var recyclerRecientes: RecyclerView
    private lateinit var recyclerPasadas: RecyclerView
    private lateinit var doctor: Doctor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doctor = requireArguments().getParcelable("doctor")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diagnostico2, container, false)
        recyclerRecientes = view.findViewById(R.id.recycler_recetas_recientes)
        recyclerPasadas = view.findViewById(R.id.recycler_recetas_pasadas)
        recyclerRecientes.layoutManager = LinearLayoutManager(requireContext())
        recyclerPasadas.layoutManager = LinearLayoutManager(requireContext())

        // Separar recetas recientes y pasadas
        val recetasRecientes = doctor.recetas.filter { it.esReciente }
        val recetasPasadas = doctor.recetas.filter { !it.esReciente }

        recyclerRecientes.adapter = RecetaAdapter(recetasRecientes) { receta ->
            (activity as? HomeActivity)?.navigateToDiagnostico3(doctor, receta)
        }
        recyclerPasadas.adapter = RecetaAdapter(recetasPasadas) { receta ->
            (activity as? HomeActivity)?.navigateToDiagnostico3(doctor, receta)
        }

        // Mostrar datos del doctor
        view.findViewById<TextView>(R.id.tvNombreDoctor2)?.text = doctor.nombre
        view.findViewById<TextView>(R.id.tvEspecialidadDoctor2)?.text = "Especialidad:"
        view.findViewById<TextView>(R.id.tvEspecialidadValorDoctor2)?.text = doctor.especialidad
        view.findViewById<TextView>(R.id.tvFechaDoctor2)?.text = doctor.fecha
        // La foto y otros datos ya están en el layout
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ivBack = view.findViewById<View>(R.id.ivBack)
        ivBack?.setOnClickListener {
            (activity as? HomeActivity)?.supportFragmentManager?.commit {
                replace<DiagnosticoFragment1>(R.id.frameContainer)
            }
        }
    }

    companion object {
        fun newInstance(doctor: Doctor): DiagnosticoFragment2 {
            val fragment = DiagnosticoFragment2()
            val args = Bundle()
            args.putParcelable("doctor", doctor)
            fragment.arguments = args
            return fragment
        }
    }
} 