package com.example.tecnof

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.squareup.picasso.Picasso

class Perfil : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance() // Inicializar FirebaseAuth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout para este fragment
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)

        // Obtener referencias a los elementos de la interfaz
        val nombreTextView = view.findViewById<TextView>(R.id.nombre)
        val gmailTextView = view.findViewById<TextView>(R.id.gmail)
        val fotoPerfilImageView = view.findViewById<ImageView>(R.id.fotoPerfil) // ImageView para la foto de perfil
        val cerrarSeccionButton = view.findViewById<Button>(R.id.CerrarSeccion)

        // Obtener los datos del usuario y mostrarlos en los TextView
        val user: FirebaseUser? = auth.currentUser
        user?.let {
            nombreTextView.text = it.displayName ?: "Nombre no disponible"
            gmailTextView.text = it.email ?: "Correo no disponible"

            // Cargar la foto de perfil si está disponible
            it.photoUrl?.let { photoUrl ->
                // Usar Picasso para cargar la imagen
                Picasso.get().load(photoUrl).into(fotoPerfilImageView)
            } ?: run {
                // Si no hay foto de perfil, asignar una predeterminada
                fotoPerfilImageView.setImageResource(R.drawable.ic_menu_camera)
            }
        }

        // Configurar listener para cerrar sesión
        cerrarSeccionButton.setOnClickListener {
            cerrarSesion()
        }

        return view
    }



    private fun cerrarSesion() {
        auth.signOut() // Cierra la sesión de Firebase
        GoogleSignIn.getClient(requireContext(), GoogleSignInOptions.DEFAULT_SIGN_IN).signOut() // Cierra la sesión de Google

        // Elimina el historial de las actividades
        val intent = Intent(activity, Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Esto borra las actividades anteriores del stack
        startActivity(intent)

        // Finaliza la actividad actual para evitar que el usuario regrese
        activity?.finish()

        // Muestra un mensaje de confirmación
        Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
    }

}
