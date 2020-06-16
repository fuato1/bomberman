package model.enemigo;

import java.util.Random;

import model.ObjetoGrafico;
import model.bomberman.Bomberman;
import model.bomberman.Mundo;
import model.interfaces.ObjetoCambianteMovible;

public abstract class Enemigo extends ObjetoGrafico implements ObjetoCambianteMovible {
    public final static int ENEMIGO_ROSA = 0;
    public final static int ENEMIGO_AZUL = 1;

    private final double ENEMY_DISPLACEMENT = 100.0;
    protected int ENEMY_MOVING_TIME = 30;
    protected String directions[] = {"up", "down", "left", "right"};
    protected String CURRENT_DIRECTION;
    protected boolean IS_DEAD = false;

    private boolean IS_OVER_WALL = false;
    private boolean IS_NEXT_TO_WALL = false;

    public Enemigo(String filename) {
        super(filename);
        Random r = new Random();
        CURRENT_DIRECTION = directions[r.nextInt(3)];
    }

    /*
        Getters
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

    /*
        Setters
    */
    public void stop() {
        IS_DEAD = true;
    }

    public void checkVerticalMovement() {
        for (int i = 0; i < 11; i++) {
            if((64 + 64*i)+1 < getX() + 28 && getX() < (96 + 64*i)-1) {
                IS_OVER_WALL = true;
            }
        }
    }

    public void checkHorizontalMovement() {
        for (int i = 0; i < 5; i++) {
            if((137 + 64*i)+1 < getY() + 28 && getY() < (169 + 64*i)-1) {
                IS_NEXT_TO_WALL = true;
            }
        }
    }

    public void up(double delta) {
        checkVerticalMovement();

        if(!(getY() < Bomberman.UPPER_WALL_LIMIT+1) && !IS_OVER_WALL) {
            IS_NEXT_TO_WALL = false;
            setPosition(getX(), getY() - ENEMY_DISPLACEMENT * delta);
        }
    }

    public void down(double delta) {
        checkVerticalMovement();

        if(!(getY() > Bomberman.LOWER_WALL_LIMIT - 33) && !IS_OVER_WALL) {
            IS_NEXT_TO_WALL = false;
            setPosition(getX(), getY() + ENEMY_DISPLACEMENT * delta);
        }
    }

    public void left(double delta) {
        checkHorizontalMovement();

        if(!(getX() < 33) && !IS_NEXT_TO_WALL) {
            IS_OVER_WALL = false;
            setPosition(getX() - ENEMY_DISPLACEMENT * delta, getY());
        }
    }

    public void right(double delta) {    
        Mundo w = Mundo.getInstance();

        checkHorizontalMovement();

        if(!((getX() + getWidth()) > w.getWidth()-33) && !IS_NEXT_TO_WALL) {
            IS_OVER_WALL = false;
            setPosition(getX() + ENEMY_DISPLACEMENT * delta, getY());
        }
    }
}