package com.dam.simonmedijo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

@OptIn(ExperimentalCoroutinesApi::class)
class TestViewModel {

    private lateinit var vm: MyVM

    @BeforeEach
    fun setup() {
        vm = MyVM()
        // Reiniciamos Datos antes de cada test
        Datos.secuencia.value = mutableListOf()
        Datos.ronda.value = 0
        Datos.record.value = 0
        Datos.estado.value = Estado.IDLE
        Datos.currentColorEncendido.value = null
    }

    @Test
    fun `añadirColorASecuencia incrementa secuencia`() {
        val initialSize = Datos.secuencia.value.size
        vm.añadirColorASecuencia()
        assertEquals(initialSize + 1, Datos.secuencia.value.size)
    }

    @Test
    fun `comprobarEleccionEnSecuencia devuelve true si acierta`() {
        val color = Colores.CLASE_ROJO
        Datos.secuencia.value = mutableListOf(color)
        assertTrue(vm.comprobarEleccionEnSecuencia(color, 0))
    }

    @Test
    fun `comprobarEleccionEnSecuencia devuelve false si falla`() {
        Datos.secuencia.value = mutableListOf(Colores.CLASE_VERDE)
        assertFalse(vm.comprobarEleccionEnSecuencia(Colores.CLASE_ROJO, 0))
    }

    @Test
    fun `comprobarRecord actualiza record si es mayor`() {
        Datos.ronda.value = 5
        Datos.record.value = 3
        vm.comprobarRecord()
        assertEquals(5, Datos.record.value)
    }

    @Test
    fun `comprobarRecord no cambia record si es menor`() {
        Datos.ronda.value = 2
        Datos.record.value = 5
        vm.comprobarRecord()
        assertEquals(5, Datos.record.value)
    }

    @Test
    fun `volverAlIdle cambia estado a IDLE`() {
        Datos.estado.value = Estado.FINALIZADO
        vm.volverAlIdle()
        assertEquals(Estado.IDLE, Datos.estado.value)
    }
}
