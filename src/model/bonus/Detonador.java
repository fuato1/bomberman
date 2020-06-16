package model.bonus;

import model.ObjetoGrafico;
import model.bonus.strategy.Bonus;

public class Detonador extends ObjetoGrafico implements Bonus {
    public Detonador() {
        super("/imagenes/bonus/detonador.png");
    }

    @Override
    public void activateBonus() {
        System.out.println("detonador");
    }

    @Override
    public void bonusHit() {
        System.out.println("bonus golpeado");
    }

    @Override
    public void changeObject() {
        checkAnimationCounter(20);

        if(ANIMATION_COUNTER < 10)
            update("/imagenes/bonus/detonador.png");
    }
}