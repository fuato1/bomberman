package model.explosion;

import java.util.HashMap;
import java.util.Vector;

import model.ObjetoGrafico;
import model.factory.OGAbstractFactory;
import model.factory.OGFactoryProducer;
import model.interfaces.ObjetoCambianteEstatico;

public class Explosion extends ObjetoGrafico implements ObjetoCambianteEstatico {
    /*
     * direcciones de la explosion
     */
    public final static int EXPLOSION_UP = 1;
    public final static int EXPLOSION_DOWN = 2;
    public final static int EXPLOSION_LEFT = 3;
    public final static int EXPLOSION_RIGHT = 4;

    private boolean vanishedExplosion = false;
    private int range = 1;

    HashMap<String, Vector<ParteExplosion>> explosion;

    public Explosion(String filename, int range) {
        super(filename);
        this.range = range;

        explosion = new HashMap<String, Vector<ParteExplosion>>(4);

        OGAbstractFactory factory = OGFactoryProducer.getFactory();

        explosion.put("up", factory.getExplosionBranch(EXPLOSION_UP, range));
        explosion.put("down", factory.getExplosionBranch(EXPLOSION_DOWN, range));
        explosion.put("left", factory.getExplosionBranch(EXPLOSION_LEFT, range));
        explosion.put("right", factory.getExplosionBranch(EXPLOSION_RIGHT, range));
    }

    /*
     * Getters
     */
    public HashMap<String, Vector<ParteExplosion>> getExplosion() {
        return explosion;
    }

    public boolean isVanished() {
        return this.vanishedExplosion;
    }

    /*
     * Setters
     */
    @Override
    public void setPosition(double x, double y) {
        super.setPosition(x, y);
        setExplosion();
    }

    private void setExplosion() {
        for (String dir : explosion.keySet()) {
            if (dir == "up") {
                for (int i = 0; i < explosion.get(dir).size(); i++) {
                    explosion.get(dir).get(i).setPosition(getX(), getY() - 28 * (i + 1));
                }
            } else if (dir == "down") {
                for (int i = 0; i < explosion.get(dir).size(); i++) {
                    explosion.get(dir).get(i).setPosition(getX(), getY() + 28 * (i + 1));
                }
            } else if (dir == "left") {
                for (int i = 0; i < explosion.get(dir).size(); i++) {
                    explosion.get(dir).get(i).setPosition(getX() - 28 * (i + 1), getY());
                }
            } else if (dir == "right") {
                for (int i = 0; i < explosion.get(dir).size(); i++) {
                    explosion.get(dir).get(i).setPosition(getX() + 28 * (i + 1), getY());
                }
            }
        }
    }

    /*
     * cambio de sprites
     */
    @Override
    public void changeObject() {
        if (ANIMATION_COUNTER > 60) {
            ANIMATION_COUNTER = 60;
            vanishedExplosion = true;
        } else if (ANIMATION_COUNTER >= 50) {
            for (String dir : explosion.keySet()) {
                for (ParteExplosion pe : explosion.get(dir)) {
                    pe.changeObject();
                }
            }
            update("/imagenes/null.png");

            ANIMATION_COUNTER++;
        } else {
            for (int i = 10; i <= 40; i += 10) {
                if (i - 10 <= ANIMATION_COUNTER && ANIMATION_COUNTER < i)
                    update("/imagenes/explosiones/center/center_exp-" + (50 - i) / 10 + ".png");
            }

            for (String dir : explosion.keySet()) {
                for (ParteExplosion pe : explosion.get(dir)) {
                    pe.changeObject();
                }
            }

            ANIMATION_COUNTER++;
        }
    }

    @Override
    public void hit() {
        // TODO Auto-generated method stub

    }
}