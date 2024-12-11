package com.example.tecnof

import android.app.DatePickerDialog
import android.icu.text.DateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

class donaciones : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("onCreate: Fragmento Donaciones creado")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("onCreateView: Inflando el layout del fragmento")
        val view = inflater.inflate(R.layout.fragment_donaciones, container, false)

        // Inicializamos la referencia de Firebase
        database = FirebaseDatabase.getInstance().reference

        // Obtener el ID del usuario actual
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // Referencias a los campos de texto y al botón
        val nombreEmpresaEditText = view.findViewById<EditText>(R.id.editTextText)
        val telefonoEditText = view.findViewById<EditText>(R.id.editTextPhone)
        val emailEditText = view.findViewById<EditText>(R.id.editTextTextEmailAddress)
        val descripcionEditText = view.findViewById<EditText>(R.id.editTextText3)
        val fechaEditText = view.findViewById<EditText>(R.id.editTextDate)
        val ubicacionEditText = view.findViewById<EditText>(R.id.editTextText4)
        val comentarioEditText = view.findViewById<EditText>(R.id.editTextText5)
        val enviarButton = view.findViewById<Button>(R.id.button3)

        // Configuración del DatePickerDialog para el campo de fecha
        val calendar = Calendar.getInstance()

        fechaEditText.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    // Actualizar el calendario y mostrar la fecha seleccionada en el EditText
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    fechaEditText.setText(formatoFecha.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Acción cuando se presiona el botón "Enviar"
        enviarButton.setOnClickListener {
            // Obtener los datos ingresados
            val nombreEmpresa = nombreEmpresaEditText.text.toString().trim()
            val telefono = telefonoEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val descripcion = descripcionEditText.text.toString().trim()
            val fecha = fechaEditText.text.toString().trim()
            val ubicacion = ubicacionEditText.text.toString().trim()
            val comentario = comentarioEditText.text.toString().trim()

            // Validar que los campos requeridos no estén vacíos
            if (nombreEmpresa.isEmpty() || telefono.isEmpty() || descripcion.isEmpty() || fecha.isEmpty() || ubicacion.isEmpty() || email.isEmpty()) {
                Toast.makeText(context, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Verificar el número de donaciones del usuario para ese día
                val fechaHoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val donacionesRef = database.child("donaciones").child(userId).child(fechaHoy)

                donacionesRef.get().addOnSuccessListener { snapshot ->
                    val count = snapshot.child("count").getValue(Int::class.java) ?: 0

                    if (count < 2) {
                        // Guardar la donación si no se ha superado el límite
                        val donacionId = donacionesRef.push().key
                        if (donacionId != null) {
                            // Guardar los datos de la donación
                            val donacionData = hashMapOf(
                                "nombreEmpresa" to nombreEmpresa,
                                "telefono" to telefono,
                                "email" to email,
                                "descripcion" to descripcion,
                                "fecha" to fecha,
                                "ubicacion" to ubicacion,
                                "comentario" to comentario
                            )

                            donacionesRef.child(donacionId).setValue(donacionData)
                                .addOnSuccessListener {
                                    // Incrementar el contador de donaciones
                                    donacionesRef.child("count").setValue(count + 1)

                                    // Mostrar mensaje de éxito
                                    Toast.makeText(context, "Datos enviados correctamente", Toast.LENGTH_SHORT).show()

                                    // Limpiar los campos de texto después de enviar los datos
                                    nombreEmpresaEditText.text.clear()
                                    telefonoEditText.text.clear()
                                    emailEditText.text.clear()
                                    descripcionEditText.text.clear()
                                    fechaEditText.text.clear()
                                    ubicacionEditText.text.clear()
                                    comentarioEditText.text.clear()
                                }
                                .addOnFailureListener { e ->
                                    // Mostrar mensaje de error con detalle
                                    Toast.makeText(context, "Error al enviar los datos: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            // Si el ID no se pudo generar
                            Toast.makeText(context, "Error al generar ID para la donación", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Mostrar mensaje si el usuario ha alcanzado el límite de 2 donaciones
                        Toast.makeText(context, "Ya has realizado 2 donaciones hoy", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    // Error al leer el número de donaciones
                    Toast.makeText(context, "Error al verificar las donaciones", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }
}
