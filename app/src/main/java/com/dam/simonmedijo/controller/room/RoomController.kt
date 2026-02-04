package com.dam.simonmedijo.controller.room

import android.content.Context
import androidx.room.Room
import com.dam.simonmedijo.controller.Conexion
import com.dam.simonmedijo.model.Datos
import com.dam.simonmedijo.model.Record
import java.util.Date

object RoomController: Conexion {


    /**
     * Obtiene el ultimo record de la base de datos
     * @param context El contexto de la aplicación.
     */
    override fun obtenerRecord(context: Context): Record {
        /*
            No es recomendable usar "allowMainThreadQueries()" porq si la consulta es grande puede saturar la UI.
            Pero para este caso no debería ser un problema, habría que implementar lauch o funciones suspend
            para que se ejecute en un hilo secundario.
         */
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "base-record"
        ).allowMainThreadQueries().build()
        // Puede retornar un nulo, si retorna un nulo devuelve un record a 0 con la fecha actual
        val miRecord = db.recordDao().getRecord() ?: return Record(0, Date())
        db.close()


        //se que esto no deberia estar aqui
        Datos.nombreJugador.value = miRecord.nombre



        //Pasamos de nuestro RecordEntity a nuestroRecord
        return Record(miRecord.record, Date(miRecord.fecha))
    }

    /**
     * Actualiza el record en la base de datos.
     * @param nuevoRecord El nuevo record a actualizar.
     * @param fecha La fecha asociada al nuevo record.
     */
    override fun actualizarRecord(
        nuevoRecord: Int,
        fecha: Date,
        context: Context
    ): Record {
        //Primero obtenemos la base de datos
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "base-record"
        ).allowMainThreadQueries().build()
        // insertamos el nuevo record

        val miNuevoRecord =db.recordDao().insert(RecordEntity(record = nuevoRecord, fecha = fecha.time, nombre=Datos.nombreJugador.value))
        db.close()
        return Record(nuevoRecord, fecha)
    }


}