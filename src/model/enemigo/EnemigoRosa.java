package model.enemigo;

import java.util.Random;

public class EnemigoRosa extends Enemigo {
    public EnemigoRosa(String fileName) {
        super(fileName);
    }

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
                    update("/imagenes/enemigos/rosa/" + dir + "/enemigo_rosa-" + i/10 + ".png");
            }

            ANIMATION_COUNTER++;
        }
    }

    @Override
    public void kill() {
        if(ANIMATION_COUNTER > 70) {
            ANIMATION_COUNTER = 0;
        }
        else if(ANIMATION_COUNTER >= 60) {
            this.update("/imagenes/null.png");
            ANIMATION_COUNTER++;
        }
        else {
            for (int i = 10; i <= 50; i += 10) {
                if(i-10 <= ANIMATION_COUNTER && ANIMATION_COUNTER < i)
                    update("/imagenes/enemigos/rosa/eliminado/enemigo_rosa_M" + i/10 + ".png");
            }

            ANIMATION_COUNTER++;
        }
    }
}