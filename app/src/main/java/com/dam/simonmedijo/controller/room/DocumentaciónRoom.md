# Acceso a Base de Datos con Room (Android)

Este documento describe la estructura y el funcionamiento del módulo de acceso a datos implementado con **Room** para gestionar el *record* de la aplicación.

---

## 🧱 Arquitectura general

La arquitectura separa claramente:

* **Modelo de dominio** (`Record`): objeto usado por la lógica de la aplicación.
* **Entidad Room** (`RecordEntity`): representación de la tabla en la base de datos.
* **DAO** (`RecordDao`): acceso a datos (consultas e inserciones).
* **Base de datos** (`AppDatabase`): configuración principal de Room.
* **Controlador** (`RoomController`): capa intermedia que conecta la app con Room y transforma datos.

---

## 🧠 Modelo de dominio

### `Record`

```kotlin
data class Record(
    val record: Int = 0,
    val fecha: Date = Date()
)
```

**Descripción:**

* Representa el *record* dentro de la aplicación.
* Usa `Date` para facilitar el trabajo con fechas a nivel lógico.
* **No** está ligado directamente a Room.

---

## 🗄️ Entidad de base de datos

### `RecordEntity`

```kotlin
@Entity(tableName = "records")
data class RecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val record: Int,
    val fecha: Long
)
```

**Descripción:**

* Representa la tabla `records` en la base de datos SQLite.
* `id` es la clave primaria autogenerada.
* `fecha` se guarda como `Long` (timestamp) para compatibilidad con SQLite.

---

## 🔌 DAO (Data Access Object)

### `RecordDao`

```kotlin
@Dao
interface RecordDao {

    @Query("SELECT * FROM records ORDER BY id DESC LIMIT 1")
    fun getRecord(): RecordEntity?

    @Insert
    fun insert(record: RecordEntity)
}
```

**Responsabilidades:**

* Acceder a la tabla `records`.
* Obtener el último record insertado.
* Insertar nuevos records.

**Detalles importantes:**

* `getRecord()` puede devolver `null` si la tabla está vacía.
* La consulta ordena por `id DESC` para obtener el último registro.

---

## 🗃️ Base de datos Room

### `AppDatabase`

```kotlin
@Database(entities = [RecordEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recordDao(): RecordDao
}
```

**Descripción:**

* Define la base de datos de Room.
* Incluye la entidad `RecordEntity`.
* Expone el DAO mediante `recordDao()`.

---

## 🎮 Controlador de acceso a datos

### `RoomController`

```kotlin
object RoomController : Conexion
```

**Descripción:**

* Implementa la interfaz `Conexion`.
* Centraliza el acceso a la base de datos.
* Convierte entre `RecordEntity` y `Record`.

---

### 📥 Obtener el último record

```kotlin
override fun obtenerRecord(context: Context): Record
```

**Funcionamiento:**

1. Crea una instancia de la base de datos con `Room.databaseBuilder`.
2. Obtiene el último registro mediante `recordDao().getRecord()`.
3. Si no existe ningún registro, devuelve un `Record` con valor `0` y fecha actual.
4. Convierte `RecordEntity` a `Record`.
5. Cierra la base de datos.

**Notas:**

* Se usa `allowMainThreadQueries()` solo por simplicidad.
* En aplicaciones reales se recomienda usar *coroutines* (`suspend`, `launch`).

---

### 📤 Actualizar (insertar) un record

```kotlin
override fun actualizarRecord(
    nuevoRecord: Int,
    fecha: Date,
    context: Context
): Record
```

**Funcionamiento:**

1. Abre la base de datos.
2. Inserta un nuevo `RecordEntity` con el valor del record y la fecha en timestamp.
3. Cierra la base de datos.
4. Devuelve un objeto `Record` con los datos insertados.

**Observación:**

* No se actualiza un registro existente, se inserta uno nuevo.
* El histórico de records queda almacenado en la base de datos.

---

## ⚠️ Consideraciones importantes

*  `allowMainThreadQueries()` no es recomendable en produción porque blouea la Interfaz Grafica. Es recomendable el uso de `suspend` y `launch`.



---

## ✅ Resumen

Este módulo proporciona un acceso sencillo y claro a la base de datos usando Room, manteniendo:

* Código organizado
* Buena separación de responsabilidades
* Facilidad de mantenimiento y ampliación

Ideal para proyectos pequeños o educativos donde se está aprendiendo Room y persistencia en Android.
