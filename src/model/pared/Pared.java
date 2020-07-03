package model.pared;

import model.ObjetoGrafico;
import model.interfaces.ObjetoCambianteEstatico;

public class Pared extends ObjetoGrafico implements ObjetoCambianteEstatico {
    /*
     * Constantes para identificar a la pared de piedra y ladrillo.
     */
    public final static int PARED_PIEDRA = 0;
    public final static int PARED_LADRILLO = 1;

    /*
     * Varaibles de estado para cuando las paredes de ladrillo son destruidas por
     * las explosiones. Se setea WAS_HIT = true cuando se detecta la colision y
     * WAS_DESTROYED = true cuando las pared termina su animacion.
     */
    protected boolean WAS_DESTROYED = false;
    protected boolean WAS_HIT = false;

    public Pared(String filename) {
        super(filename);
    }

    /*
     * Getters.
     */
    public boolean wasDestroyed() {
        return this.WAS_DESTROYED;
    }

    public boolean wasHit() {
        return this.WAS_HIT;
    }

    /*
     * Metodods de ObjetoCambianteEstatico. Animaciones de moviviento y golpe de
     * explosion.
     */
    @Override
    public void changeObject() {
    }

    @Override
    public void hit() {

    }
}