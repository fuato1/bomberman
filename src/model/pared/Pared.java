package model.pared;

import model.ObjetoGrafico;
import model.interfaces.ObjetoCambianteEstatico;

public class Pared extends ObjetoGrafico implements ObjetoCambianteEstatico {
    public final static int PARED_PIEDRA = 0;
    public final static int PARED_LADRILLO = 1;

    public Pared(String filename) {
        super(filename);
    }

    @Override
    public void changeObject() {
    }
}