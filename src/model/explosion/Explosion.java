package model.explosion;

import java.util.HashMap;
import java.util.Vector;

import model.ObjetoGrafico;
import model.bomberman.Bomberman;
import model.interfaces.ObjetoCambianteEstatico;

public class Explosion extends ObjetoGrafico implements ObjetoCambianteEstatico {
    /*
     * Constantes para identificar a las direcciones de las explosion.
     */
    public final static int EXPLOSION_UP = 1;
    public final static int EXPLOSION_DOWN = 2;
    public final static int EXPLOSION_LEFT = 3;
    public final static int EXPLOSION_RIGHT = 4;

    /*
     * Variable de estado para controlar cuando la explosion desaparecio.
     */
    private boolean vanishedExplosion = false;

    /*
     * Rango de la explosion.
     */
    private int range = 1;

    /*
     * Partes de la explosion por direcciones.
     */
    HashMap<String, Vector<ParteExplosion>> explosion;

    public Explosion(String filename, int range) {
        super(filename);
        this.range = range;

        explosion = new HashMap<String, Vector<ParteExplosion>>(4);

        explosion.put("up", Bomberman.factory.getExplosionBranch(EXPLOSION_UP, range));
        explosion.put("down", Bomberman.factory.getExplosionBranch(EXPLOSION_DOWN, range));
        explosion.put("left", Bomberman.factory.getExplosionBranch(EXPLOSION_LEFT, range));
        explosion.put("right", Bomberman.factory.getExplosionBranch(EXPLOSION_RIGHT, range));
    }

    /*
     * Getters.
     */
    public HashMap<String, Vector<ParteExplosion>> getExplosion() {
        return explosion;
    }

    public boolean isVanished() {
        return this.vanishedExplosion;
    }

    /*
     * Setters.
     */
    @Override
    public void setPosition(double x, double y) {
        super.setPosition(x, y);
        setExplosion();
    }

    /*
     * Metodo para ubicar a las partes de explosion desde la posicion central.
     */
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
     * Metodods de ObjetoCambianteEstatico. Animaciones de moviviento y golpe de
     * explosion.
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
    }
}