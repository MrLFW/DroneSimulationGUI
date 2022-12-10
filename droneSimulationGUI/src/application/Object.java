package application;

abstract class Object {

	private double x, y, radius;

	/**
	 * returns true if object is inside the bounds of the arena, returns false if
	 * it's not.
	 * 
	 * @param mc
	 */
	public boolean inBounds(MyCanvas mc) {
		if (getX() - getRadius() <= 0 || getX() + getRadius() > mc.getXCanvasSize()
				|| (getY() - getRadius() <= 0 || getY() + getRadius() > mc.getYCanvasSize()))
			return false; // returns false if drone outside arena
		return true;

	}

	/**
	 * checks if object is touching other object
	 * 
	 * @param x1
	 * @param y1
	 * @param r1
	 * @return
	 */
	public boolean isHere(double x1, double y1, double r1) {
		return Math.hypot(getX() - x1, getY() - y1) <= (getRadius() + r1);
	}

	/**
	 * set drone to position x,y
	 * 
	 * @param newX
	 * @param newY
	 */
	public void setXY(double newX, double newY) {
		setX(newX);
		setY(newY);
	}

	/**
	 * sets all the objects values
	 * 
	 * @param x1
	 * @param y1
	 * @param rad1
	 */
	public void setAll(double x1, double y1, double rad1) {
		setX(x1);
		setY(y1);
		setRadius(rad1);
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * @return the radius
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * @param radius the radius to set
	 */
	public void setRadius(double radius) {
		this.radius = radius;
	}

}
