package application;

import java.util.List;
import java.util.Random;

/**
 * @author luke
 *
 */
public class Drone extends MoveableObject {

	// position and size of drone + angle traveling and speed

	/**
	 * Constructor
	 */
	public Drone() {
		setX(100);
		setY(180);
		setRadius(20);
		Random random = new Random();
		setAngle(360 * random.nextDouble());
		setSpeed(1);
	}

	/**
	 * function to check where drone is, and adjust angle if is hitting side of
	 * canvas
	 * 
	 * @param mc
	 */
	public void checkDrone(MyCanvas mc, List<Bedrock> bedrocks) {
		for (Bedrock aBedrock : bedrocks)
			if (aBedrock.isHere(getX(), getY(), getRadius()) == true) {
				// Random random = new Random();
				double angle = (double) Math.toDegrees(Math.atan2(getY() - aBedrock.getY(), getX() - aBedrock.getX()));
				setAngle(angle);
			}
		if (getX() - getRadius() <= 0) {			
			setX(0 + getRadius());
			setAngle(180 - getAngle());
		}

		if (getX() + getRadius() > mc.getXCanvasSize()) {
			setX(mc.getXCanvasSize() - getRadius());
			setAngle(180 - getAngle());
		}

		// if drone hit (tried to go through) left or right walls, set mirror angle,
		// being 180-angle

		if (getY() - getRadius() <= 0) {			
			setY(0 + getRadius());
			setAngle(-getAngle());
		}
		if (getY() + getRadius() > mc.getYCanvasSize()) {
			setY(mc.getYCanvasSize() - getRadius());
			setAngle(-getAngle());
		}
		// if drone hit (tried to go through) top or bottom walls, set mirror angle,
		// being -angle
	}

	/**
	 * move the drone according to speed and angle
	 */
	public void adjustDrone() {
		double radAngle = getAngle() * Math.PI / 180; // put angle in radians
		setX(getX() + getSpeed() * Math.cos(radAngle)); // new X position
		setY(getY() + getSpeed() * Math.sin(radAngle)); // new Y position
	}

	/**
	 * update the world -
	 * 
	 * @param mc canvas in which drawn
	 */
	public void updateDrone(MyCanvas mc, List<Bedrock> bedrocks) {
		checkDrone(mc, bedrocks); // check if about to hit side of wall, adjust angle if so
		adjustDrone(); // calculate new position
		drawDrone(mc);
	}

	/**
	 * draw the drone into the canvas mc
	 * 
	 * @param mc
	 */
	public void drawDrone(MyCanvas mc) {
		// mc.clearCanvas();
		mc.showCircle(getX(), getY(), getRadius(), 'r'); // call interface's routine
	}

	/**
	 * return string describing drone and its position
	 * 
	 * @returns String
	 */
	public String toString() {
		return "Drone at " + Math.round(getX()) + ", " + Math.round(getY());
	}

	/**
	 * returns drone data in csv format
	 * 
	 * @returns String
	 */
	public String toFile() {
		return "D" + "," + Math.round(getX()) + "," + Math.round(getY()) + "," + Math.round(getRadius()) + ","
				+ Math.round(getAngle()) + "," + Math.round(getSpeed()) + "\n";
	}

}
