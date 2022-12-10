/**
 * 
 */
package application;

/**
 * @author luke
 *
 */
abstract class MoveableObject extends Object {
	private double angle, speed;

	/**
	 * sets all object values
	 * 
	 * @Overrides Object.setAll
	 */
	protected void setAll(double x1, double y1, double rad1, double angle1, double speed1) {
		setAll(x1, y1, rad1);
		setAngle(angle1);
		setSpeed(speed1);
	}

	/**
	 * @return angle
	 */
	protected double getAngle() {
		return angle;
	}

	/**
	 * @return speed
	 */
	protected double getSpeed() {
		return speed;
	}

	/**
	 * @param angle the angle to set
	 */
	public void setAngle(double angle) {
		this.angle = angle;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}
}
