package model.panel_superior;

import model.bomberman.Bomberman;
import java.awt.Graphics2D;
import java.awt.Color;

public class Vidas {
    private int currentLife;
    private double x;

    public Vidas() {
        x = 9 * 32;
    }

    /*
     * Getters.
     */
    public int getLife() {
        return this.currentLife;
    }

    public double getX() {
        return this.x;
    }

    /*
     * Setters.
     */
    public void setLife(int currentLife) {
        this.currentLife = currentLife;
    }

    public void setX(double x) {
        this.x = x;
    }

    /*
     * Metodo para dibujar el puntaje en el panel superior.
     */
    public void draw(Graphics2D g) {
        g.setColor(Color.black);
        g.drawString("Vidas: " + currentLife, (int) x, (int) (Bomberman.UPPER_WALL_LIMIT / 2));

        g.setColor(Color.white);
        g.drawString("Vidas: " + currentLife, (int) x + 2, (int) (Bomberman.UPPER_WALL_LIMIT / 2) + 2);
    }
}