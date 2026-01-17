---
titulo: "Integración Directa MongoDB Atlas (Conexión REST sin Backend ni Realm)"
proposito: "feature"
componente: "mongodb-direct-rest"
version: 1.0
fecha_creacion: "2026-01-17"
ultima_actualizacion: "2026-01-17"
propietario: "Daniel Figueroa"
estado: "Planificado"
prioridad: "ALTA"
esfuerzo: "BAJO"
---

# Plan de Desarrollo: Conexión Directa a MongoDB Atlas (Sin Backend, Sin Realm)

![Estado: PLANIFICADO](https://img.shields.io/badge/estado-PLANIFICADO-0099ff)
![Prioridad: ALTA](https://img.shields.io/badge/prioridad-ALTA-red)
![Esfuerzo: BAJO](https://img.shields.io/badge/esfuerzo-BAJO-green)

**Versión**: 1.0  
**Última actualización**: 2026-01-17  
**Propietario**: Daniel Figueroa

## 🎯 Objetivo Principal

Integrar **MongoDB Atlas mediante REST API directa** desde la aplicación Android SimonMeDijo sin necesidad de backend personalizado ni Realm SDK. Usar la API nativa de MongoDB Atlas para sincronizar récords del juego en la nube con autenticación JWT, manteniendo compatibilidad total con la interfaz `Conexion` existente y el stack Kotlin + Coroutines + Flow actual.

## 📋 Contexto y Motivación

### ¿Por qué este proyecto?
- [x] **Simplicidad**: Conexión directa MongoDB → Android sin capas intermedias
- [x] **Aprendizaje**: MongoDB REST API, Atlas App Services Auth, manejo directo de JSON
- [x] **Rapidez**: Configuración mínima, máximo 2 semanas
- [x] **Portafolio**: Demostrar integración cloud nativa sin backend

### ¿Qué problema resuelve?
1. **Problema actual**: Datos almacenados solo localmente, sin sincronización en nube
2. **Solución**: Sincronizar récords en MongoDB Atlas mediante API REST nativa, sin backend propio

## 🎨 Alcance (Scope)

### ✅ INCLUIDO

- [x] **REQ-001**: Configurar MongoDB Atlas Data API (REST endpoint)
- [x] **REQ-002**: Autenticación con Atlas App Services API Key o JWT
- [x] **REQ-003**: Crear clase `MongoDirectConexion` implementando `Conexion`
- [x] **REQ-004**: Método `obtenerRecord()` - GET a `/api/records/{userId}`
- [x] **REQ-005**: Método `actualizarRecord()` - POST/PUT a `/api/records`
- [x] **REQ-006**: Sincronización offline-first con caché local (Room)
- [x] **REQ-007**: Manejo de errores de red y reintentos automáticos
- [x] **REQ-008**: Testing unitario básico
- [x] **REQ-009**: Documentación de setup

### ❌ NO INCLUIDO

- [ ] Realm SDK
- [ ] Backend Node.js/Express
- [ ] WebSockets o conexión persistente
- [ ] Real-time listeners
- [ ] Multi-región o sharding avanzado

## 🛠️ Stack Tecnológico

| Categoría | Tecnología | Justificación |
|-----------|-----------|---------------|
| **BD Remota** | MongoDB Atlas | Cloud-managed NoSQL |
| **API** | MongoDB Data API (REST) | Conexión directa, sin backend |
| **Autenticación** | Atlas API Key o JWT | Estateless, simple |
| **Cliente Android** | Kotlin + Retrofit + OkHttp | HTTP client tipo-seguro |
| **BD Local** | Room (SQLite) | Caché offline, ya existe |
| **Sincronización** | WorkManager | Background sync |
| **Testing** | JUnit + Mockito | Testing unitario |
| **Versionamiento** | Gitflow (feature/mongodb-direct-rest) | Seguir el flujo |

## 📊 Arquitectura de Alto Nivel

```
┌─────────────────────────────────────────────────┐
│         ANDROID APPLICATION                      │
│  ┌───────────────────────────────────────────┐  │
│  │  UI Layer (Jetpack Compose)               │  │
│  ├───────────────────────────────────────────┤  │
│  │  ViewModel (MyVM)                         │  │
│  ├───────────────────────────────────────────┤  │
│  │  Repository Pattern                       │  │
│  │  └─ MongoDirectConexion                  │  │
│  │     ├─ obtenerRecord(context)            │  │
│  │     └─ actualizarRecord()                │  │
│  ├───────────────────────────────────────────┤  │
│  │  Room Database (Local Cache)              │  │
│  │  └─ GameRecord entity (caché)            │  │
│  ├───────────────────────────────────────────┤  │
│  │  Retrofit + OkHttp                        │  │
│  │  └─ HTTP client para API REST            │  │
│  ├───────────────────────────────────────────┤  │
│  │  WorkManager                              │  │
│  │  └─ SyncWorker (sync periódica)          │  │
│  └───────────────────────────────────────────┘  │
└─────────────────────────────────────────────────┘
                      ↕ HTTPS
┌─────────────────────────────────────────────────┐
│      MONGODB ATLAS DATA API (REST)              │
│  https://data.mongodb-api.com/app/{APP_ID}/    │
│     endpoint/data/v1/action/find                │
│     endpoint/data/v1/action/insertOne           │
│     endpoint/data/v1/action/updateOne           │
│     endpoint/data/v1/action/deleteOne           │
└─────────────────────────────────────────────────┘
                      ↕
┌─────────────────────────────────────────────────┐
│         MONGODB ATLAS CLUSTER                    │
│  Database: simonmedijo                          │
│  Collection: game_records                       │
│  ├─ _id: ObjectId                             │
│  ├─ owner_id: String                          │
│  ├─ puntuacion: Int                           │
│  ├─ fecha: Date                               │
│  ├─ dispositivo: String                       │
│  └─ createdAt: Date                           │
└─────────────────────────────────────────────────┘
```

## 📅 Plan de Acción Detallado

### Fase 1: Configuración de MongoDB Atlas (Día 1)

**Objetivo**: Tener MongoDB Atlas con Data API habilitada y credenciales listas.

#### TASK-101: Configurar MongoDB Atlas Data API

| Aspecto | Detalle |
|--------|---------|
| **Plataforma** | https://cloud.mongodb.com/ |
| **Paso 1** | Crear cluster (o usar existente) |
| **Paso 2** | Ir a: App Services → Create App Service |
| **Paso 3** | Nombrar: `SimonMeDijo-DirectAPI` |
| **Paso 4** | Link cluster |
| **Paso 5** | Habilitar: Data API en Settings |
| **Paso 6** | Copiar: Data API URL y App ID |
| **Criterio de éxito** | Data API URL copiada y funcional |

**Nota**: Data API URL se verá así:
```
https://data.mongodb-api.com/app/{APP_ID}/endpoint/data/v1/
```

#### TASK-102: Crear API Key para autenticación

| Aspecto | Detalle |
|--------|---------|
| **Plataforma** | App Services → API Keys |
| **Acción** | Crear nuevo API Key |
| **Nombre** | `SimonMeDijo-Android-Key` |
| **Copiar** | Key value (usarlo como Authorization: Bearer {KEY}) |
| **Seguridad** | No commitear en git, guardar en `local.properties` |
| **Criterio de éxito** | API Key generada y testeable con Postman |

#### TASK-103: Definir esquema y crear base de datos

| Aspecto | Detalle |
|--------|---------|
| **Database** | `simonmedijo` |
| **Collection** | `game_records` |
| **Documentos** | `{ _id, owner_id, puntuacion, fecha, dispositivo, createdAt }` |
| **Validación** | Schema JSON en Atlas para requerir campos |
| **Índices** | Crear índice `(owner_id, createdAt)` |
| **Criterio de éxito** | Insertar documento test via Postman |

**Documento ejemplo**:
```json
{
  "_id": {"$oid": "507f1f77bcf86cd799439011"},
  "owner_id": "user123",
  "puntuacion": 2500,
  "fecha": {"$date": "2026-01-17T10:30:00Z"},
  "dispositivo": "SM-G991B",
  "createdAt": {"$date": "2026-01-17T10:30:00Z"}
}
```

### Fase 2: Implementar MongoDirectConexion en Android (Día 2-3)

**Objetivo**: Crear clase que implementa `Conexion` conectando directamente a MongoDB Atlas.

#### TASK-201: Crear DTO para GameRecord MongoDB

| Aspecto | Detalle |
|--------|---------|
| **Archivo** | `app/src/main/java/com/dam/simonmedijo/model/GameRecordMongo.kt` |
| **Clase** | `data class GameRecordMongo` |
| **Campos** | `_id: String, owner_id: String, puntuacion: Int, fecha: Long, dispositivo: String, createdAt: Long` |
| **Serialización** | @Serializable o usar Gson |
| **Criterio de éxito** | DTO compila, mapea desde JSON MongoDB |

#### TASK-202: Configurar Retrofit para MongoDB Data API

| Aspecto | Detalle |
|--------|---------|
| **Archivo** | `app/src/main/java/com/dam/simonmedijo/data/remote/MongoApiService.kt` |
| **Base URL** | `https://data.mongodb-api.com/app/{APP_ID}/endpoint/data/v1/` |
| **Métodos** | `find(filter)`, `insertOne(document)`, `updateOne(filter, update)`, `deleteOne(filter)` |
| **Headers** | `Authorization: Bearer {API_KEY}`, `Content-Type: application/json` |
| **Timeout** | Connect: 10s, Read: 30s, Write: 30s |
| **Criterio de éxito** | Retrofit client creado, testeable |

**Interfaz de servicio**:
```kotlin
interface MongoApiService {
    @POST("action/find")
    suspend fun find(@Body request: FindRequest): Response<FindResponse>
    
    @POST("action/insertOne")
    suspend fun insertOne(@Body request: InsertRequest): Response<InsertResponse>
    
    @POST("action/updateOne")
    suspend fun updateOne(@Body request: UpdateRequest): Response<UpdateResponse>
    
    @POST("action/deleteOne")
    suspend fun deleteOne(@Body request: DeleteRequest): Response<DeleteResponse>
}
```

#### TASK-203: Implementar MongoDirectConexion

| Aspecto | Detalle |
|--------|---------|
| **Archivo** | `app/src/main/java/com/dam/simonmedijo/controller/MongoDirectConexion.kt` |
| **Clase** | `object MongoDirectConexion : Conexion` |
| **Interfaz** | Implementar `obtenerRecord()` y `actualizarRecord()` |
| **Criterio de éxito** | Compila, satisface interfaz `Conexion` |

#### TASK-204: Implementar obtenerRecord()

| Aspecto | Detalle |
|--------|---------|
| **Lógica** | 1. Obtener userId del Context (SharedPreferences) 2. POST /find con filter `{owner_id: userId}` 3. Ordenar por fecha DESC 4. Retornar primero 5. Mapear GameRecordMongo → Record |
| **Offline** | Si falla, leer desde Room local |
| **Error** | Loggear, retornar Record(puntuacion=0) |
| **Criterio de éxito** | Obtiene record correctamente |

**Request find**:
```json
{
  "dataSource": "Cluster0",
  "database": "simonmedijo",
  "collection": "game_records",
  "filter": {"owner_id": "user123"},
  "sort": {"fecha": -1},
  "limit": 1
}
```

#### TASK-205: Implementar actualizarRecord()

| Aspecto | Detalle |
|--------|---------|
| **Lógica** | 1. Obtener userId 2. Crear documento con {owner_id, puntuacion, fecha, dispositivo} 3. POST /insertOne 4. Guardar también en Room caché 5. Retornar Record mapeado |
| **Timestamp** | Usar fecha actual del servidor (Java Date → MongoDB ISODate) |
| **Error** | Si falla insertOne remoto, guardar en Room con flag "pendiente_sync" |
| **Criterio de éxito** | Record insertado en MongoDB y Room |

**Request insertOne**:
```json
{
  "dataSource": "Cluster0",
  "database": "simonmedijo",
  "collection": "game_records",
  "document": {
    "owner_id": "user123",
    "puntuacion": 2500,
    "fecha": {"$date": "2026-01-17T10:30:00Z"},
    "dispositivo": "SM-G991B",
    "createdAt": {"$date": {"$numberLong": "1705485000000"}}
  }
}
```

### Fase 3: Integración con MyVM y UI (Día 3-4)

**Objetivo**: Integrar MongoDirectConexion en ViewModel y mostrar récord en UI.

#### TASK-301: Crear RecordViewModel

| Aspecto | Detalle |
|--------|---------|
| **Archivo** | `app/src/main/java/com/dam/simonmedijo/ViewModel/RecordViewModel.kt` |
| **Clase** | `class RecordViewModel(application: Application) : AndroidViewModel(application)` |
| **State** | `MutableStateFlow<Record> _record` |
| **State** | `MutableStateFlow<Boolean> _isSyncing` |
| **State** | `MutableStateFlow<String> _errorMessage` |
| **Métodos** | `loadRecord()`, `updateRecord(puntuacion)` |
| **Criterio de éxito** | ViewModel funcional con Flows |

#### TASK-302: Integrar RecordViewModel en MyVM

| Aspecto | Detalle |
|--------|---------|
| **Archivo** | `app/src/main/java/com/dam/simonmedijo/ViewModel/MyVM.kt` |
| **Cambio** | Agregar instancia de RecordViewModel |
| **Cambio** | Cuando juego termina: `recordViewModel.updateRecord(puntuacion_actual)` |
| **Mantener** | Todos los estados del juego (IDLE, GENERAR_SECUENCIA, etc.) |
| **Criterio de éxito** | Juego funciona, al terminar guarda record en MongoDB |

#### TASK-303: Mostrar récord en UI

| Aspecto | Detalle |
|--------|---------|
| **Archivo** | `app/src/main/java/com/dam/simonmedijo/view/UI.kt` |
| **Composable** | Agregar `RecordDisplay()` que muestre puntuación |
| **Composable** | Mostrar "Sincronizando..." si está activa |
| **Compatibilidad** | Mantener UI actual del juego intacta |
| **Criterio de éxito** | UI muestra récord y estado de sync |

### Fase 4: Caché Local y Sincronización (Día 4-5)

**Objetivo**: Implementar Room para caché offline y WorkManager para sync periódica.

#### TASK-401: Extender Room con tabla de caché

| Aspecto | Detalle |
|--------|---------|
| **Archivo** | `app/src/main/java/com/dam/simonmedijo/data/local/entity/GameRecordCacheEntity.kt` |
| **Campos** | `id: Int PRIMARY KEY, mongoId: String, owner_id, puntuacion, fecha, dispositivo, synced: Boolean, createdAt` |
| **DAO** | Agregar métodos: `insertCache()`, `getAllPending()`, `markAsSynced()` |
| **Criterio de éxito** | Entidad compila, migraciones OK |

#### TASK-402: Crear SyncWorker

| Aspecto | Detalle |
|--------|---------|
| **Archivo** | `app/src/main/java/com/dam/simonmedijo/worker/MongoSyncWorker.kt` |
| **Clase** | `class MongoSyncWorker : CoroutineWorker` |
| **Lógica** | 1. Obtener registros no sincronizados de Room 2. Para cada uno: intentar insertOne en MongoDB 3. Si éxito: marcar como synced 4. Si fallo: reintentar |
| **Período** | Ejecutar cada 15 minutos si hay conexión |
| **Criterio de éxito** | Worker sincroniza datos pendientes automáticamente |

#### TASK-403: Configurar WorkManager

| Aspecto | Detalle |
|--------|---------|
| **Archivo** | `app/src/main/java/com/dam/simonmedijo/MainActivity.kt` |
| **Código** | `scheduleSyncWorker()` en onCreate() |
| **Configuración** | `PeriodicWorkRequestBuilder<MongoSyncWorker>(15 minutos)` |
| **Restricciones** | Requerir conectividad |
| **Criterio de éxito** | Worker se ejecuta automáticamente |

### Fase 5: Testing y Validación (Día 5-6)

**Objetivo**: Asegurar que sincronización funciona correctamente.

#### TASK-501: Testing unitario

| Aspecto | Detalle |
|--------|---------|
| **Archivo** | `app/src/test/java/com/dam/simonmedijo/controller/MongoDirectConexionTest.kt` |
| **Tests** | `testObtenerRecord()`, `testActualizarRecord()`, `testMapeoJSON()` |
| **Mocking** | Mockear OkHttp responses |
| **Criterio de éxito** | 80%+ coverage |

#### TASK-502: Testing integración

| Aspecto | Detalle |
|--------|---------|
| **Archivo** | `app/src/androidTest/java/com/dam/simonmedijo/MongoIntegrationTest.kt` |
| **Test** | Crear record, leer desde MongoDB real (si keys disponibles en CI) |
| **Alternativa** | Mock responses con MockWebServer |
| **Criterio de éxito** | End-to-end flow funciona |

#### TASK-503: Testing manual

| Escenario | Pasos | Esperado |
|-----------|-------|----------|
| **Juego → Record en MongoDB** | Jugar, terminar, ver UI | Record aparece en MongoDB Atlas |
| **Offline → Online** | Desactivar red, jugar, activar red | Record se sincroniza cuando vuelve |
| **Multi-dispositivo** | Loguear en otro dispositivo con mismo usuario | Ve records sincronizados |

### Fase 6: Documentación (Día 6-7)

**Objetivo**: Documentar setup y uso.

#### TASK-601: Crear README de setup

| Archivo | Contenido |
|---------|-----------|
| `docs/MONGODB_DIRECT_SETUP.md` | Paso a paso: crear cluster, Data API, API Key, copiar APP_ID |
| `docs/MONGODB_DIRECT_USAGE.md` | Cómo usar MongoDirectConexion, offline mode |
| `BUILD_CONFIG_EXAMPLE.md` | Template para BuildConfig con credenciales |

#### TASK-602: Documentar arquitectura

| Archivo | Contenido |
|---------|-----------|
| `docs/MONGO_DIRECT_ARCH.md` | Diagrama, flujos, sincronización offline |

## 🔒 Seguridad

### Data API
- **REQ-SEC-001**: API Key en `local.properties` (nunca en git)
- **REQ-SEC-002**: BuildConfig para inyectar en tiempo de compilación
- **REQ-SEC-003**: HTTPS obligatorio (Data API automaticamente)
- **REQ-SEC-004**: Validar HTTPS certificate pinning (opcional)

### Validación MongoDB
- **REQ-SEC-005**: Schema validation en Atlas para requerir campos
- **REQ-SEC-006**: Índice `(owner_id, createdAt)` para performance
- **REQ-SEC-007**: Reglas de acceso: cada usuario solo ve su owner_id

```javascript
// En MongoDB Atlas - Validación de documento:
{
  "$jsonSchema": {
    "bsonType": "object",
    "required": ["owner_id", "puntuacion", "fecha"],
    "properties": {
      "owner_id": {"bsonType": "string"},
      "puntuacion": {"bsonType": "int", "minimum": 0},
      "fecha": {"bsonType": "date"},
      "dispositivo": {"bsonType": "string"},
      "createdAt": {"bsonType": "date"}
    }
  }
}
```

## ⚙️ Dependencias y Versiones

### Android (build.gradle.kts)

```kotlin
dependencies {
    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.google.code.gson:gson:2.10.1")
    
    // Room (ya existe probablemente)
    implementation("androidx.room:room-runtime:2.5.1")
    kapt("androidx.room:room-compiler:2.5.1")
    
    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.10.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
```

### Variables de Entorno

```properties
# local.properties (NUNCA commitear)
MONGO_API_KEY=xxxxxxxxxxxxxxxxxxxxx
MONGO_APP_ID=yourappid
MONGO_API_URL=https://data.mongodb-api.com/app/yourappid/endpoint/data/v1/
```

### BuildConfig (auto-inyectado)

```kotlin
// app/build.gradle.kts
android {
    defaultConfig {
        buildConfigField("String", "MONGO_API_KEY", "\"${getLocalProperty("MONGO_API_KEY")}\"")
        buildConfigField("String", "MONGO_APP_ID", "\"${getLocalProperty("MONGO_APP_ID")}\"")
        buildConfigField("String", "MONGO_API_URL", "\"${getLocalProperty("MONGO_API_URL")}\"")
    }
}

fun getLocalProperty(key: String): String {
    val file = rootProject.file("local.properties")
    val props = java.util.Properties()
    props.load(file.inputStream())
    return props.getProperty(key, "")
}
```

## 📊 Criterios de Éxito

- [x] MongoDB Data API habilitada y funcionando
- [x] MongoDirectConexion implementada y satisface interfaz `Conexion`
- [x] Records se guardan en MongoDB desde Android
- [x] Sincronización offline-first funciona
- [x] UI muestra récord y estado de sync
- [x] Tests unitarios pasan
- [x] Documentación completa

## 🔄 Timeline

| Fase | Duración | Estado |
|------|----------|--------|
| Fase 1: MongoDB Setup | Día 1 | ⏳ |
| Fase 2: MongoDirectConexion | Día 2-3 | ⏳ |
| Fase 3: Integración UI | Día 3-4 | ⏳ |
| Fase 4: Caché + Sync | Día 4-5 | ⏳ |
| Fase 5: Testing | Día 5-6 | ⏳ |
| Fase 6: Docs | Día 6-7 | ⏳ |
| **TOTAL** | **~1.5 semanas** | |
---

**Documento generado** ✓ Listo para ejecución
