package model.bomberman;

import java.awt.image.BufferedImage;

import model.ObjetoGrafico;

public class Fondo extends ObjetoGrafico {
	public Fondo(String filename) {
        super(filename);
        setPosition(0, 0);
    }
    
    public BufferedImage getBG() {
        return image;
    }

    public void setImage(String image) {
        this.update(image);
    }
}