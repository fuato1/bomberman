package model;

import model.bonus.strategy.Bonus;

public class Heroe extends ObjetoGrafico {
    private int life = 3;
    private final double HERO_DISPLACEMENT = 250.0;

    private boolean IS_OVER_WALL = false;
    private boolean IS_NEXT_TO_WALL = false;

    public Heroe() {
        super("/imagenes/bomberman/down/b_down-1.png");
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

    public void setBomb(Bomba b) {}

    public void consumeBonus(Bonus b) {
        b.activateBonus();
    }

    /*
        Movimiento
    */
    public void up(double delta) {
        for (int i = 0; i < 5; i++) {
            if((64 + 64*i)+1 < getX() + 28 && getX() < (96 + 64*i)-1) {
                IS_OVER_WALL = true;
            }
        }

        if(!(getY() < Bomberman.UPPER_WALL_LIMIT+1) && !IS_OVER_WALL) {
            IS_NEXT_TO_WALL = !IS_NEXT_TO_WALL;
            setPosition(getX(), getY() - HERO_DISPLACEMENT * delta);
        }
    }

    public void down(double delta) {
        for (int i = 0; i < 5; i++) {
            if((64 + 64*i)+1 < getX() + 28 && getX() < (96 + 64*i)-1) {
                IS_OVER_WALL = true;
            }
        }

        if(!(getY() > Bomberman.LOWER_WALL_LIMIT - 33) && !IS_OVER_WALL) {
            IS_NEXT_TO_WALL = !IS_NEXT_TO_WALL;
            setPosition(getX(), getY() + HERO_DISPLACEMENT * delta);
        }
    }

    public void left(double delta) {
        for (int i = 0; i < 5; i++) {
            if((137 + 64*i)+1 < getY() + 28 && getY() < (169 + 64*i)-1) {
                IS_NEXT_TO_WALL = true;
            }
        }

        if(!(getX() < 33) && !IS_NEXT_TO_WALL) {
            IS_OVER_WALL = false;
            setPosition(getX() - HERO_DISPLACEMENT * delta, getY());
        }
    }

    public void right(double delta) {    
        Mundo w = Mundo.getInstance();

        for (int i = 0; i < 5; i++) {
            if((137 + 64*i)+1 < getY() + 28 && getY() < (169 + 64*i)-1) {
                IS_NEXT_TO_WALL = true;
            }
        }

        if(!((getX() + getWidth()) > w.getWidth()-33) && !IS_NEXT_TO_WALL) {
            IS_OVER_WALL = false;
            setPosition(getX() + HERO_DISPLACEMENT * delta, getY());
        }
    }
}