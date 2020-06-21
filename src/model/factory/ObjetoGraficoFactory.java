package model.factory;

import java.util.Vector;

import model.Bomba;
import model.bonus.BombaExtra;
import model.bonus.Detonador;
import model.bonus.Puerta;
import model.bonus.RangoExplosion;
import model.bonus.SaltoBomba;
import model.bonus.Velocidad;
import model.bonus.VidaExtra;
import model.bonus.strategy.Bonus;
import model.enemigo.Enemigo;
import model.enemigo.EnemigoAzul;
import model.enemigo.EnemigoRosa;
import model.explosion.Explosion;
import model.explosion.ParteExplosion;
import model.pared.Pared;
import model.pared.ParedLadrillo;

public class ObjetoGraficoFactory implements OGAbstractFactory {

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

    @Override
    public Explosion getExplosion() {
        return new Explosion("/imagenes/explosiones/center/center_exp-1.png");
    }

    @Override
    public Vector<ParteExplosion> getTipExplosion(int id) {
        Vector<ParteExplosion> tipExp = new Vector<ParteExplosion>(1);

        if (id == Explosion.EXPLOSION_UP) {
            tipExp.add(new ParteExplosion("/imagenes/explosiones/up/up_exp-1.png", "up"));
            return tipExp;
        }
        if (id == Explosion.EXPLOSION_DOWN) {
            tipExp.add(new ParteExplosion("/imagenes/explosiones/down/down_exp-1.png", "down"));
            return tipExp;
        }
        if (id == Explosion.EXPLOSION_LEFT) {
            tipExp.add(new ParteExplosion("/imagenes/explosiones/left/left_exp-1.png", "left"));
            return tipExp;
        }
        if (id == Explosion.EXPLOSION_RIGHT) {
            tipExp.add(new ParteExplosion("/imagenes/explosiones/right/right_exp-1.png", "right"));
            return tipExp;
        }

        return null;
    }

    @Override
    public Vector<ParteExplosion> getMidExplosions(int id, int n) {
        Vector<ParteExplosion> midExp = new Vector<ParteExplosion>(n);

        if (id == Explosion.EXPLOSION_UP) {
            for (int i = 0; i < n; i++) {
                midExp.add(new ParteExplosion("/imagenes/explosiones/vertical/vertical_exp-1.png", "vertical"));
            }
        }
        if (id == Explosion.EXPLOSION_LEFT) {
            for (int i = 0; i < n; i++) {
                midExp.add(new ParteExplosion("/imagenes/explosiones/horizontal/horizontal_exp-1.png", "horizontal"));
            }
        }

        return midExp;
    }

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

    @Override
    public Bomba getBomba() {
        return new Bomba();
    }
}