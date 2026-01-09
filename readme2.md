# Simon Me Dixo

![Icono da app](app/src/main/res/mipmap-mdpi/ic_launcher.webp)

Un xogo de memoria en Android desenvolvido con Jetpack Compose: reproduce secuencias de cores e sons que o usuario debe repetir; controla niveis, rexistros e efectos de son.

> [!NOTE]
> Este ficheiro está escrito en galego e contén instrucións para compilar, executar e probar a aplicación.

## Características principais

- Xogo tipo «Simón di» con secuencias de cores e sons.
- UI construída con Jetpack Compose.
- Arquitectura baseada en ViewModel para xestionar estado.
- Uso de recursos en `res/raw` para efectos de son.
- Almacenamento de puntuacións mediante SharedPreferences (ControllerShPre).

## Capturas / Imaxes

A icona da aplicación está dispoñible en:

- `app/src/main/res/mipmap-mdpi/ic_launcher.webp`
- `app/src/main/res/mipmap-mdpi/ic_launcher_round.webp`

Podes referencialas directamente no markdown como se mostra arriba.

## Requisitos

- Android Studio (versión recomendada: recente que soporte Jetpack Compose)
- JDK 11 (proxecto configurado con `jvmTarget = 11`)
- Android SDK con API 36 (compileSdk = 36, targetSdk = 36)
- Gradle wrapper incluído (`./gradlew`)

## Instalación

1. Clona este repositorio:

```bash
git clone <url-do-repositorio>
cd SimonMeDijo_DanielFigueroa
```

2. Abre o proxecto en Android Studio: "File → Open" e selecciona a carpeta raíz do proxecto.

3. Compila desde terminal (raíz do proxecto):

```bash
./gradlew clean assembleDebug
```

4. Para instalar nun dispositivo/emulador conectado:

```bash
./gradlew installDebug
```

> [!WARNING]
> Se usas un emulador, os sons poden depender da configuración do host; para probas de audio fiables recomenda usar un dispositivo físico.

## Uso

- ApplicationId: `com.dam.simonmedijo`
- VersionName: `1.0`

Fluxo básico do xogo:

1. Estado inicial: espera a que o usuario comece.
2. Xeración da secuencia (cores + sons).
3. Usuario repite a secuencia; o ViewModel valida as pulsacións.
4. Ao final do nivel, actualízase o récord e pódese reiniciar.

Controles básicos: inicio do xogo, botóns de cor para repetir a secuencia, reiniciar xogo.

## Probas

Executa os tests de unidade locais:

```bash
./gradlew test
```

Para tests de instrumentación (necesítase dispositivo ou emulador conectado):

```bash
./gradlew connectedAndroidTest
```

> [!NOTE]
> Os recursos de audio están en `app/src/main/res/raw`. Se non se escoitan sons, comproba o volume do emulador/dispositivo e os permisos relacionados.

## Contribución (mínima)

- Forkea o proxecto e crea unha rama cun nome claro (`feat/`, `fix/`, `docs/`).
- Mantén o código en Kotlin e formatea segundo as convencións do proxecto.
- Engade tests cando sexa relevante.
- Abre un Pull Request con descrición clara do cambio.

## Estrutura do proxecto (resumo)

- `app/src/main/java` — código Kotlin da aplicación.
- `app/src/main/res/raw` — efectos de son e recursos multimedia.
- `app/src/main/res/mipmap-*` — iconas da aplicación.
- `app/build.gradle.kts` — configuración do módulo app (compileSdk, dependencias, versionName).

## Problemas comúns

- Emulador sen son: usa un dispositivo físico ou comproba a saída de audio do host.
- Erros de compilación por versións: comproba que tes JDK 11 e Android SDK API 36 instalados.

## Suxerencias e melloras futuras

- Engadir integración continua (GitHub Actions) para compilar e correr tests.
- Engadir badges na parte superior do README (build, license, kotlin version).
- Mellorar cobertura de tests unitarios e instrumentados.

## Autoría
Daniel Figueroa Vidal

---

Se queres, podo xerar tamén unha versión resumida para a páxina do proxecto en GitHub ou engadir badges e comandos específicos de CI.

