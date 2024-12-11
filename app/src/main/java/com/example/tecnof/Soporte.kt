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

class soporte : Fragment() {

    // Declare the variables for your form fields
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var messageEditText: EditText
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
        val view = inflater.inflate(R.layout.fragment_soporte, container, false)

        // Initialize the views
        nameEditText = view.findViewById(R.id.editTextNombre)
        emailEditText = view.findViewById(R.id.editTextEmail)
        phoneEditText = view.findViewById(R.id.editTextPhone)
        messageEditText = view.findViewById(R.id.editTextMessage)
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
        val name = nameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val phone = phoneEditText.text.toString().trim()
        val message = messageEditText.text.toString().trim()
        val termsAccepted = termsCheckBox.isChecked

        // Validate the inputs
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(message) || !termsAccepted) {
            Toast.makeText(context, "Por favor completa todos los campos y acepta los términos.", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a map with the form data
        val formData = HashMap<String, Any>()
        formData["name"] = name
        formData["email"] = email
        formData["phone"] = phone
        formData["message"] = message
        formData["termsAccepted"] = termsAccepted

        // Send the data to Firebase under the "soporte" node
        database.child("soporte").push().setValue(formData)
            .addOnSuccessListener {
                Toast.makeText(context, "Formulario enviado exitosamente.", Toast.LENGTH_SHORT).show()
                clearFormFields() // Clear the fields after sending the data
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error al enviar el formulario. Intenta de nuevo.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearFormFields() {
        nameEditText.text.clear()
        emailEditText.text.clear()
        phoneEditText.text.clear()
        messageEditText.text.clear()
        termsCheckBox.isChecked = false
    }

    companion object {
        // Factory method to create a new instance of the fragment
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            soporte().apply {
                arguments = Bundle().apply {
                    putString("ARG_PARAM1", param1)
                    putString("ARG_PARAM2", param2)
                }
            }
    }
}
