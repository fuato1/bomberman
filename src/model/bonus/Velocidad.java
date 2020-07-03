package model.bonus;

import java.util.Vector;

import model.Heroe;
import model.ObjetoGrafico;
import model.bomberman.Bomberman;
import model.enemigo.Enemigo;

public class Velocidad extends ObjetoGrafico implements Bonus {
    private boolean WAS_HIT = false;

    public Velocidad() {
        super("/imagenes/bonus/velocidad.png");
    }

    @Override
    public boolean wasHit() {
        return WAS_HIT;
    }

    @Override
    public void setWasHit(boolean WAS_HIT) {
        this.WAS_HIT = WAS_HIT;
    }

    @Override
    public void activateBonus(Heroe h) {
        h.setHeroDisplacement(h.getHeroDisplacement() + 50);
    }

    @Override
    public void deactivateBonus(Heroe h) {
        h.setHeroDisplacement(h.getHeroDisplacement() - 50);
    }

    @Override
    public void changeObject() {
    }

    @Override
    public Vector<Enemigo> spawnEnemies() {
        Vector<Enemigo> enemies = Bomberman.factory.getEnemigos(Enemigo.ENEMIGO_AZUL, 6);

        for (Enemigo e : enemies) {
            e.setImmunityTime(80);
            e.setPosition(getX(), getY());
        }

        return enemies;
    }

    @Override
    public void hit() {
        WAS_HIT = true;
    }
}