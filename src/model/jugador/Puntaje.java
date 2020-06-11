package model.jugador;

import java.awt.Color;
import java.awt.Graphics2D;

import model.bomberman.Bomberman;
import model.interfaces.ObjetoCambianteEstatico;

public class Puntaje {
    private int score;
    private double ANIMATION_COUNTER = 0;
    private double x, y;

    public Puntaje() {
        this.score = 0;
        this.x = 500;
        this.y = Bomberman.UPPER_WALL_LIMIT/2;
    }

    /*
        Getters
    */
    public int getScore() {
        return score;
    }

    public double getX() {
        return this.x;
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

    public void draw(Graphics2D g) {
        g.setColor(Color.black);
        g.drawString("Puntos: " + this.score, (int) this.x+2, (int) this.y+2);

    	g.setColor(Color.white);
        g.drawString("Puntos: " + this.score, (int) this.x, (int) this.y);
    }

    public void countScore() {
        if(ANIMATION_COUNTER > 100)
            ANIMATION_COUNTER = 0;
        else
            ANIMATION_COUNTER++;

        if(ANIMATION_COUNTER == 100)
            this.score++;
    }
}