package model.bonus;

import model.ObjetoGrafico;
import model.bonus.strategy.Bonus;

public class Puerta extends ObjetoGrafico implements Bonus {
    public Puerta() {
        super("/imagenes/bonus/puerta.png");
    }

    @Override
    public void activateBonus() {
        System.out.println("Puerta");
    }

    @Override
    public void bonusHit() {
        System.out.println("bonus golpeado");
    }
}