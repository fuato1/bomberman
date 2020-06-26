package model;

import java.awt.Color;
import java.awt.Graphics2D;

import model.bomberman.Bomberman;

public class Puntaje {
    private int currentScore = 0;
    private double x;

    public Puntaje() {
        x = 17*32;
    }

    /*
        Getters
    */
    public int getScore() {
        return currentScore;
    }

    public double getX() {
        return x;
    }

    /*
        Setters
    */
    public void setScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.black);
        g.drawString("Puntos: " + currentScore, (int) x+2, (int) (Bomberman.UPPER_WALL_LIMIT/2)+2);

    	g.setColor(Color.white);
        g.drawString("Puntos: " + currentScore, (int) x, (int) (Bomberman.UPPER_WALL_LIMIT/2));
    }
}