package com.example.tecnof.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tecnof.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // Esta propiedad solo es válida entre onCreateView y onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Crear o recuperar una instancia de HomeViewModel
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        // Inflar el diseño del fragmento
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Aquí puedes realizar otras inicializaciones necesarias

        return root // Retornar la vista inflada
    }

    override fun onDestroyView() {
        super.onDestroyView() // Llamar al método de la superclase
        _binding = null // Liberar la referencia de binding
    }
}
