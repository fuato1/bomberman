package model.bomberman;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

import javax.lang.model.element.Element;

import com.entropyinteractive.JGame;
import com.entropyinteractive.Keyboard;

import model.Bomba;
import model.Heroe;
import model.Reloj;
import model.bonus.strategy.Bonus;
import model.db.DB;
import model.enemigo.Enemigo;
import model.explosion.Explosion;
import model.explosion.ParteExplosion;
import model.factory.OGAbstractFactory;
import model.factory.OGFactoryProducer;
import model.fx_player.FXPlayer;
import model.jugador.Jugador;
import model.pared.Pared;
import model.pared.ParedLadrillo;

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

    /*
        jugadores (prueba)
    */
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

    public Bomberman() {
        super("Bomberman", Mundo.WORLD_WIDTH, Mundo.WORLD_HEIGHT);

        /*
            se inicia la camara y su region visible, el fondo de comienzo como pantalla de inicio
            la flecha que se utiliza para navegar las opciones de "Jugar" y "Continuar", y el se toma la instancia
            del mundo
        */
        cam = new Camara(0, 0);
        cam.setVisibleRegion(640);
        bg = new Fondo("/imagenes/start_screen.png");
        arrow = new Flecha("/imagenes/arrow.png");
        world = Mundo.getInstance();

        player = new Jugador("Player");

        /*
            se instancian los objetos principales del juego
        */
        hero = new Heroe();
        walls = new Vector<Pared>();
        brickWalls = new Vector<Pared>();
        enemies = new Vector<Enemigo>();
        bonus = new Vector<Bonus>();
        bombs = new Vector<Bomba>(0);
        explosions = new Vector<Explosion>(0);
        clock = new Reloj();

        OGAbstractFactory f = OGFactoryProducer.getFactory();
        walls.addAll(f.getParedes(Pared.PARED_PIEDRA, BORDER_WALLS + INTERIOR_WALLS));
        brickWalls.addAll(f.getParedes(Pared.PARED_LADRILLO, 5));

        enemies.addAll(f.getEnemigos(Enemigo.ENEMIGO_ROSA, 2));
        enemies.addAll(f.getEnemigos(Enemigo.ENEMIGO_AZUL, 2));

        bonus.add(f.getBonus(Bonus.VIDA_EXTRA));
        bonus.add(f.getBonus(Bonus.BOMBA_EXTRA));
        bonus.add(f.getBonus(Bonus.VELOCIDAD));
        bonus.add(f.getBonus(Bonus.DETONADOR));
        bonus.add(f.getBonus(Bonus.RANGO_EXPLOSION));
        bonus.add(f.getBonus(Bonus.SALTO_BOMBA));
        bonus.add(f.getBonus(Bonus.PUERTA));
    }

    public static void pause(int n) {
        try {
            Thread.sleep(n * 1000);
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void gameDraw(Graphics2D g) {
        /*
            si el se esta en la pantalla de inicio PLAY = false,
            cuando se presiona ENTER sobre "Jugar" PLAY = true
        */
        if(!PLAY) {
            /*
                dibujando fondo y movimiento de la flecha de seleccion en
                la pantalla de inicio
            */
            bg.draw(g);
            arrow.draw(g);

            /*
                se inicia el track de pantalla principal
            */
            // fx = FXPlayer.TITLE_SCREEN;
            // fx.play();
        }
        else {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.translate(cam.getX(), cam.getY());

            /*
                mostrando el mundo, dibujando el fondo, heroe y timer
            */
            bg.draw(g);
            world.display(g);
            clock.draw(g);
            player.getScore().draw(g);
            hero.draw(g);
            
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
            for (Pared p : walls) {
                p.draw(g);
            }

            for (Pared p : brickWalls) {
                p.draw(g);
            }

            /*
                dibujando enemigos
            */
            for (Enemigo e : enemies) {
                e.draw(g);
            }

            // /*
            //     dibujando bonus
            // */
            // for (Bonus b : bonus) {
            //     b.draw(g);
            // }

            g.translate(-cam.getX(), -cam.getY());
        }
    }
    
    @Override
    public void gameUpdate(double delta) {
        Keyboard keyboard = this.getKeyboard();

        /*
            si el jugador todavia no presiono enter en "JUGAR"
        */
        if(!PLAY) {
            /*
                si se presiona la tecla izquierda o derecha se posiciona
                a la flecha
            */
            if (keyboard.isKeyPressed(KeyEvent.VK_LEFT)) {
                arrow.setPosition(150, 300);
            }
            if (keyboard.isKeyPressed(KeyEvent.VK_RIGHT)) {
                arrow.setPosition(311, 300);
            }

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
                bg.setImage("/imagenes/fondo.png");
                world.setWorldLimits(bg.getWidth(), bg.getHeight());
                PLAY = true;
                // pause(3);
            }
        }
        else {
            // for (Pared bw : brickWalls) {
            //     bw.changeObject();
            // }

            // for (Enemigo e : enemies) {
            //     e.changeObject("up");
            // }
                
            // for (Bonus b : bonus) {
            //     b.changeObject();
            // }

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
                        b.setTime(3);
                        Bomba.setActive(false);
                    }
                }
                else {
                    /*
                        si hace la animacion de la bomba
                    */
                    b.changeSprites();
                }
            }

            for (int i = 0; i < explosions.size(); i++) {
                /*
                    si alguna explosion todavia no se desvanecio se
                    sigue con su animacion
                */
                if(!explosions.get(i).isVanished()) {
                    explosions.get(i).changeSprites();
                }
            }

            /*
                animaciones del reloj y contador de puntos del jugador
            */
            clock.countTime();
            player.getScore().countScore();
            
            /*
                controles de direccion para el heroe
            */
            if(keyboard.isKeyPressed(KeyEvent.VK_UP)) {
                hero.changeObject("up");
                hero.up(delta);
            }

            if(keyboard.isKeyPressed(KeyEvent.VK_DOWN)) {
                hero.changeObject("down");
                hero.down(delta);
            }

            if(keyboard.isKeyPressed(KeyEvent.VK_LEFT)) {
                hero.changeObject("left");
                hero.left(delta);
            }

            if(keyboard.isKeyPressed(KeyEvent.VK_RIGHT)) {
                hero.changeObject("right");
                hero.right(delta);
            }

            /*
                se checkea que na haya mas bombas de las que el heroe puede usar (sin bonus)
            */
            if(bombs.size() == 2 && !Bomba.isActive()) {
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

            for (KeyEvent ke : keyboard.getEvents()) {
                if((ke.getID() == KeyEvent.KEY_PRESSED) && (ke.getKeyCode() == KeyEvent.VK_SPACE)) {
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

            cam.followUpperPanel(clock, player.getScore());
            cam.followHero(hero);
        }

        /*
            control de tecla para salir del juego
        */
        LinkedList <KeyEvent> keyEvents = keyboard.getEvents();
        for (KeyEvent event: keyEvents) {
            if ((event.getID() == KeyEvent.KEY_PRESSED) && (event.getKeyCode() == KeyEvent.VK_ESCAPE)) {
                stop();
            }
        }
    }

    @Override
    public void gameStartup() {
        /*
            posicionando flecha de seleccion
        */
        arrow.setPosition(150, 300);

        /*
            posicionando heroe
        */
        hero.setPosition(LEFT_WALL_LIMIT+1, UPPER_WALL_LIMIT+1);

        /*
            posicionando borde de paredes de piedra (se usan 73 para los bordes)
        */
        for (int i = 0; i < walls.size(); i++) {
            // paredes de arriba
            if(i >= 0 && i < 25)
                walls.get(i).setPosition(LEFT_WALL_LIMIT*i, UPPER_WALL_LIMIT-32);

            // paredes izquierda
            if(i >= 25 && i < 36)
                walls.get(i).setPosition(0, UPPER_WALL_LIMIT + (i-25)*32);

            // paredes derecha
            if(i >= 36 && i < 48)
                walls.get(i).setPosition(RIGHT_WALL_LIMIT, UPPER_WALL_LIMIT + 32*(i-36));

            // paredes abajo
            if(i >= 48)
                walls.get(i).setPosition(32*(i-48), LOWER_WALL_LIMIT);
        }

        /*
            posicionando paredes de piedra centrales (se usan 5 filas x 11 columnas)
        */
        int aux = BORDER_WALLS;

        for (int j = 1; j < 12; j++) { // columnas
            for (int i = 0; i < 5; i++) { // filas
                walls.get(aux).setPosition(64*j, UPPER_WALL_LIMIT + 32*(2*i + 1));
                aux++;
            }
        }

        /*
            posicionando a las paredes de ladrillo
        */
        for (Pared bw : brickWalls) {
            Random r = new Random();
            
            double x = LEFT_WALL_LIMIT + 32*( 2 * (r.nextInt(11)));
            double y = UPPER_WALL_LIMIT + 32*(2 * (r.nextInt(5)));
            bw.setPosition(x, y);
        }

        /*
            posicionando a los enemigos
        */
        for (Enemigo e : enemies) {
            Random r = new Random();
            
            double x = LEFT_WALL_LIMIT + 32*( 2 * (r.nextInt(11)));
            double y = UPPER_WALL_LIMIT + 32*(2 * (r.nextInt(5)));
            e.setPosition(x, y);
        }
    }

    @Override
    public void gameShutdown() {
        /*
            se conecta a la BD para almacenar el puntaje del juagdor
        */
        DB sqlt = new DB();
        sqlt.initDBConn();
        // sqlt.deleteScores();
        sqlt.insertScore(player.getNickName(), player.getScore().getScore());

        System.out.println("***** Mostrar Todos los Puntajes *****");
        sqlt.selectAll();
    }
}