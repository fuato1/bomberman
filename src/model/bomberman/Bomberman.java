package model.bomberman;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.Vector;

import com.entropyinteractive.JGame;
import com.entropyinteractive.Keyboard;

import model.Bomba;
import model.Heroe;
import model.ObjetoGrafico;
import model.Puntaje;
import model.Ranking;
import model.Reloj;
import model.Vidas;
import model.bonus.Puerta;
import model.bonus.strategy.Bonus;
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
import model.pared.Pared;
import model.pared.ParedLadrillo;
import model.tests.ConsoleColors;

public class Bomberman extends JGame {
    /*
        objetos graficos del juego
    */
    private Heroe hero;
    private Vector<Enemigo> enemies;
    private Vector<Pared> walls, brickWalls;
    private Vector<Bonus> bonus;
    private Vector<Bomba> bombs;
    private Vector<Explosion> explosions;
    private Reloj clock;
    private Ranking ranking;

    /*
        jugadores (prueba). No esta en UML, ver si va en SistemJuegos
    */
    private Puntaje points;
    private Vidas life;
    private Jugador player;

    /*
        objetos graficos y clases secundarias
    */
    private Fondo bg;
    private Flecha arrow;
    private Mundo world;
    private Camara cam;
    private FXPlayer fx;

    /*
        representacion del mapa y variables de representacion
        de cada objeto grafico en el mapa
    */
    private int[][] mapPositions;
    private final int STONE_WALL = 1;
    private final int ENEMY = 2;
    private final int HERO = 3;
    private final int BRICK_WALL = 4;
    private final int BONUS = 5;
    private final int BOMB = 6;
    
    /*
        limites del escenario
    */
    public static final int UPPER_WALL_LIMIT = 105;
    public static final int LOWER_WALL_LIMIT = 457;
    public static final int LEFT_WALL_LIMIT = 32;
    public static final int RIGHT_WALL_LIMIT = 768;

    /*
        cantidad de paredes de los bordes e interiores
    */
    public static final int BORDER_WALLS = 73;
    public static final int INTERIOR_WALLS = 55;

    /*
        variable de estado para iniciar el juego
    */
    private boolean PLAY = false;
    private boolean IS_MAIN_SCREEN = true;
    private boolean END_GAME = false;

    /*
        variables de cambio y estado de nivel
    */
    private boolean IS_CHANGE_STAGE = false;
    private int CHANGE_STATE_COUNTER = 170;
    private int CURRENT_STAGE = 1;

    Random r = new Random(System.currentTimeMillis());

    public Bomberman() {
        super("Bomberman", Mundo.WORLD_WIDTH, Mundo.WORLD_HEIGHT);
    }

    public void showMap() {
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 25; j++) {
                if(mapPositions[i][j] == 3) {
                    System.out.print(ConsoleColors.RED + mapPositions[i][j] + ConsoleColors.RESET + " ");
                }
                else if(mapPositions[i][j] == 2) {
                    System.out.print(ConsoleColors.YELLOW + mapPositions[i][j] + ConsoleColors.RESET + " ");
                }
                else if(mapPositions[i][j] == 4) {
                    System.out.print(ConsoleColors.BLUE + mapPositions[i][j] + ConsoleColors.RESET + " ");
                }
                else if(mapPositions[i][j] == 5) {
                    System.out.print(ConsoleColors.GREEN + mapPositions[i][j] + ConsoleColors.RESET + " ");
                }
                else if(mapPositions[i][j] == 6) {
                    System.out.print(ConsoleColors.CYAN + mapPositions[i][j] + ConsoleColors.RESET + " ");
                }
                else {
                    System.out.print(mapPositions[i][j] + " ");
                }
            }
            System.out.println("\n");
        }
        System.out.println("\n\n");
    }

    @Override
    public void gameDraw(Graphics2D g) {
        /*
            si el se esta en la pantalla de inicio PLAY = false,
            cuando se presiona ENTER sobre "Jugar" PLAY = true
        */
        if(!PLAY) {
            CHANGE_STATE_COUNTER--;

            if(IS_MAIN_SCREEN) {
                mainScreen(g);
                
                if(CHANGE_STATE_COUNTER <= 85) {
                    IS_MAIN_SCREEN = false;
                }
            }
            else if(IS_CHANGE_STAGE) {
                changeStage(g);
            }
            else {
                rankingScreen(g);
                
                if(CHANGE_STATE_COUNTER == 0) {
                    CHANGE_STATE_COUNTER = 170;
                    IS_MAIN_SCREEN = true;
                }
            }
        }
        else {
            gameScreen(g);
        }
    }

    @Override
    public void gameUpdate(double delta) {
        Keyboard keyboard = this.getKeyboard();

        /*
            control de tecla para salir del juego
        */
        for(KeyEvent event: keyboard.getEvents()) {
            if ((event.getID() == KeyEvent.KEY_PRESSED) && (event.getKeyCode() == KeyEvent.VK_ESCAPE)) {
                stop();
            }
        }

        /*
            si el jugador todavia no presiono enter en "JUGAR"
        */
        if(!PLAY) {
            if(IS_MAIN_SCREEN) {
                mainScreenUpdate(delta);
            }
            else {
                for(KeyEvent event: keyboard.getEvents()) {
                    if ((event.getID() == KeyEvent.KEY_PRESSED) && (event.getKeyCode() == KeyEvent.VK_R)) {
                        PLAY = true;
                        gameStartup();
                    }
                }
            }
        }
        else {            
            gameScreenUpdate(delta);
        }
    }

    private void mainScreen(Graphics2D g) {
        /*
            dibujando fondo y movimiento de la flecha de seleccion en
            la pantalla de inicio
        */
        bg.setImage("/imagenes/start_screen.png");
        world.setWorldLimits(bg.getWidth(), bg.getHeight());
        bg.draw(g);
        arrow.draw(g);

        /*
            se inicia el track de pantalla principal
        */
        // fx = FXPlayer.TITLE_SCREEN;
        // fx.play();
    }

    private void rankingScreen(Graphics2D g) {
        bg.setImage("/imagenes/end_game.png");
        bg.draw(g);

        g.setColor(Color.black);
        g.drawString("Ranking: ", 280, 50);
        g.setColor(Color.white);
        g.drawString("Ranking: ", 282, 50);

        if(END_GAME) {
            g.setColor(Color.black);
            g.drawString("Press 'R' to play again", 100, 460);
            g.setColor(Color.white);
            g.drawString("Press 'R' to play again", 102, 460);
        }

        g.setColor(Color.black);
        g.drawString("Press 'Esc' to exit", 400, 460);
        g.setColor(Color.white);
        g.drawString("Press 'Esc' to exit", 402, 460);

        Jugador player;
        for (int i = 0; i < ranking.getScores().size(); i++) {
            player = ranking.getScores().get(i);

            g.setColor(Color.black);
            g.drawString(player.getNickName() + ": " + player.getScore(), 280, 140 + 32*(i));

            g.setColor(Color.white);
            g.drawString(player.getNickName() + ": " + player.getScore(), 282, 140 + 32*(i));
        }
    }

    private void changeStage(Graphics2D g) {
        bg.setImage("/imagenes/end_game.png");
        bg.draw(g);

        g.setColor(Color.black);
        g.drawString("Stage " + CURRENT_STAGE, 300, 240);
        g.setColor(Color.white);
        g.drawString("Stage " + CURRENT_STAGE, 302, 240);

        // fx = FXPlayer.STAGE_START;
        // fx.play();
        
        if(CHANGE_STATE_COUNTER == 0) {
            if(CURRENT_STAGE == 2) {
                IS_CHANGE_STAGE = false;
                // fx.stop();
            }
            else {
                PLAY = true;
                IS_CHANGE_STAGE = false;
                // fx.stop();

                gameStartup();
            }
        }
    }

    private void gameScreen(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.translate(cam.getX(), cam.getY());

        /*
            mostrando el mundo, dibujando el fondo, heroe y timer
        */
        bg.setImage("/imagenes/fondo.png");
        world.setWorldLimits(bg.getWidth(), bg.getHeight());

        bg.draw(g);
        world.display(g);
        clock.draw(g);
        points.draw(g);
        hero.draw(g);

        life.setLife(hero.getLife());
        life.draw(g);
        
        for (Bomba b : bombs) {
            if(b.getTime() > 0) {
                b.draw(g);
            }
        }

        for (Explosion e : explosions) {
            if(!e.isVanished()) {
                e.draw(g);

                for (Vector<ParteExplosion> vDir : e.getExplosion().values()) {
                    for (ParteExplosion pe : vDir) {
                        int row = (int) pe.getY()/32 - 2;
                        int col = (int) pe.getX()/32;

                        if(mapPositions[row][col] == 1 || mapPositions[row][col] == 4) {
                            break;
                        }

                        pe.draw(g);
                    }
                }
            }
        }

        /*
            se inicia el track de juego
        */
        // fx = FXPlayer.STAGE_THEME;
        // fx.play();

        /*
            dibujando paredes de piedra y ladrillo
        */
        for(Pared p : walls) {
            p.draw(g);
        }

        /*
            dibujando bonus
        */
        for(Bonus b : bonus) {
            b.draw(g);
        }

        for(Pared p : brickWalls) {
            p.draw(g);
        }

        /*
            dibujando enemigos
        */
        for(Enemigo e : enemies) {
            e.draw(g);
        }

        g.translate(-cam.getX(), -cam.getY());
    }

    private void mainScreenUpdate(double delta) {
        Keyboard keyboard = this.getKeyboard();

        /*
            si se presiona enter el juego inicia
        */
        if (keyboard.isKeyPressed(KeyEvent.VK_ENTER)) {
            /*
                se para el track de pantalla principal, se setea el fondo del mapa de juego y 
                nuevos limites del mapa, se hace una pausa para pasar a la pantalla de prejuego
                y se inicia el juego
            */
            // fx.stop();
            IS_MAIN_SCREEN = false;
            IS_CHANGE_STAGE = true;
        }
    }

    private void gameScreenUpdate(double delta) {
        Keyboard keyboard = this.getKeyboard();

        /*
            animaciones del reloj y contador de puntos del jugador
        */
        clock.countTime();
        points.countScore();
        
        /*
            controles de movimiento para el heroe y los enemigos

            si el heroe no esta muerto los enemigos seguiran moviendose y el heroe igual

            caso contrario si el heroe todavia tiene vidas se le quitara una y se hace la animacion de muerte

            si el heroe ya no tiene vidas se pasa a la pantalla de game over donde esta el ranking.
        */
        if(!hero.isDead()) {
            /*
                se itera sobre el mapa para setear las posiciones de el heroe y
                los enemigos en 0 y liberar los espacios por los que se van moviendo
                
                adicionalmente se coloca un 1 en las paredes de piedra y 4 en las paredes de
                ladrillo ya que por problemas de calcula muchas veces los objetos que se mueven
                setean en 0 posiciones que no deberian.

                otra razon es que haciendo el seteo en el mapa a 0 de las posiciones anteriores
                de los objetos movibles en los controles de movimiento de los mismos muchas veces deja
                valores 2 y 3 sobre el mapa invalidos que no son quitados
            */
            for (int i = 1; i < 13; i++) { // filas
                for (int j = 1; j < 25; j++) { // columnas
                    if(i%2 == 0) { // fila con paredes de piedra
                        if(j%2 != 0) { // si no hay una pared de piedra central
                            if(mapPositions[i][j] != 4 && mapPositions[i][j] != 5) { // si no hay paredes de piedra
                                if(mapPositions[i][j] == 3) { // si es la posicion del heroe
                                    mapPositions[i][j] = 0;
                                }
                                else if(mapPositions[i][j] == 2) { // si es la posicion de un enemigo
                                    mapPositions[i][j] = 0;
                                }
                                else if(mapPositions[i][j] == 6) {
                                    mapPositions[i][j] = 0;
                                }
                            }
                        }
                        else { // restaurando posibles seteos en 0 por los objetos movibles
                            mapPositions[i][j] = 1;
                        }
                    }
                    else { // fila sin paredes de piedra
                        if(mapPositions[i][j] != 4 && mapPositions[i][j] != 5) { // si no hay paredes de ladrillo
                            if(mapPositions[i][j] == 3) { // si es la posicion del heroe
                                mapPositions[i][j] = 0;
                            }
                            else if(mapPositions[i][j] == 2) { // si es la posicion de un enemigo
                                mapPositions[i][j] = 0;
                            }
                            else if(mapPositions[i][j] == 6) {
                                mapPositions[i][j] = 0;
                            }
                        }
                    }
                }
            }

            int row;
            int col;

            for (Bomba b : bombs) {
                /*
                    si alguna bomba va a explotar
                */
                if(b.getTime() == 0) {
                    /*
                        y hay una explosion menos que cantidad de bombas en el mapa
                    */
                    if(bombs.size()-1 == explosions.size()) {
                        /*
                            la bomba detona (instancia una Explosion y la setea en su posicion),
                            se agrega la explosion al mapa y se da permiso para agregar una nueva bomba
                        */
                        explosions.add(b.detonate());
                        Bomba.setActive(false);
                    }
                }
                else {
                    /*
                        se hace la animacion de la bomba
                    */
                    b.changeObject();

                    if(b.getTime() > 0) {
                        setOnMap(b);
                    }
                }
            }

            /*
                se setea en el mapa la posicion actual del heroe con un 3
                (habiendo borrado anteriormente la ultima posicion en la que
                se encontraba)
            */
            setOnMap(hero);

             /*
                movimiento de los enemigos
            */
            for (Enemigo e : enemies) {
                /*
                    si el enemigo no esta muerto (esto por ahora se usa por controles de testeo,
                    lo ideal es quitarlo del vector una vez que fue tocado por una explosion)
                */
                if(!e.isDead()) {
                    /*
                        cambio de sprites segun la direccion que toma el enemigo
                        (el enemigo toma direcciones de forma aleatoria)
                    */
                    e.changeObject(e.getCurrentDirection());

                    /*
                        se setea en el mapa la posicion actual del enemigo con un 2
                        (habiendo borrado anteriormente la ultima posicion en la que
                        se encontraba)
                    */
                    setOnMap(e);

                    if(e.getCurrentDirection() == "up") {
                        /*
                            file y columna actual (con modificaciones con respecto al mapa
                            porque sino genera superposiciones entre sprites)
                        */
                        row = (int) (e.getY()+20)/32 - 2;
                        col = (int) e.getX()/32;

                        /*
                            si la posicion de arriba no es una pared de piedra
                        */
                        if(
                            mapPositions[row-1][col] != 1 && 
                            mapPositions[row-1][col] != 4 && 
                            mapPositions[row-1][col] != 5 &&
                            mapPositions[row-1][col] != 6) {
                            e.up(delta);
                        }
                        /*
                            si la posicion de arriba es el heroe
                        */
                        if(mapPositions[row-1][col] == 3) {
                            hero.stop();
                        }
                    }
                    if(e.getCurrentDirection() == "down") {
                        /*
                            file y columna actual (con modificaciones con respecto al mapa
                            porque sino genera superposiciones entre sprites)
                        */
                        row = (int) (e.getY()-10) / 32 - 2;
                        col = (int) e.getX() / 32;

                        /*
                            si la posicion de arriba no es una pared de piedra
                        */
                        if(
                            mapPositions[row+1][col] != 1 && 
                            mapPositions[row+1][col] != 4 && 
                            mapPositions[row+1][col] != 5 &&
                            mapPositions[row+1][col] != 6) {
                            e.down(delta);
                        }
                        /*
                            si la posicion de arriba es el heroe
                        */
                        if(mapPositions[row+1][col] == 3) {
                            hero.stop();
                        }
                    }
                    if(e.getCurrentDirection() == "left") {
                        /*
                            file y columna actual (con modificaciones con respecto al mapa
                            porque sino genera superposiciones entre sprites)
                        */
                        row = (int) e.getY() / 32 - 2;
                        col = (int) (e.getX()+30) / 32;

                        /*
                            si la posicion de arriba no es una pared de piedra
                        */
                        if(
                            mapPositions[row][col-1] != 1 && 
                            mapPositions[row][col-1] != 4 && 
                            mapPositions[row][col-1] != 5 &&
                            mapPositions[row][col-1] != 6) {
                            e.left(delta);
                        }
                        /*
                            si la posicion de arriba es el heroe
                        */
                        if(mapPositions[row][col-1] == 3) {
                            hero.stop();
                        }
                    }
                    if(e.getCurrentDirection() == "right") {
                        /*
                            file y columna actual (con modificaciones con respecto al mapa
                            porque sino genera superposiciones entre sprites)
                        */
                        row = (int) (e.getY()+14) / 32 - 2;
                        col = (int) (e.getX()) / 32;

                        /*
                            si la posicion de arriba no es una pared de piedra
                        */
                        if(
                            mapPositions[row][col+1] != 1 && 
                            mapPositions[row][col+1] != 4 && 
                            mapPositions[row][col+1] != 5 &&
                            mapPositions[row][col+1] != 6) {
                            e.right(delta);
                        }
                        /*
                            si la posicion de arriba es el heroe
                        */
                        if(mapPositions[row][col+1] == 3) {
                            hero.stop();
                        }
                    }
                }
            }

            /*
                movimiento de heroe, los comentarios para los enemigos aplican
                para el heroe. Se cambian los sprites segun una direccion, se calcula la fila y columna actual
                (con alguna modificaciones por superposicion de sprites), se checkea si el heroe va a chocar
                contra un enemigo (en ese caso perdera una vida) y se checkea que no pase por las paredes de
                ladrillo
            */
            if(keyboard.isKeyPressed(KeyEvent.VK_UP)) {
                hero.changeObject("up");

                row = (int) ((hero.getY()+20)/32 - 2);
                col = (int) (hero.getX()/32);

                if(mapPositions[row-1][col] == 2) {
                    hero.stop();
                }
                else if(
                    mapPositions[row-1][col] != 1 && 
                    mapPositions[row-1][col] != 4 && 
                    mapPositions[row-1][col] != 5 &&
                    mapPositions[row-1][col] != 6) {
                    hero.up(delta);
                }
            }

            if(keyboard.isKeyPressed(KeyEvent.VK_DOWN)) {
                hero.changeObject("down");

                row = (int) ((hero.getY()-10)/32 - 2);
                col = (int) (hero.getX()/32);

                if(mapPositions[row+1][col] == 2) {
                    hero.stop();
                }
                else if(
                    mapPositions[row+1][col] != 1 &&
                    mapPositions[row+1][col] != 4 && 
                    mapPositions[row+1][col] != 5 &&
                    mapPositions[row+1][col] != 6) {
                    hero.down(delta);
                }
            }

            if(keyboard.isKeyPressed(KeyEvent.VK_LEFT)) {
                hero.changeObject("left");

                row = (int) (hero.getY()/32 - 2);
                col = (int) ((hero.getX()+30) / 32);

                if(mapPositions[row][col-1] == 2) {
                    hero.stop();
                }
                else if(
                    mapPositions[row][col-1] != 1 && 
                    mapPositions[row][col-1] != 4 && 
                    mapPositions[row][col-1] != 5 &&
                    mapPositions[row][col-1] != 6) {
                    hero.left(delta);
                }
            }

            if(keyboard.isKeyPressed(KeyEvent.VK_RIGHT)) {
                hero.changeObject("right");

                row = (int) ((hero.getY()+14)/32 - 2);
                col = (int) (hero.getX()/32);

                if(mapPositions[row][col+1] == 2) {
                    hero.stop();
                }
                else if(
                    mapPositions[row][col+1] != 1 && 
                    mapPositions[row][col+1] != 4 && 
                    mapPositions[row][col+1] != 5 &&
                    mapPositions[row][col+1] != 6) {
                    hero.right(delta);
                }
            }
    
            for (Explosion e : explosions) {
                /*
                    si alguna explosion todavia no se desvanecio se
                    sigue con su animacion
                */
                if(!e.isVanished()) {
                    e.changeObject();

                    row = (int) ((e.getY()+16)/32 - 2);
                    col = (int) ((e.getX()+16)/32);
    
                    if(mapPositions[row][col] == 3) {
                        hero.stop();
                    }
                    if(mapPositions[row][col] == 2) {
                        for (int i = 0; i < enemies.size(); i++) {
                            if(isInside(e, enemies.get(i))) {
                                enemies.get(i).kill();
                                enemies.remove(i);
                                i--;
                            }   
                        }
                    }
    
                    for (Vector<ParteExplosion> vDir : e.getExplosion().values()) {
                        for (ParteExplosion pe : vDir) {
                            row = (int) ((pe.getY()+16)/32 - 2);
                            col = (int) ((pe.getX()+16)/32);
                            
                            /*
                                si el heroe esta dentro de la explosion
                            */
                            if(isInside(pe, hero)) {
                                hero.stop();
                            }
                            if(mapPositions[row][col] == 2) {          
                                for (int i = 0; i < enemies.size(); i++) {
                                    if(isInside(pe, enemies.get(i))) {
                                        enemies.get(i).kill();
                                        enemies.remove(i);
                                        i--;
                                    }   
                                }
                            }
                            if(mapPositions[row][col] == 1 || mapPositions[row][col] == 4 || mapPositions[row][col] == 5) {
                                if(mapPositions[row][col] == 4 || mapPositions[row][col] == 5) {
                                    for (Pared bw : brickWalls) {
                                        if(isInside(pe, bw)) {
                                            bw.hit();
                                            mapPositions[row][col] = 0;
                                            break;                                            
                                        }
                                    }
                                }
                                
                                break;
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < brickWalls.size(); i++) {
                if(brickWalls.get(i).wasDestroyed()) {
                    brickWalls.remove(brickWalls.get(i));
                }
                else if(!brickWalls.get(i).wasDestroyed() && brickWalls.get(i).wasHit()) {
                    brickWalls.get(i).changeObject();
                }
            }

            for (Bonus b : bonus) {
                row = (int) ((b.getY()+16)/32 - 2);
                col = (int) ((b.getX()+16)/32);
                mapPositions[row][col] = BONUS;

                if(b.getClass() == Puerta.class) {
                    if(
                        mapPositions[row-1][col] == 3 ||
                        mapPositions[row+1][col] == 3 ||
                        mapPositions[row][col-1] == 3 ||
                        mapPositions[row][col+1] == 3
                    ) {
                        PLAY = false;
                        CURRENT_STAGE++;
                        CHANGE_STATE_COUNTER = 170;
                        IS_CHANGE_STAGE = true;
                        END_GAME = true;
                    }
                }
            }

            /*
                se checkea que na haya mas bombas de las que el heroe puede usar (sin bonus)
            */
            if(bombs.size() <= 2 && !Bomba.isActive()) {
                /*
                    se checkea que haya bombas y explosiones para ir quitando del mapa
                    y que no haya ninguna explosion activa (solo se puede explotar una bomba y
                    luego colocar otra cuando la anterior se desvanecio)
                */
                if(!bombs.isEmpty()) {
                    bombs.remove(0);
                }
                if(!explosions.isEmpty()) {
                    explosions.remove(0);
                }
            }

            /*
                tecla para mostrar el mapa por consola (solo para test)
            */
            for(KeyEvent e: keyboard.getEvents()) {
                if(e.getID() == KeyEvent.KEY_PRESSED) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        showMap();
                    }
                    if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                        if(mapPositions[(int) hero.getY()/32 - 2][(int) hero.getX()/32] != 1) {
                            /*
                                el heroe instancia y setea la bomba en su posicion actual
                                para colocarla
                            */
                            Bomba b = hero.setBomb();
    
                            /*
                                si el objeto Bomba no es nulo
                            */
                            if(b != null) {
                                /*
                                    y no hay una bomba activa
                                */
                                if(!Bomba.isActive()) {
                                    /*
                                        se da permiso a una nueva bomba para ser
                                        colocada y se la agrega al mapa
                                    */
                                    Bomba.setActive(true);
                                    bombs.add(b);
                                }
                            }
                        }
                    }
                }
            }
    
            cam.followUpperPanel(clock, points, life);
            cam.followHero(hero);
        }
        else {
            if(hero.getLife() > 0) {
                hero.kill();
                life.setLife(hero.getLife());
            }
            else {
                PLAY = false;
                IS_MAIN_SCREEN = false;
                END_GAME = true;

                /*
                    ver puntaje del juagador y si entra en el ranking
                */
                player.setScore(points.getScore());

                DB sqlt = new DB();
                sqlt.initDBConn();

                sqlt.insertScore(player.getNickName(), player.getScore());
                getRaking();
            }
        }
    }

    private void setOnMap(ObjetoGrafico o) {
        int row = (int) ((o.getY()+16)/32 - 2);
        int col = (int) ((o.getX()+16)/32);

        if(o.getClass() == Pared.class) {
            mapPositions[row][col] = STONE_WALL;
        }
        else if(o.getClass() == Heroe.class) {
            mapPositions[row][col] = HERO;
        }
        else if(o.getClass() == EnemigoRosa.class || o.getClass() == EnemigoAzul.class) {
            mapPositions[row][col] = ENEMY;
        }
        else if(o.getClass() == ParedLadrillo.class) {
            mapPositions[row][col] = BRICK_WALL;
        }
        else if(o.getClass() == Bomba.class) {
            mapPositions[row][col] = BOMB;
        }
    }

    private boolean isInside(ObjetoGrafico o1, ObjetoGrafico o2) {
        /*
            variables de checkeo para ver si el heroe esta dentro de
            la explosion
        */
        boolean INSIDE_LEFT_RIGHT = 
            (o1.getX() <= o2.getX() && o2.getX() <= o1.getX()+32 ||
            o1.getX() <= o2.getX()+32 && o2.getX()+32 <= o1.getX()+32) &&
            o1.getY() <= o2.getY()+16 && o2.getY()+16 <= o1.getY()+32;

        boolean INSIDE_UP_DOWN =
            (o1.getY() <= o2.getY()+28 && o2.getY()+28 <= o1.getY()+32 ||
            o1.getY() <= o2.getY() && o2.getY() <= o1.getY()+32) &&
            o1.getX() <= o2.getX()+16 && o2.getX()+16 <= o1.getX()+32;

        return INSIDE_LEFT_RIGHT || INSIDE_UP_DOWN;
    }

    private void getRaking() {
        DB sqlt = new DB();
        sqlt.initDBConn();
        ranking.getScores().removeAllElements();
        sqlt.selectAllInto(ranking);

        for (int i = 1; i < ranking.getScores().size(); i++) {
            if(ranking.getScores().get(i-1).getScore() < ranking.getScores().get(i).getScore()) {
                Jugador aux = ranking.getScores().get(i-1);

                ranking.getScores().set(i-1, ranking.getScores().get(i));
                ranking.getScores().set(i, aux);
            }
        }
    }

    @Override
    public void gameStartup() {
        if(!PLAY) {
            /*
                se inicia la camara y su region visible, el fondo de comienzo como pantalla de inicio
                la flecha que se utiliza para navegar las opciones de "Jugar" y "Continuar", y el se toma la instancia
                del mundo
            */
            cam = new Camara(0, 0);
            cam.setVisibleRegion(640);
            bg = new Fondo("/imagenes/start_screen.png");
            world = Mundo.getInstance();

            arrow = new Flecha("/imagenes/arrow.png");
            arrow.setPosition(240, 365);

            ranking = new Ranking();
            getRaking();
        }
        else {
            /*
                se instancian los objetos para iniciar el juego
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
            player = new Jugador();

            /*
                instanciando a los enemigos, paredes de piedra, paredes de ladrillo
                y bonus
            */
            OGAbstractFactory factory = OGFactoryProducer.getFactory();
            walls.addAll(factory.getParedes(Pared.PARED_PIEDRA, BORDER_WALLS + INTERIOR_WALLS));
            brickWalls.addAll(factory.getParedes(Pared.PARED_LADRILLO, 30));

            enemies.addAll(factory.getEnemigos(Enemigo.ENEMIGO_ROSA, 1));

            /*
                eligiendo dos bonus de forma aleatoria
            */
            bonus.add(factory.getBonus(r.nextInt(6)));
            bonus.add(factory.getBonus(r.nextInt(6)));
            bonus.add(factory.getBonus(Bonus.PUERTA));

            /*
                posicionando heroe
            */
            hero.setPosition(LEFT_WALL_LIMIT+2, UPPER_WALL_LIMIT+2);
            mapPositions[1][1] = 3;

            /*
                posicionando borde de paredes de piedra (se usan 73 para los bordes)
            */
            for (int i = 0; i < walls.size(); i++) {
                // paredes horizontales
                if(i < 25) {
                    walls.get(i).setPosition(LEFT_WALL_LIMIT*i, UPPER_WALL_LIMIT-32); // arriba
                    mapPositions[0][i] = 1;

                    walls.get(i+25).setPosition(32*i, LOWER_WALL_LIMIT); // abajo
                    mapPositions[12][i] = 1;
                }

                // paredes verticales
                if(i >= 25 && i <= 36) {
                    walls.get(i+25).setPosition(0, UPPER_WALL_LIMIT + 32*(i-25)); // izquierda
                    mapPositions[i-25][0] = 1;

                    walls.get(i+37).setPosition(RIGHT_WALL_LIMIT, UPPER_WALL_LIMIT + 32*(i-25)); // derecha
                    mapPositions[i-25][24] = 1;
                }
            }

            /*
                posicionando paredes de piedra centrales (se usan 5 filas x 11 columnas)
            */
            int aux = BORDER_WALLS;

            for (int j = 1; j < 12; j++) { // columnas
                for (int i = 0; i < 5; i++) { // filas
                    walls.get(aux).setPosition(64*j, UPPER_WALL_LIMIT + 32*(2*i + 1));
                    aux++;

                    mapPositions[2*(i+1)][2*j] = 1;
                }
            }

            /*
                posicionando a las paredes de ladrillo
            */
            for (Pared bw : brickWalls) {
                double x = LEFT_WALL_LIMIT + 32*(2 * (r.nextInt(11)));
                double y = UPPER_WALL_LIMIT + 32*(2 * (r.nextInt(5)));

                int row = (int) y/32 - 2;
                int col = (int) x/32;

                boolean NULL_WALL_POS1 = row == 1 && col == 1;
                boolean NULL_WALL_POS2 = row == 1 && col == 2;
                boolean NULL_WALL_POS3 = row == 2 && col == 1;

                while(mapPositions[row][col] == 1 || NULL_WALL_POS1 || NULL_WALL_POS2 || NULL_WALL_POS3) {
                    x = LEFT_WALL_LIMIT + 32*(2 * (r.nextInt(11)));
                    y = UPPER_WALL_LIMIT + 32*(2 * (r.nextInt(5)));

                    row = (int) (y/32 - 2);
                    col = (int) (x/32);

                    NULL_WALL_POS1 = row == 1 && col == 1;
                    NULL_WALL_POS2 = row == 1 && col == 2;
                    NULL_WALL_POS3 = row == 2 && col == 1;
                }

                bw.setPosition(x, y);
                mapPositions[row][col] = 4;
            }

            /*
                posicionando bonus
            */
            for (Bonus b : bonus) {
                Pared bw = brickWalls.get(r.nextInt(30));
                b.setPosition(bw.getX(), bw.getY());

                int row = (int) (b.getY()/32 - 2);
                int col = (int) (b.getX()/32);

                while(mapPositions[row][col] == 5) {
                    bw = brickWalls.get(r.nextInt(2));
                    b.setPosition(bw.getX(), bw.getY());

                    row = (int) (b.getY()/32 - 2);
                    col = (int) (b.getX()/32);
                }

                mapPositions[row][col] = 5;
            }

            /*
                posicionando a los enemigos
            */
            for (Enemigo e : enemies) {
                double x = LEFT_WALL_LIMIT + 32*(2 * (r.nextInt(11)));
                double y = UPPER_WALL_LIMIT + 32*(2 * (r.nextInt(5)));

                int row = (int) (y/32 - 2);
                int col = (int) (x/32);

                boolean NULL_WALL_POS = mapPositions[row][col] == 1;
                boolean NULL_HERO_POS = mapPositions[row][col] == 3;
                boolean NULL_BRICK_WALL_POS = mapPositions[row][col] == 4;

                while(NULL_WALL_POS || NULL_HERO_POS || NULL_BRICK_WALL_POS) {
                    x = LEFT_WALL_LIMIT + 32*(2 * (r.nextInt(11)));
                    y = UPPER_WALL_LIMIT + 32*(2 * (r.nextInt(5)));
    
                    row = (int) (y/32 - 2);
                    col = (int) (x/32);

                    NULL_WALL_POS = mapPositions[row][col] == 1;
                    NULL_HERO_POS = mapPositions[row][col] == 3;
                    NULL_BRICK_WALL_POS = mapPositions[row][col] == 4;
                }

                e.setPosition(x, y);
                mapPositions[row][col] = 2;
            }
        }
    }

    @Override
    public void gameShutdown() {
       
    }
}