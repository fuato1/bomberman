package model;

import javax.imageio.ImageIO;

public class Bomba extends ObjetoGrafico implements ObjetoCambiante {
    private int time = 3;
    private int range = 2;

    public Bomba() {
        super("/imagenes/bombas/bomb-1.png");
    }

    // public Explosion detonate() {}

    /*
        Getters
    */
    public int getTime() {
        return time;
    }

    public int getRange() {
        return range;
    }

    /*
        Setters
    */
    public void setTime(int time) {
        this.time = time;
    }

    public void setRange(int range) {
        this.range = range;
    }

    @Override
    public void update(String fileName) {
        try {
            image = ImageIO.read(getClass().getResource(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}