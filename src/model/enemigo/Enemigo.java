package model.enemigo;

import java.util.Random;

import model.ObjetoGrafico;
import model.bomberman.Bomberman;
import model.bomberman.Mundo;
import model.interfaces.ObjetoCambianteMovible;

public abstract class Enemigo extends ObjetoGrafico implements ObjetoCambianteMovible {
    /*
     * Constantes que identifican a los enemigos.
     */
    public final static int ENEMIGO_ROSA = 0;
    public final static int ENEMIGO_AZUL = 1;

    /*
     * Variables usadas para el movimiento de los enemigos.
     */
    private final double ENEMY_DISPLACEMENT = 100.0;
    protected int ENEMY_MOVING_TIME = 30;
    protected String directions[] = { "up", "down", "left", "right" };
    protected String CURRENT_DIRECTION;

    /*
     * Inmunidad de los enemigos por cierto tiempo para que no mueran inmediatamente
     * despues de aparecer cerca a una explosion (ej: golpear un bonus con una
     * explosion).
     */
    protected int IMMUNITY = 0;

    /*
     * Variables de estado para los enemigos.
     */
    protected boolean IS_DEAD = false;
    protected boolean WAS_HIT = false;
    private boolean IS_OVER_WALL = false;
    private boolean IS_NEXT_TO_WALL = false;

    public Enemigo(String filename) {
        super(filename);
        Random r = new Random(System.currentTimeMillis());
        CURRENT_DIRECTION = directions[r.nextInt(3)];
    }

    /*
     * Getters.
     */
    public boolean isOverWall() {
        checkVerticalMovement();
        return IS_OVER_WALL;
    }

    public boolean isNextToWall() {
        checkHorizontalMovement();
        return IS_NEXT_TO_WALL;
    }

    public int getEnemyMovingTime() {
        return this.ENEMY_MOVING_TIME;
    }

    public String getCurrentDirection() {
        return this.CURRENT_DIRECTION;
    }

    public boolean isDead() {
        return this.IS_DEAD;
    }

    public boolean wasHit() {
        return this.WAS_HIT;
    }

    public int getImmunityTime() {
        return IMMUNITY;
    }

    /*
     * Setters.
     */
    public void stop() {
        this.WAS_HIT = true;
    }

    public void setImmunityTime(int IMMUNITY) {
        this.IMMUNITY = IMMUNITY;
    }

    private void checkVerticalMovement() {
        for (int i = 0; i < 11; i++) {
            if ((64 + 64 * i) + 1 < getX() + 28 && getX() < (96 + 64 * i) - 1) {
                IS_OVER_WALL = true;
            }
        }
    }

    private void checkHorizontalMovement() {
        for (int i = 0; i < 5; i++) {
            if ((137 + 64 * i) + 1 < getY() + 28 && getY() < (169 + 64 * i) - 1) {
                IS_NEXT_TO_WALL = true;
            }
        }
    }

    public void up(double delta) {
        checkVerticalMovement();

        if (!(getY() < Bomberman.UPPER_WALL_LIMIT + 1) && !IS_OVER_WALL) {
            IS_NEXT_TO_WALL = false;
            setPosition(getX(), getY() - ENEMY_DISPLACEMENT * delta);
        }
    }

    public void down(double delta) {
        checkVerticalMovement();

        if (!(getY() > Bomberman.LOWER_WALL_LIMIT - 33) && !IS_OVER_WALL) {
            IS_NEXT_TO_WALL = false;
            setPosition(getX(), getY() + ENEMY_DISPLACEMENT * delta);
        }
    }

    public void left(double delta) {
        checkHorizontalMovement();

        if (!(getX() < 33) && !IS_NEXT_TO_WALL) {
            IS_OVER_WALL = false;
            setPosition(getX() - ENEMY_DISPLACEMENT * delta, getY());
        }
    }

    public void right(double delta) {
        Mundo w = Mundo.getInstance();

        checkHorizontalMovement();

        if (!((getX() + getWidth()) > w.getWidth() - 33) && !IS_NEXT_TO_WALL) {
            IS_OVER_WALL = false;
            setPosition(getX() + ENEMY_DISPLACEMENT * delta, getY());
        }
    }
}