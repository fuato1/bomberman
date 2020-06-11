package model;

import java.util.Vector;

import model.bomberman.Bomberman;
import model.bomberman.Mundo;
import model.bonus.strategy.Bonus;
import model.factory.OGAbstractFactory;
import model.factory.OGFactoryProducer;
import model.interfaces.ObjetoCambianteMovible;

public class Heroe extends ObjetoGrafico implements ObjetoCambianteMovible {
    private int life = 3;
    private final double HERO_DISPLACEMENT = 200.0;
    private boolean IS_OVER_WALL = false;
    private boolean IS_NEXT_TO_WALL = false;
    private Vector<Bonus> activeBonus;

    public Heroe() {
        super("/imagenes/bomberman/down/b_down-1.png");
        activeBonus = new Vector<Bonus>(0);
    }

    /*
        Getters
    */
    public int getLife() {
        return life;
    }

    /*
        Setters
    */
    public void setLife(int life) {
        this.life = life;
    }

    public Bomba setBomb() {
        checkHorizontalMovement();
        checkHorizontalMovement();

        if(!IS_OVER_WALL && !IS_NEXT_TO_WALL) {
            OGAbstractFactory factory = OGFactoryProducer.getFactory();
            Bomba b = factory.getBomba();
            b.setPosition(this.getX(), this.getY());

            return b;
        }

        return null;
    }

    public void consumeBonus(Bonus b) {
        // b.activateBonus();
    }

    /*
        cambio de sprites
    */
    @Override
    public void changeObject(String dir) {
        if(ANIMATION_COUNTER > 40) {
            ANIMATION_COUNTER = 0;
        }
        else {
            if(ANIMATION_COUNTER < 10)
                update("/imagenes/bomberman/" + dir + "/b_" + dir + "-1.png");
            if(ANIMATION_COUNTER >= 20)
                update("/imagenes/bomberman/" + dir + "/b_" + dir + "-2.png");
            if(ANIMATION_COUNTER >= 30)
                update("/imagenes/bomberman/" + dir + "/b_" + dir + "-3.png");

            ANIMATION_COUNTER++;
        }
    }

    /*
        movimiento
    */
    private void checkVerticalMovement() {
        for (int i = 0; i < 11; i++) {
            if((64 + 64*i)+1 < getX() + 28 && getX() < (96 + 64*i)-1) {
                IS_OVER_WALL = true;
            }
        }
    }

    private void checkHorizontalMovement() {
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
            setPosition(getX(), getY() - HERO_DISPLACEMENT * delta);
        }
    }

    public void down(double delta) {
        checkVerticalMovement();

        if(!(getY() > Bomberman.LOWER_WALL_LIMIT - 33) && !IS_OVER_WALL) {
            IS_NEXT_TO_WALL = false;
            setPosition(getX(), getY() + HERO_DISPLACEMENT * delta);
        }
    }

    public void left(double delta) {
        checkHorizontalMovement();

        if(!(getX() < 33) && !IS_NEXT_TO_WALL) {
            IS_OVER_WALL = false;
            setPosition(getX() - HERO_DISPLACEMENT * delta, getY());
        }
    }

    public void right(double delta) {    
        Mundo w = Mundo.getInstance();

        checkHorizontalMovement();

        if(!((getX() + getWidth()) > w.getWidth()-33) && !IS_NEXT_TO_WALL) {
            IS_OVER_WALL = false;
            setPosition(getX() + HERO_DISPLACEMENT * delta, getY());
        }
    }
}