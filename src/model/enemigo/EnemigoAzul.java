package model.enemigo;

public class EnemigoAzul extends Enemigo {
    public EnemigoAzul(String fileName) {
        super(fileName);
    }

    /*
        cambio de sprites
    */
    @Override
    public void changeObject(String dir) {
        
    }

    @Override
    public void kill() {
        checkAnimationCounter(40);
        
        if(ANIMATION_COUNTER < 10)
            update("/imagenes/enemigos/azul/enemigo_azul-1.png");
        if(ANIMATION_COUNTER >= 20 && ANIMATION_COUNTER < 30)
            update("/imagenes/enemigos/azul/enemigo_azul_M1.png");
    }
}