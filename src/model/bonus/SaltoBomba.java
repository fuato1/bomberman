package model.bonus;

import model.ObjetoGrafico;
import model.bonus.strategy.Bonus;

public class SaltoBomba extends ObjetoGrafico implements Bonus {
    public SaltoBomba() {
        super("/imagenes/bonus/salto_bomba.png");
    }

    @Override
    public void activateBonus() {
        System.out.println("salto bomba");
    }

    @Override
    public void bonusHit() {
        System.out.println("bonus golpeado");
    }

    @Override
    public void changeSprites() {
        checkAnimationCounter(20);

        if(ANIMATION_COUNTER < 10)
            update("/imagenes/bonus/salto_bomba.png");
    }
}