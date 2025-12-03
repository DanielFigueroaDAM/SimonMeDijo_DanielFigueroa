package com.dam.simonmedijo

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class TestMyViewModel {

    private lateinit var app: Application
    private lateinit var viewModel: MyVM

    @Before
    fun setup() {
        // Mock del Application
        app = mock(Application::class.java)

        // Mock del objeto ControllerShPre
        mockkObject(ControllerShPre)
        every { ControllerShPre.obtenerRecord(app) } returns Record(0, Date())
        justRun { ControllerShPre.actualizarRecord(any(), any(), any()) }

        // Inicializamos el ViewModel
        viewModel = MyVM(app)

        // Reiniciamos Datos
        Datos.secuencia.value = mutableListOf()
        Datos.estado.value = Estado.IDLE
        Datos.ronda.value = 0
        Datos.currentColorEncendido.value = null
    }

    @Test
    fun `comprobarEleccionEnSecuencia devuelve true si es correcta`() {
        Datos.secuencia.value = mutableListOf(Colores.CLASE_ROJO, Colores.CLASE_AZUL)
        val resultado = viewModel.comprobarEleccionEnSecuencia(Colores.CLASE_ROJO, 0)
        assertTrue(resultado)
    }

    @Test
    fun `comprobarEleccionEnSecuencia devuelve false si es incorrecta`() {
        Datos.secuencia.value = mutableListOf(Colores.CLASE_ROJO, Colores.CLASE_AZUL)
        val resultado = viewModel.comprobarEleccionEnSecuencia(Colores.CLASE_VERDE, 0)
        assertFalse(resultado)
    }

    @Test
    fun `añadirColorASecuencia aumenta tamaño de secuencia`() {
        val tamañoInicial = Datos.secuencia.value.size
        viewModel.añadirColorASecuencia()
        assertEquals(tamañoInicial + 1, Datos.secuencia.value.size)
    }


    @Test
    fun `colorSeleccionado reinicia secuencia si es incorrecto`() {
        Datos.secuencia.value = mutableListOf(Colores.CLASE_ROJO)
        viewModel.posicion = 0
        viewModel.colorSeleccionado(Colores.CLASE_VERDE)
        assertEquals(0, viewModel.posicion)
        assertEquals(0, Datos.ronda.value)
        assertTrue(Datos.secuencia.value.isEmpty())
        assertEquals(Estado.FINALIZADO, Datos.estado.value)
    }


    @Test
    fun `volverAlIdle pone estado en IDLE`() {
        Datos.estado.value = Estado.FINALIZADO
        viewModel.volverAlIdle()
        assertEquals(Estado.IDLE, Datos.estado.value)
    }

    @Test
    fun `iniciarJuego agrega color y cambia estado`() = runTest {
        val tamañoInicial = Datos.secuencia.value.size
        viewModel.iniciarJuego()
        // Se debe agregar un color a la secuencia
        assertEquals(tamañoInicial + 1, Datos.secuencia.value.size)
        // El estado debería estar en GENERAR_SECUENCIA o ELECCION_USUARIO según timing
        assertTrue(Datos.estado.value == Estado.GENERAR_SECUENCIA || Datos.estado.value == Estado.ELECCION_USUARIO)
    }
}
