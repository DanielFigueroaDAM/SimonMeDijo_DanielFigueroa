package com.dam.simonmedijo

import android.app.Application
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class MyVM(application: Application) : AndroidViewModel(application){

    var record = MutableStateFlow(ControllerShPre.obtenerRecord(getApplication()).record) // El record persistente del juego

    var posicion = 0 // Esta es la posición de secuencia de elección del usuario



    init {
        record.value = ControllerShPre.obtenerRecord(getApplication()).record // Obtenemos el record de las preferencias
    }

    /**
     * Comprueba si el color seleccionado por el usuario es el mismo que el de la secuencia
     * @param color Colores
     * @param numeroSecuencia Int
     * @author Daniel Figueroa Vidal
     * @return Boolean
     */
    fun comprobarEleccionEnSecuencia(color: Colores, numeroSecuencia: Int):Boolean{ //
        if(Datos.secuencia.value[numeroSecuencia] == color){
            return true
        }
        return false
    }

    /**
     * Realiza la secuencia de colores, se ilumina el color aleatorio
     * @author Daniel Figueroa Vidal
     */
    fun realizarSecuencia(){
        viewModelScope.launch {
            Datos.estado.value = Estado.GENERAR_SECUENCIA
            Log.d("App", "Estado de la secuencia: ${Datos.estado.value}")

            for(color in Datos.secuencia.value){
                Datos.currentColorEncendido.value = color
                delay(1000)
                Datos.currentColorEncendido.value = null // Manejo de nulos, si se pone en nulo en la interfaz no se enciende ningun color
                delay(1000)
            }
            Datos.estado.value = Estado.ELECCION_USUARIO // Cuando termina la secuencia se cambia el estado a ELECCION_USUARIO
            Log.d("App", "Estado de la secuencia: ${Datos.estado.value}")
        }
    }

    /**
     * Añade un color aleatorio a la secuencia
     * @author Daniel Figueroa Vidal
     */
    fun añadirColorASecuencia(){
        var colorAleatorio  = Colores.entries.toTypedArray().random()
        Datos.secuencia.value.add(colorAleatorio)
    }

    /**
     * Inicia el juego, se genera la secuencia y se inicia la secuencia de colores
     * @author Daniel Figueroa Vidal
     */
    fun iniciarJuego(){
        añadirColorASecuencia()
        realizarSecuencia()
    }

    /**
     * Comprueba si el color seleccionado por el usuario es el mismo que el de la secuencia
     * Este es el método que comparten los botones de colores en su onclick
     * @author Daniel Figueroa Vidal
     * @param colorSelect Colores
     */
    fun colorSeleccionado(colorSelect:Colores){
        //Comprobamos si la elección del usuario es correcta
        if(comprobarEleccionEnSecuencia(colorSelect, posicion)){
            posicion++ // Si es correcta aumentamos la posición
            if(posicion == Datos.secuencia.value.size) { // Si hemos llegado al final de la secuencia
                //Se completa una ronda
                Log.d("App", "Completaste una ronda")
                Datos.ronda.value++ // Aumentamos la ronda
                añadirColorASecuencia() // Añadimos un color a la secuencia
                realizarSecuencia() // Realizamos la visualizacion de la secuencia
                posicion = 0 // Reiniciamos la posición
            }

        }else{ // Si el usuario falla la secuencia

            Datos.secuencia.value = mutableListOf() // Reiniciamos la secuencia
            comprobarRecord() // Comprobamos si es record, para actualizarlo si hace falta
            Datos.ronda.value = 0
            posicion = 0
            Log.d("App", "ERROR")
            Datos.estado.value = Estado.FINALIZADO //Cambiamos el estado para el correcto manejo de botones
        }


    }

    /**
     * Volvemos al estado IDLE
     */
    fun volverAlIdle(){ // Volvemos al estado IDLE
        Datos.estado.value = Estado.IDLE
    }

    /**
     * Comprueba si el record es mayor que la ronda actual
     * @author Daniel Figueroa Vidal
     */
    fun comprobarRecord(){
        if(Datos.ronda.value > ControllerShPre.obtenerRecord(getApplication()).record) {
            record.value = Datos.ronda.value
            ControllerShPre.actualizarRecord(Datos.ronda.value, Date(), getApplication())
        }
    }

}








