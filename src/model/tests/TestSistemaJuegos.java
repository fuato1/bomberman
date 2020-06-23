package model.tests;

import model.SistemaJuegos;
import model.jugador.Jugador;

public class TestSistemaJuegos {
    public static void main(String[] args) {
        Jugador player = new Jugador("Player");
        SistemaJuegos sj = new SistemaJuegos(player);
        sj.launch();
    }
}