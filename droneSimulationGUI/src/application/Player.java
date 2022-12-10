package application;

import java.util.List;

import javafx.scene.input.KeyCode;

public class Player extends MoveableObject {

	private double dx = 0;
	private double dy = 0;

	/**
	 * @param x
	 * @param y
	 */
	public Player(double x, double y) { // constructor
		setX(x);
		setY(y);
		setSpeed(3);
		setRadius(15);
	}

	/**
	 * checks if player is touching drone
	 * 
	 * @param drones
	 * @return if player is touching drone
	 */
	public boolean checkTouchingDrone(List<Drone> drones) {
		for (Drone aDrone : drones)
			if (aDrone.isHere(getX() + dx, getY() + dy, getRadius()) == true) {
				DroneBorderPane.showMessage(null, "You touched a killer drone and died!");//+ "\nYou were alive for "
						//+ DroneBorderPane.timer.toString() + " seconds.");
				return true;
			}
		return false;
	}

	/**
	 * checks if the player can move
	 * 
	 * @param mc
	 * @param drones
	 * @param bedrocks
	 * @param dx
	 * @param dy
	 * @return true if the player can move
	 */
	public boolean canMovePlayer(MyCanvas mc, List<Drone> drones, List<Bedrock> bedrocks, double dx, double dy) {
//		for (Drone aDrone : drones)
//			if (aDrone.isHere(getX() + dx, getY() + dy, getRadius()) == true) {
//				dead = true;
//				return true;
//			}
		for (Bedrock aBedrock : bedrocks)
			if (aBedrock.isHere(getX() + dx, getY() + dy, getRadius()) == true) { // check if there is an obstacle in
																					// the way
				return false;
			}
		if (getX() + dx - getRadius() <= 0 || getX() + dx + getRadius() > mc.getXCanvasSize() // checks if future
																								// position is in the
																								// arena
				|| getY() + dy - getRadius() <= 0 || getY() + dy + getRadius() > mc.getYCanvasSize()) {
			return false;
		}
		return true;
	}

	// make drone collide into player kill player
	/**
	 * @param mc
	 * @param drones
	 * @param bedrocks
	 * @param key
	 */
	public void adjustPlayer(MyCanvas mc, List<Drone> drones, List<Bedrock> bedrocks, KeyCode key) {
		if (key == KeyCode.W) { // check the key input
			dy = -getSpeed();
		}
		if (key == KeyCode.A) {
			dx = -getSpeed();
		}
		if (key == KeyCode.S) {
			dy = getSpeed();
		}
		if (key == KeyCode.D) {
			dx = getSpeed();
		}
		if (canMovePlayer(mc, drones, bedrocks, dx, dy)) { // checks if the player can move
			setX(getX() + dx);
			setY(getY() + dy);
		}
		dx = 0; // reset acceleration
		dy = 0;
	}

	/**
	 * updates the players pos and draws on canvas
	 * 
	 * @param mc
	 * @param drones
	 * @param bedrocks
	 * @param keyPressed
	 */
	public void updatePlayer(MyCanvas mc, List<Drone> drones, List<Bedrock> bedrocks, KeyCode keyPressed) {
//		if (canMovePlayer(mc, drones, bedrocks))
		adjustPlayer(mc, drones, bedrocks, keyPressed);
		drawPlayer(mc);
	}

	/**
	 * draws player on canvas
	 * 
	 * @param mc
	 */
	public void drawPlayer(MyCanvas mc) {
		mc.showCircle(getX(), getY(), getRadius(), 'g');
	}

	/**
	 * @return returns player pos and radius
	 */
	public String toFile() {
		return "P" + "," + Math.round(getX()) + "," + Math.round(getY()) + "," + Math.round(getRadius()) + "\n";
	}
}
