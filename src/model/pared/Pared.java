package model.pared;

import model.ObjetoGrafico;

public class Pared extends ObjetoGrafico {
    public static int PARED_PIEDRA = 0;
    public static int PARED_LADRILLO = 1;

    public Pared(String filename) {
        super(filename);
    }
}