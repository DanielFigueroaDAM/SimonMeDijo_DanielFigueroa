package com.dam.simonmedijo

import android.media.SoundPool
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Date

object Record {
    var record = MutableStateFlow(0) // El record persistente del juego
    var fecha = MutableStateFlow<Date>( Date()) // La fecha de la última partida
}

