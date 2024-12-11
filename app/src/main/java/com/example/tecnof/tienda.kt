package com.example.tecnof

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.net.http.SslError
import androidx.fragment.app.Fragment
import com.example.tecnof.databinding.FragmentTiendaBinding

class tienda : Fragment() {

    private var _binding: FragmentTiendaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTiendaBinding.inflate(inflater, container, false)

        // Configuración del WebView
        val webView: WebView = binding.root.findViewById(R.id.webView)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                handler.proceed() // Ignora errores SSL en fase de prueba
            }
        }

        webView.loadUrl("https://tecno-fusion-360.onrender.com/tienda/tienda_movil") // Reemplaza con la IP y puerto de tu servidor

        // Habilita JavaScript si es necesario
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
