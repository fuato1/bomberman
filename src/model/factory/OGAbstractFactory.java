package model.factory;

import java.util.Vector;

import model.Bomba;
import model.bonus.Bonus;
import model.enemigo.Enemigo;
import model.explosion.Explosion;
import model.explosion.ParteExplosion;
import model.pared.Pared;

public interface OGAbstractFactory {
    /*
     * Constantes para cada factory individual.
     */
    public static int BONUS_FACTORY = 0;
    public static int PARED_FACTORY = 1;
    public static int EXPLOSION_FACTORY = 2;
    public static int ENEMIGO_FACTORY = 3;
    public static int BOMBA_FACTORY = 4;

    /*
     * Metodos de la fabrica para Bonus.
     */
    public Bonus getBonus(int id);

    /*
     * Metodos de la fabrica para Pared.
     */
    public Pared getPared(int id);

    public Vector<Pared> getParedes(int id, int n);

    /*
     * Metodos de la fabrica para Explosion.
     */
    public Explosion getExplosion(int range);

    public Vector<ParteExplosion> getExplosionBranch(int id, int range);

    /*
     * Metodos de la fabrica para Enemigo.
     */
    public Enemigo getEnemigo(int id);

    public Vector<Enemigo> getEnemigos(int id, int n);

    /*
     * Metodos de la fabrica para Bomba.
     */
    public Bomba getBomba();
}