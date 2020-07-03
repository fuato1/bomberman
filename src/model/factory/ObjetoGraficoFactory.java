package model.factory;

import java.util.Vector;

import model.Bomba;
import model.bonus.BombaExtra;
import model.bonus.Bonus;
import model.bonus.Detonador;
import model.bonus.Puerta;
import model.bonus.RangoExplosion;
import model.bonus.SaltoBomba;
import model.bonus.Velocidad;
import model.bonus.VidaExtra;
import model.enemigo.Enemigo;
import model.enemigo.EnemigoAzul;
import model.enemigo.EnemigoRosa;
import model.explosion.Explosion;
import model.explosion.ParteExplosion;
import model.pared.Pared;
import model.pared.ParedLadrillo;

public class ObjetoGraficoFactory implements OGAbstractFactory {

    /*
     * Creacion de bonus.
     */
    @Override
    public Bonus getBonus(int id) {
        if (id == Bonus.VIDA_EXTRA)
            return new VidaExtra();
        if (id == Bonus.BOMBA_EXTRA)
            return new BombaExtra();
        if (id == Bonus.VELOCIDAD)
            return new Velocidad();
        if (id == Bonus.DETONADOR)
            return new Detonador();
        if (id == Bonus.RANGO_EXPLOSION)
            return new RangoExplosion();
        if (id == Bonus.SALTO_BOMBA)
            return new SaltoBomba();
        if (id == Bonus.PUERTA)
            return new Puerta();

        return null;
    }

    /*
     * Creacion de paredes.
     */
    @Override
    public Pared getPared(int id) {
        if (id == Pared.PARED_PIEDRA)
            return new Pared("/imagenes/paredes/pared_piedra.png");
        if (id == Pared.PARED_LADRILLO)
            return new ParedLadrillo("/imagenes/paredes/pared_ladrillo.png");

        return null;
    }

    @Override
    public Vector<Pared> getParedes(int id, int n) {
        Vector<Pared> walls = new Vector<Pared>(n);

        for (int i = 0; i < n; i++) {
            walls.add(getPared(id));
        }

        return walls;
    }

    /*
     * Creacion de explosiones.
     */
    @Override
    public Explosion getExplosion(int range) {
        return new Explosion("/imagenes/explosiones/center/center_exp-1.png", range);
    }

    @Override
    public Vector<ParteExplosion> getExplosionBranch(int id, int range) {
        Vector<ParteExplosion> expBranch = new Vector<ParteExplosion>(range);

        if (id == Explosion.EXPLOSION_UP) {
            for (int i = 0; i < range - 1; i++) {
                expBranch.add(new ParteExplosion("/imagenes/explosiones/vertical/vertical_exp-1.png", "vertical"));
            }

            expBranch.add(new ParteExplosion("/imagenes/explosiones/up/up_exp-1.png", "up"));
            return expBranch;
        }
        if (id == Explosion.EXPLOSION_DOWN) {
            for (int i = 0; i < range - 1; i++) {
                expBranch.add(new ParteExplosion("/imagenes/explosiones/vertical/vertical_exp-1.png", "vertical"));
            }

            expBranch.add(new ParteExplosion("/imagenes/explosiones/down/down_exp-1.png", "down"));
            return expBranch;
        }
        if (id == Explosion.EXPLOSION_LEFT) {
            for (int i = 0; i < range - 1; i++) {
                expBranch
                        .add(new ParteExplosion("/imagenes/explosiones/horizontal/horizontal_exp-1.png", "horizontal"));
            }

            expBranch.add(new ParteExplosion("/imagenes/explosiones/left/left_exp-1.png", "left"));
            return expBranch;
        }
        if (id == Explosion.EXPLOSION_RIGHT) {
            for (int i = 0; i < range - 1; i++) {
                expBranch
                        .add(new ParteExplosion("/imagenes/explosiones/horizontal/horizontal_exp-1.png", "horizontal"));
            }

            expBranch.add(new ParteExplosion("/imagenes/explosiones/right/right_exp-1.png", "right"));
            return expBranch;
        }

        return null;
    }

    /*
     * Creacion de enemigos.
     */
    @Override
    public Enemigo getEnemigo(int id) {
        if (id == Enemigo.ENEMIGO_AZUL)
            return new EnemigoAzul("/imagenes/enemigos/azul/left/enemigo_azul-1.png");
        if (id == Enemigo.ENEMIGO_ROSA)
            return new EnemigoRosa("/imagenes/enemigos/rosa/left/enemigo_rosa-1.png");

        return null;
    }

    @Override
    public Vector<Enemigo> getEnemigos(int id, int n) {
        Vector<Enemigo> enemies = new Vector<Enemigo>(n);

        for (int i = 0; i < n; i++) {
            enemies.add(getEnemigo(id));
        }

        return enemies;
    }

    /*
     * Creacion de la bomba.
     */
    @Override
    public Bomba getBomba() {
        return new Bomba();
    }
}