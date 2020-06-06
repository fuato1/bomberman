package model.explosion;

import java.util.HashMap;
import java.util.Vector;

import model.Heroe;
import model.ObjetoGrafico;
import model.bonus.strategy.Bonus;
import model.enemigo.Enemigo;
import model.factory.OGAbstractFactory;
import model.factory.OGFactoryProducer;
import model.pared.ParedLadrillo;

public class Explosion extends ObjetoGrafico {
    /*
        direccion
    */
    public static int EXPLOSION_UP = 1;
    public static int EXPLOSION_DOWN = 2;
    public static int EXPLOSION_LEFT = 3;
    public static int EXPLOSION_RIGHT = 4;

    HashMap<String, Vector<ParteExplosion>> explosion;

    public Explosion(String filename) {
        super(filename);

        explosion = new HashMap<String, Vector<ParteExplosion>>(4);

        OGAbstractFactory expFactory = OGFactoryProducer.getFactory(OGAbstractFactory.EXPLOSION_FACTORY);

        explosion.put("up", expFactory.getMidExplosions(Explosion.EXPLOSION_UP, 1));
        explosion.get("up").add(expFactory.getTipExplosion(Explosion.EXPLOSION_UP));

        explosion.put("down", expFactory.getMidExplosions(Explosion.EXPLOSION_UP, 1));
        explosion.get("down").add(expFactory.getTipExplosion(Explosion.EXPLOSION_DOWN));

        explosion.put("left", expFactory.getMidExplosions(Explosion.EXPLOSION_LEFT, 1));
        explosion.get("left").add(expFactory.getTipExplosion(Explosion.EXPLOSION_LEFT));

        explosion.put("right", expFactory.getMidExplosions(Explosion.EXPLOSION_LEFT, 1));
        explosion.get("right").add(expFactory.getTipExplosion(Explosion.EXPLOSION_RIGHT));
    }
    
    public void herirJugador(Heroe hero) {
        
    }

    public void destroyWall(ParedLadrillo pl) {
        
    }

    public void destroyEnemy(Enemigo e) {
        
    }

    public void hitBonus(Bonus bn) {
        
    }

    public HashMap<String, Vector<ParteExplosion>> getExplosion() {
        return explosion;
    }
}