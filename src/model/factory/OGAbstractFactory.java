package model.factory;

import java.util.Vector;

import model.Bomba;
import model.bonus.strategy.Bonus;
import model.enemigo.Enemigo;
import model.explosion.Explosion;
import model.explosion.ParteExplosion;
import model.pared.Pared;

public interface OGAbstractFactory {
    public static int BONUS_FACTORY = 0;
    public static int PARED_FACTORY = 1;
    public static int EXPLOSION_FACTORY = 2;
    public static int ENEMIGO_FACTORY = 3;
    public static int BOMBA_FACTORY = 4;

    public Bomba getBomba();

    public Bonus getBonus(int id);

    public Pared getPared(int id);
    public Vector<Pared> getParedes(int id, int n);

    public Explosion getExplosion();
    public ParteExplosion getTipExplosion(int id);
    public Vector<ParteExplosion> getMidExplosions(int id, int n);
    
    public Enemigo getEnemigo(int id);
    public Vector<Enemigo> getEnemigos(int id, int n);
}