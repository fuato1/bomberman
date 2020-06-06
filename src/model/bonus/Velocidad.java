package model.bonus;

import model.ObjetoGrafico;
import model.bonus.strategy.Bonus;

public final class Velocidad extends ObjetoGrafico implements Bonus {
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
}