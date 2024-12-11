package com.example.tecnof

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

data class GarantiaForm(
    val codigoFactura: String,
    val nombreCliente: String,
    val telefono: String,
    val email: String,
    val descripcion: String,
    val termsAccepted: Boolean
)

class navgarantia : Fragment() {

    // Declare the variables for your form fields
    private lateinit var codigoFacturaEditText: EditText
    private lateinit var nombreClienteEditText: EditText
    private lateinit var telefonoEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var descripcionEditText: EditText
    private lateinit var termsCheckBox: CheckBox
    private lateinit var submitButton: Button

    // Firebase Database Reference
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase
        database = FirebaseDatabase.getInstance().reference
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_navgarantia, container, false)

        // Initialize the views
        codigoFacturaEditText = view.findViewById(R.id.editTextCodigoFactura)
        nombreClienteEditText = view.findViewById(R.id.editTextNombreCliente)
        telefonoEditText = view.findViewById(R.id.editTextTelefono)
        emailEditText = view.findViewById(R.id.editTextEmail)
        descripcionEditText = view.findViewById(R.id.editTextDescripcion)
        termsCheckBox = view.findViewById(R.id.checkboxTerms)
        submitButton = view.findViewById(R.id.buttonEnviar)

        // Set up the click listener for the submit button
        submitButton.setOnClickListener {
            sendFormDataToFirebase()
        }

        return view
    }

    private fun sendFormDataToFirebase() {
        // Get the values from the EditTexts and CheckBox
        val codigoFactura = codigoFacturaEditText.text.toString().trim()
        val nombreCliente = nombreClienteEditText.text.toString().trim()
        val telefono = telefonoEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val descripcion = descripcionEditText.text.toString().trim()
        val termsAccepted = termsCheckBox.isChecked

        // Validate the inputs
        if (TextUtils.isEmpty(codigoFactura) || TextUtils.isEmpty(nombreCliente) || TextUtils.isEmpty(telefono) || TextUtils.isEmpty(email) || TextUtils.isEmpty(descripcion) || !termsAccepted) {
            Toast.makeText(requireContext(), "Por favor completa todos los campos y acepta los términos.", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a form data object
        val formData = GarantiaForm(
            codigoFactura,
            nombreCliente,
            telefono,
            email,
            descripcion,
            termsAccepted
        )

        // Send the data to Firebase under the "garantia" node
        database.child("garantia").push().setValue(formData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Formulario enviado exitosamente.", Toast.LENGTH_SHORT).show()
                clearFormFields() // Clear the fields after sending the data
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al enviar el formulario. Intenta de nuevo.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearFormFields() {
        codigoFacturaEditText.text.clear()
        nombreClienteEditText.text.clear()
        telefonoEditText.text.clear()
        emailEditText.text.clear()
        descripcionEditText.text.clear()
        termsCheckBox.isChecked = false
    }
}
