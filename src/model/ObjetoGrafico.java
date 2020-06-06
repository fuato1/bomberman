package model;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class ObjetoGrafico {
    protected BufferedImage image; // sprite del OG
    protected Point2D.Double position = new Point2D.Double(); // posicion

    public ObjetoGrafico(String filename) {
        try {
            this.image = ImageIO.read(getClass().getResource(filename));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /*
        Getters 
    */
    public int getWidth() {
		return this.image.getWidth();
    }
    
	public int getHeight() {
		return this.image.getHeight();
    }
    
    public double getX() {
		return this.position.getX();
	}

	public double getY() {
		return this.position.getY();
    }

    /*
        Setters
    */
	public void setPosition(double x, double y) {
		this.position.setLocation(x, y);
	}

    public void draw(Graphics2D g) {
        g.drawImage(this.image, (int) this.position.getX(), (int) this.position.getY(), null);
    }

    public void update(String fileName) {
        try {
            this.image = ImageIO.read(getClass().getResource(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}