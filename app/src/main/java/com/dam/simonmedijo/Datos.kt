package com.dam.simonmedijo

import android.content.Context
import android.media.SoundPool
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow

object Datos {

    var ronda = MutableStateFlow(0) // La ronda actual del juego
    var estado = MutableStateFlow(Estado.IDLE) // El estado del juego
    var secuencia = MutableStateFlow(mutableListOf<Colores>()) //La lista de colores que hay en cada ronda

    var currentColorEncendido: MutableStateFlow<Colores?> = MutableStateFlow(null) // El color que se va a encender en cada fase de la ronda

    lateinit var soundPool: SoundPool
    var sonidoVerde = 0
    var sonidoRojo = 0
    var sonidoAzul = 0
    var sonidoAmarillo = 0
    var sonidoError1 = 0
    var sonidoError2 = 0
    var sonidoError3 = 0
    var sonidoError4 = 0

}


/**
 *  Estados del juego, para el manejo de botones y texto del panel
 *  @author Daniel Figueroa Vidal
 *  @param botoneraIsActive Boolean
 *  @param botonStartIsActive Boolean
 *  @param textoPanel String
 */

enum class Estado(val botoneraIsActive: Boolean, val botonStartIsActive: Boolean, val textoPanel: String) {
        IDLE( botoneraIsActive = false, botonStartIsActive = true, textoPanel = "Presiona Start para comenzar" ),
        GENERAR_SECUENCIA( botoneraIsActive = false, botonStartIsActive = false, textoPanel = "Generando secuencia"),
        ELECCION_USUARIO( botoneraIsActive = true, botonStartIsActive = false, textoPanel = "Adivina la secuencia"),
        FINALIZADO( botoneraIsActive = false, botonStartIsActive = true, textoPanel = "Fallaste, vuelve a intentarlo")
    }

    /**
     * Colores utilizados
     * @author Daniel Figueroa Vidal
     * @param color Color
     * @param txt String
     */
    enum class Colores(val color: Color, val txt: String) {
        CLASE_ROJO(color = Color.Red, txt = "Rojo"),
        CLASE_VERDE(color = Color.Green, txt = "Verde"),
        CLASE_AZUL(color = Color.Blue, txt = "Azul"),
        CLASE_MORADO(color = Color.Magenta, txt = "Mora")
    }

/**
 * Inicializa los sonidos del juego
 * @author Daniel Figueroa Vidal
 */
fun inicializarSonidos(context: Context) { // El contexto es necesario para cargar los sonidos
    Datos.soundPool = SoundPool.Builder()
        .setMaxStreams(8) // Aumentado para manejar más sonidos simultáneos
        .build()
    Datos.sonidoVerde = Datos.soundPool.load(context, R.raw.do_alto, 1)
    Datos.sonidoRojo = Datos.soundPool.load(context, R.raw.mi, 1)
    Datos.sonidoAzul = Datos.soundPool.load(context, R.raw.sol, 1)
    Datos.sonidoAmarillo = Datos.soundPool.load(context, R.raw.do_bajo, 1)
    Datos.sonidoError1 = Datos.soundPool.load(context, R.raw.error_1, 1)
    Datos.sonidoError2 = Datos.soundPool.load(context, R.raw.error_2, 1)
    Datos.sonidoError3 = Datos.soundPool.load(context, R.raw.error_3, 1)
    Datos.sonidoError4 = Datos.soundPool.load(context, R.raw.error_4, 1)
}

