package model.bonus;

import model.ObjetoGrafico;
import model.bonus.strategy.Bonus;

public final class VidaExtra extends ObjetoGrafico implements Bonus {
    public VidaExtra() {
        super("/imagenes/bonus/vida_extra.png");
    }

    @Override
    public void activateBonus() {
        System.out.print("vida extra");
    }

    @Override
    public void bonusHit() {
        System.out.println("bonus golpeado");
    }
}