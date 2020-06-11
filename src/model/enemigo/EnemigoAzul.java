package model.enemigo;

import model.Heroe;

public class EnemigoAzul extends Enemigo {
    public EnemigoAzul(String fileName) {
        super(fileName);
    }

    @Override
    public void hurtHero(Heroe hero) {
        System.out.print("El enemigo azul hirio a Bomberman....");
    }

    /*
        cambio de sprites
    */
    @Override
    public void changeObject(String dir) {
        checkAnimationCounter(40);
        
        if(ANIMATION_COUNTER < 10)
            update("/imagenes/enemigos/azul/enemigo_azul-1.png");
        if(ANIMATION_COUNTER >= 20 && ANIMATION_COUNTER < 30)
            update("/imagenes/enemigos/azul/enemigo_azul_M1.png");
    }
}