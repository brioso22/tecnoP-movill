package com.example.tecnof

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Referencia al ImageView en el layout
        val gifImageView: ImageView = findViewById(R.id.gifImageView)

        // Cargar el GIF usando Glide
        Glide.with(this)
            .asGif()
            .load(R.drawable.logo__1_) // Reemplaza "logo" con el nombre de tu archivo GIF en drawable
            .into(gifImageView)

        // Duración del splash en milisegundos
        Handler(Looper.getMainLooper()).postDelayed({
            // Verificar si hay cuenta registrada
            val sharedPreferences = getSharedPreferences("com.example.tecnof.PREFERENCES", Context.MODE_PRIVATE)

            // Verificar si hay una cuenta registrada almacenando algún dato (por ejemplo, un correo electrónico o nombre de usuario)
            val accountStored = sharedPreferences.getString("accountEmail", null) // "accountEmail" es la clave de ejemplo

            // Verificar si se encontró un email registrado (lo que indica que hay una cuenta)
            if (accountStored != null) {
                // Si existe una cuenta, redirigir a la pantalla principal
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // Si no existe una cuenta, redirigir a la pantalla de login
                startActivity(Intent(this, Login::class.java))
            }

            finish() // Finaliza la actividad actual para que no se vuelva a abrir al presionar atrás
        }, 2000) // Cambia 2000 por la duración deseada en milisegundos
    }
}
