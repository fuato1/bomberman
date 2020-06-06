package model;

import java.awt.*;
import java.awt.geom.*;

public class Mundo {
    public static final int WORLD_WIDTH = 640;
    public static final int WORLD_HEIGHT = 480;

    private static Mundo INSTANCE = null;
    private Rectangle2D world;

    private Mundo() {
        world = new Rectangle2D.Double(0, 0, WORLD_WIDTH, WORLD_HEIGHT);
    }

    private static void createInstance() {
        if(INSTANCE == null) {
            synchronized (Mundo.class) {
                if(INSTANCE == null)
                    INSTANCE = new Mundo();
            }
        }
    }

    public static Mundo getInstance() {
        if (INSTANCE == null)
            createInstance();

        return INSTANCE;
    }

    public boolean contains(int x, int y) {
        return world.contains(x, y);
    }

    public boolean contains(int x, int y, int w, int h) {
        return world.contains(x, y, w, h);
    }

    public void setWorldLimits(int w, int h) {
        world = new Rectangle2D.Double(0, 0, w, h);
    }

    public Rectangle2D getRectangle() {
        return this.world;
    }

    public float getWidth() {
        return (float) world.getWidth();
    }

    public float getHeight() {
        return (float) world.getHeight();
    }

    public void display(Graphics2D g) {
        g.draw(world);
    }
}