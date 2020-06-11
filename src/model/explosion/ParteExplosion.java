package model.explosion;

import model.ObjetoGrafico;
import model.interfaces.ObjetoCambianteEstatico;

public class ParteExplosion extends ObjetoGrafico implements ObjetoCambianteEstatico {
    private String dir;

    public ParteExplosion(String filename, String dir) {
        super(filename);
        this.dir = dir;
    }

    @Override
    public void changeSprites() {
        if(this.ANIMATION_COUNTER > 60)
            this.ANIMATION_COUNTER = 60;
        else if(this.ANIMATION_COUNTER >= 50) {
            this.update("/imagenes/null.png");
            this.ANIMATION_COUNTER++;
        }
        else {
            for (int i = 10; i <= 40; i += 10) {
                if(i-10 <= this.ANIMATION_COUNTER && this.ANIMATION_COUNTER < i)
                    this.update("/imagenes/explosiones/" + dir + "/" + dir + "_exp-" + (50-i)/10 + ".png");
            }

            this.ANIMATION_COUNTER++;
        }
    }
}