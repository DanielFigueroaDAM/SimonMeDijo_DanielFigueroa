---
goal: Integración de MongoDB en SimonMeDijo - Android Application
version: 1.0
date_created: 2026-01-13
last_updated: 2026-01-13
owner: Equipo de Desenvolvemento
status: 'Planned'
tags: ['feature', 'database', 'backend', 'mongodb', 'integration']
---

# Introdución

![Status: Planned](https://img.shields.io/badge/status-Planned-blue)

Plan de implementación para integrar MongoDB como sistema de almacenamento remoto de datos na aplicación **SimonMeDijo**. Actualmente, a aplicación usa SharedPreferences para almacenar localmente os récords do xogo. Con esta integración, poderemos sincronizar os datos cos servidores de MongoDB, permitindo un almacenamento en cloud e a xestión de varios usuarios.

## 1. Requisitos e Restricións

- **REQ-001**: A aplicación debe conservar a funcionalidade existente de xogo con SharedPreferences como almacenamento local por defecto
- **REQ-002**: Implementar un sistema de sincronización de récords entre o almacenamento local e MongoDB
- **REQ-003**: Crear un backend (por exemplo con Node.js/Express) que actúe como intermediario entre a aplicación Android e MongoDB
- **REQ-004**: Implementar autenticación básica de usuarios para identificar xogadores en MongoDB
- **REQ-005**: Proporcionar capacidade de login/rexistro na aplicación
- **REQ-006**: Implementar manexo de erros de rede e sincronización en segundo plano
- **REQ-007**: Engadir testes unitarios para as novas clases de sincronización
- **SEC-001**: Todas as comunicacións coa API deben usar HTTPS
- **SEC-002**: As contraseñas deben ser encriptadas (usando bcrypt ou similar no backend)
- **SEC-003**: Os tokens de autenticación deben ter expiración
- **CON-001**: A aplicación debe funcionar mesmo sen conexión a internet (usando datos locais)
- **CON-002**: A sincronización non debe bloquear a UI principal
- **GUD-001**: Seguir o patrón MVVM existente na aplicación
- **GUD-002**: Usar Coroutines para operacións asincrónicas
- **GUD-003**: Implementar inyección de dependencias
- **PAT-001**: Manter a interfaz `Conexion` como abstracción para cambiar entre SharedPreferences e MongoDB
- **PAT-002**: Usar o padrón Repository para acceder aos datos

## 2. Pasos de Implementación

### Fase 1: Preparación e Infraestrutura Backend

- **GOAL-001**: Establecer a infraestrutura necesaria para soportar MongoDB e crear a API REST

| Tarefa | Descrición | Completada | Data |
|--------|-----------|-----------|------|
| TASK-001 | Crear proxecto Node.js/Express backend en repositorio separado ou rama `develop` | | |
| TASK-002 | Configurar MongoDB Atlas (cloud) ou MongoDB local con Docker | | |
| TASK-003 | Crear modelos de datos en MongoDB (schemas para User e GameRecord) | | |
| TASK-004 | Implementar endpoints REST para autenticación (POST /api/auth/register, POST /api/auth/login) | | |
| TASK-005 | Implementar endpoints REST para xestión de récords (GET /api/records, POST /api/records, PUT /api/records/:id) | | |
| TASK-006 | Implementar middleware de autenticación (JWT) no backend | | |
| TASK-007 | Configurar CORS para permitir peticiones desde a aplicación Android | | |
| TASK-008 | Crear script de validación e testes do backend (Postman, Jest ou similar) | | |

### Fase 2: Dependencias e Configuración Android

- **GOAL-002**: Engadir dependencias necesarias e configurar a aplicación para comunicación con API REST

| Tarefa | Descrición | Completada | Data |
|--------|-----------|-----------|------|
| TASK-009 | Engadir dependencia Retrofit2 para chamadas HTTP | | |
| TASK-010 | Engadir dependencia OkHttp para interceptores de autenticación | | |
| TASK-011 | Engadir dependencia Hilt/Dagger2 para inyección de dependencias | | |
| TASK-012 | Engadir dependencia kotlinx.serialization ou Gson para serialización JSON | | |
| TASK-013 | Engadir dependencia WorkManager para sincronización en segundo plano | | |
| TASK-014 | Configurar DataStore (sustituto moderno de SharedPreferences) para almacenar token de autenticación | | |
| TASK-015 | Actualizar AndroidManifest.xml con permisos de INTERNET | | |

### Fase 3: Modelos de Datos e DTOs

- **GOAL-003**: Crear as estruturas de datos necesarias para a comunicación con MongoDB

| Tarefa | Descrición | Completada | Data |
|--------|-----------|-----------|------|
| TASK-016 | Crear data class `User` con campos: id, username, email, password (hash) | | |
| TASK-017 | Crear data class `GameRecord` con campos: id, userId, record, fecha, round_details | | |
| TASK-018 | Crear DTOs para respuestas da API (LoginResponse con token, RecordResponse) | | |
| TASK-019 | Crear data class `AuthRequest` para peticiones de login/rexistro | | |
| TASK-020 | Crear seladores Serializable/Parcelable para os modelos se necesario | | |

### Fase 4: Capa de Rede (Retrofit + API Service)

- **GOAL-004**: Implementar a capa de comunicación con MongoDB a través da API REST

| Tarefa | Descrición | Completada | Data |
|--------|-----------|-----------|------|
| TASK-021 | Crear interface `GameRecordApiService` con métodos Retrofit para: login, rexistro, obter récords, actualizar récord | | |
| TASK-022 | Configurar `RetrofitClient` como singleton con base URL da API | | |
| TASK-023 | Crear interceptor OkHttp para engadir token JWT ás peticións | | |
| TASK-024 | Implementar manexo centralizado de erros de rede (NetworkException, ServerException) | | |
| TASK-025 | Crear Data Transfer Objects (DTOs) específicos para as respostas do servidor | | |

### Fase 5: Capa de Repositorio

- **GOAL-005**: Implementar a capa de repositorio que abstrae a fonte de datos (local ou remota)

| Tarefa | Descrición | Completada | Data |
|--------|-----------|-----------|------|
| TASK-026 | Crear clase `RemoteGameRecordRepository` que implementa `Conexion` | | |
| TASK-027 | Modificar `ControllerShPre` para soporte de sincronización coa copia en caché local | | |
| TASK-028 | Crear clase `GameRecordRepositoryImpl` (ou similar) que decide entre usar local ou remoto | | |
| TASK-029 | Implementar sincronización bidirecional (local ↔ remoto) con caché | | |
| TASK-030 | Engadir lóxica de resolución de conflitos cando hai discrepancias entre datos locais e remotos | | |

### Fase 6: Módulo de Autenticación

- **GOAL-006**: Implementar o sistema de autenticación de usuarios

| Tarefa | Descrición | Completada | Data |
|--------|-----------|-----------|------|
| TASK-031 | Crear classe `AuthManager` para xestión de login/rexistro | | |
| TASK-032 | Implementar almacenamento seguro do token JWT en DataStore | | |
| TASK-033 | Crear mecanismo de refresco automático do token (refresh token) | | |
| TASK-034 | Implementar validación de sesión e manexo de logout | | |
| TASK-035 | Crear Composables para UI de login e rexistro | | |

### Fase 7: Sincronización en Segundo Plano

- **GOAL-007**: Implementar sincronización automática de datos en background

| Tarefa | Descrición | Completada | Data |
|--------|-----------|-----------|------|
| TASK-036 | Configurar WorkManager para tarefas de sincronización periódicas | | |
| TASK-037 | Crear `SyncWorker` para sincronizar récords cando hai conexión | | |
| TASK-038 | Implementar política de reintentos con exponential backoff | | |
| TASK-039 | Engadir notificacións para informar ao usuario sobre sincronización | | |

### Fase 8: Actualización de ViewModel e UI

- **GOAL-008**: Integrar a nova funcionalidade de MongoDB no ViewModel e na interfaz de usuario

| Tarefa | Descrición | Completada | Data |
|--------|-----------|-----------|------|
| TASK-040 | Actualizar `MyVM` para usar `GameRecordRepository` en lugar de acceso directo a SharedPreferences | | |
| TASK-041 | Engadir estados para sincronización (sincronizando, sincronizado, erro) | | |
| TASK-042 | Crear UI para mostrar estado de sincronización | | |
| TASK-043 | Engadir pantalla de perfil de usuario con opción de logout | | |
| TASK-044 | Crear fluxo de navegación para autenticación (login -> xogo ou rexistro) | | |

### Fase 9: Testes

- **GOAL-009**: Implementar testes para verificar a integración con MongoDB

| Tarefa | Descrición | Completada | Data |
|--------|-----------|-----------|------|
| TASK-045 | Crear testes unitarios para `RemoteGameRecordRepository` con Mockito | | |
| TASK-046 | Crear testes de integración para a capa Retrofit (usando MockWebServer) | | |
| TASK-047 | Crear testes para `AuthManager` (login, rexistro, logout) | | |
| TASK-048 | Crear testes para sincronización e resolución de conflitos | | |
| TASK-049 | Crear testes de UI (Compose testing) para pantallas de autenticación | | |

### Fase 10: Documentación e Publicación

- **GOAL-010**: Documentar a implementación e preparar para producción

| Tarefa | Descrición | Completada | Data |
|--------|-----------|-----------|------|
| TASK-050 | Escribir documentación da API REST en arquivo separado ou Swagger/OpenAPI | | |
| TASK-051 | Crear guía de setup para desenvolvedores (variables de ambiente, configuración) | | |
| TASK-052 | Crear changelof cos cambios introducidos | | |
| TASK-053 | Realizar revisión de seguridade do código | | |
| TASK-054 | Crear rama `release/1.1` e actualizar versión a `1.1.0` segundo SemVer | | |

## 3. Alternativas

- **ALT-001**: Usar Firebase Realtime Database ou Firestore en lugar de MongoDB. Vantaxe: menos setup. Desvantaxe: menos control, posible vendor lock-in
- **ALT-002**: Usar GraphQL en lugar de REST API. Vantaxe: consultas máis flexibles. Desvantaxe: maior complexidade
- **ALT-003**: Usar Room Database (SQLite local) en lugar de MongoDB. Vantaxe: máis sinxelo. Desvantaxe: sen sincronización en cloud
- **ALT-004**: Implementar sincronización unidireccional (só lectura desde MongoDB). Vantaxe: máis simple. Desvantaxe: sen capacidade de escribir na cloud

## 4. Dependencias

- **DEP-001**: Servidor MongoDB (Atlas ou local con Docker)
- **DEP-002**: Backend Node.js/Express (que será desenvolvido en paralelo ou de forma separada)
- **DEP-003**: Retrofit2 v2.11.0+
- **DEP-004**: OkHttp v4.12.0+
- **DEP-005**: Hilt v2.48+ (ou Dagger2)
- **DEP-006**: kotlinx.serialization v1.6.0+ (ou Gson v2.10.1+)
- **DEP-007**: WorkManager v2.8.1+
- **DEP-008**: DataStore v1.0.0+
- **DEP-009**: JWT Library (com.auth0:java-jwt ou similares)

## 5. Ficheiros

### Ficheiros a Crear:

- **FILE-001**: `app/src/main/java/com/dam/simonmedijo/model/User.kt` - Modelo de usuario
- **FILE-002**: `app/src/main/java/com/dam/simonmedijo/model/GameRecord.kt` - Modelo de récord actualizado
- **FILE-003**: `app/src/main/java/com/dam/simonmedijo/api/GameRecordApiService.kt` - Interface Retrofit
- **FILE-004**: `app/src/main/java/com/dam/simonmedijo/api/RetrofitClient.kt` - Configuración de Retrofit
- **FILE-005**: `app/src/main/java/com/dam/simonmedijo/api/AuthInterceptor.kt` - Interceptor de autenticación
- **FILE-006**: `app/src/main/java/com/dam/simonmedijo/api/ApiException.kt` - Excepcións personalizadas
- **FILE-007**: `app/src/main/java/com/dam/simonmedijo/repository/RemoteGameRecordRepository.kt` - Repositorio remoto
- **FILE-008**: `app/src/main/java/com/dam/simonmedijo/repository/GameRecordRepositoryImpl.kt` - Repositorio híbrido
- **FILE-009**: `app/src/main/java/com/dam/simonmedijo/auth/AuthManager.kt` - Xestión de autenticación
- **FILE-010**: `app/src/main/java/com/dam/simonmedijo/sync/SyncWorker.kt` - Worker para sincronización
- **FILE-011**: `app/src/main/java/com/dam/simonmedijo/ui/screens/LoginScreen.kt` - UI de login
- **FILE-012**: `app/src/main/java/com/dam/simonmedijo/ui/screens/RegisterScreen.kt` - UI de rexistro
- **FILE-013**: `app/src/main/java/com/dam/simonmedijo/ui/screens/ProfileScreen.kt` - UI de perfil
- **FILE-014**: Backend: `backend/src/models/User.js` - Modelo MongoDB User
- **FILE-015**: Backend: `backend/src/models/GameRecord.js` - Modelo MongoDB GameRecord
- **FILE-016**: Backend: `backend/src/routes/auth.js` - Rutas de autenticación
- **FILE-017**: Backend: `backend/src/routes/records.js` - Rutas de récords
- **FILE-018**: Backend: `backend/src/middleware/auth.js` - Middleware de JWT

### Ficheiros a Modificar:

- **FILE-019**: `app/build.gradle.kts` - Engadir dependencias
- **FILE-020**: `app/src/main/AndroidManifest.xml` - Engadir permisos de INTERNET
- **FILE-021**: `app/src/main/java/com/dam/simonmedijo/ViewModel/MyVM.kt` - Actualizar para usar repositorio
- **FILE-022**: `app/src/main/java/com/dam/simonmedijo/controller/ControllerShPre.kt` - Engadir sincronización
- **FILE-023**: `app/src/main/java/com/dam/simonmedijo/model/Conexion.kt` - Posible ampliación da interfaz
- **FILE-024**: `app/src/main/java/com/dam/simonmedijo/MainActivity.kt` - Engadir fluxo de autenticación

## 6. Testes

- **TEST-001**: Test unitario: `RemoteGameRecordRepositoryTest` - Verificar chamadas a API con Mockito
- **TEST-002**: Test de integración: `RetrofitApiServiceTest` - Usar MockWebServer para simular servidor
- **TEST-003**: Test unitario: `AuthManagerTest` - Verificar login, rexistro, almacenamento de token
- **TEST-004**: Test unitario: `SyncWorkerTest` - Verificar lóxica de sincronización
- **TEST-005**: Test de integración: `GameRecordRepositoryImplTest` - Sincronización local ↔ remota
- **TEST-006**: Test de UI: `LoginScreenTest` - Verificar funcionalidade da pantalla de login
- **TEST-007**: Test de UI: `RegisterScreenTest` - Verificar funcionalidade da pantalla de rexistro
- **TEST-008**: Test de backend: `authRoutes.test.js` - Verificar endpoints de autenticación
- **TEST-009**: Test de backend: `recordRoutes.test.js` - Verificar endpoints de récords
- **TEST-010**: Test e2e: Fluxo completo de rexistro, login, xogo, sincronización

## 7. Riscos e Asuncións

- **RISK-001**: A falta de conexión a internet non debe impedir o funcionamento local do xogo. *Mitigación*: Implementar fallback a SharedPreferences local
- **RISK-002**: Conflitos de datos entre versión local e remota. *Mitigación*: Implementar estratexia de resolución (timestamp, merge logic)
- **RISK-003**: Problemas de rendemento se a sincronización é moi frecuente. *Mitigación*: Usar debouncing e sincronización periódica
- **RISK-004**: Seguridade: Tokens JWT expostos. *Mitigación*: Almacenar en DataStore encriptado, non en SharedPreferences
- **RISK-005**: Problemas de compatibilidade con versións antigas de Android. *Mitigación*: Testar con minSdk 24+, usar bibliotecas compatibles
- **ASSUMPTION-001**: O backend será desenvolvido de forma separada ou en rama paralela
- **ASSUMPTION-002**: MongoDB Atlas estará dispoñible e configurado antes de comezar o desenvolvemento
- **ASSUMPTION-003**: A aplicación seguirá usando Jetpack Compose para a UI
- **ASSUMPTION-004**: O equipo está familiarizado con Kotlin, Coroutines e Retrofit
- **ASSUMPTION-005**: Haberá acceso a un ambiente de staging para testes antes de producción

## 8. Especificacións Relacionadas / Lecturas Complementarias

- [Retrofit2 Documentation](https://square.github.io/retrofit/)
- [OkHttp Interceptors Guide](https://square.github.io/okhttp/interceptors/)
- [Hilt Dependency Injection](https://developer.android.com/training/dependency-injection/hilt-android)
- [Android WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager)
- [Protocol Buffers vs JSON](https://developers.google.com/protocol-buffers)
- [JWT Tokens Best Practices](https://tools.ietf.org/html/rfc7519)
- [MongoDB Atlas Documentation](https://docs.atlas.mongodb.com/)
- [Express.js Guide](https://expressjs.com/)
- [Jetpack Compose Testing](https://developer.android.com/jetpack/compose/testing)
- [Android Security Best Practices](https://developer.android.com/privacy-and-security/best-practices)

