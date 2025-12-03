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



@RunWith(MockitoJUnitRunner::class)
class TestControllerShPre {

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockSharedPreferences: SharedPreferences

    @Mock
    private lateinit var mockEditor: SharedPreferences.Editor

    @Captor
    private lateinit var recordCaptor: ArgumentCaptor<Int>

    @Captor
    private lateinit var fechaCaptor: ArgumentCaptor<String>

    private val formatoFecha = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)

    @Before
    fun setUp() {

        whenever(mockContext.getSharedPreferences(any(), any())).thenReturn(mockSharedPreferences)
        whenever(mockSharedPreferences.edit()).thenReturn(mockEditor)

        whenever(mockEditor.putInt(any(), any())).thenReturn(mockEditor)
        whenever(mockEditor.putString(any(), any())).thenReturn(mockEditor)

        // apply no devuelve nada
        whenever(mockEditor.apply()).then { }
    }

    @Test
    fun obtenerRecord_devuelve_datos_guardados() {

        val fechaGuardada = Date()
        val fechaGuardadaString = formatoFecha.format(fechaGuardada)

        whenever(mockSharedPreferences.getInt("record", 0)).thenReturn(10)
        whenever(mockSharedPreferences.getString("fecha", null)).thenReturn(fechaGuardadaString)

        val resultado = ControllerShPre.obtenerRecord(mockContext)

        assertEquals(10, resultado.record)
        assertEquals(fechaGuardada.time / 1000, resultado.fecha.time / 1000)
    }

    @Test
    fun obtenerRecord_devuelve_valores_por_defecto_si_no_hay_datos() {

        whenever(mockSharedPreferences.getInt("record", 0)).thenReturn(0)
        whenever(mockSharedPreferences.getString("fecha", null)).thenReturn(null)

        val resultado = ControllerShPre.obtenerRecord(mockContext)

        assertEquals(0, resultado.record)

        val ahora = Date().time / 1000
        val fechaResultado = resultado.fecha.time / 1000

        assertTrue((ahora - fechaResultado) < 2)
    }

    @Test
    fun actualizarRecord_guarda_valores_correctos() {

        val nuevoRecord = 25
        val nuevaFecha = Date()
        val nuevaFechaString = formatoFecha.format(nuevaFecha)

        ControllerShPre.actualizarRecord(nuevoRecord, nuevaFecha, mockContext)

        verify(mockEditor).putInt(eq("record"), recordCaptor.capture())
        verify(mockEditor).putString(eq("fecha"), fechaCaptor.capture())
        verify(mockEditor).apply()

        assertEquals(nuevoRecord, recordCaptor.value)
        assertEquals(nuevaFechaString, fechaCaptor.value)
    }
}
