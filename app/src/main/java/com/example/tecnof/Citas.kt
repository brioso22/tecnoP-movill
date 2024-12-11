package com.example.tecnof

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Citas : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var gridDias: GridLayout
    private lateinit var gridHoras: GridLayout
    private var horaSeleccionada: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_citas, container, false)

        // Inicialización de Firebase y vistas
        database = FirebaseDatabase.getInstance().reference
        gridDias = view.findViewById(R.id.gridDias)
        gridHoras = view.findViewById(R.id.gridHoras)

        generarBloquesDias()

        // Botón para guardar datos en Firebase
        val buttonEnviar = view.findViewById<Button>(R.id.button4)
        buttonEnviar.setOnClickListener {
            enviarCitasAFirebase()
        }

        return view
    }

    private fun generarBloquesDias() {
        val calendario = Calendar.getInstance()
        val formatoFecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        for (i in 0 until 14) { // 2 semanas de días
            val dia = formatoFecha.format(calendario.time)

            val cuadroDia = TextView(context).apply {
                text = dia
                gravity = Gravity.CENTER
                textSize = 14f
                setPadding(16, 16, 16, 16)
                setBackgroundColor(Color.BLUE)
                setTextColor(Color.WHITE)
                isClickable = true
                setOnClickListener {
                    gridHoras.removeAllViews() // Limpiar las horas anteriores
                    generarBloquesHoras(dia)
                }
            }

            val params = GridLayout.LayoutParams().apply {
                width = 0
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(8, 8, 8, 8)
            }

            gridDias.addView(cuadroDia, params)
            calendario.add(Calendar.DATE, 1) // Avanzar al siguiente día
        }
    }

    private fun generarBloquesHoras(dia: String) {
        val formatoHora = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val calendario = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
        }

        val horaFin = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 15)
            set(Calendar.MINUTE, 0)
        }

        while (calendario.before(horaFin)) {
            val horaActual = formatoHora.format(calendario.time)
            val fechaHora = "$dia $horaActual"

            if (calendario.get(Calendar.HOUR_OF_DAY) == 12) {
                calendario.add(Calendar.MINUTE, 90) // Omitir hora de almuerzo
                continue
            }

            // Consultar Firebase para verificar disponibilidad
            database.child("citas").orderByChild("fechaHora").equalTo(fechaHora)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val reservada = snapshot.exists()

                        val cuadroHora = TextView(context).apply {
                            text = fechaHora
                            gravity = Gravity.CENTER
                            textSize = 14f
                            setPadding(16, 16, 16, 16)
                            isClickable = !reservada

                            if (reservada) {
                                setBackgroundColor(Color.rgb(27, 6, 27)) // Hora reservada
                                setTextColor(Color.WHITE)
                                isClickable = false // No se puede seleccionar una hora reservada
                            } else {
                                setBackgroundColor(Color.GREEN) // Hora disponible
                                setTextColor(Color.WHITE)
                                setOnClickListener {
                                    if (horaSeleccionada == this) {
                                        // Deseleccionar la hora seleccionada
                                        setBackgroundColor(Color.GREEN) // Regresa al color de disponible
                                        horaSeleccionada = null
                                    } else {
                                        // Seleccionar una nueva hora
                                        horaSeleccionada?.setBackgroundColor(Color.GREEN) // Resetear color previo
                                        setBackgroundColor(Color.RED) // Resaltar la hora seleccionada
                                        horaSeleccionada = this
                                    }
                                }


                        }
                        }

                        val params = GridLayout.LayoutParams().apply {
                            width = 0
                            height = ViewGroup.LayoutParams.WRAP_CONTENT
                            columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                            rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                            setMargins(8, 8, 8, 8)
                        }

                        gridHoras.addView(cuadroHora, params)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, "Error al cargar disponibilidad.", Toast.LENGTH_SHORT).show()
                    }
                })

            calendario.add(Calendar.MINUTE, 90) // Avanzar 1.5 horas
        }
    }
    private fun enviarCitasAFirebase() {
        val nombre = view?.findViewById<EditText>(R.id.editTextText9)?.text.toString()
        val telefono = view?.findViewById<EditText>(R.id.editTextText10)?.text.toString()
        val motivo = view?.findViewById<EditText>(R.id.editTextText11)?.text.toString()
        val fechaHora = horaSeleccionada?.text.toString()

        if (nombre.isEmpty() || telefono.isEmpty() || motivo.isEmpty() || fechaHora.isEmpty()) {
            Toast.makeText(context, "Por favor completa todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        val cita = mapOf(
            "nombre" to nombre,
            "telefono" to telefono,
            "motivo" to motivo,
            "fechaHora" to fechaHora,
            "estado" to "reservada"
        )

        database.child("citas").push().setValue(cita)
            .addOnSuccessListener {
                // Limpiar campos y cerrar la lista de horas
                limpiarCampos()
                cerrarDespliegueHoras()

                Toast.makeText(context, "Cita guardada exitosamente.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error al guardar la cita.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cerrarDespliegueHoras() {
        gridHoras.removeAllViews() // Limpiar la vista de horas desplegadas
    }

    private fun limpiarCampos() {
        view?.findViewById<EditText>(R.id.editTextText9)?.text?.clear()
        view?.findViewById<EditText>(R.id.editTextText10)?.text?.clear()
        view?.findViewById<EditText>(R.id.editTextText11)?.text?.clear()
        horaSeleccionada?.setBackgroundColor(Color.GREEN)
        horaSeleccionada = null
    }
}