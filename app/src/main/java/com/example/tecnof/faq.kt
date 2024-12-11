package com.example.tecnof

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class faq : Fragment() {

    private lateinit var textView14: TextView
    private lateinit var textView16: TextView
    private lateinit var textView18: TextView
    private lateinit var textView20: TextView
    private lateinit var textView22: TextView
    private lateinit var textView24: TextView
    private lateinit var textView26: TextView
    private lateinit var textView28: TextView
    private lateinit var textView30: TextView
    private lateinit var textView32: TextView
    private lateinit var textView33: TextView
    private lateinit var textView34: TextView

    // Variables para rastrear el estado de expansión de las preguntas y respuestas
    private var isExpandedQ1R1 = false
    private var isExpandedQ2R2 = false
    private var isExpandedQ3R3 = false
    private var isExpandedQ4R4 = false
    private var isExpandedQ5R5 = false
    private var isExpandedQ6R6 = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout para este fragmento
        val rootView = inflater.inflate(R.layout.fragment_faq, container, false)

        // Obtener las referencias de los TextViews
        textView14 = rootView.findViewById(R.id.Q1)
        textView16 = rootView.findViewById(R.id.R1)
        textView18 = rootView.findViewById(R.id.Q2)
        textView20 = rootView.findViewById(R.id.R2)
        textView22 = rootView.findViewById(R.id.Q3)
        textView24 = rootView.findViewById(R.id.R3)
        textView26 = rootView.findViewById(R.id.Q4)
        textView28 = rootView.findViewById(R.id.R4)
        textView30 = rootView.findViewById(R.id.Q5)
        textView32 = rootView.findViewById(R.id.R5)
        textView33 = rootView.findViewById(R.id.Q6)
        textView34 = rootView.findViewById(R.id.R6)

        // Configurar los OnClickListeners para las preguntas (Q1, Q2, Q3, Q4, Q5)
        textView14.setOnClickListener {
            toggleVisibility(textView16, isExpandedQ1R1)
            isExpandedQ1R1 = !isExpandedQ1R1
        }

        textView18.setOnClickListener {
            toggleVisibility(textView20, isExpandedQ2R2)
            isExpandedQ2R2 = !isExpandedQ2R2
        }

        textView22.setOnClickListener {
            toggleVisibility(textView24, isExpandedQ3R3)
            isExpandedQ3R3 = !isExpandedQ3R3
        }

        textView26.setOnClickListener {
            toggleVisibility(textView28, isExpandedQ4R4)
            isExpandedQ4R4 = !isExpandedQ4R4
        }

        textView30.setOnClickListener {
            toggleVisibility(textView32, isExpandedQ5R5)
            isExpandedQ5R5 = !isExpandedQ5R5
        }
        textView33.setOnClickListener {
            toggleVisibility(textView34, isExpandedQ6R6)
            isExpandedQ6R6 = !isExpandedQ6R6
        }

        return rootView
    }

    // Función para alternar la visibilidad y la altura
    private fun toggleVisibility(responseTextView: TextView, isExpanded: Boolean) {
        if (isExpanded) {
            responseTextView.layoutParams.height = 5  // Altura original
            responseTextView.visibility = View.INVISIBLE  // Hacer invisible la respuesta
        } else {
            responseTextView.layoutParams.height = 400  // Ajustar la altura para la respuesta
            responseTextView.visibility = View.VISIBLE  // Hacer visible la respuesta
        }
        responseTextView.requestLayout()  // Asegurarse de que se actualice la vista
    }
}
