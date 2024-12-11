package com.example.tecnof

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.AuthCredential

class CrearCuenta : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var progressBar: ProgressBar

    private lateinit var nombreClienteEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var createAccountButton: Button
    private lateinit var googleButton: Button

    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cuenta)

        // Inicializa FirebaseAuth y DatabaseReference
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        progressBar = findViewById(R.id.progressBar)

        // Referencias a los elementos de la interfaz
        nombreClienteEditText = findViewById(R.id.usernameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmarpass)
        createAccountButton = findViewById(R.id.createAccountButton)
        googleButton = findViewById(R.id.google)

        // Configura los listeners para crear cuenta y para el inicio con Google
        createAccountButton.setOnClickListener { createAccount() }
        googleButton.setOnClickListener { signInWithGoogle() }
    }

    private fun createAccount() {
        progressBar.visibility = View.VISIBLE

        val nombreCliente = nombreClienteEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val confirmPassword = confirmPasswordEditText.text.toString().trim()

        if (TextUtils.isEmpty(nombreCliente) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            progressBar.visibility = View.GONE
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            progressBar.visibility = View.GONE
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(nombreCliente)
                            .build()
                        it.updateProfile(profileUpdates).addOnCompleteListener { profileTask ->
                            if (profileTask.isSuccessful) {
                                saveUserInDatabase(user, nombreCliente, email)
                            } else {
                                Toast.makeText(this, "Error al guardar el nombre", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "Error al crear cuenta: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserInDatabase(user: FirebaseUser, nombreCliente: String, email: String) {
        val userId = user.uid
        val userData = mapOf("nombre" to nombreCliente, "email" to email)

        database.child("usuarios").child(userId).setValue(userData)
            .addOnSuccessListener {
                Toast.makeText(this, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show()
                goToMainActivity()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al guardar los datos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun signInWithGoogle() {
        progressBar.visibility = View.VISIBLE

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account?.idToken
                if (idToken != null) {
                    firebaseAuthWithGoogle(idToken)
                } else {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Error: No se pudo obtener el token de Google", Toast.LENGTH_SHORT).show()
                }
            } catch (e: ApiException) {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Google sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val userId = user?.uid
                    val userInfo = mapOf("nombre" to user?.displayName, "email" to user?.email)
                    userId?.let {
                        database.child("usuarios").child(it).setValue(userInfo)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Cuenta creada con Google", Toast.LENGTH_SHORT).show()
                                goToMainActivity()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Error al guardar los datos: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(this, "Error al iniciar sesión con Google: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
