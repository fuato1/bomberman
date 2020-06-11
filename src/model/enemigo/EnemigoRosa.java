package model.enemigo;

import model.Heroe;

public class EnemigoRosa extends Enemigo {
    public EnemigoRosa(String fileName) {
        super(fileName);
    }

    @Override
    public void hurtHero(Heroe hero) {
        System.out.print("El enemigo rosa hirio a Bomberman....");
    }

    @Override
    public void changeObject(String dir) {
        checkAnimationCounter(70);
        
        for (int i = 10; i <= 50; i += 10) {
            if(i-10 <= ANIMATION_COUNTER && ANIMATION_COUNTER < i)
                update("/imagenes/enemigos/rosa/eliminado/enemigo_rosa_M" + i/10 + ".png");
        }
    }
}