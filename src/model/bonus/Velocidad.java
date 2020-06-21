package model.bonus;

import model.ObjetoGrafico;
import model.bonus.strategy.Bonus;

public class Velocidad extends ObjetoGrafico implements Bonus {
    public Velocidad() {
        super("/imagenes/bonus/velocidad.png");
    }

    @Override
    public void activateBonus() {
        System.out.print("velocidad");
    }

    @Override
    public void bonusHit() {
        System.out.println("bonus golpeado");
    }

    @Override
    public void changeObject() {
        checkAnimationCounter(20);

        if(ANIMATION_COUNTER < 10)
            update("/imagenes/bonus/velocidad.png");
    }

    @Override
    public void hit() {
        // TODO Auto-generated method stub

    }
}