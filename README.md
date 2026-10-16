# JavaGameEngine

## Español

JavaGameEngine (JGE) es un pequeño motor de videojuegos 2D escrito en Java. Ofrece una estructura ligera para crear juegos con bucle principal, escenas, entidades posicionables, sprites, comportamientos mediante scripts, comprobaciones de colisión y proximidad, entrada de teclado, temporizadores, cámara, texto en pantalla y reproducción básica de audio.

La ventana y el renderizador están construidos con Java Swing y AWT. El proyecto requiere Java 21 y Maven. JGE está pensado como un motor compacto para aprender y desarrollar proyectos 2D pequeños; no incluye editor, sistema de mapas por tiles, cargador de recursos ni simulación física completa.

### Funciones

- Bucle de juego con FPS objetivo, delta de tiempo limitado y utilidades globales de tiempo.
- Escenas que actualizan entidades, ejecutan la lógica propia, eliminan entidades marcadas y entregan instantáneas al renderizador.
- Objetos posicionables con coordenadas basadas en el centro, dimensiones, velocidad y límites.
- Sprites basados en un color sólido o una secuencia de fotogramas `BufferedImage`.
- Scripts con callbacks de inicio, actualización, solapamiento, colisión y proximidad.
- Comprobaciones AABB, resolución básica de colisiones por penetración mínima, proximidad y grupos.
- Comprobaciones explícitas de triggers para una entidad o un grupo.
- Lectura del teclado, movimiento y zoom de cámara, texto en pantalla y modo de depuración.
- Clips de audio y una colección sencilla de recursos indexados por clave.

### Requisitos

- JDK 21 o posterior.
- Maven 3.8 o posterior.
- Entorno de escritorio gráfico para ejecutar `GameWindow` (el renderizador usa Swing).

### Cómo funciona el motor

#### Ventana y bucle

`GameWindow` crea una ventana Swing y un panel `Render2D`. Inicia `GameLoop` en un hilo dedicado con los FPS objetivo indicados. En cada iteración actualiza `Time`, avanza los `Timer` globales, ejecuta el callback de actualización y solicita a Swing que vuelva a dibujar la instantánea más reciente.

```java
new GameWindow(60, new Dimension(960, 540), new MyScene());
```

El `Dimension` de este constructor es `rroyo.jge.core.gameutils.Dimension` de JGE, no `java.awt.Dimension`.

#### Escenas y orden de cada frame

Una escena representa un estado del juego, como un nivel, menú o pantalla de fin de partida. Crea una subclase de `Scene` e implementa `update()`. Las entidades posicionables se registran en su escena al construirse. Un frame sigue este orden:

1. Cada entidad registrada y no eliminada se mueve según su velocidad y el delta de tiempo.
2. Se inician los scripts que lo necesiten y se actualizan; los sprites animados avanzan de fotograma.
3. Se ejecuta el método `update()` de la escena.
4. Se eliminan las entidades y textos marcados; el renderizador recibe instantáneas para dibujar.

Por ello, los cambios de velocidad o entrada realizados dentro de `Scene.update()` normalmente afectan al movimiento del frame siguiente. Las comprobaciones de colisión son explícitas: el motor no comprueba automáticamente todos los pares de objetos.

#### Coordenadas, movimiento y tiempo

La posición de una entidad corresponde al centro de sus límites y usa coordenadas del mundo. El ancho y el alto se expresan en unidades del mundo. La velocidad se interpreta en unidades del mundo por segundo y se multiplica por `Time.deltaTime()` al ejecutar `move()`. En cambio, `move(dx, dy)` aplica inmediatamente un desplazamiento en coordenadas del mundo.

`Time.deltaTime()` depende de la escala global de tiempo; `Time.unscaledDeltaTime()` no. El bucle limita el delta sin escalar a 0,1 segundos para reducir saltos grandes tras una pausa o bloqueo. `Time.setTimeScale(0)` pausa el movimiento escalado y los temporizadores; valores por encima o debajo de 1 aceleran o ralentizan el tiempo escalado.

#### Entidades, scripts e interacciones

Usa `PositionableGameObject` para una entidad física sin sprite, `ScriptableGameObject` para una entidad controlada mediante scripts o `SpriteGameObject` para una entidad dibujable que admite los métodos de interacción de `Event2d`. Los scripts incluidos en el constructor de un objeto scriptable se adjuntan y reciben `start` inmediatamente; `update` se ejecuta en cada frame de la escena.

`Event2d` ofrece:

- `inRange(...)` para una comprobación amplia de proximidad. Cuando están suficientemente cerca, notifica `onInRange` a los participantes con scripts.
- `overlap(...)` para comprobar la intersección AABB sin mover los objetos. Si los límites se intersectan, notifica `onOverlap`.
- `collide(...)` para comprobar el solapamiento y separar los objetos por el eje de menor penetración. Detiene la velocidad del objeto que se mueve en ese eje y notifica `onCollide`.

Estas comprobaciones pueden aplicarse a una entidad posicionable o a un `Group`. Las operaciones de grupo recorren sus miembros, pero no registran ni son propietarias de esas entidades.

`Trigger` es un sensor controlado mediante scripts. Llama explícitamente a `trigger(entity)` o `trigger(group)`; cada miembro que se solape invoca la implementación de `onTrigger(...)` de la subclase. Añadir un trigger a una escena no ejecuta comprobaciones automáticamente.

#### Renderizado, cámara y texto

El renderizador dibuja sprites de color o de imagen, omite las entidades ocultas o eliminadas y descarta las que quedan fuera de los límites visibles de la cámara. El zoom de cámara se limita a un valor mínimo positivo. La posición del texto usa coordenadas de pantalla y no se transforma con la cámara. Activa la depuración con `scene.setDebugMode(true)` para mostrar los límites de las entidades y las conexiones de proximidad.

#### Entrada, temporizadores y recursos

Consulta el teclado desde la lógica de actualización con `Keyboard.key(KeyEvent.VK_SPACE)`, `Keyboard.key('A')` o métodos de flechas como `Keyboard.keyLeft()`. El motor libera las teclas registradas cuando la ventana pierde el foco.

Programa un callback único o repetitivo con `Timer.schedule(seconds, callback)` o `Timer.schedule(seconds, callback, true)`. Los temporizadores son globales y el bucle los avanza usando el delta de tiempo escalado.

`Sprite` puede contener un `Color`, una imagen o varios fotogramas. El constructor animado recibe la duración de cada fotograma en segundos. La aplicación puede cargar imágenes con APIs estándar de Java como `ImageIO`; JGE no incluye un cargador de recursos propio. `Audio` carga archivos compatibles con Java Sound en un `Clip`; llama a `close()` cuando ya no necesites un recurso. `AssetGroup` almacena recursos con claves de texto y no los cierra automáticamente.

### Inicio rápido

Este ejemplo crea un jugador de color, un grupo de paredes, una zona trigger, una etiqueta de HUD, movimiento con teclado, control de cámara, un temporizador y una comprobación básica de colisión. Guárdalo como `Main.java` en un proyecto que incluya la dependencia de JGE.

```java
import rroyo.jge.core.assets.Sprite;
import rroyo.jge.core.gameessentials.GameWindow;
import rroyo.jge.core.gameessentials.Scene;
import rroyo.jge.core.gameobjects.GameObject;
import rroyo.jge.core.gameobjects.Group;
import rroyo.jge.core.gameobjects.Text;
import rroyo.jge.core.gameobjects.positionable.PositionableGameObject;
import rroyo.jge.core.gameobjects.positionable.SpriteGameObject;
import rroyo.jge.core.gameobjects.positionable.Trigger;
import rroyo.jge.core.gameutils.Dimension;
import rroyo.jge.core.gameutils.Keyboard;
import rroyo.jge.core.gameutils.Timer;
import rroyo.jge.interfaces.Script;

import java.awt.Color;
import java.awt.event.KeyEvent;

public final class Main {
    public static void main(String[] args) {
        new GameWindow(60, new Dimension(960, 540), new DemoScene());
    }

    private static final class DemoScene extends Scene {
        private final SpriteGameObject player;
        private final SpriteGameObject wall;
        private final Group walls;
        private final Trigger goal;

        private DemoScene() {
            player = new SpriteGameObject(
                    this, 120, 160, 32, 32, new Sprite(Color.CYAN));
            wall = new SpriteGameObject(
                    this, 320, 160, 40, 180, new Sprite(Color.GRAY));
            walls = new Group(wall);

            goal = new Trigger(this, 520, 160, 48, 48) {
                @Override
                public void onTrigger(PositionableGameObject entity) {
                    addText(new Text("¡Objetivo alcanzado!", 16, 48)
                            .setForeground(Color.YELLOW));
                }
            };

            addText(new Text("Muévete con las flechas", 16, 16)
                    .setForeground(Color.WHITE));
            Timer.schedule(5.0f, () ->
                    addText(new Text("Han pasado cinco segundos", 16, 32)
                            .setForeground(Color.GREEN)));
        }

        @Override
        public void update() {
            double speed = 180.0;
            double dx = (Keyboard.key(KeyEvent.VK_RIGHT) ? speed : 0)
                    - (Keyboard.key(KeyEvent.VK_LEFT) ? speed : 0);
            double dy = (Keyboard.key(KeyEvent.VK_DOWN) ? speed : 0)
                    - (Keyboard.key(KeyEvent.VK_UP) ? speed : 0);
            player.setVelocity(dx, dy);

            // Las comprobaciones de colisión y trigger se ejecutan explícitamente en cada frame.
            player.collide(walls);
            player.inRange(walls); // También notifica onInRange a los scripts cuando hay proximidad.
            goal.trigger(player);

            if (camera != null) {
                camera.setPosition((int) player.getPosition().getX(),
                        (int) player.getPosition().getY());
            }
            setDebugMode(Keyboard.key('D'));
        }
    }
}
```

El jugador se mueve antes de ejecutar `update()`, así que la velocidad elegida a partir del teclado se aplica en el frame siguiente. Pulsa `D` para activar o desactivar la depuración. El ejemplo solo usa sprites de color sólido generados en el código, por lo que no requiere archivos de imagen ni audio.

#### Animación de imágenes

Crea fotogramas con `BufferedImage` (o cárgalos mediante `ImageIO`) e indica la duración de cada fotograma en segundos:

```java
BufferedImage idle = ImageIO.read(new File("assets/idle.png"));
BufferedImage walk1 = ImageIO.read(new File("assets/walk-1.png"));
BufferedImage walk2 = ImageIO.read(new File("assets/walk-2.png"));

Sprite walkCycle = new Sprite(0.12f, idle, walk1, walk2);
SpriteGameObject character = new SpriteGameObject(
        scene, 100, 100, 48, 48, walkCycle);
```

El sprite avanza por sus fotogramas mientras la escena procesa los frames. Puedes voltearlo horizontalmente asignándole un `SpriteHorizontalDirection`.

#### Colecciones de recursos y audio

Los recursos pueden guardarse con claves de texto para recuperarlos cómodamente. La creación de `Audio` puede lanzar excepciones comprobadas si el formato no es compatible o el sistema no dispone de una línea de audio:

```java
import rroyo.jge.core.assets.AssetGroup;
import rroyo.jge.core.assets.Audio;

import java.io.File;

AssetGroup assets = new AssetGroup();
Audio music = new Audio(new File("assets/theme.wav"));
assets.add("theme", music);

((Audio) assets.get("theme")).loop();

// Cuando el juego ya no necesite el sonido:
music.close();
```

Coloca la carga en un método o constructor que gestione `IOException`, `UnsupportedAudioFileException` y `LineUnavailableException`. Los formatos compatibles dependen de los proveedores Java Sound instalados en el sistema.

#### Callbacks de scripts

Adjunta un script a un `SpriteGameObject` para reaccionar a su ciclo de vida y a sus interacciones:

```java
SpriteGameObject actor = new SpriteGameObject(
        scene, 100, 100, 32, 32, new Sprite(Color.ORANGE), new Script() {
            @Override
            public void start(GameObject self) {
                System.out.println("Entidad inicializada");
            }

            @Override
            public void update(GameObject self) {
                // Se ejecuta una vez por frame de escena para este script.
            }

            @Override
            public void onCollide(GameObject self, GameObject other) {
                System.out.println("Colisión con " + other);
            }
        });
```

Para que se ejecuten los callbacks, la lógica del juego debe realizar la comprobación correspondiente mediante `overlap`, `collide` o `inRange`. El método `start` de un script pasado al constructor se ejecuta al adjuntarlo.

### Importar la dependencia

El proyecto publica artefactos Maven en GitHub Packages, en `https://maven.pkg.github.com/rroyo6500/JavaGameEngine`. Añade este repositorio y la dependencia al `pom.xml` de tu aplicación:

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/rroyo6500/JavaGameEngine</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>rroyo.jge</groupId>
        <artifactId>javagameengine</artifactId>
        <version>VERSION</version>
    </dependency>
</dependencies>
```

Sustituye `VERSION` por una versión que se haya publicado realmente y a la que tu cuenta tenga acceso.

#### Autenticación de Maven

GitHub Packages puede requerir autenticación cuando Maven descarga el artefacto. Crea un token de acceso personal de GitHub (classic) con, como mínimo, permiso `read:packages` y comprueba que tu cuenta tenga acceso al paquete. Guarda las credenciales en la configuración Maven de tu usuario (`~/.m2/settings.xml` en Linux/macOS o `%USERPROFILE%\.m2\settings.xml` en Windows), no en el POM del proyecto ni en el control de versiones:

```xml
<settings>
    <servers>
        <server>
            <id>github</id>
            <username>${env.GITHUB_USERNAME}</username>
            <password>${env.GITHUB_TOKEN}</password>
        </server>
    </servers>
</settings>
```

Define `GITHUB_USERNAME` con tu nombre de usuario de GitHub y `GITHUB_TOKEN` con el token antes de ejecutar Maven. El `<id>` debe coincidir con el id del repositorio (`github`) indicado en el POM. Mantén el token privado: no lo subas al repositorio ni lo incluyas en la declaración de dependencia. Consulta la guía oficial de GitHub: [Uso del registro de Maven de Apache](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry).

### Compilar y ejecutar las pruebas

Desde un clon de este repositorio, ejecuta:

```shell
mvn clean verify
```

Este comando compila el proyecto y ejecuta las pruebas JUnit. El proyecto está configurado para Java 21. Para generar solo el JAR sin ejecutar la fase de verificación, usa `mvn package`.

### Limitaciones actuales

- Las comprobaciones de colisión son manuales; no hay una pasada automática de colisiones ni un índice espacial de fase amplia.
- La respuesta de colisión es una separación AABB sencilla, no sustituye a un motor de física completo.
- El tiempo, los temporizadores, el teclado y el registro de entidades usan utilidades estáticas globales; varias instancias de juego en un mismo proceso no están aisladas.
- `GameWindow.setScene(...)` actualiza el callback guardado.
- El renderizado usa Swing/AWT y está orientado a aplicaciones de escritorio.

### Coordenadas del proyecto

| Campo | Valor |
| --- | --- |
| Group ID | `rroyo.jge` |
| Artifact ID | `javagameengine` |
| Versión actual del POM | `0.3.0` |
| Versión de Java | `21` |
| Registro de paquetes | GitHub Packages |

---

## English

JavaGameEngine (JGE) is a small 2D game engine written in Java. It provides a lightweight structure for building games with a game loop, scenes, positionable entities, sprites, scripted behavior, collision and proximity checks, keyboard input, timers, camera movement, text rendering, and basic audio playback.

The renderer and window are built on Java Swing and AWT. The project targets Java 21 and Maven. JGE is intended as a compact engine for learning and small 2D projects; it does not include an editor, a tile-map system, a resource loader, or a full physics simulation.

## Features

- A target-FPS game loop with a capped frame delta and global time utilities.
- Scenes that update entities, run scene-specific logic, remove deleted entities, and submit render snapshots.
- Positionable objects with center-based coordinates, dimensions, velocity, and bounds.
- Sprite objects backed by a solid color or a sequence of `BufferedImage` frames.
- Scripts with start, update, overlap, collision, and proximity callbacks.
- AABB overlap checks, basic minimum-penetration collision resolution, proximity checks, and groups.
- Explicit trigger checks for one entity or a group.
- Keyboard polling, camera translation and zoom, on-screen text, and a debug-rendering mode.
- Audio clips and a simple keyed asset collection.

## Requirements

- JDK 21 or newer.
- Maven 3.8 or newer.
- A graphical desktop environment for running `GameWindow` (the renderer uses Swing).

## How the engine works

### Window and loop

`GameWindow` creates a Swing window and a `Render2D` panel. It starts a `GameLoop` on a dedicated thread at the requested target FPS. Each loop iteration updates `Time`, advances global `Timer` instances, runs the active update callback, and asks Swing to repaint the latest render snapshot.

```java
new GameWindow(60, new Dimension(960, 540), new MyScene());
```

The `Dimension` in this constructor is JGE's `rroyo.jge.core.gameutils.Dimension`, not `java.awt.Dimension`.

### Scenes and frame order

A scene is a game state such as a level, menu, or game-over screen. Subclass `Scene` and implement `update()`. Positionable entities register themselves with their scene when they are constructed. A typical frame proceeds in this order:

1. Each registered, non-deleted entity moves using its velocity and the frame delta.
2. Attached scripts are started if necessary and updated; animated sprites advance.
3. The scene's `update()` method runs.
4. Deleted entities and text are removed, then the renderer receives snapshots for painting.

As a result, velocity or input changes made inside `Scene.update()` normally affect movement on the next frame. Collision checks are explicit; the engine does not automatically test every pair of objects.

### Coordinates, movement, and time

Entity positions are the center of their bounding boxes and use world coordinates. Width and height are world units. Velocity is interpreted as world units per second and multiplied by `Time.deltaTime()` when `move()` runs. `move(dx, dy)` applies an immediate world-space displacement instead.

`Time.deltaTime()` is affected by the global time scale; `Time.unscaledDeltaTime()` is not. The loop caps the unscaled frame delta at 0.1 seconds to reduce large jumps after a pause or stall. `Time.setTimeScale(0)` pauses scaled movement and timers; values above or below 1 speed up or slow down scaled time.

### Entities, scripts, and interactions

Use `PositionableGameObject` for a physical entity without a sprite, `ScriptableGameObject` for an entity driven by scripts, or `SpriteGameObject` for an entity that can be drawn and supports the `Event2d` interaction methods. Scripts supplied to a scriptable object's constructor are attached and receive `start` immediately; `update` runs on each scene frame.

`Event2d` offers:

- `inRange(...)` for a broad proximity check. It dispatches `onInRange` to scriptable participants when they are close enough.
- `overlap(...)` for an AABB intersection check without moving either object. It dispatches `onOverlap` when the bounds intersect.
- `collide(...)` for an overlap check followed by a basic separation along the axis of least penetration. It clears the moving object's velocity on that axis and dispatches `onCollide`.

These checks can target an individual positionable entity or a `Group`. Group operations visit their members; they do not automatically register or own those entities.

`Trigger` is a scriptable sensor. Call `trigger(entity)` or `trigger(group)` explicitly; every overlapping member invokes the subclass's `onTrigger(...)` implementation. Merely placing a trigger in a scene does not cause automatic checks.

### Rendering, camera, and text

The renderer draws color-based or image-based sprites, skips hidden and deleted entities, and culls entities outside the camera's visible world bounds. A camera's zoom is clamped to a positive minimum. Text positions are screen-space coordinates and are not transformed by the camera. Enable debug rendering with `scene.setDebugMode(true)` to draw entity bounds and proximity links.

### Input, timers, and assets

Poll keyboard state from update logic with `Keyboard.key(KeyEvent.VK_SPACE)`, `Keyboard.key('A')`, or the arrow helpers such as `Keyboard.keyLeft()`. The engine clears pressed keys when the window loses focus.

Schedule a one-shot or looping callback with `Timer.schedule(seconds, callback)` or `Timer.schedule(seconds, callback, true)`. Timers are global and are advanced by the game loop using scaled delta time.

`Sprite` can hold a `Color`, one image, or multiple image frames. The animated constructor accepts a frame duration in seconds. Images can be loaded by the application with standard Java APIs such as `ImageIO`; JGE does not currently provide a separate asset loader. `Audio` loads a Java Sound supported file into a `Clip`; close assets with `close()` when they are no longer needed. `AssetGroup` stores assets under string keys and does not close them automatically.

## Quick start

The following example creates a colored player, a wall group, a trigger area, a HUD label, keyboard movement, camera control, a timer, and a basic collision check. Save it as `Main.java` in a project that includes the JGE dependency.

```java
import rroyo.jge.core.assets.Sprite;
import rroyo.jge.core.gameessentials.GameWindow;
import rroyo.jge.core.gameessentials.Scene;
import rroyo.jge.core.gameobjects.Group;
import rroyo.jge.core.gameobjects.Text;
import rroyo.jge.core.gameobjects.positionable.SpriteGameObject;
import rroyo.jge.core.gameobjects.positionable.Trigger;
import rroyo.jge.core.gameutils.Dimension;
import rroyo.jge.core.gameutils.Keyboard;
import rroyo.jge.core.gameutils.Timer;
import rroyo.jge.core.gameobjects.GameObject;
import rroyo.jge.interfaces.Script;

import java.awt.Color;
import java.awt.event.KeyEvent;

public final class Main {
    public static void main(String[] args) {
        new GameWindow(60, new Dimension(960, 540), new DemoScene());
    }

    private static final class DemoScene extends Scene {
        private final SpriteGameObject player;
        private final SpriteGameObject wall;
        private final Group walls;
        private final Trigger goal;

        private DemoScene() {
            player = new SpriteGameObject(
                    this, 120, 160, 32, 32, new Sprite(Color.CYAN));
            wall = new SpriteGameObject(
                    this, 320, 160, 40, 180, new Sprite(Color.GRAY));
            walls = new Group(wall);

            goal = new Trigger(this, 520, 160, 48, 48) {
                @Override
                public void onTrigger(rroyo.jge.core.gameobjects.positionable.PositionableGameObject entity) {
                    addText(new Text("Goal reached!", 16, 48).setForeground(Color.YELLOW));
                }
            };

            addText(new Text("Move with the arrow keys", 16, 16).setForeground(Color.WHITE));
            Timer.schedule(5.0f, () ->
                    addText(new Text("Five seconds have passed", 16, 32).setForeground(Color.GREEN)));
        }

        @Override
        public void update() {
            double speed = 180.0;
            double dx = (Keyboard.key(KeyEvent.VK_RIGHT) ? speed : 0)
                    - (Keyboard.key(KeyEvent.VK_LEFT) ? speed : 0);
            double dy = (Keyboard.key(KeyEvent.VK_DOWN) ? speed : 0)
                    - (Keyboard.key(KeyEvent.VK_UP) ? speed : 0);
            player.setVelocity(dx, dy);

            // Collision and trigger checks are explicit and run once per frame here.
            player.collide(walls);
            player.inRange(walls); // Also sends onInRange to attached scripts when nearby.
            goal.trigger(player);

            if (camera != null) {
                camera.setPosition((int) player.getPosition().getX(),
                        (int) player.getPosition().getY());
            }
            setDebugMode(Keyboard.key('D'));
        }
    }
}
```

The player is moved before `update()` is called, so the velocity selected from keyboard input is applied on the following frame. Press `D` to toggle debug rendering. The example uses only generated solid-color sprites, so it requires no external image or audio files.

### Image animation

Create frames with `BufferedImage` (or load them with `ImageIO`) and provide a frame duration in seconds:

```java
BufferedImage idle = ImageIO.read(new File("assets/idle.png"));
BufferedImage walk1 = ImageIO.read(new File("assets/walk-1.png"));
BufferedImage walk2 = ImageIO.read(new File("assets/walk-2.png"));

Sprite walkCycle = new Sprite(0.12f, idle, walk1, walk2);
SpriteGameObject character = new SpriteGameObject(
        scene, 100, 100, 48, 48, walkCycle);
```

The sprite advances through its frames as the scene processes each frame. A sprite can be flipped horizontally by setting its `SpriteHorizontalDirection`.

### Asset collections and audio

Assets can be stored under string keys for convenient retrieval. `Audio` construction can throw checked exceptions when the format is unsupported or the system has no available audio line:

```java
import rroyo.jge.core.assets.AssetGroup;
import rroyo.jge.core.assets.Audio;

import java.io.File;

AssetGroup assets = new AssetGroup();
Audio music = new Audio(new File("assets/theme.wav"));
assets.add("theme", music);

((Audio) assets.get("theme")).loop();

// When the game no longer needs the sound:
music.close();
```

Place loading code in a method or constructor that handles `IOException`, `UnsupportedAudioFileException`, and `LineUnavailableException`. Supported audio formats depend on the Java Sound providers installed on the system.

### Script callbacks

Attach a script to a `SpriteGameObject` to react to its lifecycle and interactions:

```java
SpriteGameObject actor = new SpriteGameObject(
        scene, 100, 100, 32, 32, new Sprite(Color.ORANGE), new Script() {
            @Override
            public void start(GameObject self) {
                System.out.println("Actor initialized");
            }

            @Override
            public void update(GameObject self) {
                // Runs once for this script during each scene frame.
            }

            @Override
            public void onCollide(GameObject self, GameObject other) {
                System.out.println("Collision with " + other);
            }
        });
```

For callbacks to run, game logic must perform the corresponding `overlap`, `collide`, or `inRange` check. `start` for a script passed to the constructor runs as the script is attached.

## Importing the dependency

The project publishes Maven artifacts to GitHub Packages at `https://maven.pkg.github.com/rroyo6500/JavaGameEngine`. Add this repository and dependency to your application's `pom.xml`:

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/rroyo6500/JavaGameEngine</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>rroyo.jge</groupId>
        <artifactId>javagameengine</artifactId>
        <version>0.3.0</version>
    </dependency>
</dependencies>
```

Replace `0.3.0` with a version that has actually been published and that your account can access. The repository's `pom.xml` currently declares `0.3.0`; its publishing workflow can publish a manually selected version or publish the version from a pushed `v*` tag.

### Maven authentication

GitHub Packages may require authentication when Maven resolves the artifact. Create a GitHub personal access token (classic) with at least `read:packages` access, and make sure your account has permission to access the package. Store credentials in your user Maven settings file (`~/.m2/settings.xml` on Linux/macOS or `%USERPROFILE%\.m2\settings.xml` on Windows), not in the project POM or source control:

```xml
<settings>
    <servers>
        <server>
            <id>github</id>
            <username>${env.GITHUB_USERNAME}</username>
            <password>${env.GITHUB_TOKEN}</password>
        </server>
    </servers>
</settings>
```

Set `GITHUB_USERNAME` to your GitHub username and `GITHUB_TOKEN` to the token before running Maven. The `<id>` must match the repository id (`github`) in the POM. Keep the token private; do not commit it or paste it into the dependency declaration. GitHub documents Maven registry authentication and installation in [Working with the Apache Maven registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry).

## Build and tests

From a clone of this repository, run:

```shell
mvn clean verify
```

This compiles the project and runs the JUnit tests. The project currently targets Java 21. To build only the JAR without running verification, use `mvn package`.

## Current limitations

- Entity collision checks are manual; there is no automatic collision pass or broad-phase spatial index.
- The collision response is a simple AABB separation and is not a replacement for a complete physics engine.
- The engine keeps time, timers, keyboard state, and the entity registry in static global utilities, so independent game instances in one process are not isolated.
- `GameWindow.setScene(...)` updates the stored game-loop callback.
- Rendering is based on Swing/AWT and is intended for desktop applications.

## Project coordinates

| Field | Value |
| --- | --- |
| Group ID | `rroyo.jge` |
| Artifact ID | `javagameengine` |
| Current POM version | `0.3.0` |
| Java release | `21` |
| Package registry | GitHub Packages |
