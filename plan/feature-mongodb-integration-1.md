---
titulo: "Integración de MongoDB en SimonMeDijo"
proposito: "feature"
componente: "mongodb-integration"
version: 1.0
fecha_creacion: "2026-01-14"
ultima_actualizacion: "2026-01-14"
propietario: "Daniel Figueroa"
estado: "Planificado"
prioridad: "ALTA"
esfuerzo: "MEDIO"
---

# Plan de Desarrollo: Integración de MongoDB en SimonMeDijo

![Estado: PLANIFICADO](https://img.shields.io/badge/estado-PLANIFICADO-0099ff)
![Prioridad: ALTA](https://img.shields.io/badge/prioridad-ALTA-red)
![Esfuerzo: MEDIO](https://img.shields.io/badge/esfuerzo-MEDIO-yellow)

## 🎯 Objetivo Principal

Integrar MongoDB como base de datos remota en la aplicación Android SimonMeDijo para persistir registros de juego en la nube, permitiendo sincronización de datos entre dispositivos, backup automático y análisis de estadísticas en tiempo real, manteniendo una estrategia offline-first con sincronización bidireccional cuando haya conectividad.

## 📋 Contexto y Motivación

### ¿Por qué este proyecto?
- [x] **Necesidad personal**: Extender SimonMeDijo con capacidades cloud para sincronizar datos de juego entre dispositivos y proporcionar persistencia de datos en la nube
- [x] **Aprendizaje**: Aprender integración de MongoDB con Android, arquitectura cliente-servidor, sincronización offline-first, autenticación JWT y gestión de datos distribuidos
- [x] **Portafolio**: Demostrar habilidades en arquitectura escalable, integración backend-frontend, manejo de datos complejos y patrones modernos de sincronización

### ¿Qué problema resuelve?
1. **Problema actual**: Los datos de juego en SimonMeDijo se almacenan solo localmente en Room/SQLite, sin posibilidad de sincronizar entre dispositivos o recuperar datos tras limpiar el almacén
2. **Solución propuesta**: 
   - Sincronización automática con MongoDB Atlas para persistencia en la nube
   - Autenticación segura con JWT para acceso multiusuario
   - Estrategia offline-first: funciona sin internet y sincroniza cuando hay conexión
   - Estadísticas y análisis centralizados en backend
   - Portabilidad de datos entre dispositivos del mismo usuario

## 🎨 Alcance (Scope)

### ✅ INCLUIDO

- [x] **REQ-001**: Configurar backend Node.js/Express con MongoDB Atlas
- [x] **REQ-002**: Implementar autenticación JWT en backend y cliente Android
- [x] **REQ-003**: Crear API REST para CRUD de registros de juego (GameRecord)
- [x] **REQ-004**: Implementar cliente HTTP en Android (Retrofit + OkHttp)
- [x] **REQ-005**: Diseñar esquema de sincronización offline-first en Room
- [x] **REQ-006**: Implementar WorkManager para sincronización automática en background
- [x] **REQ-007**: Manejo de conflictos de datos con estrategia last-write-wins
- [x] **REQ-008**: Encriptación de datos sensibles en tránsito (TLS) y almacenamiento local
- [x] **REQ-009**: Testing unitario e integración de sincronización
- [x] **REQ-010**: Documentación de API y arquitectura

### ❌ NO INCLUIDO (por ahora)

- [ ] Replicación multi-región de MongoDB
- [ ] Real-time collaboration (WebSockets)
- [ ] GraphQL API (usar REST por ahora)
- [ ] Caché distribuida (Redis)
- [ ] Analítica avanzada y dashboards
- [ ] Versionado de datos con historial completo

## 🛠️ Stack Tecnológico

| Categoría | Tecnología Elegida | Justificación |
|-----------|-------------------|---------------|
| **BD Remota** | MongoDB Atlas (Cloud) | Escalabilidad, flexibilidad de esquema, excelente integración con Node.js |
| **Backend** | Node.js + Express.js | Ligereza, NPM ecosystem, fácil deployment en cloud |
| **Autenticación** | JWT + bcrypt | Stateless, seguro, estándar de la industria |
| **BD Local** | Room (SQLite) | Ya existe en el proyecto, SQL normalizadas, excelente para offline-first |
| **HTTP Client** | Retrofit + OkHttp | Tipo-seguro, interceptores para autenticación, manejo robusto de errores |
| **Sincronización** | WorkManager + ViewModel | Background sync confiable, respeta lifecycle, manejo de estado robusto |
| **Seguridad** | TLS 1.2+ | Encriptación en tránsito, certificados válidos |
| **Testing** | JUnit + Mockito + MockWebServer | Testing unitario e integración completo |
| **Hosting Backend** | Heroku / Railway / Render | Fácil deployment, soporte Node.js, free tier disponible |
| **Versionamiento** | Gitflow (release/1.1) | Seguir las instrucciones del repositorio |

## 📊 Arquitectura de Alto Nivel

```
┌─────────────────────────────────────────────────────────────┐
│                    ANDROID APPLICATION                       │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │  UI Layer (Activities/Fragments)                        │ │
│  ├─────────────────────────────────────────────────────────┤ │
│  │  ViewModel Layer (State Management)                     │ │
│  ├─────────────────────────────────────────────────────────┤ │
│  │  Repository Pattern (Data Abstraction)                  │ │
│  │  ├─ Room Database (Local)                              │ │
│  │  └─ RetrofitClient (Remote API)                        │ │
│  ├─────────────────────────────────────────────────────────┤ │
│  │  WorkManager (Background Sync)                          │ │
│  │  └─ SyncWorker (Sincronización periódica)             │ │
│  ├─────────────────────────────────────────────────────────┤ │
│  │  Interceptores (Autenticación JWT)                      │ │
│  └─────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                            ↕ HTTPS
┌─────────────────────────────────────────────────────────────┐
│                    BACKEND (Node.js/Express)                 │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │  API Routes (/api/v1/...)                               │ │
│  │  ├─ POST /auth/register                                 │ │
│  │  ├─ POST /auth/login                                    │ │
│  │  ├─ GET/POST /gamerecords                               │ │
│  │  ├─ GET /gamerecords/:id                                │ │
│  │  ├─ PUT /gamerecords/:id                                │ │
│  │  └─ DELETE /gamerecords/:id                             │ │
│  ├─────────────────────────────────────────────────────────┤ │
│  │  Middleware (Auth, Validation, Error Handling)          │ │
│  ├─────────────────────────────────────────────────────────┤ │
│  │  Controllers (Business Logic)                           │ │
│  ├─────────────────────────────────────────────────────────┤ │
│  │  Models (Mongoose Schemas)                              │ │
│  └─────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                            ↕
┌─────────────────────────────────────────────────────────────┐
│              MongoDB Atlas (Cloud Database)                   │
│  Collections: users, gamerecords, syncLog                    │
└─────────────────────────────────────────────────────────────┘
```

## 📅 Plan de Acción Detallado

### Fase 1: Preparación e Infraestructura Backend (Semana 1)

**Objetivo**: Tener un servidor backend funcional con MongoDB conectado y autenticación JWT implementada.

#### TASK-101: Crear proyecto Node.js/Express con estructura base

| Aspecto | Detalle |
|--------|---------|
| **Archivos a crear** | `/backend/package.json`, `/backend/src/server.js`, `/backend/src/config/database.js` |
| **Dependencias** | express, mongoose, dotenv, jsonwebtoken, bcryptjs, cors |
| **Estructura de carpetas** | backend/ → src/ → {config, controllers, models, routes, middleware, utils} |
| **Puerto** | 3000 (desarrollo) / 8000 (producción) |
| **Configuración** | Variables de entorno: MONGODB_URI, JWT_SECRET, PORT |
| **Criterio de éxito** | npm start ejecuta servidor sin errores, responde en localhost:3000 |

#### TASK-102: Configurar MongoDB Atlas y Mongoose

| Aspecto | Detalle |
|--------|---------|
| **Plataforma** | MongoDB Atlas (cluster gratuito o paid según necesidad) |
| **Conexión** | URI de conexión en variable de entorno MONGODB_URI |
| **Archivo** | `/backend/src/config/database.js` - función connectDB() |
| **Collections** | users, gamerecords, syncLogs |
| **Indices** | Crear índice en gamerecords.userId, gamerecords.createdAt |
| **Criterio de éxito** | Conexión exitosa con retry logic, manejo de errores |

#### TASK-103: Implementar autenticación JWT

| Aspecto | Detalle |
|--------|---------|
| **Archivos** | `/backend/src/middleware/auth.js`, `/backend/src/routes/auth.js`, `/backend/src/controllers/authController.js` |
| **Rutas** | POST /api/v1/auth/register, POST /api/v1/auth/login, POST /api/v1/auth/refresh |
| **Modelo User** | `/backend/src/models/User.js` - email, passwordHash, createdAt, lastLogin |
| **Tokenización** | JWT expiración 1h, refresh token 7d almacenado en HTTP-only cookie |
| **Hash Password** | bcryptjs con salt rounds = 10 |
| **Criterio de éxito** | Registro/login funcional, tokens válidos, refresh token funciona |

### Fase 2: API Backend y Modelos de Datos (Semana 1-2)

**Objetivo**: Implementar endpoints REST completos para gestión de registros de juego.

#### TASK-201: Diseñar y crear modelo GameRecord en MongoDB

| Aspecto | Detalle |
|--------|---------|
| **Archivo** | `/backend/src/models/GameRecord.js` |
| **Schema** | Campos: _id, userId, score, level, moves, duration, date, status (synced/pending), version |
| **Validación** | Validar score ≥ 0, level ≥ 1, moves ≥ 0, userId válido |
| **Índices** | userId + createdAt (compound), status |
| **Versionado** | Campo `__v` para control de versión de documento |
| **Soft Delete** | Campo deletedAt (null por defecto, para recuperación) |
| **Criterio de éxito** | Schema validado en Mongoose, prueba de creación de documento |

#### TASK-202: Implementar endpoints CRUD para GameRecords

| Ruta | Método | Autenticación | Descripción | Archivo |
|------|--------|---------------|-------------|---------|
| `/api/v1/gamerecords` | GET | JWT | Listar registros del usuario | `/backend/src/routes/gamerecords.js` |
| `/api/v1/gamerecords` | POST | JWT | Crear nuevo registro | `/backend/src/routes/gamerecords.js` |
| `/api/v1/gamerecords/:id` | GET | JWT | Obtener registro por ID | `/backend/src/routes/gamerecords.js` |
| `/api/v1/gamerecords/:id` | PUT | JWT | Actualizar registro | `/backend/src/routes/gamerecords.js` |
| `/api/v1/gamerecords/:id` | DELETE | JWT | Eliminar (soft delete) | `/backend/src/routes/gamerecords.js` |
| `/api/v1/gamerecords/sync/bulk` | POST | JWT | Sincronizar múltiples registros | `/backend/src/routes/gamerecords.js` |

**Controlador**: `/backend/src/controllers/gameRecordController.js` con validación de propiedad de datos

#### TASK-203: Implementar middleware de autorización y manejo de errores

| Componente | Ubicación | Responsabilidad |
|-----------|-----------|-----------------|
| authMiddleware | `/backend/src/middleware/auth.js` | Validar JWT, extraer userId |
| errorHandler | `/backend/src/middleware/errorHandler.js` | Estandarizar respuestas de error |
| requestLogger | `/backend/src/middleware/requestLogger.js` | Log de requests (morgan) |
| validationMiddleware | `/backend/src/middleware/validation.js` | Validar body, params, query |

### Fase 3: Integración Android - Cliente HTTP (Semana 2)

**Objetivo**: Implementar comunicación segura entre Android y backend con autenticación JWT.

#### TASK-301: Crear modelo de datos Retrofit y configurar cliente HTTP

| Aspecto | Detalle |
|--------|---------|
| **Archivo DTO** | `app/src/main/java/com/simonmedijo/.../data/remote/dto/GameRecordDTO.kt` |
| **Retrofit Service** | `app/src/main/java/com/simonmedijo/.../data/remote/ApiService.kt` |
| **Dependencias** | retrofit2, retrofit-coroutines, okhttp3, logging-interceptor |
| **Base URL** | Variable de configuración (build.gradle) - usar BuildConfig |
| **Timeout** | Connect: 10s, Read: 30s, Write: 30s |
| **Certificados** | TLS 1.2+, validación de certificados habilitada |
| **Criterio de éxito** | Cliente HTTP configurado, prueba GET sin autenticación |

#### TASK-302: Implementar interceptor de autenticación JWT

| Aspecto | Detalle |
|--------|---------|
| **Archivo** | `app/src/main/java/com/simonmedijo/.../data/remote/AuthInterceptor.kt` |
| **Funcionalidad** | Agregar header Authorization: Bearer {token} a cada request |
| **Manejo de 401** | Usar refresh token para obtener nuevo access token |
| **Sincronización** | Usar mutex para evitar race conditions en refresh |
| **Storage** | EncryptedSharedPreferences para almacenar tokens |
| **Criterio de éxito** | Requests incluyen token válido, refresh automático funciona |

#### TASK-303: Crear interface ApiService con todos los endpoints

| Endpoint | Firma de función | Return type |
|----------|------------------|------------|
| Register | `suspend fun register(email: String, password: String): Response<AuthResponse>` | AuthResponse |
| Login | `suspend fun login(email: String, password: String): Response<AuthResponse>` | AuthResponse |
| Get GameRecords | `suspend fun getGameRecords(): Response<List<GameRecordDTO>>` | List<GameRecordDTO> |
| Create GameRecord | `suspend fun createGameRecord(record: GameRecordDTO): Response<GameRecordDTO>` | GameRecordDTO |
| Update GameRecord | `suspend fun updateGameRecord(id: String, record: GameRecordDTO): Response<GameRecordDTO>` | GameRecordDTO |
| Delete GameRecord | `suspend fun deleteGameRecord(id: String): Response<Unit>` | Unit |
| Sync Bulk | `suspend fun syncBulk(records: List<GameRecordDTO>): Response<SyncResponse>` | SyncResponse |

### Fase 4: Sincronización Offline-First (Semana 2-3)

**Objetivo**: Implementar sincronización bidireccional entre Room local y MongoDB remoto.

#### TASK-401: Extender modelo Room para soportar sincronización

| Aspecto | Detalle |
|--------|---------|
| **Archivo** | `app/src/main/java/com/simonmedijo/.../data/local/entity/GameRecordEntity.kt` |
| **Nuevos campos** | syncStatus (PENDING/SYNCED/CONFLICT), lastSyncAttempt, remoteId, version |
| **Enums** | SyncStatus: PENDING, SYNCING, SYNCED, FAILED, CONFLICT |
| **Migration** | Agregar columnas nuevas con DEFAULT valores |
| **DAO Update** | Métodos para filtrar por syncStatus, actualizar estado |
| **Criterio de éxito** | Schema actualizado sin errores de compilación, migration funciona |

#### TASK-402: Crear SyncWorker con WorkManager

| Aspecto | Detalle |
|--------|---------|
| **Archivo** | `app/src/main/java/com/simonmedijo/.../workers/SyncWorker.kt` |
| **Política** | OneTimeWorkRequest + PeriodicWorkRequest (12 horas) |
| **Lógica** | 1. Sync down: obtener cambios del servidor 2. Sync up: enviar cambios locales 3. Resolver conflictos |
| **Constraints** | Requiere conectividad, prefiere WiFi, batería suficiente |
| **Retry** | ExponentialBackoff: inicial 15s, máximo 12 horas |
| **Notificación** | Mostrar progreso mientras se sincroniza |
| **Criterio de éxito** | Sincronización automática cada 12 horas, manual on-demand |

#### TASK-403: Implementar lógica de resolución de conflictos

| Estrategia | Detalles | Archivo |
|-----------|---------|---------|
| Last-Write-Wins | Comparar timestamp, la versión más reciente gana | `app/src/main/java/com/simonmedijo/.../sync/ConflictResolver.kt` |
| Server Wins | En conflictos, prevalece el valor del servidor | Implementar en ConflictResolver |
| Field-level merge | Merge cambios en campos diferentes | Fallback si cambios son en campos distintos |
| Manual Resolution | Mostrar UI para que usuario elija | Guardar en tabla de conflictos |

**Implementación**: Clase ConflictResolver con función `resolveConflict(local, remote): GameRecord`

#### TASK-404: Crear Repository con abstracción de datos

| Aspecto | Detalle |
|--------|---------|
| **Archivo** | `app/src/main/java/com/simonmedijo/.../data/repository/GameRecordRepository.kt` |
| **Métodos públicos** | getAllGameRecords(), getGameRecord(id), createRecord(), updateRecord(), deleteRecord() |
| **Retorno** | Flow<List<GameRecord>> o LiveData para observabilidad |
| **Lógica** | Priorizar datos locales (Room) si disponibles, actualizar en background |
| **Sincronización** | Llamar a SyncWorker tras crear/actualizar/eliminar |
| **Offline mode** | Retornar datos locales aunque falle petición remota |
| **Criterio de éxito** | Datos se actualizan en UI en tiempo real via Flow, sincronización en background |

### Fase 5: Autenticación en Android (Semana 3)

**Objetivo**: Implementar login/registro seguro con almacenamiento de tokens encriptado.

#### TASK-501: Crear AuthRepository y usar EncryptedSharedPreferences

| Aspecto | Detalle |
|--------|---------|
| **Archivo** | `app/src/main/java/com/simonmedijo/.../data/repository/AuthRepository.kt` |
| **Storage** | EncryptedSharedPreferences (androidx.security:security-crypto) |
| **Tokens almacenados** | accessToken, refreshToken, userId, userEmail |
| **Validación** | Verificar token no expirado al iniciar app |
| **Login** | POST /api/v1/auth/login → guardar tokens → sincronizar datos |
| **Logout** | Limpiar tokens → delete local user data |
| **Auto-login** | Si token válido al startup, saltar pantalla de login |
| **Criterio de éxito** | Tokens encriptados, login/logout funcionan, recuperación de sesión |

#### TASK-502: Crear UI de Autenticación

| Pantalla | Componente | Archivo |
|----------|-----------|---------|
| Login | LoginFragment/LoginActivity | `app/src/.../ui/auth/LoginFragment.kt` |
| Register | RegisterFragment/RegisterActivity | `app/src/.../ui/auth/RegisterFragment.kt` |
| Password Recovery | PasswordResetFragment | `app/src/.../ui/auth/PasswordResetFragment.kt` |
| Profile | ProfileFragment | `app/src/.../ui/profile/ProfileFragment.kt` |

**ViewModel**: `AuthViewModel.kt` con estados: IDLE, LOADING, SUCCESS, ERROR

#### TASK-503: Integrar autenticación en Navigation

| Aspecto | Detalle |
|--------|---------|
| **Archivo** | `app/src/main/res/navigation/nav_graph.xml` |
| **Lógica** | Si no autenticado → mostrar auth_graph, else → mostrar main_graph |
| **Deep Links** | Soportar password reset via deep link |
| **Back Stack** | Evitar volver a login tras logout |

### Fase 6: Testing y Validación (Semana 3-4)

**Objetivo**: Asegurar calidad de código, funcionalidad correcta y robustez de sincronización.

#### TASK-601: Testing unitario de sincronización

| Test | Clase | Archivo |
|------|-------|---------|
| Test SyncWorker happy path | SyncWorkerTest | `app/src/test/java/...SyncWorkerTest.kt` |
| Test conflicto resuelto | ConflictResolverTest | `app/src/test/java/...ConflictResolverTest.kt` |
| Test offline mode | GameRecordRepositoryTest | `app/src/test/java/...GameRecordRepositoryTest.kt` |
| Test auth interceptor | AuthInterceptorTest | `app/src/test/java/...AuthInterceptorTest.kt` |

**Framework**: JUnit 4 + Mockito + MockWebServer

#### TASK-602: Testing de integración

| Prueba | Descripción | Archivo |
|--------|------------|---------|
| Test E2E login → sync | Simular flujo completo | `app/src/androidTest/...AuthAndSyncTest.kt` |
| Test Room ↔ API | Verificar persistencia | `app/src/androidTest/...RepositoryTest.kt` |
| Test conflictos | Resolver conflictos automáticamente | `app/src/androidTest/...ConflictTest.kt` |

**Framework**: AndroidX Test + Espresso + MockWebServer

#### TASK-603: Testing manual y validación

| Escenario | Pasos | Esperado |
|-----------|-------|----------|
| **Nuevo usuario** | Register → Login → Crear registro | Registro visible en app, luego en server |
| **Offline mode** | Desactivar red → Crear registro | Se guarda en Room, estado PENDING |
| **Reconexión** | Reactivar red | SyncWorker ejecuta, sube registro, estado SYNCED |
| **Multi-device** | Login en otro dispositivo | Ve registros sincronizados |
| **Conflicto** | Editar offline y online | Resuelve automático, log visible |

### Fase 7: Documentación y Deployment (Semana 4)

**Objetivo**: Documentar arquitectura, APIs y guías de desarrollo. Preparar para producción.

#### TASK-701: Documentar API Backend

| Documento | Contenido | Ubicación |
|-----------|----------|-----------|
| **API Spec** | Endpoints, request/response, status codes | `/backend/API.md` |
| **Auth Flow** | Diagrama de login/refresh/logout | `/backend/AUTH.md` |
| **Error Codes** | Listado completo de errores | `/backend/ERROR_CODES.md` |
| **Deployment** | Guía deploy a Heroku/Railway | `/backend/DEPLOYMENT.md` |

#### TASK-702: Documentar implementación Android

| Documento | Contenido | Ubicación |
|-----------|----------|-----------|
| **Architecture** | Diagrama MVVM, flujos de datos | `/docs/ARCHITECTURE.md` |
| **Sync Guide** | Cómo funciona sincronización | `/docs/SYNC_GUIDE.md` |
| **Setup Dev** | Instrucciones para desarrolladores | `/docs/DEV_SETUP.md` |
| **Troubleshooting** | Problemas comunes y soluciones | `/docs/TROUBLESHOOTING.md` |

#### TASK-703: Configurar variables de entorno y secrets

| Variable | Backend | Android |
|----------|---------|---------|
| API_BASE_URL | .env (Heroku config) | BuildConfig (productFlavors) |
| MONGODB_URI | .env (Heroku config) | N/A |
| JWT_SECRET | .env (Heroku config) | N/A |
| API_TIMEOUT | .env (default 30s) | BuildConfig |

#### TASK-704: Preparar release y merge a main

| Aspecto | Detalle |
|--------|---------|
| **Branch** | feature/mongodb-integration → release/1.1 → main |
| **Versionado** | Android: 1.1.0 (versionCode++), Backend: tag v1.1.0 |
| **Checklist** | ✓ Tests pasan ✓ Documentación completa ✓ Zero warnings ✓ Changelog |
| **Testing previo** | Pruebas en staging con datos reales |

## 📊 Sistema de Seguimiento y Validación

### Tablero de Progreso

| Fase | Estado | Tareas | Completadas | % |
|------|--------|--------|-------------|---|
| **Fase 1** | ⏳ Planificado | 3 | 0 | 0% |
| **Fase 2** | ⏳ Planificado | 3 | 0 | 0% |
| **Fase 3** | ⏳ Planificado | 3 | 0 | 0% |
| **Fase 4** | ⏳ Planificado | 4 | 0 | 0% |
| **Fase 5** | ⏳ Planificado | 3 | 0 | 0% |
| **Fase 6** | ⏳ Planificado | 3 | 0 | 0% |
| **Fase 7** | ⏳ Planificado | 4 | 0 | 0% |
| **TOTAL** | | 23 | 0 | 0% |

### Criterios de Aceptación por Fase

#### Fase 1: Completa cuando...
- [ ] Servidor Express responde en localhost:3000
- [ ] MongoDB Atlas conecta sin errores
- [ ] JWT token se genera correctamente
- [ ] Refresh token actualiza access token
- [ ] Tests unitarios para auth pasan

#### Fase 2: Completa cuando...
- [ ] Schema GameRecord en MongoDB es válido
- [ ] Todos 6 endpoints CRUD funcionan
- [ ] Autorización verifica userId del recurso
- [ ] Error handling retorna status codes correctos
- [ ] Postman collection disponible y testeable

#### Fase 3: Completa cuando...
- [ ] Retrofit client se conecta al backend
- [ ] AuthInterceptor agrega tokens automáticamente
- [ ] Refresh token renueva access token
- [ ] EncryptedSharedPreferences almacena tokens seguros
- [ ] API calls funcionan en emulador

#### Fase 4: Completa cuando...
- [ ] Room schema incluye campos de sincronización
- [ ] SyncWorker se ejecuta cada 12 horas
- [ ] Conflictos se resuelven automáticamente
- [ ] Repository abstrae Room y API
- [ ] Datos se sincronizan offline-first

#### Fase 5: Completa cuando...
- [ ] Login funciona con credenciales válidas
- [ ] Register valida email y password
- [ ] Sesión persiste entre app restarts
- [ ] Logout limpia todos los datos
- [ ] UI de auth es funcional

#### Fase 6: Completa cuando...
- [ ] 80%+ cobertura de test de sincronización
- [ ] Tests de integración pasan en CI/CD
- [ ] Pruebas manuales en 3+ dispositivos OK
- [ ] Performance: sync < 10s en conexión 4G
- [ ] Cero crashes relacionados con sincronización

#### Fase 7: Completa cuando...
- [ ] API documentada con ejemplos
- [ ] Guía de arquitectura disponible
- [ ] Deployment manual y automático funciona
- [ ] Release notes generadas
- [ ] PR mergeado a main sin conflictos

## 🔐 Consideraciones de Seguridad

### Autenticación y Autorización
- **REQ-SEC-001**: Todos los endpoints requieren JWT válido (excepto /auth/register, /auth/login)
- **REQ-SEC-002**: Validar que userId del token coincide con userId del recurso (no acceder datos ajenos)
- **REQ-SEC-003**: Expiración de token: 1 hora para access, 7 días para refresh
- **REQ-SEC-004**: Refresh tokens deben regenerarse en cada uso (rotate tokens)

### Encriptación
- **REQ-SEC-005**: Almacenar passwordHash con bcryptjs (salt 10 rondas)
- **REQ-SEC-006**: Transmitir vía HTTPS/TLS 1.2+ obligatorio
- **REQ-SEC-007**: Tokens en EncryptedSharedPreferences con Android Keystore
- **REQ-SEC-008**: No loggear tokens ni datos sensibles

### Validación
- **REQ-SEC-009**: Validar email formato en backend antes de crear usuario
- **REQ-SEC-010**: Validar password: mínimo 8 caracteres, uppercase, number, special char
- **REQ-SEC-011**: Rate limiting en /auth/login (máx 5 intentos/15 min por IP)
- **REQ-SEC-012**: Validar tipos y rangos en GameRecord: score ≥ 0, level ≥ 1

### Datos y Privacidad
- **REQ-SEC-013**: Soft delete: registros no eliminados permanentemente, solo marcados
- **REQ-SEC-014**: No enviar passwordHash en respuestas API
- **REQ-SEC-015**: GDPR: permitir exportar/eliminar datos del usuario

## 🚀 Dependencias y Versiones

### Backend (Node.js)
```json
{
  "dependencies": {
    "express": "^4.18.2",
    "mongoose": "^7.0.0",
    "jsonwebtoken": "^9.0.0",
    "bcryptjs": "^2.4.3",
    "dotenv": "^16.0.3",
    "cors": "^2.8.5",
    "express-validator": "^7.0.0",
    "morgan": "^1.10.0"
  },
  "devDependencies": {
    "jest": "^29.0.0",
    "supertest": "^6.3.0",
    "nodemon": "^2.0.20"
  }
}
```

### Android (build.gradle.kts)
```kotlin
dependencies {
    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    
    // Database
    implementation("androidx.room:room-runtime:2.5.1")
    kapt("androidx.room:room-compiler:2.5.1")
    
    // Security
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    
    // Background Jobs
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    
    // JSON Serialization
    implementation("com.google.code.gson:gson:2.10.1")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.10.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
```

## ⚠️ Riesgos y Mitigaciones

| Riesgo | Probabilidad | Impacto | Mitigación |
|--------|-------------|--------|-----------|
| MongoDB Atlas downtime | Media | Alto | Implementar retry logic, fallback a datos locales |
| Conflictos de sincronización | Media | Medio | Algoritmo last-write-wins + timestamp |
| Pérdida de tokens | Baja | Alto | Refresh token rotation, secure storage |
| API rate limiting | Baja | Medio | Implementar request queuing, backoff exponencial |
| Código no sincronizado | Media | Medio | Feature branch, PR reviews antes de merge |

## 📞 Contacto y Escalaciones

- **Propietario**: Daniel Figueroa
- **Preguntas técnicas**: Revisar documentación en `/docs/`
- **Issues**: Abrir GitHub issue con label `mongodb-integration`
- **Escalaciones**: Si bloqueado por +1 día, contactar propietario

## 📝 Histórico de Cambios

| Versión | Fecha | Cambios |
|---------|-------|---------|
| 1.0 | 2026-01-14 | Plan inicial creado |
| | | Incluyendo 23 tareas distribuidas en 7 fases |
| | | Stack: Node.js+Express, MongoDB Atlas, Retrofit, Room |

---

**Documento generado automáticamente** ✓ Apto para ejecución por agentes de IA y humanos

