package model.enemigo;

import model.Heroe;

public class EnemigoAzul extends Enemigo {
    public EnemigoAzul(String fileName) {
        super(fileName);
    }

    @Override
    public void hurtHero(Heroe hero) {
        System.out.print("El enemigo azul hirio a Bomberman....");
    }
}