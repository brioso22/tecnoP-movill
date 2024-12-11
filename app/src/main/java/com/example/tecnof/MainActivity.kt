package com.example.tecnof

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.tecnof.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Verificar si el usuario está autenticado
        val user = auth.currentUser
        if (user == null) {
            // Si no hay usuario, redirige al Login
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish() // Finaliza la actividad actual para evitar regresar a Main
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        // Configura el Floating Action Button
        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }

        // Configuración de Drawer y Navigation
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_tienda, R.id.nav_soporte, R.id.nav_faq, R.id.nav_donaciones, R.id.nav_perf
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Configura el botón para abrir Google Maps
        val botonMaps: Button = findViewById(R.id.boton_maps)
        botonMaps.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:0,0?q=GQCX+38, San Miguel")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

            // Verificar si Google Maps está instalado
            val packageManager = packageManager
            try {
                mapIntent.setPackage("com.google.android.apps.maps")

                // Iniciar la actividad de Maps si está disponible
                startActivity(mapIntent)

            } catch (e: PackageManager.NameNotFoundException) {
                // Mostrar mensaje si Google Maps no está instalado
                Snackbar.make(botonMaps, "Google Maps no está instalado", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, Settings::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
