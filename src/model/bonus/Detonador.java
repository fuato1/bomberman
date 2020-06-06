package model.bonus;

import model.ObjetoGrafico;
import model.bonus.strategy.Bonus;

public final class Detonador extends ObjetoGrafico implements Bonus {
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
}