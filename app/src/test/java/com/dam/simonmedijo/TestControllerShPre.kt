package com.dam.simonmedijo

import android.content.Context
import android.content.SharedPreferences
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
// Web de Mockito : https://site.mockito.org/
/**
 * Clase de pruebas unitarias para [ControllerShPre].
 * Utiliza Mockito para simular las dependencias de Android como [Context] y [SharedPreferences].
 */
@RunWith(MockitoJUnitRunner::class)
class TestControllerShPre {

    // Mock del contexto de Android.
    @Mock
    private lateinit var mockContext: Context

    // Mock de las SharedPreferences.
    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    // Mock del editor de SharedPreferences.
    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    // Captor para verificar el valor entero (record) guardado.
    @Captor
    private lateinit var recordCaptor: ArgumentCaptor<Int>

    // Captor para verificar el valor String (fecha) guardado.
    @Captor
    private lateinit var fechaCaptor: ArgumentCaptor<String>

    // Formato de fecha utilizado para guardar y leer la fecha del récord.
    private val formatoFecha = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)

    /**
     * Configuración inicial para cada test.
     * Se establecen los comportamientos por defecto de los mocks.
     */
    @Before
    fun setUp() {

        // Cuando se pida SharedPreferences, devolver el mock.
        whenever(mockContext.getSharedPreferences(any(), any())).thenReturn(mockSharedPreferences)
        // Cuando se pida editar las SharedPreferences, devolver el mock del editor.
        whenever(mockSharedPreferences.edit()).thenReturn(mockEditor)

        // Configurar el comportamiento encadenado del editor.
        whenever(mockEditor.putInt(any(), any())).thenReturn(mockEditor)
        whenever(mockEditor.putString(any(), any())).thenReturn(mockEditor)

        // Simular la llamada a apply(), que no devuelve nada.
        whenever(mockEditor.apply()).then { }
    }

    /**
     * Prueba que [ControllerShPre.obtenerRecord] devuelve correctamente los datos
     * cuando ya existen un récord y una fecha guardados en SharedPreferences.
     */
    @Test
    fun obtenerRecord_devuelve_datos_guardados() {

        // Preparación de datos de prueba.
        val fechaGuardada = Date()
        val fechaGuardadaString = formatoFecha.format(fechaGuardada)

        // Simular que SharedPreferences devuelve un récord y una fecha.
        whenever(mockSharedPreferences.getInt("record", 0)).thenReturn(10)
        whenever(mockSharedPreferences.getString("fecha", null)).thenReturn(fechaGuardadaString)

        // Llamada al método a probar.
        val resultado = ControllerShPre.obtenerRecord(mockContext)

        assertEquals(10, resultado.record)
        assertEquals(fechaGuardada.time / 1000, resultado.fecha.time / 1000)
    }

    /**
     * Prueba que [ControllerShPre.obtenerRecord] devuelve los valores por defecto
     * (récord 0 y fecha actual) cuando no hay datos guardados en SharedPreferences.
     */
    @Test
    fun obtenerRecord_devuelve_valores_por_defecto_si_no_hay_datos() {

        // Simular que SharedPreferences no tiene datos guardados.
        whenever(mockSharedPreferences.getInt("record", 0)).thenReturn(0)
        whenever(mockSharedPreferences.getString("fecha", null)).thenReturn(null)

        // Llamada al método a probar.
        val resultado = ControllerShPre.obtenerRecord(mockContext)

        // Verificación del récord por defecto.
        assertEquals(0, resultado.record)

        // Verificación de que la fecha devuelta es la actual (con un margen de error pequeño).
        val ahora = Date().time / 1000
        val fechaResultado = resultado.fecha.time / 1000

        assertTrue((ahora - fechaResultado) < 2)
    }

    /**
     * Prueba que [ControllerShPre.actualizarRecord] guarda correctamente los nuevos
     * valores de récord y fecha en SharedPreferences.
     */
    @Test
    fun actualizarRecord_guarda_valores_correctos() {

        // Preparación de datos de prueba.
        val nuevoRecord = 25
        val nuevaFecha = Date()
        val nuevaFechaString = formatoFecha.format(nuevaFecha)

        // Llamada al método a probar.
        ControllerShPre.actualizarRecord(nuevoRecord, nuevaFecha, mockContext)

        // Verificar que los métodos de guardado se llamaron con las claves correctas y capturar los valores.
        verify(mockEditor).putInt(eq("record"), recordCaptor.capture())
        verify(mockEditor).putString(eq("fecha"), fechaCaptor.capture())
        verify(mockEditor).apply()

        // Verificar que los valores capturados son los correctos.
        assertEquals(nuevoRecord, recordCaptor.value)
        assertEquals(nuevaFechaString, fechaCaptor.value)
    }
}
