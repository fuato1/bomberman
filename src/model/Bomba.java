package model;

import model.bomberman.Bomberman;
import model.explosion.Explosion;
import model.interfaces.ObjetoCambianteEstatico;

public class Bomba extends ObjetoGrafico implements ObjetoCambianteEstatico {
    private int time = 3;

    public Bomba() {
        super("/imagenes/bombas/bomb-1.png");
    }

    /*
     * Getters.
     */
    public int getTime() {
        return this.time;
    }

    /*
     * Detonacion de la bomba.
     */
    public Explosion detonate(int range) {
        Explosion e = Bomberman.factory.getExplosion(range);
        e.setPosition(this.getX(), this.getY());
        setPosition(0, 0);

        return e;
    }

    /*
     * Setters.
     */
    public void setTime(int time) {
        this.time = time;
    }

    /*
     * Metodos de ObjetoCambianteEstatico. Animaciones de moviviento y muerte.
     */
    @Override
    public void changeObject() {
        if (this.ANIMATION_COUNTER > 120) {
            this.ANIMATION_COUNTER = 120;
        } else if (this.ANIMATION_COUNTER > 90) {
            this.update("/imagenes/null.png");
            this.ANIMATION_COUNTER++;
        } else {
            for (int i = 30; i <= 90; i += 30) {
                if (this.ANIMATION_COUNTER == i)
                    time--;

                if (i - 30 <= this.ANIMATION_COUNTER && this.ANIMATION_COUNTER < i) {
                    this.update("/imagenes/bombas/bomb-" + i / 30 + ".png");
                }
            }

            this.ANIMATION_COUNTER++;
        }
    }

    @Override
    public void hit() {

    }
}