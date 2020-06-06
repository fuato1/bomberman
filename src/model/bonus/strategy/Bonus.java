package model.bonus.strategy;
import java.awt.Graphics2D;

public interface Bonus {
    public final static int VIDA_EXTRA = 0;
    public final static int BOMBA_EXTRA = 1;
    public final static int VELOCIDAD = 2;
    public final static int DETONADOR = 3;
    public final static int RANGO_EXPLOSION = 4;
    public final static int SALTO_BOMBA = 5;
    public final static int PUERTA = 6;

    public void activateBonus();
    public void bonusHit();
    public void draw(Graphics2D g);
    public void setPosition(double x, double y);
    public void update(String fileName);
}