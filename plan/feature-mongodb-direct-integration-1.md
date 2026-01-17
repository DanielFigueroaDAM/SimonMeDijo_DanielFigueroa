---
titulo: "Integración Directa de MongoDB Atlas con SimonMeDijo"
proposito: "feature"
componente: "mongodb-direct-integration"
version: 1.0
fecha_creacion: "2026-01-15"
ultima_actualizacion: "2026-01-15"
propietario: "Daniel Figueroa"
estado: "Planificado"
prioridad: "ALTA"
esfuerzo: "MEDIO"
---

# Plan de Desarrollo: Integración Directa con MongoDB Atlas (Sin Backend Propio)

![Estado: PLANIFICADO](https://img.shields.io/badge/estado-PLANIFICADO-0099ff)
![Prioridad: ALTA](https://img.shields.io/badge/prioridad-ALTA-red)
![Esfuerzo: MEDIO](https://img.shields.io/badge/esfuerzo-MEDIO-yellow)

## 🎯 Objetivo Principal

Reemplazar la arquitectura cliente-servidor por una conexión directa y segura desde la aplicación Android a MongoDB Atlas utilizando **Atlas Device Sync (Realm)**. El objetivo es persistir los récords del juego (`GameRecord`) en la nube con una estrategia offline-first, eliminando la necesidad de mantener un backend personalizado.

## 🛠️ Stack Tecnológico

| Categoría | Tecnología Elegida | Justificación |
|-----------|-------------------|---------------|
| **BD Remota** | MongoDB Atlas (Cloud) | Base de datos NoSQL gestionada, escalable y flexible. |
| **Sincronización**| Atlas Device Sync (Realm) | Provee sincronización de datos bidireccional, segura y en tiempo real entre el cliente y la nube. Offline-first por diseño. |
| **BD Local** | Realm Kotlin SDK | Base de datos local de alto rendimiento, orientada a objetos y que se integra nativamente con Device Sync. |
| **Autenticación** | Atlas App Services Authentication | Maneja la autenticación de usuarios (Email/Pass, Google, etc.) de forma segura sin un backend propio. |
| **Cliente Android**| Kotlin + Coroutines + Flow | Stack moderno para el desarrollo de Android. Realm SDK se integra perfectamente con Flows para datos reactivos. |

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
│  │  └─ Realm Kotlin SDK (Local DB & Sync Manager)         │ │
│  ├─────────────────────────────────────────────────────────┤ │
│  │  Data Models (RealmObject)                              │ │
│  └─────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                            ↕ Protocolo WebSocket Seguro (WSS)
┌─────────────────────────────────────────────────────────────┐
│               MONGODB ATLAS APP SERVICES                    │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │  Device Sync Service                                    │ │
│  ├─────────────────────────────────────────────────────────┤ │
│  │  Authentication Service                                 │ │
│  ├─────────────────────────────────────────────────────────┤ │
│  │  Data Access Rules & Permissions                        │ │
│  └─────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                            ↕
┌─────────────────────────────────────────────────────────────┐
│                 MongoDB Atlas Database                      │
│  - Colección: GameRecord                                   │
└─────────────────────────────────────────────────────────────┘
```

## 📅 Plan de Acción Detallado

### Fase 1: Configuración de MongoDB Atlas y App Services (Semana 1)

**Objetivo**: Tener un backend-as-a-service configurado en Atlas, listo para conectar con la app.

#### TASK-101: Configurar una App en Atlas App Services

| Aspecto | Detalle |
|--------|---------|
| **Plataforma** | [MongoDB Atlas UI](https://cloud.mongodb.com/) |
| **Acción** | Dentro de tu clúster, ve a la pestaña `App Services` y crea una nueva aplicación. |
| **App ID** | Anota el `App ID` generado. Será necesario para inicializar el SDK en Android. |
| **Criterio de éxito** | Una aplicación de App Services está creada y vinculada a tu clúster. |

#### TASK-102: Habilitar Autenticación de Usuarios

| Aspecto | Detalle |
|--------|---------|
| **Plataforma** | UI de Atlas App Services → `Authentication` |
| **Proveedor** | Habilitar el proveedor `Email/Password`. |
| **Configuración** | Configurar la confirmación automática de usuarios para simplificar el registro. |
| **Criterio de éxito** | Los usuarios se pueden registrar y autenticar desde el SDK de Android. |

#### TASK-103: Definir el Esquema y Habilitar Device Sync

| Aspecto | Detalle |
|--------|---------|
| **Plataforma** | UI de Atlas App Services → `Device Sync` |
| **Acción** | Habilitar Device Sync. Elegir una `Partition Key` (clave de partición). `owner_id` es una excelente opción. |
| **Esquema** | Definir el esquema JSON para la colección `GameRecord`. Debe coincidir con el modelo de datos en la app. |
| **Permisos** | Configurar las reglas de acceso. Una regla típica es que los usuarios solo pueden leer y escribir los documentos donde el campo `owner_id` coincide con su propio ID de usuario. |
| **Criterio de éxito** | Device Sync está activo y las reglas de seguridad impiden que un usuario vea los datos de otro. |

### Fase 2: Integración del Cliente Android (Semana 1-2)

**Objetivo**: Integrar el SDK de Realm en la app de Android y gestionar la autenticación.

#### TASK-201: Añadir dependencias y configurar el SDK

| Aspecto | Detalle |
|--------|---------|
| **Archivo** | `app/build.gradle.kts` |
| **Plugin** | Añadir el plugin `io.realm.kotlin`. |
| **Dependencia** | `implementation("io.realm.kotlin:library-sync:...")` |
| **Inicialización** | En la clase `Application`, inicializar el `App` de Realm con el `App ID` obtenido en la Fase 1. |
| **Criterio de éxito** | El proyecto compila y la aplicación se inicia sin errores relacionados con Realm. |

#### TASK-202: Crear el Modelo de Datos Local (`RealmObject`)

| Aspecto | Detalle |
|--------|---------|
| **Archivo** | `.../data/local/GameRecord.kt` |
| **Definición** | Crear una clase `GameRecord` que herede de `RealmObject`. |
| **Campos** | `_id` (ObjectId), `owner_id` (String), `score` (Int), `level` (Int), `timestamp` (RealmInstant). El campo `owner_id` será la clave de partición. |
| **Anotaciones** | `@PrimaryKey` para `_id`. `@PersistedName` si los nombres en Kotlin no coinciden con el esquema de Atlas. |
| **Criterio de éxito** | El modelo de datos está definido y el plugin de Realm lo procesa correctamente. |

#### TASK-203: Implementar UI y Lógica de Autenticación

| Aspecto | Detalle |
|--------|---------|
| **Archivos** | `AuthViewModel.kt`, `LoginFragment.kt`, `RegisterFragment.kt` |
| **Lógica** | Usar `app.emailPasswordAuth.registerUser(...)` para el registro y `app.login(Credentials.emailPassword(...))` para el login. |
| **Gestión de sesión** | El objeto `app.currentUser` permite saber si hay un usuario logueado. Persiste entre reinicios de la app. |
| **Logout** | Llamar a `app.currentUser?.logOut()`. |
| **Criterio de éxito** | Un usuario puede registrarse, iniciar sesión y cerrar sesión en la aplicación. La sesión se mantiene al reiniciar la app. |

### Fase 3: CRUD y Sincronización de Datos (Semana 2)

**Objetivo**: Leer y escribir récords del juego, permitiendo que Device Sync los sincronice automáticamente.

#### TASK-301: Abrir una Instancia de Realm Sincronizada

| Aspecto | Detalle |
|--------|---------|
| **Lógica** | Después de que un usuario inicie sesión, configurar `SyncConfiguration` con el `user` y la clave de partición (`owner_id`). |
| **Instancia** | Abrir una instancia de Realm con esta configuración: `Realm.open(syncConfig)`. |
| **Inyección** | Proveer esta instancia de Realm a los ViewModels/Repositorios (ej. con Hilt/Koin). |
| **Criterio de éxito** | La app abre una conexión con Atlas y está lista para sincronizar datos para el usuario logueado. |

#### TASK-302: Implementar el Repositorio de Datos

| Aspecto | Detalle |
|--------|---------|
| **Archivo** | `GameRecordRepository.kt` |
| **Lectura (Read)** | Usar `realm.query<GameRecord>()` para obtener un `Flow<ResultsChange<GameRecord>>`. La UI se actualizará automáticamente cuando los datos cambien. |
| **Escritura (Create/Update)** | Dentro de un bloque `realm.write { ... }`, crear una nueva instancia de `GameRecord` usando `copyToRealm()` o modificar un objeto ya existente. |
| **Borrado (Delete)** | Dentro de un bloque `realm.write { ... }`, llamar a `delete()` sobre la consulta del objeto a borrar. |
| **Criterio de éxito** | La app puede crear, leer, actualizar y borrar récords del juego. Los cambios se reflejan en la UI en tiempo real. |

#### TASK-303: Verificar Sincronización en la Nube

| Aspecto | Detalle |
|--------|---------|
| **Acción** | Realiza cambios en la aplicación (crea un nuevo récord). |
| **Verificación** | Ve a la UI de MongoDB Atlas y busca en la colección `GameRecord`. El nuevo documento debe aparecer allí después de unos segundos. |
| **Prueba Inversa** | Edita un documento directamente en Atlas. El cambio debe reflejarse en la aplicación en tiempo real (gracias al `Flow` de Realm). |
| **Prueba Offline** | Pon el dispositivo en modo avión, haz cambios y luego reconecta. Los cambios pendientes deben sincronizarse automáticamente. |
| **Criterio de éxito** | La sincronización de datos es bidireccional y funciona correctamente, incluso después de estar offline. |

Este enfoque simplifica enormemente la arquitectura, elimina los costos y el mantenimiento de un servidor, y te da una solución de persistencia y sincronización robusta y escalable de forma nativa.