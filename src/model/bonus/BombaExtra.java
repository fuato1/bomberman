package model.bonus;

import model.ObjetoGrafico;
import model.bonus.strategy.Bonus;

public class BombaExtra extends ObjetoGrafico implements Bonus {
    public BombaExtra() {
        super("/imagenes/bonus/bomba_extra.png");
    }

    @Override
    public void activateBonus() {
        System.out.println("bomba extra");
    }

    @Override
    public void bonusHit() {
        System.out.println("bonus golpeado");
    }

    @Override
    public void changeObject() {
        checkAnimationCounter(20);

        if(ANIMATION_COUNTER < 10)
            update("/imagenes/bonus/bomba_extra.png");
    }

    @Override
    public void hit() {
        // TODO Auto-generated method stub

    }
}