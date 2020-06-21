package model;

import model.explosion.Explosion;
import model.factory.OGAbstractFactory;
import model.factory.OGFactoryProducer;
import model.interfaces.ObjetoCambianteEstatico;

public class Bomba extends ObjetoGrafico implements ObjetoCambianteEstatico {
    private int time = 3;
    private int range = 2;
    private static boolean IS_ACTIVE = false;

    public Bomba() {
        super("/imagenes/bombas/bomb-1.png");
    }

    /*
        Getters
    */
    public int getTime() {
        return this.time;
    }

    public int getRange() {
        return this.range;
    }

    public static boolean isActive() {
        return IS_ACTIVE;
    }

    /*
        detonacion de la bomba
    */
    public Explosion detonate() {
        OGAbstractFactory factory = OGFactoryProducer.getFactory();
        Explosion e = factory.getExplosion();
        e.setPosition(this.getX(), this.getY());
        setPosition(0, 0);

        return e;
    }

    /*
        Setters
    */
    public void setTime(int time) {
        this.time = time;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public static void setActive(boolean IS_ACTIVE) {
        Bomba.IS_ACTIVE = IS_ACTIVE;
    }

    /*
        cambio de sprites
    */
    @Override
    public void changeObject() {
        if(this.ANIMATION_COUNTER > 120) {
            this.ANIMATION_COUNTER = 120;
        }
        else if(this.ANIMATION_COUNTER > 90) {
            this.update("/imagenes/null.png");
            this.ANIMATION_COUNTER++;
        }
        else {
            for (int i = 30; i <= 90; i += 30) {
                if(this.ANIMATION_COUNTER == i)
                    time--;

                if(i-30 <= this.ANIMATION_COUNTER && this.ANIMATION_COUNTER < i) {
                    this.update("/imagenes/bombas/bomb-" + i/30 + ".png");
                }
            }

            this.ANIMATION_COUNTER++;
        }
    }

    @Override
    public void hit() {
        // TODO Auto-generated method stub

    }
}