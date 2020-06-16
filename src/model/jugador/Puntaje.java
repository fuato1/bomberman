package model.jugador;

import java.awt.Color;
import java.awt.Graphics2D;

import model.bomberman.Bomberman;

public class Puntaje {
    private int score = 0;
    private double x;
    private double ANIMATION_COUNTER = 0;

    public Puntaje() {
        x = 500;
    }

    /*
        Getters
    */
    public int getScore() {
        return score;
    }

    public double getX() {
        return x;
    }

    /*
        Setters
    */
    public void setScore(int score) {
        this.score = score;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void countScore() {
        if(ANIMATION_COUNTER > 100)
            ANIMATION_COUNTER = 0;
        else
            ANIMATION_COUNTER++;

        if(ANIMATION_COUNTER == 100)
            score++;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.black);
        g.drawString("Puntos: " + score, (int) x+2, (int) (Bomberman.UPPER_WALL_LIMIT/2)+2);

    	g.setColor(Color.white);
        g.drawString("Puntos: " + score, (int) x, (int) (Bomberman.UPPER_WALL_LIMIT/2));
    }
}