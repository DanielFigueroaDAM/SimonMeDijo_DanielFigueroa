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
/**
 * Clase de pruebas unitarias para [MyVM].
 * Utiliza Robolectric para emular el entorno de Android y MockK para la creación de mocks.
 * @property app Instancia de la aplicación Android, mockeada para las pruebas.
 * @property viewModel Instancia del ViewModel [MyVM] que se está probando.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class TestMyViewModel {

    private lateinit var app: Application
    private lateinit var viewModel: MyVM
    /**
     * Configuración inicial para cada prueba.
     * - Mockea la clase [Application].
     * - Mockea el objeto [ControllerShPre] para simular la obtención y actualización de récords.
     * - Inicializa [MyVM] con la aplicación mockeada.
     * - Reinicia los datos del juego (secuencia, estado, ronda, etc.) a su estado inicial.
     */
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

    /**
     * Prueba que [MyVM.comprobarEleccionEnSecuencia] devuelve `true` cuando el color
     * y la posición seleccionados por el usuario son correctos.
     */
    @Test
    fun `comprobarEleccionEnSecuencia devuelve true si es correcta`() {
        Datos.secuencia.value = mutableListOf(Colores.CLASE_ROJO, Colores.CLASE_AZUL)
        val resultado = viewModel.comprobarEleccionEnSecuencia(Colores.CLASE_ROJO, 0)
        assertTrue(resultado)
    }
    /**
     * Prueba que [MyVM.comprobarEleccionEnSecuencia] devuelve `false` cuando el color
     * seleccionado por el usuario es incorrecto para la posición actual.
     */
    @Test
    fun `comprobarEleccionEnSecuencia devuelve false si es incorrecta`() {
        Datos.secuencia.value = mutableListOf(Colores.CLASE_ROJO, Colores.CLASE_AZUL)
        val resultado = viewModel.comprobarEleccionEnSecuencia(Colores.CLASE_VERDE, 0)
        assertFalse(resultado)
    }
    /**
     * Prueba que [MyVM.añadirColorASecuencia] incrementa el tamaño de la secuencia del juego en uno.
     */
    @Test
    fun `añadirColorASecuencia aumenta tamaño de secuencia`() {
        val tamañoInicial = Datos.secuencia.value.size
        viewModel.añadirColorASecuencia()
        assertEquals(tamañoInicial + 1, Datos.secuencia.value.size)
    }
    /**
     * Prueba que al seleccionar un color incorrecto, el juego se reinicia.
     * - La posición del jugador vuelve a 0.
     * - La ronda vuelve a 0.
     * - La secuencia de colores se vacía.
     * - El estado del juego cambia a [Estado.FINALIZADO].
     */
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
    /**
     * Prueba que el método [MyVM.volverAlIdle] cambia el estado del juego a [Estado.IDLE].
     */
    @Test
    fun `volverAlIdle pone estado en IDLE`() {
        Datos.estado.value = Estado.FINALIZADO
        viewModel.volverAlIdle()
        assertEquals(Estado.IDLE, Datos.estado.value)
    }

    /**
     * Prueba que [MyVM.iniciarJuego] correctamente inicia el juego.
     * - Añade un nuevo color a la secuencia.
     * - Cambia el estado del juego para comenzar la secuencia o esperar la elección del usuario.
     */
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
