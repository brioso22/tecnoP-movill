package com.example.tecnof
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var progressBar: ProgressBar // Variable para la ProgressBar
    private val RC_SIGN_IN = 9001 // Código de solicitud para Google Sign-In

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        // Configuración de Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Obtén el ID del cliente desde Firebase
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        setContentView(R.layout.activity_login)

        // Inicializa la ProgressBar
        progressBar = findViewById(R.id.progressBar2)

        val googleSignInButton = findViewById<Button>(R.id.google)
        googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }

        val emailEditText = findViewById<EditText>(R.id.EmailTextText)
        val passwordEditText = findViewById<EditText>(R.id.passTextText)
        val loginButton = findViewById<Button>(R.id.button2)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Muestra la ProgressBar mientras se realiza el inicio de sesión
                progressBar.visibility = View.VISIBLE

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        // Oculta la ProgressBar una vez que el proceso haya terminado
                        progressBar.visibility = View.GONE

                        if (task.isSuccessful) {
                            // Guardar el correo electrónico en SharedPreferences
                            val sharedPreferences = getSharedPreferences("com.example.tecnof.PREFERENCES", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putString("accountEmail", email) // Guardamos el email de la cuenta
                            editor.apply()

                            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "Error en el inicio de sesión", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Por favor ingresa tu correo y contraseña", Toast.LENGTH_SHORT).show()
            }
        }

        val createAccountTextView = findViewById<TextView>(R.id.textView8)
        createAccountTextView.setOnClickListener {
            val intent = Intent(this, CrearCuenta::class.java) // CrearCuentaActivity representa la actividad para el registro de usuarios
            startActivity(intent)
        }

        val facebookImageView = findViewById<ImageView>(R.id.afecView2)
        val instagramImageView = findViewById<ImageView>(R.id.instaView)
        val youtube = findViewById<ImageView>(R.id.youtube)


        facebookImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/profile.php?id=61568113989326"))
            startActivity(intent)
        }

        instagramImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/tecnofusion"))
            startActivity(intent)
        }

        youtube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/playlist?list=PL7LQ3sL-u0HbysreGJwRDW2yWNAGP8D0U"))
            startActivity(intent)
        }
    }

    private fun signInWithGoogle() {
        // Muestra la ProgressBar mientras se realiza el inicio de sesión con Google
        progressBar.visibility = View.VISIBLE

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE // Oculta la ProgressBar si hay error
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                progressBar.visibility = View.GONE // Oculta la ProgressBar

                if (task.isSuccessful) {
                    // Guardar el correo electrónico en SharedPreferences
                    val sharedPreferences = getSharedPreferences("com.example.tecnof.PREFERENCES", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("accountEmail", account?.email) // Guardamos el email de la cuenta
                    editor.apply()

                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Error en el inicio de sesión con Google", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
