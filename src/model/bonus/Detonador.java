package model.bonus;

import java.util.Vector;

import model.Heroe;
import model.ObjetoGrafico;
import model.bonus.strategy.Bonus;
import model.enemigo.Enemigo;
import model.factory.OGAbstractFactory;
import model.factory.OGFactoryProducer;

public class Detonador extends ObjetoGrafico implements Bonus {
    private boolean WAS_HIT = false;
    
    public Detonador() {
        super("/imagenes/bonus/detonador.png");
    }
    
    public boolean wasHit() {
        return WAS_HIT;
    }

    public void setWasHit(boolean WAS_HIT) {
        this.WAS_HIT = WAS_HIT;
    }

    @Override
    public void activateBonus(Heroe h) {
        h.setHasDetonator(true);
    }

    @Override
    public void deactivateBonus(Heroe h) {
        h.setHasDetonator(false);
    }

    @Override
    public void changeObject() {}

    public Vector<Enemigo> spawnEnemies() {
        OGAbstractFactory factory = OGFactoryProducer.getFactory();
        Vector<Enemigo> enemies = factory.getEnemigos(Enemigo.ENEMIGO_AZUL, 6);

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