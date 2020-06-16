package model.explosion;

import java.util.HashMap;
import java.util.Vector;

import model.ObjetoGrafico;
import model.factory.OGAbstractFactory;
import model.factory.OGFactoryProducer;
import model.interfaces.ObjetoCambianteEstatico;

public class Explosion extends ObjetoGrafico implements ObjetoCambianteEstatico {
    /*
        direcciones de la explosion
    */
    public final static int EXPLOSION_UP = 1;
    public final static int EXPLOSION_DOWN = 2;
    public final static int EXPLOSION_LEFT = 3;
    public final static int EXPLOSION_RIGHT = 4;

    private boolean vanishedExplosion = false;

    HashMap<String, Vector<ParteExplosion>> explosion;

    public Explosion(String filename) {
        super(filename);

        explosion = new HashMap<String, Vector<ParteExplosion>>(4);

        OGAbstractFactory factory = OGFactoryProducer.getFactory();

        explosion.put("up", factory.getTipExplosion(EXPLOSION_UP));
        explosion.put("down", factory.getTipExplosion(EXPLOSION_DOWN));
        explosion.put("left", factory.getTipExplosion(EXPLOSION_LEFT));
        explosion.put("right", factory.getTipExplosion(EXPLOSION_RIGHT));
    }

    /*
        Getters
    */
    public HashMap<String, Vector<ParteExplosion>> getExplosion() {
        return explosion;
    }

    public boolean isVanished() {
        return this.vanishedExplosion;
    }

    /*
        Setters
    */
    @Override
    public void setPosition(double x, double y) {
        super.setPosition(x, y);
        setExplosion();
    }

    private void setExplosion() {
        for (String dir : explosion.keySet()) {
            if(dir == "up") {
                for (int i = 0; i < explosion.get(dir).size(); i++) {
                    explosion.get(dir).get(i).setPosition(getX(), getY() - 28*(i+1));
                }
            }
            else if(dir == "down") {
                for (int i = 0; i < explosion.get(dir).size(); i++) {
                    explosion.get(dir).get(i).setPosition(getX(), getY() + 28*(i+1));
                }
            }
            else if(dir == "left") {
                for (int i = 0; i < explosion.get(dir).size(); i++) {
                    explosion.get(dir).get(i).setPosition(getX() - 28*(i+1), getY());
                }
            }
            else if(dir == "right") {
                for (int i = 0; i < explosion.get(dir).size(); i++) {
                    explosion.get(dir).get(i).setPosition(getX() + 28*(i+1), getY());
                }
            }
        }
    }

    /*
        cambio de sprites
    */
    @Override
    public void changeObject() {
        if(this.ANIMATION_COUNTER > 60) {
            this.ANIMATION_COUNTER = 60;
            this.vanishedExplosion = true;
        }
        else if(this.ANIMATION_COUNTER >= 50) {
            for (String dir : explosion.keySet()) {
                for (ParteExplosion pe : explosion.get(dir)) {
                    pe.changeObject();
                }
            }
            this.update("/imagenes/null.png");
            
            this.ANIMATION_COUNTER++;
        }
        else {
            for (int i = 10; i <= 40; i += 10) {
                if(i-10 <= this.ANIMATION_COUNTER && this.ANIMATION_COUNTER < i)
                    this.update("/imagenes/explosiones/center/center_exp-" + (50-i)/10 + ".png");
            }
    
            for (String dir : explosion.keySet()) {
                for (ParteExplosion pe : explosion.get(dir)) {
                    pe.changeObject();
                }
            }

            this.ANIMATION_COUNTER++;
        }
    }
}