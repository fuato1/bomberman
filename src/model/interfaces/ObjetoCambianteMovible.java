package model.interfaces;

public interface ObjetoCambianteMovible {
    /*
     * Esta interfaz se utiliza para los objetos graficos que se mueven por el mapa
     * (ej: el heroe y los enemigos).
     */

    public void changeObject(String dir);

    public void kill();
}