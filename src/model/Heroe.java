package model;

import java.util.Vector;

import model.bomberman.Bomberman;
import model.bomberman.Mundo;
import model.bonus.Bonus;
import model.interfaces.ObjetoCambianteMovible;

public class Heroe extends ObjetoGrafico implements ObjetoCambianteMovible {
    /*
     * Variables para la cantidad de vidas, velocidad de desplazamiento y bonus que
     * estan activos.
     */
    private int life;
    private double HERO_DISPLACEMENT = 100.0;
    private Vector<Bonus> bonus;

    /*
     * Variables de estado para control de colisiones y muerte del heroe.
     */
    private boolean IS_OVER_WALL = false;
    private boolean IS_NEXT_TO_WALL = false;
    private boolean IS_DEAD = false;

    /*
     * Variables de estado que se modifican con los bonus que el heroe va
     * consiguiendo.
     */
    private int MAX_BOMBS = 1;
    private boolean HAS_DETONATOR = false;
    private boolean CAN_JUMP_BOMBS = false;
    private int EXPLOSION_RANGE = 1;
    private boolean HAS_REACHED_DOOR = false;

    public Heroe() {
        super("/imagenes/bomberman/down/b_down-1.png");
        this.life = 3;
        this.bonus = new Vector<Bonus>(0);
    }

    /*
     * Getters.
     */
    public int getLife() {
        return life;
    }

    public double getHeroDisplacement() {
        return HERO_DISPLACEMENT;
    }

    public int getMaxBombs() {
        return MAX_BOMBS;
    }

    public boolean hasDetonator() {
        return HAS_DETONATOR;
    }

    public boolean canJumpBombs() {
        return CAN_JUMP_BOMBS;
    }

    public int getExplosionRange() {
        return EXPLOSION_RANGE;
    }

    public boolean hasReachedDoor() {
        return HAS_REACHED_DOOR;
    }

    public boolean isOverWall() {
        return IS_OVER_WALL;
    }

    public boolean isNextToWall() {
        return IS_NEXT_TO_WALL;
    }

    public boolean isDead() {
        return IS_DEAD;
    }

    public Vector<Bonus> getBonus() {
        return bonus;
    }

    /*
     * Setters.
     */
    public void setLife(int life) {
        this.life = life;
    }

    public void setHeroDisplacement(double HERO_DISPLACEMENT) {
        this.HERO_DISPLACEMENT = HERO_DISPLACEMENT;
    }

    public void setMaxBombs(int MAX_BOMBS) {
        this.MAX_BOMBS = MAX_BOMBS;
    }

    public void setHasDetonator(boolean HAS_DETONATOR) {
        this.HAS_DETONATOR = HAS_DETONATOR;
    }

    public void setCanJumpBombs(boolean CAN_JUMP_BOMBS) {
        this.CAN_JUMP_BOMBS = CAN_JUMP_BOMBS;
    }

    public void setExplosionRange(int EXPLOSION_RANGE) {
        this.EXPLOSION_RANGE = EXPLOSION_RANGE;
    }

    public void setHasReachedDoor(boolean HAS_REACHED_DOOR) {
        this.HAS_REACHED_DOOR = HAS_REACHED_DOOR;
    }

    /*
     * Metodos del heroe para agregar bonus activos y quitarlos.
     */
    public void consumeBonus(Bonus b) {
        if (!(b == null)) {
            bonus.add(b);
            b.activateBonus(this);
        }
    }

    private void deactivateBonus() {
        for (Bonus b : bonus) {
            b.deactivateBonus(this);
        }
        bonus.removeAllElements();
    }

    /*
     * Metodo para que el heroe ponga una bomba
     */
    public Bomba setBomb() {
        checkHorizontalMovement();
        checkHorizontalMovement();

        Bomba b = Bomberman.factory.getBomba();

        b.setPosition(checkHorizontalBombPosition(), checkVerticalBombPosition());

        return b;
    }

    /*
     * checkHorizontalBombPosition: para control de posicion horizontal de la bomba.
     * 
     * checkVerticalBombPosition: para control de posicion vertical de la bomba.
     */
    private int checkHorizontalBombPosition() {
        double left, right;

        for (int i = 0; i < 25; i++) {
            if (32 * (i + 1) <= getX() + 16 && getX() + 16 <= 32 * (i + 2)) {
                left = 32 * (i + 1) - getX();
                right = 32 * (i + 2) - getX();

                if (left < right) {
                    return 32 * (i + 1);
                } else if (left >= right) {
                    return 32 * (i + 2);
                }
            }
        }

        return 0;
    }

    private int checkVerticalBombPosition() {
        double up, down;

        for (int i = 0; i < 13; i++) {
            if (105 + 32 * i <= getY() + 16 && getY() + 16 <= 137 + 32 * i) {
                up = 105 + 32 * i - getY();
                down = 137 + 32 * i - getY();

                if (up < down) {
                    return 105 + 32 * i;
                } else if (up >= down) {
                    return 137 + 32 * i;
                }
            }
        }

        return 0;
    }

    /*
     * Metodos de ObjetoCambianteEstatico. Animaciones de moviviento y muerte.
     */
    @Override
    public void changeObject(String dir) {
        if (ANIMATION_COUNTER > 40) {
            ANIMATION_COUNTER = 0;
        } else {
            if (ANIMATION_COUNTER < 10)
                update("/imagenes/bomberman/" + dir + "/b_" + dir + "-1.png");
            if (ANIMATION_COUNTER >= 20)
                update("/imagenes/bomberman/" + dir + "/b_" + dir + "-2.png");
            if (ANIMATION_COUNTER >= 30)
                update("/imagenes/bomberman/" + dir + "/b_" + dir + "-3.png");

            ANIMATION_COUNTER++;
        }
    }

    @Override
    public void kill() {
        if (ANIMATION_COUNTER > 80) {
            ANIMATION_COUNTER = 80;
            IS_DEAD = false;
            setPosition(Bomberman.LEFT_WALL_LIMIT, Bomberman.UPPER_WALL_LIMIT);
            update("/imagenes/bomberman/down/b_down-1.png");
        } else if (ANIMATION_COUNTER > 70) {
            update("/imagenes/null.png");
            ANIMATION_COUNTER++;
        } else {
            for (int i = 10; i <= 70; i += 10) {
                if (i - 10 <= ANIMATION_COUNTER && ANIMATION_COUNTER < i) {
                    update("/imagenes/bomberman/eliminado/bomberman_M" + i / 10 + ".png");
                }
            }

            ANIMATION_COUNTER++;
        }
    }

    /*
     * Control para las colisiones con las paredes de piedra y movimiento por el
     * mapa. Estos metodos modifican IS_OVER_WALL y IS_NEXT_TO_WALL.
     */
    private void checkVerticalMovement() {
        for (int i = 0; i < 11; i++) {
            if ((64 + 64 * i) + 1 < getX() + 30 && getX() < (96 + 64 * i) - 1) {
                IS_OVER_WALL = true;
            }
        }
    }

    private void checkHorizontalMovement() {
        for (int i = 0; i < 5; i++) {
            if ((137 + 64 * i) + 1 < getY() + 30 && getY() < (169 + 64 * i) - 1) {
                IS_NEXT_TO_WALL = true;
            }
        }
    }

    /*
     * Metodo para que mata al heroe.
     */
    public void stop() {
        IS_DEAD = true;
        life--;
        deactivateBonus();
    }

    /*
     * Movimiento del heroe.
     */
    public void up(double delta) {
        checkVerticalMovement();

        if (!(getY() < Bomberman.UPPER_WALL_LIMIT + 2) && !IS_OVER_WALL) {
            IS_NEXT_TO_WALL = false;
            setPosition(getX(), getY() - HERO_DISPLACEMENT * delta);
        }
    }

    public void down(double delta) {
        checkVerticalMovement();

        if (!(getY() > Bomberman.LOWER_WALL_LIMIT - 33) && !IS_OVER_WALL) {
            IS_NEXT_TO_WALL = false;
            setPosition(getX(), getY() + HERO_DISPLACEMENT * delta);
        }
    }

    public void left(double delta) {
        checkHorizontalMovement();

        if (!(getX() < 34) && !IS_NEXT_TO_WALL) {
            IS_OVER_WALL = false;
            setPosition(getX() - HERO_DISPLACEMENT * delta, getY());
        }
    }

    public void right(double delta) {
        Mundo w = Mundo.getInstance();

        checkHorizontalMovement();

        if (!((getX() + getWidth()) > w.getWidth() - 34) && !IS_NEXT_TO_WALL) {
            IS_OVER_WALL = false;
            setPosition(getX() + HERO_DISPLACEMENT * delta, getY());
        }
    }
}