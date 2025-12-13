package com.dam.simonmedijo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import com.dam.simonmedijo.controller.FeedReaderDbHelper
import com.dam.simonmedijo.controller.miTabla
import com.dam.simonmedijo.model.Record
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.Date

// Web de Mockito: https://site.mockito.org/
// Robolectric: https://robolectric.org/

/**
 * Clase de pruebas unitarias para [com.dam.simonmedijo.controller.ControllerSQLite].
 * Utiliza Robolectric para simular el entorno de Android y Mockito para los mocks.
 */
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, sdk = [29])
class TestControllerSQLite {

    // Contexto de Android proporcionado por Robolectric
    private lateinit var context: Context

    // Mock del DbHelper
    @Mock
    private lateinit var mockDbHelper: FeedReaderDbHelper

    // Mock de la base de datos de lectura
    @Mock
    private lateinit var mockReadableDatabase: SQLiteDatabase

    // Mock de la base de datos de escritura
    @Mock
    private lateinit var mockWritableDatabase: SQLiteDatabase

    // Mock del cursor
    @Mock
    private lateinit var mockCursor: Cursor

    // Captor para verificar los ContentValues en la inserción
    @Captor
    private lateinit var contentValuesCaptor: ArgumentCaptor<ContentValues>

    /**
     * Configuración inicial para cada test.
     * Se establecen los comportamientos por defecto de los mocks.
     */
    @Before
    fun setUp() {
        // Inicializar Robolectric
        context = ApplicationProvider.getApplicationContext()

        // Inicializar Mockito manualmente (ya que no usamos @RunWith(MockitoJUnitRunner::class))
        MockitoAnnotations.openMocks(this)
    }

    /**
     * Prueba que [com.dam.simonmedijo.controller.ControllerSQLite.obtenerRecord] devuelve correctamente los datos
     * cuando existe un registro en la base de datos.
     */
    @Test
    fun obtenerRecord_devuelve_datos_guardados() {
        // Preparación de datos de prueba
        val fechaEsperada = Date()
        val recordEsperado = 150

        // Configurar mocks
        val projection = arrayOf(miTabla.PUNTUACION, miTabla.FECHA)
        val sortOrder = "${miTabla.FECHA} DESC"
        val limit = "1"

        whenever(mockDbHelper.readableDatabase).thenReturn(mockReadableDatabase)
        whenever(mockCursor.moveToFirst()).thenReturn(true)
        whenever(mockCursor.getInt(any())).thenReturn(recordEsperado)
        whenever(mockCursor.getLong(any())).thenReturn(fechaEsperada.time)
        whenever(mockCursor.getColumnIndexOrThrow(miTabla.PUNTUACION)).thenReturn(0)
        whenever(mockCursor.getColumnIndexOrThrow(miTabla.FECHA)).thenReturn(1)

        whenever(mockReadableDatabase.query(
            eq(miTabla.TABLE_NAME),
            eq(projection),
            eq(null),
            eq(null),
            eq(null),
            eq(null),
            eq(sortOrder),
            eq(limit)
        )).thenReturn(mockCursor)

        // Llamada al método a probar (versión modificada para testing)
        val resultado = obtenerRecordConMock(mockDbHelper)

        assertEquals(recordEsperado, resultado.record)
        assertEquals(fechaEsperada.time, resultado.fecha.time)

        // Verificar que se cerraron los recursos
        verify(mockCursor).close()
        verify(mockReadableDatabase).close()
    }

    /**
     * Prueba que [com.dam.simonmedijo.controller.ControllerSQLite.obtenerRecord] devuelve valores por defecto
     * cuando no hay registros en la base de datos.
     */
    @Test
    fun obtenerRecord_devuelve_valores_por_defecto_si_no_hay_datos() {
        // Configurar mocks
        val projection = arrayOf(miTabla.PUNTUACION, miTabla.FECHA)
        val sortOrder = "${miTabla.FECHA} DESC"
        val limit = "1"

        whenever(mockDbHelper.readableDatabase).thenReturn(mockReadableDatabase)
        whenever(mockCursor.moveToFirst()).thenReturn(false)

        whenever(mockReadableDatabase.query(
            eq(miTabla.TABLE_NAME),
            eq(projection),
            eq(null),
            eq(null),
            eq(null),
            eq(null),
            eq(sortOrder),
            eq(limit)
        )).thenReturn(mockCursor)

        // Llamada al método a probar (versión modificada para testing)
        val resultado = obtenerRecordConMock(mockDbHelper)

        assertEquals(0, resultado.record)

        // Verificar que la fecha devuelta es reciente (dentro de 5 segundos)
        val ahora = Date().time
        val fechaResultado = resultado.fecha.time
        assertTrue("La fecha debería ser actual", (ahora - fechaResultado) < 5000)

        // Verificar que se cerraron los recursos
        verify(mockCursor).close()
        verify(mockReadableDatabase).close()
    }

    /**
     * Prueba que [com.dam.simonmedijo.controller.ControllerSQLite.actualizarRecord] inserta correctamente
     * un nuevo registro en la base de datos.
     */
    @Test
    fun actualizarRecord_guarda_valores_correctos() {
        // Preparación de datos de prueba
        val nuevoRecord = 200
        val nuevaFecha = Date()
        val nuevoRowId = 1L

        // Configurar mocks
        whenever(mockDbHelper.writableDatabase).thenReturn(mockWritableDatabase)
        whenever(mockWritableDatabase.insert(
            eq(miTabla.TABLE_NAME),
            eq(null),
            any()
        )).thenReturn(nuevoRowId)

        // Llamada al método a probar (versión modificada para testing)
        val resultado = actualizarRecordConMock(nuevoRecord, nuevaFecha, mockDbHelper)

        // Verificar que se llamó a insert con los valores correctos
        verify(mockWritableDatabase).insert(
            eq(miTabla.TABLE_NAME),
            eq(null),
            contentValuesCaptor.capture()
        )

        // Verificar los valores capturados
        val capturedValues = contentValuesCaptor.value
        assertEquals(nuevoRecord, capturedValues.getAsInteger(miTabla.PUNTUACION))
        assertEquals(nuevaFecha.time, capturedValues.getAsLong(miTabla.FECHA))

        // Verificar el resultado
        assertEquals(nuevoRecord, resultado.record)
        assertEquals(nuevaFecha.time, resultado.fecha.time)

        // Verificar que se cerró la conexión
        verify(mockDbHelper).close()
    }

    /**
     * Prueba que [com.dam.simonmedijo.controller.ControllerSQLite.actualizarRecord] maneja correctamente
     * un error en la inserción (devuelve -1).
     */
    @Test
    fun actualizarRecord_maneja_error_de_insercion() {
        // Preparación de datos de prueba
        val nuevoRecord = 200
        val nuevaFecha = Date()
        val errorRowId = -1L

        // Configurar mocks
        whenever(mockDbHelper.writableDatabase).thenReturn(mockWritableDatabase)
        whenever(mockWritableDatabase.insert(
            eq(miTabla.TABLE_NAME),
            eq(null),
            any()
        )).thenReturn(errorRowId)

        // Llamada al método a probar (versión modificada para testing)
        val resultado = actualizarRecordConMock(nuevoRecord, nuevaFecha, mockDbHelper)

        // Aún con error, debería devolver el Record que intentamos guardar
        assertEquals(nuevoRecord, resultado.record)
        assertEquals(nuevaFecha.time, resultado.fecha.time)

        // Verificar que se cerró la conexión
        verify(mockDbHelper).close()
    }

    // Métodos auxiliares para testing que aceptan el DbHelper como parámetro
    private fun obtenerRecordConMock(dbHelper: FeedReaderDbHelper): Record {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(miTabla.PUNTUACION, miTabla.FECHA)
        val sortOrder = "${miTabla.FECHA} DESC"
        val limit = "1"

        val cursor = db.query(
            miTabla.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            sortOrder,
            limit
        )

        var recordValue = 0
        var fechaValue = Date()

        with(cursor) {
            if (moveToFirst()) {
                recordValue = getInt(getColumnIndexOrThrow(miTabla.PUNTUACION))
                val fechaLong = getLong(getColumnIndexOrThrow(miTabla.FECHA))
                fechaValue = Date(fechaLong)
            }
        }

        cursor.close()
        db.close()

        return Record(recordValue, fechaValue)
    }

    private fun actualizarRecordConMock(
        nuevoRecord: Int,
        fecha: Date,
        dbHelper: FeedReaderDbHelper
    ): Record {
        val values = ContentValues().apply {
            put(miTabla.PUNTUACION, nuevoRecord)
            put(miTabla.FECHA, fecha.time)
        }

        val newRowId = dbHelper.writableDatabase.insert(miTabla.TABLE_NAME, null, values)
        dbHelper.close()

        return Record(nuevoRecord, fecha)
    }
}