package model;

public class Camara {
	private double x, y;
	private double resX;

    public Camara(double x, double y) {
    	this.x = x;
    	this.y = y;
    }

	public void followHero(Heroe obj) {
		Mundo w = Mundo.getInstance();

		x = -obj.getX() + resX/2;
		if(x > 0)
			x = 0;

		if(x < -(w.getWidth() - resX))
			x = -(w.getWidth() - resX);
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

    public void setX(double x) {
    	this.x = x;
	}
	
    public void setY(double y) {
    	this.y = y;
	}
}