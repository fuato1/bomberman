package model;

import java.util.Vector;

import com.entropyinteractive.JGame;

import model.jugador.Jugador;

public class SistemaJuegos {
    private Jugador player;
    private Vector<JGame> games;

    public SistemaJuegos(Jugador player) {
        this.player = player;
    }

    /*
        Getters
    */
    public Jugador getPlayer() {
        return player;
    }

    public Vector<JGame> getGames() {
        return games;
    }

    /*
        Setters
    */
    public void setPlayer(Jugador player) {
        this.player = player;
    }
}