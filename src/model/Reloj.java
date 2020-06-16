package model;

import java.awt.Color;
import java.awt.Graphics2D;

import model.bomberman.Bomberman;

public class Reloj {
    private int time = 200;
    private double x;
    private double ANIMATION_COUNTER = 0;

    public Reloj() {
        x = Bomberman.LEFT_WALL_LIMIT;
    }

    /*
        Getters
    */
    public int getTime() {
        return time;
    }

    public double getX() {
        return x;
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
        if(time == 0)
            time = 200;

        g.setColor(Color.black);
        g.drawString("Tiempo de Juego: " + time, (int) x+2, (int) (Bomberman.UPPER_WALL_LIMIT/2)+2);

    	g.setColor(Color.white);
        g.drawString("Tiempo de Juego: " + time, (int) x, (int) (Bomberman.UPPER_WALL_LIMIT/2));
    }
}