package model.enemigo;

import model.Heroe;
import model.ObjetoGrafico;

public abstract class Enemigo extends ObjetoGrafico {
    public static int ENEMIGO_ROSA = 0;
    public static int ENEMIGO_AZUL = 1;

    public Enemigo(String filename) {
        super(filename);
    }

    public abstract void hurtHero(Heroe hero);
}