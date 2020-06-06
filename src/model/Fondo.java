package model;

import java.awt.image.BufferedImage;

public class Fondo extends ObjetoGrafico {
	public Fondo(String filename) {
        super(filename);
        setPosition(0, 0);
    }
    
    public BufferedImage getBG() {
        return image;
    }
}