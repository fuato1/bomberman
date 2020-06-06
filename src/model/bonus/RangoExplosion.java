package model.bonus;

import model.ObjetoGrafico;
import model.bonus.strategy.Bonus;

public final class RangoExplosion extends ObjetoGrafico implements Bonus {
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
}