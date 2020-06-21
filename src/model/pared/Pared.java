package model.pared;

import model.ObjetoGrafico;
import model.interfaces.ObjetoCambianteEstatico;

public class Pared extends ObjetoGrafico implements ObjetoCambianteEstatico {
    public final static int PARED_PIEDRA = 0;
    public final static int PARED_LADRILLO = 1;

    protected boolean WAS_DESTROYED = false;
    protected boolean WAS_HIT = false;

    public Pared(String filename) {
        super(filename);
    }

    /*
        Getters
    */
    public boolean wasDestroyed() {
        return this.WAS_DESTROYED;
    }

    public boolean wasHit() {
        return this.WAS_HIT;
    }

    @Override
    public void changeObject() {
    }

    @Override
    public void hit() {

    }
}