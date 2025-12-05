

# Test en Android

En el desarrollo de Android, no podemos probar nuestro código de la misma forma que un programa de consola simple. Nuestro código depende de clases, contextos y funcionalidades propias del sistema operativo Android (`Context`, `SharedPreferences`, `Activity`, etc.).

Los **tests unitarios** se ejecutan en un entorno de Java (la JVM de tu ordenador), no en un dispositivo o emulador Android real. Por lo tanto, si tu código intenta hacer `context.getSharedPreferences(...)`, el test fallará porque la JVM no sabe qué es un `Context`.

Para solucionar esto, usamos dos herramientas principales:

* **Mockito**: para "simular" objetos y dependencias.
* **Robolectric**: para "emular" un entorno Android dentro de la JVM.

---

## 1. Mockito: Los Actores de Reparto (Mocks)

### ¿Qué es un "mock"?

Un **mock** (simulación o imitación) es un objeto falso que se hace pasar por uno real. Es como contratar a un actor para que interprete un papel en una escena.

### ¿Para qué sirve?

Mockito nos permite crear estos actores para simular las dependencias de Android que nuestra clase necesita.
En el caso de `ControllerShPreTest`, nuestra clase necesita `Context` y `SharedPreferences`, que no existen en la JVM.

### ¿Cómo lo usamos en `ControllerShPreTest`?

* **Creamos los actores**: Con la anotación `@Mock`, le decimos a Mockito: "Crea un actor que se haga pasar por `Context` y otro que se haga pasar por `SharedPreferences`".
* **Les damos un guion**: Usamos `whenever(...).thenReturn(...)` para definir su comportamiento dentro del test.

### Ventajas principales

* **Aislamiento**: Podemos probar `ControllerShPre` de forma aislada. No importa si `SharedPreferences` funciona correctamente; solo nos importa que nuestra clase se comporte como esperamos cuando recibe ciertos datos.
* **Rapidez**: Los tests se ejecutan en la JVM sin necesidad de un emulador, por lo que son muy rápidos.

---
---
## 2. Robolectric: El Mini-Android en tu Ordenador

### ¿Qué es Robolectric?

Si Mockito nos da actores para interpretar papeles, **Robolectric nos monta un pequeño escenario que se parece a Android**.
Robolectric es una librería que recrea las clases del SDK de Android para que puedan ejecutarse en la JVM. Cuando tu código llama a `Context` o `Resources`, Robolectric intercepta esa llamada y ejecuta una versión "sombra" que funciona sin un dispositivo real.

### ¿Para qué sirve?

Se usa cuando simular con Mockito se vuelve demasiado complejo o cuando necesitamos un comportamiento más realista del framework de Android.
Es especialmente útil para probar:

* El ciclo de vida de **Activities** y **Fragments**.
* Inflado de **Views**.
* Acceso a **recursos** (strings, drawables, etc.).

### Ejemplo de uso

Si `obtenerRecord` necesitara acceder a un recurso de string de Android:

* Simular todo con Mockito sería muy complicado.
* Con Robolectric, podemos pasarle un `Context` real proporcionado por Robolectric y él se encarga de todo.

### Ventajas principales

* **Realismo**: Permite ejecutar tests usando el framework de Android de forma muy similar a como lo harían en un dispositivo real.
* **Comodidad**: Evita tener que mockear decenas de llamadas al sistema.

---

## 3. Conclusión: ¿Cuándo usar cada uno?

| Herramienta                 | Cuándo usar                                                                             | Ejemplo                                                                                   | Velocidad                                                                          |
| --------------------------- | --------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------- |
| **Mockito (Mocks)**         | Para tests unitarios puros. Cuando quieres aislar tu clase y simular sus dependencias.  | Probar un **ViewModel**, un **Repository**, o una clase de lógica como `ControllerShPre`. | Muy rápido (se ejecuta en la JVM sin emulador).                                    |
| **Robolectric (Emulación)** | Para tests de integración local. Cuando tu test depende mucho del framework de Android. | Probar un **Activity**, un **Fragment**, o una **View personalizada**.                    | Más lento que Mockito, pero mucho más rápido que un test instrumentado (emulador). |
