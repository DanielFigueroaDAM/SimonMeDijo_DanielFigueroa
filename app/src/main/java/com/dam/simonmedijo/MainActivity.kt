package com.dam.simonmedijo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.dam.simonmedijo.model.inicializarSonidos
import com.dam.simonmedijo.ui.theme.SimonMeDijoTheme
import com.dam.simonmedijo.view.ViewAll

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inicializarSonidos(this)
        enableEdgeToEdge()
        setContent {
            SimonMeDijoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ViewAll(application)
                }
            }
        }
    }
}

