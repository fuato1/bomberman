package model.enemigo;

import java.util.Random;

public class EnemigoAzul extends Enemigo {
    public EnemigoAzul(String fileName) {
        super(fileName);
    }

    /*
        cambio de sprites
    */
    @Override
    public void changeObject(String dir) {
        if(ENEMY_MOVING_TIME < 0) {
            Random r = new Random(System.currentTimeMillis());
            CURRENT_DIRECTION = directions[r.nextInt(15)%4];

            ENEMY_MOVING_TIME = 30;
        }
        else {
            ENEMY_MOVING_TIME--;
        }
        
        if(ANIMATION_COUNTER > 30) {
            ANIMATION_COUNTER = 0;
        }
        else {
            for (int i = 10; i <= 30; i += 10) {
                if(i-10 <= ANIMATION_COUNTER && ANIMATION_COUNTER < i)
                    update("/imagenes/enemigos/azul/" + dir + "/enemigo_azul-" + i/10 + ".png");
            }

            ANIMATION_COUNTER++;
        }
    }

    @Override
    public void kill() {
        if(ANIMATION_COUNTER > 70)
            ANIMATION_COUNTER = 70;
        if(ANIMATION_COUNTER >= 35)
            update("/imagenes/null.png");
        if(ANIMATION_COUNTER < 35)
            update("/imagenes/enemigos/azul/enemigo_azul_M1.png");
    }
}