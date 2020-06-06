package model.enemigo;

import model.Heroe;

public class EnemigoRosa extends Enemigo {
    public EnemigoRosa(String fileName) {
        super(fileName);
    }

    @Override
    public void hurtHero(Heroe hero) {
        System.out.print("El enemigo rosa hirio a Bomberman....");
    }
}