package model.explosion;

import model.ObjetoGrafico;
import model.interfaces.ObjetoCambianteEstatico;

public class ParteExplosion extends ObjetoGrafico implements ObjetoCambianteEstatico {
    /*
     * Direccion de la parte de explosion (se usa en la animacion).
     */
    private String dir;

    public ParteExplosion(String filename, String dir) {
        super(filename);
        this.dir = dir;
    }

    /*
     * Metodods de ObjetoCambianteEstatico. Animaciones de moviviento y golpe de
     * explosion.
     */
    @Override
    public void changeObject() {
        if (ANIMATION_COUNTER > 60)
            ANIMATION_COUNTER = 60;
        else if (ANIMATION_COUNTER >= 50) {
            update("/imagenes/null.png");
            ANIMATION_COUNTER++;
        } else {
            for (int i = 10; i <= 40; i += 10) {
                if (i - 10 <= ANIMATION_COUNTER && ANIMATION_COUNTER < i)
                    update("/imagenes/explosiones/" + dir + "/" + dir + "_exp-" + (50 - i) / 10 + ".png");
            }

            ANIMATION_COUNTER++;
        }
    }

    @Override
    public void hit() {
    }
}