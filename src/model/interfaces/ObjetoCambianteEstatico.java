package model.interfaces;

public interface ObjetoCambianteEstatico {
    /*
     * Esta interfaz se utiliza para los objetos graficos que no se mueven por el
     * mapa (ej: paredes, bonus, bombas y explosiones).
     */

    public void changeObject();

    public void hit();
}