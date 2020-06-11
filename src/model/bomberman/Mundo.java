package model.bomberman;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Mundo {
    public final static int WORLD_WIDTH = 640;
    public final static int WORLD_HEIGHT = 480;

    private static Mundo INSTANCE = null;
    private Rectangle2D world;

    private Mundo() {
        this.world = new Rectangle2D.Double(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
    }

    /*  
        Getters
    */
    public static Mundo getInstance() {
        if (INSTANCE == null) {
            synchronized (Mundo.class) {
                if(INSTANCE == null)
                    INSTANCE = new Mundo();
            }
        }

        return INSTANCE;
    }

    public double getWidth() {
        return this.world.getWidth();
    }

    public double getHeight() {
        return this.world.getHeight();
    }

    /* 
        Setters
    */
    public void setWorldLimits(int w, int h) {
        world = new Rectangle2D.Double(0, 0, w, h);
    }

    public void display(Graphics2D g) {
        g.draw(world);
    }
}