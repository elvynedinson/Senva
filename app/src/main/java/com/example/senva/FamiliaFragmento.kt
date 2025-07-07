package com.example.senva

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.senva.FamiliaUtils
import com.example.senva.model.Familiar

class FamiliaFragmento : Fragment() {
    private lateinit var adapter: FamiliaAdapter
    private lateinit var familiares: MutableList<Familiar>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_familia, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnBack = view.findViewById<ImageButton>(R.id.btnBackFamilia)
        val btnAgregar = view.findViewById<ImageView>(R.id.btnAgregarFamiliar)
        val recycler = view.findViewById<RecyclerView>(R.id.recyclerFamilia)
        val tvVacio = view.findViewById<TextView>(R.id.tvFamiliaVacio)

        familiares = FamiliaUtils.cargarFamiliares(requireContext())
        adapter = FamiliaAdapter(familiares,
            onEditar = { familiar, pos ->
                val dialog = AgregarFamiliarDialogFragment(familiar) { apodo, dni, primerNombre, segundoNombre, primerApellido, segundoApellido ->
                    familiares[pos] = Familiar(apodo, dni, primerNombre, segundoNombre, primerApellido, segundoApellido)
                    FamiliaUtils.guardarFamiliares(requireContext(), familiares)
                    adapter.notifyItemChanged(pos)
                }
                dialog.show(parentFragmentManager, "EditarFamiliarDialog")
            },
            onEliminar = { pos ->
                androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Eliminar familiar")
                    .setMessage("¿Seguro que deseas eliminar este familiar?")
                    .setPositiveButton("Sí") { _, _ ->
                        familiares.removeAt(pos)
                        FamiliaUtils.guardarFamiliares(requireContext(), familiares)
                        adapter.notifyItemRemoved(pos)
                        tvVacio.visibility = if (familiares.isEmpty()) View.VISIBLE else View.GONE
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        )
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter
        tvVacio.visibility = if (familiares.isEmpty()) View.VISIBLE else View.GONE

        btnBack.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        btnAgregar.setOnClickListener {
            if (familiares.size >= 3) {
                android.widget.Toast.makeText(requireContext(), "Solo se permiten 4 personas como máximo (incluyéndote a ti).", android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val dialog = AgregarFamiliarDialogFragment { apodo, dni, primerNombre, segundoNombre, primerApellido, segundoApellido ->
                familiares.add(Familiar(apodo, dni, primerNombre, segundoNombre, primerApellido, segundoApellido))
                FamiliaUtils.guardarFamiliares(requireContext(), familiares)
                adapter.notifyItemInserted(familiares.size - 1)
                tvVacio.visibility = View.GONE
            }
            dialog.show(parentFragmentManager, "AgregarFamiliarDialog")
        }
    }
} 