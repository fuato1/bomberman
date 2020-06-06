package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.LinkedList;
import java.util.Vector;

import com.entropyinteractive.JGame;
import com.entropyinteractive.Keyboard;
import com.entropyinteractive.Log;

import model.bonus.BombaExtra;
import model.bonus.strategy.Bonus;
import model.enemigo.Enemigo;
import model.enemigo.EnemigoAzul;
import model.explosion.Explosion;
import model.explosion.ParteExplosion;
import model.factory.OGAbstractFactory;
import model.factory.OGFactoryProducer;
import model.pared.Pared;

public class Bomberman extends JGame {
    /*
        heroe, enemigos, paredes, bonus, reloj
    */
    private Heroe hero;
    private Vector<Enemigo> enemies;
    private Vector<Pared> walls, brickWalls;
    private Vector<Bonus> bonus;
    private Vector<Bomba> bombs;
    // private Reloj clock;
    private Explosion exp;

    /*
        fondo, mundo y camara
    */
    private Fondo bg;
    private Mundo world;
    private Camara cam;

    /*
        timer
    */
    private Date dInit = new Date();
    private Date dNow;
    
    /*
        limites del escenario y variables varias 
    */
    public static final int UPPER_WALL_LIMIT = 105;
    public static final int LOWER_WALL_LIMIT = 457;
    public static final int LEFT_WALL_LIMIT = 32;
    public static final int RIGHT_WALL_LIMIT = 768;
    public static final int BORDER_WALLS = 73;
    public static final int MID_WALLS = 55;
    
    /*
        variables contadores para los OG
    */
    public static double ANIMATION_HERO = 0;
    public static double ANIMATION_BW = 0;
    public static double ANIMATION_BOMB = 0;
    public static double ANIMATION_ENEMY = 0;
    public static double ANIMATION_EXPLOSION = 0;

    public Bomberman() {
        super("Bomberman", Mundo.WORLD_WIDTH, Mundo.WORLD_HEIGHT);

        cam = new Camara(0, 0);
        cam.setVisibleRegion(640);
        bg = new Fondo("/imagenes/fondo.png");
        world = Mundo.getInstance();
        world.setWorldLimits(bg.getWidth(), bg.getHeight());

        // inicializando al heroe, paredes y enemigos
        hero = new Heroe();
        walls = new Vector<Pared>();
        brickWalls = new Vector<Pared>();
        enemies = new Vector<Enemigo>();
        bonus = new Vector<Bonus>();
        bombs = new Vector<Bomba>();

        OGAbstractFactory f = OGFactoryProducer.getFactory(OGAbstractFactory.PARED_FACTORY);
        walls.addAll(f.getParedes(Pared.PARED_PIEDRA, BORDER_WALLS + MID_WALLS));
        brickWalls.addAll(f.getParedes(Pared.PARED_LADRILLO, 3));

        enemies.addAll(f.getEnemigos(Enemigo.ENEMIGO_ROSA, 2));
        enemies.addAll(f.getEnemigos(Enemigo.ENEMIGO_AZUL, 2));

        exp = f.getExplosion();

        bonus.add(f.getBonus(Bonus.VIDA_EXTRA));
        bonus.add(f.getBonus(Bonus.BOMBA_EXTRA));
        bonus.add(f.getBonus(Bonus.VELOCIDAD));
        bonus.add(f.getBonus(Bonus.DETONADOR));
        bonus.add(f.getBonus(Bonus.RANGO_EXPLOSION));
        bonus.add(f.getBonus(Bonus.SALTO_BOMBA));
        bonus.add(f.getBonus(Bonus.PUERTA));

        bombs.add(f.getBomba());
    }

    @Override
    public void gameDraw(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.translate(cam.getX(),cam.getY());

        /*
            timer
        */
        dNow = new Date();
    	long dateDiff = dNow.getTime() - dInit.getTime();
    	long diffSeconds = dateDiff / 1000 % 60;
        long diffMinutes = dateDiff / (60 * 1000) % 60;

        /*
            mostrando el mundo, dibujando el fondo, heroe y timer
        */
        bg.draw(g);
        world.display(g);
        hero.draw(g);

        g.setColor(Color.black);
        g.drawString("Tiempo de Juego: " + diffMinutes + ":" + diffSeconds, 12, 42);

    	g.setColor(Color.white);
    	g.drawString("Tiempo de Juego: " + diffMinutes + ":" + diffSeconds, 10, 40);

        /*
            dibujando paredes
        */
        for (Pared p : walls) {
            p.draw(g);
        }

        for (Pared p : brickWalls) {
            p.draw(g);
        }

        /*
            dibujando explosion
        */
        exp.draw(g);
        for (String dir : exp.getExplosion().keySet()) {
            for (ParteExplosion pe : exp.getExplosion().get(dir)) {
                pe.draw(g);
            }
        }

        /*
            dibujando enemigos
        */
        for (Enemigo e : enemies) {
            e.draw(g);
        }

        /*
            dibujando bonus
        */
        for (Bonus b : bonus) {
            b.draw(g);
        }

        /*
            dibujando bombas
        */
        for (Bomba b : bombs) {
            b.draw(g);
        }

        g.translate(-cam.getX(), -cam.getY());
    }

    @Override
    public void gameStartup() {
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

        // /*
        //     posicionando paredes de ladrillo
        // */
        // for (int i = 0; i < brickWalls.size(); i++) {
        //     brickWalls.get(i).setPosition(32 + 32*i, LOWER_WALL_LIMIT-32);
        // }

        // /*
        //     posicionando explosiones a traves de la central
        // */
        // exp.setPosition(768/2-32, LOWER_WALL_LIMIT/2+6);

        // for (String dir : exp.getExplosion().keySet()) {
        //     if(dir == "up") {
        //         for (int i = 0; i < exp.getExplosion().get(dir).size(); i++) {
        //             exp.getExplosion().get(dir).get(i).setPosition(exp.getX(), exp.getY() - 28*(i+1));
        //         }
        //     }
        //     else if(dir == "down") {
        //         for (int i = 0; i < exp.getExplosion().get(dir).size(); i++) {
        //             exp.getExplosion().get(dir).get(i).setPosition(exp.getX(), exp.getY() + 28*(i+1));
        //         }
        //     }
        //     else if(dir == "left") {
        //         for (int i = 0; i < exp.getExplosion().get(dir).size(); i++) {
        //             exp.getExplosion().get(dir).get(i).setPosition(exp.getX() - 28*(i+1), exp.getY());
        //         }
        //     }
        //     else {
        //         for (int i = 0; i < exp.getExplosion().get(dir).size(); i++) {
        //             exp.getExplosion().get(dir).get(i).setPosition(exp.getX() + 28*(i+1), exp.getY());
        //         }
        //     }
        // }

        // /*
        //     posicionando enemigos
        // */
        // for (int i = 0; i < enemies.size(); i++) {
        //     enemies.get(i).setPosition(32 + 32*i, 233);
        // }

        // /*
        //     posicionando bonus
        // */
        // for (int i = 0; i < bonus.size(); i++) {
        //     bonus.get(i).setPosition(64 + 32*i, 105);
        // }

        // /*
        //     posicionando bombas
        // */
        // for (int i = 0; i < bombs.size(); i++) {
        //     bombs.get(i).setPosition(32, 137);
        // }
    }

    public void changeBrickWallSprites(Pared w) {
        if(ANIMATION_BW > 70)
            ANIMATION_BW = 0;
        else
            ANIMATION_BW++;

        if(ANIMATION_BW < 10)
            w.update("/imagenes/paredes/pared_ladrillo.png");
        else {
            for (int i = 20; i <= 60; i += 10) {
                if(ANIMATION_BW >= i)
                    w.update("/imagenes/paredes/pared_ladrillo_R" + ((i/10)-1) + ".png");
            }
        }
    }

    public void changeBombSprites(Bomba b) {
        if(ANIMATION_BOMB > 40)
            ANIMATION_BOMB = 0;
        else
            ANIMATION_BOMB++;

        for (int i = 10; i <= 30; i += 10) {
            if(i-10 <= ANIMATION_BOMB && ANIMATION_BOMB < i)
                b.update("/imagenes/bombas/bomb-" + i/10 + ".png");
        }
    }

    public void changeEnemySprites(Enemigo e) {
        if(ANIMATION_ENEMY > 60)
            ANIMATION_ENEMY = 0;
        else
            ANIMATION_ENEMY++;
        
        if(e.getClass() == EnemigoAzul.class) {
            if(ANIMATION_ENEMY < 30)
                e.update("/imagenes/enemigos/azul/enemigo_azul-1.png");
            if(ANIMATION_ENEMY >= 30)
                e.update("/imagenes/enemigos/azul/enemigo_azul_M1.png");
        }
        else {
            for (int i = 10; i <= 50; i += 10) {
                if(i-10 <= ANIMATION_ENEMY && ANIMATION_ENEMY < i)
                    e.update("/imagenes/enemigos/rosa/eliminado/enemigo_rosa_M" + i/10 + ".png");
            }
        }
    }

    private void changeExplosionPartSprites(Explosion exp, int imgNumber) {
        for (String dir : exp.getExplosion().keySet()) {
            for (ParteExplosion pe : exp.getExplosion().get(dir)) {
                if(dir == "up")
                    if(exp.getExplosion().get(dir).indexOf(pe, 0) == exp.getExplosion().get(dir).size())
                        pe.update("/imagenes/explosiones/up/up_exp-" + imgNumber + ".png");
                    else
                        pe.update("/imagenes/explosiones/vertical/ver_exp-" + imgNumber + ".png");
                if(dir == "down")
                    if(exp.getExplosion().get(dir).indexOf(pe, 0) == exp.getExplosion().get(dir).size())
                        pe.update("/imagenes/explosiones/down/down_exp-" + imgNumber + ".png");
                    else
                        pe.update("/imagenes/explosiones/vertical/ver_exp-" + imgNumber + ".png");
                if(dir == "left")
                    if(exp.getExplosion().get(dir).indexOf(pe, 0) == exp.getExplosion().get(dir).size())
                        pe.update("/imagenes/explosiones/left/left_exp-" + imgNumber + ".png");
                    else
                        pe.update("/imagenes/explosiones/horizontal/hor_exp-" + imgNumber + ".png");
                if(dir == "right")
                    if(exp.getExplosion().get(dir).indexOf(pe, 0) == exp.getExplosion().get(dir).size())
                        pe.update("/imagenes/explosiones/right/right_exp-" + imgNumber + ".png");
                    else
                        pe.update("/imagenes/explosiones/horizontal/hor_exp-" + imgNumber + ".png");
            }
        }
    }

    public void changeExplosionSprites(Explosion e) {
        if(ANIMATION_EXPLOSION > 50)
            ANIMATION_EXPLOSION = 0;
        else
            ANIMATION_EXPLOSION++;

        if(ANIMATION_EXPLOSION < 10) {
            e.update("/imagenes/explosiones/center/center_exp-1.png");
            changeExplosionPartSprites(e, 1);
        }
        if(ANIMATION_EXPLOSION >= 20) {
            e.update("/imagenes/explosiones/center/center_exp-2.png");
            changeExplosionPartSprites(e, 2);
        }
        if(ANIMATION_EXPLOSION >= 30) {
            e.update("/imagenes/explosiones/center/center_exp-3.png");
            changeExplosionPartSprites(e, 3);
        }
        if(ANIMATION_EXPLOSION >= 40) {
            e.update("/imagenes/explosiones/center/center_exp-4.png");
            changeExplosionPartSprites(e, 4);
        }
    }

    public void changeBonusSprites(Bonus b) {
        if(ANIMATION_BOMB < 20) {
            if(b.getClass() == BombaExtra.class) {
                b.update("/imagenes/bonus/bomba_extra.png");
            }
        }
        if(ANIMATION_BOMB >= 20)
            b.update("/imagenes/null.png");
    }
    
    @Override
    public void gameUpdate(double delta) {
        Keyboard keyboard = this.getKeyboard();

        // changeBrickWallSprites(brickWalls.get(0));
        // changeBombSprites(bombs.get(0));
        // changeExplosionSprites(exp);

        // for (Enemigo e : enemies) {
        //     changeEnemySprites(e);
        // }
            
        // for (Bonus b : bonus) {
        //     changeBonusSprites(b);
        // }
         
        // Procesar teclas de direccion
        if (keyboard.isKeyPressed(KeyEvent.VK_UP)) {
            hero.up(delta);

            if(ANIMATION_HERO > 40)
                ANIMATION_HERO = 0;
            else
                ANIMATION_HERO++;

            if(ANIMATION_HERO < 10)
                hero.update("/imagenes/bomberman/up/b_up-1.png");
            if(ANIMATION_HERO >= 20)
                hero.update("/imagenes/bomberman/up/b_up-2.png");
            if(ANIMATION_HERO >= 30)
                hero.update("/imagenes/bomberman/up/b_up-3.png");
        }

        if (keyboard.isKeyPressed(KeyEvent.VK_DOWN)) {
            hero.down(delta);
                
            if(ANIMATION_HERO > 40)
                ANIMATION_HERO = 0;
            else
                ANIMATION_HERO++;

            if(ANIMATION_HERO < 10)
                hero.update("/imagenes/bomberman/down/b_down-1.png");
            if(ANIMATION_HERO >= 20)
                hero.update("/imagenes/bomberman/down/b_down-2.png");
            if(ANIMATION_HERO >= 30)
                hero.update("/imagenes/bomberman/down/b_down-3.png");
        }

        if (keyboard.isKeyPressed(KeyEvent.VK_LEFT)) {
            hero.left(delta);

            if(ANIMATION_HERO > 60)
                ANIMATION_HERO = 0;
            else
                ANIMATION_HERO++;

            if(ANIMATION_HERO < 15)
                hero.update("/imagenes/bomberman/left/b_left-1.png");
            if(ANIMATION_HERO >= 30)
                hero.update("/imagenes/bomberman/left/b_left-2.png");
            if(ANIMATION_HERO >= 45)
                hero.update("/imagenes/bomberman/left/b_left-3.png");
        }

        if (keyboard.isKeyPressed(KeyEvent.VK_RIGHT)) {
            hero.right(delta);

            if(ANIMATION_HERO > 60)
                ANIMATION_HERO = 0;
            else
                ANIMATION_HERO++;

            if(ANIMATION_HERO < 15)
                hero.update("/imagenes/bomberman/right/b_right-1.png");
            if(ANIMATION_HERO >= 30)
                hero.update("/imagenes/bomberman/right/b_right-2.png");
            if(ANIMATION_HERO >= 45)
                hero.update("/imagenes/bomberman/right/b_right-3.png");
        }

        // Esc fin del juego
        LinkedList <KeyEvent> keyEvents = keyboard.getEvents();
        for (KeyEvent event: keyEvents) {
            if ((event.getID() == KeyEvent.KEY_PRESSED) && (event.getKeyCode() == KeyEvent.VK_ESCAPE)) {
                stop();
            }
        }

        cam.followHero(hero);
    }

    @Override
    public void gameShutdown() {
        Log.info(getClass().getSimpleName(), "Shutting down game");
    }
}