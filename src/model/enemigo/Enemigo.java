package model.enemigo;

import model.Heroe;
import model.ObjetoGrafico;
import model.interfaces.ObjetoCambianteMovible;

public abstract class Enemigo extends ObjetoGrafico implements ObjetoCambianteMovible {
    public final static int ENEMIGO_ROSA = 0;
    public final static int ENEMIGO_AZUL = 1;

    // private boolean IS_OVER_WALL = false;
    // private boolean IS_NEXT_TO_WALL = false;

    public Enemigo(String filename) {
        super(filename);
    }

    public abstract void hurtHero(Heroe hero);

    /*
        Getters
    */
    // public boolean isOverWall() {
    //     checkVerticalMovement();
    //     return IS_OVER_WALL;
    // }

    // public boolean isNextToWall() {
    //     checkHorizontalMovement();
    //     return IS_NEXT_TO_WALL;
    // }

    /*
        Setters
    */
    // public void setIsOverWall(boolean IS_OVER_WALL) {
    //     this.IS_OVER_WALL = IS_OVER_WALL;
    // }

    // public void setIsNextToWall(boolean IS_NEXT_TO_WALL) {
    //     this.IS_NEXT_TO_WALL = IS_NEXT_TO_WALL;
    // }

    // public void checkVerticalMovement() {
    //     for (int i = 0; i < 11; i++) {
    //         if((64 + 64*i)+1 < getX() + 28 && getX() < (96 + 64*i)-1) {
    //             IS_OVER_WALL = true;
    //         }
    //     }
    // }

    // public void checkHorizontalMovement() {
    //     for (int i = 0; i < 5; i++) {
    //         if((137 + 64*i)+1 < getY() + 28 && getY() < (169 + 64*i)-1) {
    //             IS_NEXT_TO_WALL = true;
    //         }
    //     }
    // }
}