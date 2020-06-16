package model;

import java.util.Vector;

import model.jugador.Jugador;

public class Ranking {
    private Vector<Jugador> scores;

    public Ranking() {
        scores = new Vector<Jugador>();
    }

    public Vector<Jugador> getScores() {
        return this.scores;
    }
}