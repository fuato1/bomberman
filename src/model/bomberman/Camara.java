package model.bomberman;

import model.Heroe;
import model.Reloj;
import model.jugador.Puntaje;

public class Camara {
	private double x, y;
	private double resX;

    public Camara(double x, double y) {
    	this.x = x;
    	this.y = y;
    }

	public void followHero(Heroe h) {
		Mundo w = Mundo.getInstance();

		x = resX/2 - h.getX();
		if(x > 0)
			x = 0;

		if(x < resX - w.getWidth())
			x = resX - w.getWidth();
	}

	public void followUpperPanel(Reloj c, Puntaje p) {
		if(x < 0) {
			c.setX(resX/2);
			p.setX(resX-32);
		}
		else {
			c.setX(32);
			p.setX(500);
		}
	}

	/*
		Getters
	*/
	public double getX() {
    	return x;
    }

    public double getY() {
    	return y;
    }

	/*
		Setters
	*/
	public void setVisibleRegion(double x) {
		resX = x;
	}
}