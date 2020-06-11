package model;

import java.awt.Color;
import java.awt.Graphics2D;

import model.bomberman.Bomberman;

public class Reloj {
    private int time = 200;
    private double ANIMATION_COUNTER = 0;
    private double x, y;

    public Reloj() {
        this.x = Bomberman.LEFT_WALL_LIMIT;
        this.y = Bomberman.UPPER_WALL_LIMIT/2;
    }

    /*
        Getters
    */
    public int getTime() {
        return this.time;
    }

    public double getX() {
        return this.x;
    }

    /*
        Setters
    */
    public void setTime(int time) {
        this.time = time;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void countTime() {
        if(ANIMATION_COUNTER > 100)
            ANIMATION_COUNTER = 0;
        else
            ANIMATION_COUNTER++;

        if(ANIMATION_COUNTER == 100)
            time--;
    }

    public void draw(Graphics2D g) {
        if(this.time == 0)
            this.time = 200;

        g.setColor(Color.black);
        g.drawString("Tiempo de Juego: " + time, (int) this.x+2, (int) this.y+2);

    	g.setColor(Color.white);
        g.drawString("Tiempo de Juego: " + time, (int) this.x, (int) this.y);
    }
}