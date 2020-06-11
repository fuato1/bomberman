package model.bonus;

import model.ObjetoGrafico;
import model.bonus.strategy.Bonus;

public class RangoExplosion extends ObjetoGrafico implements Bonus {
    public RangoExplosion() {
        super("/imagenes/bonus/rango_explosion.png");
    }

    @Override
    public void activateBonus() {
        System.out.println("rango explosion");
    }

    @Override
    public void bonusHit() {
        System.out.println("bonus golpeado");
    }

    @Override
    public void changeSprites() {
        checkAnimationCounter(20);

        if(ANIMATION_COUNTER < 10)
            update("/imagenes/bonus/rango_explosion.png");
    }
}