package model.bonus;

import java.awt.Graphics2D;
import java.util.Vector;

import model.Heroe;
import model.enemigo.Enemigo;
import model.interfaces.ObjetoCambianteEstatico;

public interface Bonus extends ObjetoCambianteEstatico {
    /*
     * Constantes para cada bonus.
     */
    public final static int VIDA_EXTRA = 0;
    public final static int BOMBA_EXTRA = 1;
    public final static int VELOCIDAD = 2;
    public final static int DETONADOR = 3;
    public final static int RANGO_EXPLOSION = 4;
    public final static int SALTO_BOMBA = 5;
    public final static int PUERTA = 6;

    /*
     * Activacion y desactivacion de los bonus.
     */
    public void activateBonus(Heroe h);

    public void deactivateBonus(Heroe h);

    /*
     * Creacion de enemigos en caso de ser golpeados por la bomba.
     */
    public Vector<Enemigo> spawnEnemies();

    public boolean wasHit();

    public void setWasHit(boolean WAS_HIT);

    /*
     * Metododos de ObjetoGrafico.
     */
    public void draw(Graphics2D g);

    public void setPosition(double x, double y);

    public void update(String fileName);

    public double getX();

    public double getY();

    /*
     * Metodos de ObjetoGraficoEstatico.
     */
    @Override
    public void changeObject();

    @Override
    public void hit();
}