package model.bomberman;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;

import com.entropyinteractive.JGame;
import com.entropyinteractive.Keyboard;

import model.Bomba;
import model.Heroe;
import model.ObjetoGrafico;
import model.Ranking;
import model.SistemaJuegos;
import model.bonus.Bonus;
import model.bonus.Puerta;
import model.db.DB;
import model.enemigo.Enemigo;
import model.enemigo.EnemigoAzul;
import model.enemigo.EnemigoRosa;
import model.explosion.Explosion;
import model.explosion.ParteExplosion;
import model.factory.OGAbstractFactory;
import model.factory.OGFactoryProducer;
import model.fx_player.FXPlayer;
import model.jugador.Jugador;
import model.panel_superior.Puntaje;
import model.panel_superior.Reloj;
import model.panel_superior.Vidas;
import model.pared.Pared;
import model.pared.ParedLadrillo;
import model.properties.controller.SettingsController;

public class Bomberman extends JGame {
    /*
     * Objetos graficos del juego.
     */
    private Heroe hero;
    private Vector<Enemigo> enemies;
    private Vector<Pared> walls, brickWalls;
    private Vector<Bonus> bonus;
    private Vector<Bomba> bombs;
    private Vector<Explosion> explosions;
    private Ranking ranking;

    private Fondo bg;
    private Flecha arrow;
    private Mundo world;
    private Camara cam;

    /*
     * Elementos del panel superior.
     */
    private Reloj clock;
    private Puntaje points;
    private Vidas life;

    /*
     * Objetos usados para los sonidos de fondo, bombas y bonus.
     */
    private FXPlayer fx;
    private FXPlayer fx_bomb = FXPlayer.BOMB_SOUND;
    private FXPlayer fx_bonus = FXPlayer.BONUS_SOUND;

    /*
     * Representacion del mapa y variables de representacion de cada objeto grafico
     * en el mapa.
     */
    private int[][] mapPositions;
    private final int STONE_WALL = 1;
    private final int ENEMY = 2;
    private final int HERO = 3;
    private final int BRICK_WALL = 4;
    private final int BONUS = 5;
    private final int BOMB = 6;

    /*
     * Limites del escenario.
     */
    public static final int UPPER_WALL_LIMIT = 105;
    public static final int LOWER_WALL_LIMIT = 457;
    public static final int LEFT_WALL_LIMIT = 32;
    public static final int RIGHT_WALL_LIMIT = 768;

    /*
     * Cantidad de paredes de piedra de los bordes, interiores y, cantiadad de
     * paredes de ladrillo a colocar en el mapa.
     */
    public static final int BORDER_WALLS = 73;
    public static final int INTERIOR_WALLS = 55;
    public final int BRICK_WALLS = 30;

    /*
     * Variables de estado para el juego.
     */
    private boolean PLAY = false;
    private boolean IS_MAIN_SCREEN = true;
    private boolean END_GAME = false;
    private boolean PAUSE = false;
    private boolean EXPLOSION_SOUND_END = true;

    /*
     * Variables de estado de la configuracion y teclas configuradas por el jugador.
     */
    private boolean SOUND;
    private HashMap<String, Integer> playerKeys;

    /*
     * Variables de cambio y estado de nivel.
     */
    private boolean IS_CHANGE_STAGE = false;
    private int CHANGE_STATE_COUNTER = 170;
    private int CURRENT_STAGE = 1;
    private int BONUS_PER_STAGE = 2;

    /*
     * Random para el posicionamiento aleatorio y factory para creacion de objetos
     * graficos.
     */
    private Random r = new Random(System.currentTimeMillis());
    public static OGAbstractFactory factory = OGFactoryProducer.getFactory();

    public Bomberman() {
        super("Bomberman", Mundo.WORLD_WIDTH, Mundo.WORLD_HEIGHT);

        /*
         * Se lee la configuracion de properties.json.
         */
        SettingsController.readSettings();

        SistemaJuegos.getPlayer().setNickName(SettingsController.getPlayerName());
        SOUND = SettingsController.getSoundState();
        playerKeys = new HashMap<String, Integer>(SettingsController.getCustomKeys().size());

        /*
         * Se colocan las teclas definidas por el jugador en playerKeys.
         */
        for (String key : SettingsController.getCustomKeys().keySet()) {
            playerKeys.put(key, Integer.parseInt(SettingsController.getCustomKeys().get(key)));
        }
    }

    @Override
    public void gameDraw(Graphics2D g) {
        /*
         * Logica para dibujar las pantallas del juego (inicio, cambio de nivel y fin
         * del juego).
         * 
         * Si el se esta en la pantalla de inicio PLAY = false, cuando se presiona ENTER
         * sobre "Jugar" PLAY = true.
         */
        if (!PLAY) {
            CHANGE_STATE_COUNTER--;

            if (IS_MAIN_SCREEN) {
                renderMainScreen(g);

                if (CHANGE_STATE_COUNTER <= 85) {
                    IS_MAIN_SCREEN = false;
                }
            } else if (IS_CHANGE_STAGE) {
                renderChangeStage(g);
            } else {
                renderRankingScreen(g);

                if (CHANGE_STATE_COUNTER == 0) {
                    CHANGE_STATE_COUNTER = 170;
                    IS_MAIN_SCREEN = true;
                }
            }
        } else {
            renderGameScreen(g);
        }
    }

    @Override
    public void gameUpdate(double delta) {
        Keyboard keyboard = this.getKeyboard();

        /*
         * Control de tecla para salir del juego.
         */
        for (KeyEvent event : keyboard.getEvents()) {
            if ((event.getID() == KeyEvent.KEY_PRESSED) && (event.getKeyCode() == KeyEvent.VK_ESCAPE)) {
                if (SOUND) {
                    fx.stop();
                }
                stop();
            }
        }

        /*
         * Logica para actualizar las pantallas del juego (inicio, cambio de nivel y fin
         * del juego).
         */
        if (!PLAY) {
            if (IS_MAIN_SCREEN) {
                mainScreenUpdate(delta);
            } else {
                for (KeyEvent event : keyboard.getEvents()) {
                    if ((event.getID() == KeyEvent.KEY_PRESSED) && (event.getKeyCode() == KeyEvent.VK_R)) {
                        fx.stop();
                        PLAY = true;
                        CURRENT_STAGE = 1;
                        gameStartup();
                    }
                }
            }
        } else {
            for (KeyEvent e : keyboard.getEvents()) {
                if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == playerKeys.get("Pausa")) {
                    PAUSE = !PAUSE;
                }
            }

            if (!PAUSE) {
                gameScreenUpdate(delta);
            }
        }
    }

    @Override
    public void gameStartup() {
        if (!PLAY) {
            /*
             * Se inicia la camara y su region visible, el fondo de comienzo como pantalla
             * de inicio la flecha que se utiliza para navegar las opciones de "Jugar" y
             * "Continuar", y el se toma la instancia del mundo.
             */
            cam = new Camara(0, 0);
            cam.setVisibleRegion(Mundo.WORLD_WIDTH);
            bg = new Fondo("/imagenes/start_screen.png");
            world = Mundo.getInstance();

            arrow = new Flecha("/imagenes/arrow.png");
            arrow.setPosition(240, 365);

            ranking = new Ranking();
            getRaking();
        } else {
            /*
             * Se instancian los objetos para iniciar el juego.
             */
            mapPositions = new int[13][25];
            hero = new Heroe();
            walls = new Vector<Pared>();
            brickWalls = new Vector<Pared>();
            enemies = new Vector<Enemigo>();
            bonus = new Vector<Bonus>();
            bombs = new Vector<Bomba>(0);
            explosions = new Vector<Explosion>(0);
            clock = new Reloj();
            points = new Puntaje();
            life = new Vidas();

            /*
             * Instanciando a los enemigos, paredes de piedra, paredes de ladrillo y bonus.
             */
            walls.addAll(factory.getParedes(Pared.PARED_PIEDRA, BORDER_WALLS + INTERIOR_WALLS));
            brickWalls.addAll(factory.getParedes(Pared.PARED_LADRILLO, BRICK_WALLS));

            enemies.addAll(factory.getEnemigos(Enemigo.ENEMIGO_ROSA, 6));

            /*
             * Eligiendo dos bonus de forma aleatoria.
             */
            for (int i = 0; i < BONUS_PER_STAGE; i++) {
                bonus.add(factory.getBonus(r.nextInt(6)));
            }
            bonus.add(factory.getBonus(Bonus.PUERTA));

            /*
             * Posicionando heroe.
             */
            hero.setPosition(LEFT_WALL_LIMIT + 2, UPPER_WALL_LIMIT + 2);
            mapPositions[1][1] = 3;

            /*
             * Posicionando borde de paredes de piedra (se usan 73 para los bordes).
             */
            for (int i = 0; i < walls.size(); i++) {
                // paredes horizontales
                if (i < 25) {
                    walls.get(i).setPosition(LEFT_WALL_LIMIT * i, UPPER_WALL_LIMIT - 32); // arriba
                    mapPositions[0][i] = 1;

                    walls.get(i + 25).setPosition(32 * i, LOWER_WALL_LIMIT); // abajo
                    mapPositions[12][i] = 1;
                }

                // paredes verticales
                if (i >= 25 && i <= 36) {
                    walls.get(i + 25).setPosition(0, UPPER_WALL_LIMIT + 32 * (i - 25)); // izquierda
                    mapPositions[i - 25][0] = 1;

                    walls.get(i + 37).setPosition(RIGHT_WALL_LIMIT, UPPER_WALL_LIMIT + 32 * (i - 25)); // derecha
                    mapPositions[i - 25][24] = 1;
                }
            }

            /*
             * Posicionando paredes de piedra centrales (se usan 5 filas x 11 columnas).
             */
            int aux = BORDER_WALLS;

            for (int j = 1; j < 12; j++) { // columnas
                for (int i = 0; i < 5; i++) { // filas
                    walls.get(aux).setPosition(64 * j, UPPER_WALL_LIMIT + 32 * (2 * i + 1));
                    aux++;

                    mapPositions[2 * (i + 1)][2 * j] = 1;
                }
            }

            /*
             * Posicionando a las paredes de ladrillo.
             */
            positionBrickWalls();

            /*
             * Posicionando bonus.
             */
            positionBonus();

            /*
             * Posicionando a los enemigos.
             */
            positionEnemies();
        }
    }

    @Override
    public void gameShutdown() {
    }

    private void renderMainScreen(Graphics2D g) {
        /*
         * Dibujando fondo y movimiento de la flecha de seleccion en la pantalla de
         * inicio.
         */
        bg.setImage("/imagenes/start_screen.png");
        world.setWorldLimits(bg.getWidth(), bg.getHeight());
        bg.draw(g);
        arrow.draw(g);

        /*
         * Se inicia el track de pantalla principal.
         */
        if (SOUND) {
            fx = FXPlayer.TITLE_SCREEN;
            fx.play();
        }
    }

    private void renderRankingScreen(Graphics2D g) {
        bg.setImage("/imagenes/black.png");
        bg.draw(g);

        g.setColor(Color.white);
        g.drawString("Ranking: ", 282, 50);

        /*
         * La "R" para volver a jugar solo se muestra si el jugador perdio.
         */
        if (END_GAME) {
            g.setColor(Color.white);
            g.drawString("Press 'R' to play again", 102, 460);
        }

        g.setColor(Color.white);
        g.drawString("Press 'Esc' to exit", 402, 460);

        Jugador player;
        for (int i = 0; i < ranking.getScores().size(); i++) {
            player = ranking.getScores().get(i);

            g.setColor(Color.white);
            g.drawString(player.getNickName() + ": " + player.getScore(), 282, 140 + 32 * (i));
        }
    }

    private void renderChangeStage(Graphics2D g) {
        bg.setImage("/imagenes/black.png");
        bg.draw(g);

        g.setColor(Color.white);
        g.drawString("Stage " + CURRENT_STAGE, 302, 240);

        if (SOUND) {
            fx = FXPlayer.STAGE_START;
            fx.play();
        }

        if (CHANGE_STATE_COUNTER == 0) {
            PLAY = true;
            IS_CHANGE_STAGE = false;

            if (SOUND) {
                fx.stop();
            }

            if (CURRENT_STAGE == 1) {
                gameStartup();
            } else {
                generateNextLevel();
            }
        }
    }

    private void renderGameScreen(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.translate(cam.getX(), cam.getY());

        /*
         * mostrando el mundo, dibujando el fondo, heroe y timer
         */
        bg.setImage("/imagenes/bg.png");
        world.setWorldLimits(bg.getWidth(), bg.getHeight());

        bg.draw(g);
        world.display(g);

        try {
            BufferedImage img = ImageIO.read(getClass().getResource("/imagenes/upper_panel.png"));
            g.drawImage(img, 0, 0, null);
        } catch (IOException e) {
            System.out.println(e);
        }

        clock.draw(g);
        points.draw(g);
        hero.draw(g);

        life.setLife(hero.getLife());
        life.draw(g);

        for (Bomba b : bombs) {
            if (b.getTime() > 0) {
                b.draw(g);
            }
        }

        for (Explosion e : explosions) {
            if (!e.isVanished()) {
                e.draw(g);

                int row, col;
                for (Vector<ParteExplosion> vDir : e.getExplosion().values()) {
                    for (ParteExplosion pe : vDir) {
                        row = (int) ((pe.getY() + 16) / 32) - 2;
                        col = (int) (pe.getX() + 16) / 32;

                        if ((row >= 0 && row <= 12) && (col >= 0 && col <= 24)) {
                            if (mapPositions[row][col] == 1 || mapPositions[row][col] == 4
                                    || mapPositions[row][col] == 5) {
                                break;
                            } else {
                                pe.draw(g);
                            }
                        }
                    }
                }
            }
        }

        /*
         * se inicia el track de juego
         */
        if (SOUND) {
            fx = FXPlayer.STAGE_THEME;
            fx.play();
        }

        /*
         * dibujando paredes de piedra y ladrillo
         */
        for (Pared p : walls) {
            p.draw(g);
        }

        /*
         * dibujando bonus
         */
        for (Bonus b : bonus) {
            b.draw(g);
        }

        for (Pared bw : brickWalls) {
            bw.draw(g);
        }

        /*
         * dibujando enemigos
         */
        for (Enemigo e : enemies) {
            e.draw(g);
        }

        g.translate(-cam.getX(), -cam.getY());
    }

    private void mainScreenUpdate(double delta) {
        Keyboard keyboard = this.getKeyboard();

        /*
         * si se presiona enter el juego inicia
         */
        if (keyboard.isKeyPressed(KeyEvent.VK_ENTER)) {
            /*
             * se para el track de pantalla principal, se setea el fondo del mapa de juego y
             * nuevos limites del mapa, se hace una pausa para pasar a la pantalla de
             * prejuego y se inicia el juego
             */
            if (SOUND) {
                fx.stop();
            }

            IS_MAIN_SCREEN = false;
            IS_CHANGE_STAGE = true;
        }
    }

    private void gameScreenUpdate(double delta) {
        Keyboard keyboard = this.getKeyboard();

        /*
         * Se incia el track del juego.
         */
        fx = FXPlayer.STAGE_THEME;
        if (SOUND) {
            fx.play();
        }

        for (KeyEvent e : keyboard.getEvents()) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                /*
                 * Tecla definida por el jugador para encender/apagar el sonido.
                 */
                if (keyboard.isKeyPressed(playerKeys.get("Sonido"))) {
                    SOUND = !SOUND;

                    if (SOUND) {
                        fx.play();
                    } else {
                        fx.stop();
                    }

                    SettingsController.setSoundState(SOUND);
                    SettingsController.saveSettings();
                }
                /*
                 * Tecla definida por el jugador para uso del detonador.
                 */
                if (e.getKeyCode() == playerKeys.get("Detonador")) {
                    hero.setHasDetonator(false);
                }
                /*
                 * Tecla definida por el jugador en la configuracion para colocar bombas.
                 */
                if (e.getKeyCode() == playerKeys.get("Acción")) {
                    if (mapPositions[(int) hero.getY() / 32 - 2][(int) hero.getX() / 32] != 1) {
                        /*
                         * El heroe instancia y setea la bomba en su posicion actual para colocarla.
                         */
                        Bomba b = hero.setBomb();

                        /*
                         * Si el objeto Bomba no es nulo.
                         */
                        if (b != null) {
                            if (bombs.size() < hero.getMaxBombs()) {
                                bombs.add(b);
                            }
                        }
                    }
                }
            }
        }

        /*
         * Animaciones del reloj.
         */
        clock.countTime();
        if (clock.getTime() == 0) {
            hero.stop();
        }

        /*
         * Controles de movimiento para el heroe y los enemigos
         * 
         * Si el heroe no esta muerto los enemigos seguiran moviendose y el heroe igual.
         * 
         * Caso contrario, si el heroe todavia tiene vidas se le quitara una y se hace
         * la animacion de muerte.
         * 
         * Si el heroe ya no tiene vidas se pasa a la pantalla de game over donde esta
         * el ranking.
         * 
         * Las variables row y col se usan durante las iteraciones de los objetos
         * graficos para ir calculando sus posiciones en el mapa y hacer controles.
         */
        int row;
        int col;

        if (hero.hasReachedDoor() && enemies.isEmpty()) {
            PLAY = false;
            IS_MAIN_SCREEN = false;
            IS_CHANGE_STAGE = true;
            CHANGE_STATE_COUNTER = 170;
            
            if(CURRENT_STAGE <= 4) {
                CURRENT_STAGE++;
            }

            fx.stop();
        } else {
            hero.setHasReachedDoor(false);

            if (!hero.isDead()) {
                /*
                 * Se itera sobre el mapa para setear las posiciones del heroe y los enemigos en
                 * 0 y liberar los espacios por los que se van moviendo.
                 * 
                 * Otra razon, es que haciendo el seteo en el mapa a 0 de las posiciones
                 * anteriores de los objetos movibles en los controles de movimiento de los
                 * mismos muchas veces deja valores 2 y 3 sobre el mapa invalidos que no son
                 * quitados.
                 */
                for (int i = 1; i < 13; i++) { // filas
                    for (int j = 1; j < 25; j++) { // columnas
                        if (i % 2 == 0) { // fila con paredes de piedra
                            if (j % 2 != 0) { // si no hay una pared de piedra central
                                mapPositions[i][j] = 0;
                            } else { // restaurando posibles seteos en 0 por los objetos movibles
                                mapPositions[i][j] = 1;
                            }
                        } else { // fila sin paredes de piedra
                            mapPositions[i][j] = 0;
                        }
                    }
                }

                for (int i = 0; i < bombs.size(); i++) {
                    Bomba b = bombs.get(i);

                    /*
                     * Si alguna bomba va a explotar.
                     */
                    if (b.getTime() == 0) {
                        /*
                         * La bomba detona (instancia una Explosion y la setea en su posicion), se
                         * agrega la explosion al mapa y se da permiso para agregar una nueva bomba.
                         */
                        explosions.add(b.detonate(hero.getExplosionRange()));
                        bombs.remove(i);
                    } else {
                        /*
                         * Se hace la animacion de la bomba.
                         */
                        if (!hero.hasDetonator()) {
                            b.changeObject();
                        }

                        /*
                         * Si el heroe tiene un bonus de saltar bombas la bomba no se setea en el mapa
                         * para que el heroe pueda pasar por arriba.
                         */
                        if (b.getTime() > 0 && !hero.canJumpBombs()) {
                            setOnMap(b);
                        }
                    }
                }

                /*
                 * Se colocan los bonus en el mapa y, en caso de que todavia no esten
                 * descubiertos, el bucle siguiente (de las paredes de ladrillo) cambiara la
                 * posicion del mapa a 4. Cuando la pared haya sido destruida ya no hara ese
                 * cambio y la posicion quedara ocupara por el identificador del bonus (5).
                 */
                for (int i = 0; i < bonus.size(); i++) {
                    Bonus b = bonus.get(i);

                    row = (int) ((b.getY() + 16) / 32 - 2);
                    col = (int) ((b.getX() + 16) / 32);

                    if(b.getClass() == Puerta.class && b.wasHit()) {
                        mapPositions[row][col] = BONUS;
                    }
                    else if (!b.wasHit()) {
                        mapPositions[row][col] = BONUS;
                    } else if (b.getClass() != Puerta.class) {
                        bonus.remove(i);
                    }
                }

                /*
                 * Si la pared de ladrillo fue golpeada por una explosion se hace la animacion
                 * de la pared destruyendose.
                 * 
                 * Si la pared de ladrillo ya fue destruida (el metodo changeObject() setea la
                 * variable WAS_DESTROYED a true cuando finaliza la animacion) se remueve la
                 * pared.
                 * 
                 * En cualquier otro caso se la posiciona en el mapa.
                 */
                for (int i = 0; i < brickWalls.size(); i++) {
                    if (brickWalls.get(i).wasHit()) {
                        brickWalls.get(i).changeObject();
                    } else if (brickWalls.get(i).wasDestroyed()) {
                        brickWalls.remove(i);
                    } else {
                        setOnMap(brickWalls.get(i));
                    }
                }

                /*
                 * Se setea en el mapa la posicion actual del heroe con un 3 (habiendo borrado
                 * anteriormente la ultima posicion en la que se encontraba).
                 */
                setOnMap(hero);

                /*
                 * Movimiento de los enemigos
                 */
                for (int i = 0; i < enemies.size(); i++) {
                    Enemigo e = enemies.get(i);

                    /*
                     * Si el enemigo no esta muerto (esto se usa para saber cuando remover a los
                     * enemigos, un enemigo estara muerto cuando su variable de estado IS_DEAD sea
                     * cambiada por el metodo kill(), el cual hace la animacion de muerte del
                     * enemigo).
                     */
                    if (!e.isDead()) {
                        /*
                         * Si el enemigo fue golpeado por una explosion se comienza con la animacion de
                         * muerte, sino, se lo mueve por el mapa.
                         */
                        if (e.wasHit() && e.getImmunityTime() == 0) {
                            e.kill();
                        } else {
                            e.changeObject(e.getCurrentDirection());
                        }

                        /*
                         * Se setea en el mapa la posicion actual del enemigo con un 2 (habiendo borrado
                         * anteriormente la ultima posicion en la que se encontraba).
                         */
                        setOnMap(e);

                        /*
                         * Cambio de sprites segun la direccion que toma el enemigo.
                         */
                        if (e.getCurrentDirection() == "up") {
                            /*
                             * Fila y columna actual (con modificaciones con respecto al mapa porque sino
                             * genera superposiciones entre sprites).
                             */
                            row = (int) (e.getY() + 20) / 32 - 2;
                            col = (int) e.getX() / 32;

                            /*
                             * El enemigo no puede pasar a traves de paredes de ladrillo, bonus y bombas
                             * (las paredes de piedra se checkean de forma interna en cada enemigo).
                             */
                            if (mapPositions[row - 1][col] != 4 && mapPositions[row - 1][col] != 6) {
                                e.up(delta);
                            }
                            /*
                             * Si la posicion de arriba es el heroe.
                             */
                            if (mapPositions[row - 1][col] == 3) {
                                hero.stop();
                            }
                        }
                        if (e.getCurrentDirection() == "down") {
                            /*
                             * Fila y columna actual (con modificaciones con respecto al mapa porque sino
                             * genera superposiciones entre sprites).
                             */
                            row = (int) (e.getY() - 10) / 32 - 2;
                            col = (int) e.getX() / 32;

                            /*
                             * El enemigo no puede pasar a traves de paredes de ladrillo, bonus y bombas
                             * (las paredes de piedra se checkean de forma interna en cada enemigo).
                             */
                            if (mapPositions[row + 1][col] != 4 && mapPositions[row + 1][col] != 6) {
                                e.down(delta);
                            }
                            /*
                             * Si la posicion de arriba es el heroe.
                             */
                            if (mapPositions[row + 1][col] == 3) {
                                hero.stop();
                            }
                        }
                        if (e.getCurrentDirection() == "left") {
                            /*
                             * Fila y columna actual (con modificaciones con respecto al mapa porque sino
                             * genera superposiciones entre sprites).
                             */
                            row = (int) e.getY() / 32 - 2;
                            col = (int) (e.getX() + 30) / 32;

                            /*
                             * El enemigo no puede pasar a traves de paredes de ladrillo, bonus y bombas
                             * (las paredes de piedra se checkean de forma interna en cada enemigo).
                             */
                            if (mapPositions[row][col - 1] != 4 && mapPositions[row][col - 1] != 6) {
                                e.left(delta);
                            }
                            /*
                             * Si la posicion de arriba es el heroe.
                             */
                            if (mapPositions[row][col - 1] == 3) {
                                hero.stop();
                            }
                        }
                        if (e.getCurrentDirection() == "right") {
                            /*
                             * Fila y columna actual (con modificaciones con respecto al mapa porque sino
                             * genera superposiciones entre sprites).
                             */
                            row = (int) (e.getY() + 14) / 32 - 2;
                            col = (int) (e.getX()) / 32;

                            /*
                             * El enemigo no puede pasar a traves de paredes de ladrillo, bonus y bombas
                             * (las paredes de piedra se checkean de forma interna en cada enemigo).
                             */
                            if (mapPositions[row][col + 1] != 4 && mapPositions[row][col + 1] != 6) {
                                e.right(delta);
                            }
                            /*
                             * Si la posicion de arriba es el heroe.
                             */
                            if (mapPositions[row][col + 1] == 3) {
                                hero.stop();
                            }
                        }
                    } else {
                        enemies.remove(i);
                    }
                }

                /*
                 * Movimiento de heroe.
                 * 
                 * Los comentarios para los enemigos aplican para el heroe. Se cambian los
                 * sprites segun una direccion, se calcula la fila y columna actual (con alguna
                 * modificaciones por superposicion de sprites), se checkea si el heroe va a
                 * chocar contra un enemigo (en ese caso perdera una vida), si va a chocar
                 * contra un bonus y se checkea que no pase por las paredes de ladrillo, piedra
                 * y bombas (en caso de no tener el bonus de saltar bombas).
                 */
                if (keyboard.isKeyPressed(playerKeys.get("Arriba"))) {
                    hero.changeObject("up");

                    row = (int) ((hero.getY() + 20) / 32 - 2);
                    col = (int) (hero.getX() / 32);

                    if (mapPositions[row - 1][col] == 2) {
                        hero.stop();
                    } else if (mapPositions[row - 1][col] == 5) {
                        hero.consumeBonus(checkForBonus(row - 1, col));
                        if (SOUND) {
                            fx_bonus.play();
                        }
                    } else if (mapPositions[row - 1][col] != 1 && mapPositions[row - 1][col] != 4
                            && mapPositions[row - 1][col] != 6) {
                        hero.up(delta);
                    }
                }

                if (keyboard.isKeyPressed(playerKeys.get("Abajo"))) {
                    hero.changeObject("down");

                    row = (int) ((hero.getY() - 10) / 32 - 2);
                    col = (int) (hero.getX() / 32);

                    if (mapPositions[row + 1][col] == 2) {
                        hero.stop();
                    } else if (mapPositions[row + 1][col] == 5) {
                        hero.consumeBonus(checkForBonus(row + 1, col));
                        if (SOUND) {
                            fx_bonus.play();
                        }
                    } else if (mapPositions[row + 1][col] != 1 && mapPositions[row + 1][col] != 4
                            && mapPositions[row + 1][col] != 6) {
                        hero.down(delta);
                    }
                }

                if (keyboard.isKeyPressed(playerKeys.get("Izquierda"))) {
                    hero.changeObject("left");

                    row = (int) (hero.getY() / 32 - 2);
                    col = (int) ((hero.getX() + 30) / 32);

                    if (mapPositions[row][col - 1] == 2) {
                        hero.stop();
                    } else if (mapPositions[row][col - 1] == 5) {
                        hero.consumeBonus(checkForBonus(row, col - 1));
                        if (SOUND) {
                            fx_bonus.play();
                        }
                    } else if (mapPositions[row][col - 1] != 1 && mapPositions[row][col - 1] != 4
                            && mapPositions[row][col - 1] != 6) {
                        hero.left(delta);
                    }
                }

                if (keyboard.isKeyPressed(playerKeys.get("Derecha"))) {
                    hero.changeObject("right");

                    row = (int) ((hero.getY() + 14) / 32 - 2);
                    col = (int) (hero.getX() / 32);

                    if (mapPositions[row][col + 1] == 2) {
                        hero.stop();
                    } else if (mapPositions[row][col + 1] == 5) {
                        hero.consumeBonus(checkForBonus(row, col + 1));
                        if (SOUND) {
                            fx_bonus.play();
                        }
                    } else if (mapPositions[row][col + 1] != 1 && mapPositions[row][col + 1] != 4
                            && mapPositions[row][col + 1] != 6) {
                        hero.right(delta);
                    }
                }

                /*
                 * La camara seguira al reloj, puntaje, vidas del heroe y al heroe.
                 */
                cam.followUpperPanel(clock, points, life);
                cam.followHero(hero);
            } else {
                if (hero.getLife() > 0) {
                    hero.kill();
                    life.setLife(hero.getLife());
                } else {
                    PLAY = false;
                    IS_MAIN_SCREEN = false;
                    END_GAME = true;

                    if (SOUND) {
                        fx.stop();
                        fx = FXPlayer.GAME_OVER;
                        fx.play();
                    }

                    /*
                     * Ver puntaje del juagador y si entra en el ranking.
                     */
                    SistemaJuegos.getPlayer().setScore(points.getScore());

                    DB sqlt = new DB();
                    sqlt.initDBConn();

                    sqlt.insertScore(SistemaJuegos.getPlayer().getNickName(), SistemaJuegos.getPlayer().getScore());
                    getRaking();
                }
            }
        }

        /*
         * Controles para las explosiones y los objetos que golpean.
         */
        for (int i = 0; i < explosions.size(); i++) {
            /*
             * Se utiliza para controlar el sonido de las explosiones.
             */
            EXPLOSION_SOUND_END = false;

            Explosion e = explosions.get(i);

            /*
             * Si alguna explosion todavia no se desvanecio se sigue con su animacion, sino,
             * se la quita.
             */
            if (!e.isVanished()) {
                e.changeObject();

                for (Vector<ParteExplosion> vDir : e.getExplosion().values()) {
                    for (ParteExplosion pe : vDir) {
                        row = (int) ((pe.getY() + 16) / 32 - 2);
                        col = (int) ((pe.getX() + 16) / 32);

                        if ((row >= 0 && row <= 12) && (col >= 0 && col <= 24)) {
                            /*
                             * Si el heroe esta dentro de la explosion.
                             */
                            if (isInside(pe, hero) && !hero.isDead()) {
                                hero.stop();
                            }
                            if (mapPositions[row][col] == 2) {
                                for (int k = 0; k < enemies.size(); k++) {
                                    if (isInside(pe, enemies.get(k)) && !enemies.get(k).wasHit()
                                            && enemies.get(k).getImmunityTime() == 0) {
                                        enemies.get(k).stop();
                                        if (enemies.get(k).getClass() == EnemigoRosa.class) {
                                            points.setScore(points.getScore() + 100);
                                        } else if (enemies.get(k).getClass() == EnemigoAzul.class) {
                                            points.setScore(points.getScore() + 150);
                                        }
                                    }
                                }
                            }
                            /*
                             * Si la explosión alcanza un bonus que no este cubierto por una pared (de eso se
                             * ocupa el segundo "for" con la ayuda del booleano BONUS_CONTROLLER).Primero se
                             * agregan 6 enemigo azules al vector de enemigos a los que se les asigna la
                             * posición de dicho bonus. Posterior a eso se elimina ese bonus del vector
                             * correspondiente.
                             */
                            /*
                             * Si la exploción alcanza a otra bomba hace que esta se detone.
                             */
                            if (mapPositions[row][col] == 5) {
                                for (int k = 0; k < bonus.size(); k++) {
                                    if (isInside(pe, bonus.get(k))) {
                                        boolean BONUS_CONTROLLER = true;

                                        for (Pared bw : brickWalls) {
                                            if (isInside(bw, bonus.get(k))) {
                                                BONUS_CONTROLLER = false;
                                                break;
                                            }
                                        }

                                        if (BONUS_CONTROLLER) {
                                            bonus.get(k).hit();
                                            enemies.addAll(bonus.get(k).spawnEnemies());
                                        }

                                        break;
                                    }
                                }
                            }
                            if (mapPositions[row][col] == 1 || mapPositions[row][col] == 4) {
                                if (mapPositions[row][col] == 4) {
                                    for (Pared bw : brickWalls) {
                                        if (isInside(pe, bw)) {
                                            bw.hit();
                                            break;
                                        }
                                    }
                                }

                                break;
                            }
                        }
                    }
                }
            } else {
                explosions.remove(i);
            }

            if (SOUND && !EXPLOSION_SOUND_END) {
                fx_bomb = FXPlayer.BOMB_SOUND;
                fx_bomb.play();
                fx_bonus.stop();
                EXPLOSION_SOUND_END = true;
            }
        }
    }

    /*
     * Metodo para setear en el mapa los diferentes objetos graficos.
     */
    private void setOnMap(ObjetoGrafico o) {
        int row = (int) ((o.getY() + 16) / 32 - 2);
        int col = (int) ((o.getX() + 16) / 32);

        if (o.getClass() == Pared.class) {
            mapPositions[row][col] = STONE_WALL;
        } else if (o.getClass() == Heroe.class) {
            mapPositions[row][col] = HERO;
        } else if (o.getClass() == EnemigoRosa.class || o.getClass() == EnemigoAzul.class) {
            mapPositions[row][col] = ENEMY;
        } else if (o.getClass() == ParedLadrillo.class) {
            mapPositions[row][col] = BRICK_WALL;
        } else if (o.getClass() == Bomba.class) {
            mapPositions[row][col] = BOMB;
        }
    }

    /*
     * Metodo para checkear con que bonus el heroe va a chocar.
     */
    private Bonus checkForBonus(int row, int col) {
        int rowB;
        int colB;
        Bonus b;

        for (int i = 0; i < bonus.size(); i++) {
            b = bonus.get(i);
            rowB = (int) (b.getY() / 32 - 2);
            colB = (int) (b.getX() / 32);

            if (rowB == row && colB == col) {
                if (b.getClass() == Puerta.class) {
                    if (enemies.isEmpty()) {
                        points.setScore(points.getScore() + 200);
                        bonus.remove(i);
                    }
                } else {
                    points.setScore(points.getScore() + 50);
                    bonus.remove(i);
                }

                return b;
            }
        }

        return null;
    }

    /*
     * Metodo para checkear si o2 esta dentro de o1.
     */
    private boolean isInside(ObjetoGrafico o, Bonus b) {
        boolean INSIDE_LEFT_RIGHT = (o.getX() <= b.getX() && b.getX() <= o.getX() + 30
                || o.getX() <= b.getX() + 28 && b.getX() + 28 <= o.getX() + 30) && o.getY() <= b.getY() + 16
                && b.getY() + 16 <= o.getY() + 30;

        boolean INSIDE_UP_DOWN = (o.getY() <= b.getY() + 28 && b.getY() + 28 <= o.getY() + 30
                || o.getY() <= b.getY() && b.getY() <= o.getY() + 30) && o.getX() <= b.getX() + 16
                && b.getX() + 16 <= o.getX() + 30;

        return INSIDE_LEFT_RIGHT || INSIDE_UP_DOWN;
    }

    /*
     * Metodo para checkear si o2 esta dentro de o1.
     */
    private boolean isInside(ObjetoGrafico o1, ObjetoGrafico o2) {
        boolean INSIDE_LEFT_RIGHT = (o1.getX() <= o2.getX() && o2.getX() <= o1.getX() + 30
                || o1.getX() <= o2.getX() + 28 && o2.getX() + 28 <= o1.getX() + 30) && o1.getY() <= o2.getY() + 16
                && o2.getY() + 16 <= o1.getY() + 30;

        boolean INSIDE_UP_DOWN = (o1.getY() <= o2.getY() + 28 && o2.getY() + 28 <= o1.getY() + 30
                || o1.getY() <= o2.getY() && o2.getY() <= o1.getY() + 30) && o1.getX() <= o2.getX() + 16
                && o2.getX() + 16 <= o1.getX() + 30;

        return INSIDE_LEFT_RIGHT || INSIDE_UP_DOWN;
    }

    /*
     * Metodo para cargar el ranking de jugadores.
     */
    private void getRaking() {
        DB sqlt = new DB();
        sqlt.initDBConn();
        ranking.getScores().removeAllElements();
        sqlt.selectAllInto(ranking);

        for (int i = 1; i < ranking.getScores().size(); i++) {
            if (ranking.getScores().get(i - 1).getScore() < ranking.getScores().get(i).getScore()) {
                Jugador aux = ranking.getScores().get(i - 1);

                ranking.getScores().set(i - 1, ranking.getScores().get(i));
                ranking.getScores().set(i, aux);
            }
        }
    }

    /*
     * Metodos para generar posiciones para las paredes de ladrillo, bonus y
     * enemigos.
     */
    private void positionBrickWalls() {
        for (Pared bw : brickWalls) {
            double x = LEFT_WALL_LIMIT + 32 * (2 * (r.nextInt(11)));
            double y = UPPER_WALL_LIMIT + 32 * (2 * (r.nextInt(5)));

            int row = (int) y / 32 - 2;
            int col = (int) x / 32;

            boolean NULL_WALL_POS1 = row == 1 && col == 1;
            boolean NULL_WALL_POS2 = row == 1 && col == 2;
            boolean NULL_WALL_POS3 = row == 2 && col == 1;

            while (mapPositions[row][col] == 1 || mapPositions[row][col] == 4 || NULL_WALL_POS1 || NULL_WALL_POS2
                    || NULL_WALL_POS3) {
                x = LEFT_WALL_LIMIT + 32 * (2 * (r.nextInt(11)));
                y = UPPER_WALL_LIMIT + 32 * (2 * (r.nextInt(5)));

                row = (int) (y / 32 - 2);
                col = (int) (x / 32);

                NULL_WALL_POS1 = row == 1 && col == 1;
                NULL_WALL_POS2 = row == 1 && col == 2;
                NULL_WALL_POS3 = row == 2 && col == 1;
            }

            bw.setPosition(x, y);
            mapPositions[row][col] = 4;
        }
    }

    private void positionBonus() {
        for (Bonus b : bonus) {
            Pared bw = brickWalls.get(r.nextInt(BRICK_WALLS));
            b.setPosition(bw.getX(), bw.getY());

            int row = (int) (b.getY() / 32 - 2);
            int col = (int) (b.getX() / 32);

            while (mapPositions[row][col] == 5) {
                bw = brickWalls.get(r.nextInt(2));
                b.setPosition(bw.getX(), bw.getY());

                row = (int) (b.getY() / 32 - 2);
                col = (int) (b.getX() / 32);
            }

            mapPositions[row][col] = 5;
        }

    }

    private void positionEnemies() {
        for (Enemigo e : enemies) {
            double x = LEFT_WALL_LIMIT + 32 * (2 * (r.nextInt(11)));
            double y = UPPER_WALL_LIMIT + 32 * (2 * (r.nextInt(5)));

            int row = (int) (y / 32 - 2);
            int col = (int) (x / 32);

            boolean NULL_WALL_POS = mapPositions[row][col] == 1;
            boolean NULL_HERO_POS = mapPositions[row][col] == 3;
            boolean NULL_BRICK_WALL_POS = mapPositions[row][col] == 4;

            while (NULL_WALL_POS || NULL_HERO_POS || NULL_BRICK_WALL_POS) {
                x = LEFT_WALL_LIMIT + 32 * (2 * (r.nextInt(11)));
                y = UPPER_WALL_LIMIT + 32 * (2 * (r.nextInt(5)));

                row = (int) (y / 32 - 2);
                col = (int) (x / 32);

                NULL_WALL_POS = mapPositions[row][col] == 1;
                NULL_HERO_POS = mapPositions[row][col] == 3;
                NULL_BRICK_WALL_POS = mapPositions[row][col] == 4;
            }

            e.setPosition(x, y);
            mapPositions[row][col] = 2;
        }
    }

    /*
     * Metodo para generar un nuevo nivel.
     */
    private void generateNextLevel() {
        if (SOUND) {
            fx = FXPlayer.STAGE_START;
            fx.play();
        }

        hero.setHasReachedDoor(false);
        cam.reset();

        /*
         * Se eliminan todos los bonus, bombas y explosiones que puedan haber quedado en
         * el mapa anterior.
         */
        bonus.removeAllElements();
        bombs.removeAllElements();
        explosions.removeAllElements();

        clock.setTime(200);
        mapPositions = new int[13][25];

        /*
         * Se setean todas las paredes de piedra donde es debido.
         */
        for (Pared p : walls) {
            setOnMap(p);
        }

        /*
         * Se crean los nuevos enemigos para el nivel.
         */
        enemies.addAll(factory.getEnemigos(Enemigo.ENEMIGO_ROSA, 6));

        /*
         * Se quitan los bonus del mapa anterior y se eligen nuevos (cada nivel habra 2
         * bonus adicionales).
         */
        for (int i = 0; i < BONUS_PER_STAGE; i++) {
            bonus.add(factory.getBonus(r.nextInt(6)));
        }
        bonus.add(factory.getBonus(Bonus.PUERTA));

        brickWalls.addAll(factory.getParedes(Pared.PARED_LADRILLO, BRICK_WALLS - brickWalls.size()));

        /*
         * Posicionando heroe.
         */
        hero.setPosition(LEFT_WALL_LIMIT + 2, UPPER_WALL_LIMIT + 2);
        mapPositions[1][1] = 3;

        /*
         * Posicionando a las paredes de ladrillo.
         */
        positionBrickWalls();

        /*
         * Posicionando bonus.
         */
        positionBonus();

        /*
         * Posicionando a los enemigos.
         */
        positionEnemies();
    }
}
