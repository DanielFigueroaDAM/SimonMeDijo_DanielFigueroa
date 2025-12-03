Contenido del Archivo Test.mdKotlin# Guía Rápida de Testing en Android: Mocks y Robolectric

En el desarrollo de Android, no podemos probar nuestro código de la misma forma que un programa de consola simple. Nuestro código depende de clases, contextos y funcionalidades propias del sistema operativo Android (`Context`, `SharedPreferences`, `Activity`, etc.).

Los tests unitarios se ejecutan en un entorno de Java (la JVM de tu ordenador), no en un dispositivo o emulador Android real. Por lo tanto, si tu código intenta hacer `context.getSharedPreferences(...)`, el test fallará porque la JVM no sabe qué es un `Context`.

Para solucionar esto, usamos dos herramientas principales: **Mockito** (para "simular") y **Robolectric** (para "emular").

## 1. Mockito: Los Actores de Reparto (Mocks)

**¿Qué es un "mock"?**

Un mock (del inglés "simulación" o "imitación") es un **objeto falso que se hace pasar por uno real**. Es como contratar a un actor para que interprete un papel en una escena.

**¿Para qué sirve?**

Mockito nos permite crear estos actores para simular las dependencias de Android que nuestra clase necesita. En el caso de `ControllerShPreTest`, nuestra clase necesita `Context` y `SharedPreferences`, que no existen en la JVM.

**¿Cómo lo usamos en `ControllerShPreTest`?**

1.  **Creamos los actores**: Con la anotación `@Mock`, le decimos a Mockito: "Crea un actor que se haga pasar por `Context` y otro que se haga pasar por `SharedPreferences`".Ventaja Principal:•Aislamiento: Nos permite probar ControllerShPre de forma aislada. No nos importa si SharedPreferences funciona bien o no; solo nos importa que nuestra clase se comporte como esperamos cuando SharedPreferences le da ciertos datos.•Rapidez: Son tests muy rápidos porque se ejecutan en la JVM sin necesidad de un emulador.2. Robolectric: El Mini-Android en tu Ordenador¿Qué es Robolectric?Si Mockito nos da actores para interpretar papeles, Robolectric nos monta un pequeño escenario que se parece a Android.Robolectric es una librería que recrea las clases del SDK de Android para que puedan ser ejecutadas en la JVM. Cuando tu código llama a Context o Resources, Robolectric intercepta esa llamada y ejecuta una versión "sombra" (shadow) de ese código que funciona sin un dispositivo real.¿Para qué sirve?Se usa cuando simular con Mockito se vuelve demasiado complejo o cuando necesitas un comportamiento más realista del framework de Android. Es especialmente útil para probar:•El ciclo de vida de Activities y Fragments.•Inflado de Views.•Acceso a recursos (strings, drawables, etc.).Ejemplo de cuándo usar Robolectric en lugar de Mockito:Imagina que obtenerRecord necesitara acceder a un recurso de string de Android:Kotlin2.  **Les damos un guion**: No basta con tener al actor, ¡necesitamos decirle qué hacer! Usamos `whenever(...).thenReturn(...)` para darle sus líneas.Simular todo el sistema de recursos con Mockito sería un infierno. Con Robolectric, simplemente le pasas un Context real (proporcionado por Robolectric) y él se encarga de todo.
    **Ventaja Principal**:
*   **Aislamiento**: Nos permite probar `ControllerShPre` de forma aislada. No nos importa si `SharedPreferences` funciona bien o no; solo nos importa que **nuestra clase se comporte como esperamos** cuando `SharedPreferences` le da ciertos datos.
*   **Rapidez**: Son tests muy rápidos porque se ejecutan en la JVM sin necesidad de un emulador.

---

## 2. Robolectric: El Mini-Android en tu Ordenador

**¿Qué es Robolectric?**

Si Mockito nos da actores para interpretar papeles, **Robolectric nos monta un pequeño escenario que se parece a Android**.

Robolectric es una librería que **recrea las clases del SDK de Android** para que puedan ser ejecutadas en la JVM. Cuando tu código llama a `Context` o `Resources`, Robolectric intercepta esa llamada y ejecuta una versión "sombra" (shadow) de ese código que funciona sin un dispositivo real.

**¿Para qué sirve?**

Se usa cuando simular con Mockito se vuelve demasiado complejo o cuando necesitas un comportamiento más realista del framework de Android. Es especialmente útil para probar:
*   El ciclo de vida de `Activities` y `Fragments`.
*   Inflado de `Views`.
*   Acceso a recursos (strings, drawables, etc.).

**Ejemplo de cuándo usar Robolectric en lugar de Mockito:**

Imagina que `obtenerRecord` necesitara acceder a un recurso de string de Android:
Ventaja Principal:•Realismo: Permite ejecutar tests que usan el framework de Android de una forma muy parecida a como lo harían en un dispositivo real.•Comodidad: Evita tener que "mockear" decenas de llamadas al sistema.Conclusión: ¿Cuándo usar cada uno?|                | Mockito (Mocks)                                                              | Robolectric (Emulación)                                                             | | -------------- | ---------------------------------------------------------------------------- | ----------------------------------------------------------------------------------- | | Cuándo usar| Para tests unitarios puros. Cuando quieres aislar tu clase y simular sus dependencias. | Para tests de integración local. Cuando tu test depende mucho del framework de Android. | | Ejemplo    | Probar un ViewModel, un Repository, una clase de lógica como ControllerShPre. | Probar un Activity, un Fragment, o una View personalizada.                      | | Velocidad  | Muy rápido.                                                                  | Más lento que Mockito, pero mucho más rápido que un test instrumentado (emulador).  |Para ControllerShPre, Mockito es la elección perfecta porque las dependencias (Context, SharedPreferences) son pocas y fáciles de simular.