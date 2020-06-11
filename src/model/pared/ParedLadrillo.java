package model.pared;

public class ParedLadrillo extends Pared {
    public ParedLadrillo(String filename) {
        super(filename);
    }

    /*
        cambio de sprites
    */
    @Override
    public void changeSprites() {
        checkAnimationCounter(70);

        if(ANIMATION_COUNTER < 10)
            update("/imagenes/paredes/pared_ladrillo.png");
        else {
            for (int i = 20; i <= 60; i += 10) {
                if(i-10 <= ANIMATION_COUNTER && ANIMATION_COUNTER < i)
                    update("/imagenes/paredes/pared_ladrillo_R" + ((i/10)-1) + ".png");
            }
        }
    }
}