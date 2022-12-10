/**
 * 
 */
package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.input.KeyCode;

/**
 * @author luke
 *
 */
public class DroneArena {
	private int xCanvasSize = 400, yCanvasSize = 500; // size of canvas
	private ArrayList<Drone> drones = new ArrayList<Drone>(); // world with a bouncing drone
	private ArrayList<Bedrock> bedrocks = new ArrayList<Bedrock>(); // world with a bouncing drone
	private Player player;
	private Random rgen = new Random(); // random number generator
	private KeyCode keyPressed;

	/**
	 * resets the arena
	 */
	public void resetArena() {
		drones = null;
		drones = new ArrayList<Drone>();
		bedrocks = null;
		bedrocks = new ArrayList<Bedrock>();
		player = null;
	}

	/**
	 * adds new player
	 * 
	 * @param x
	 * @param y
	 * @param mc
	 */
	public void addPlayer(double x, double y, MyCanvas mc) {
		player = new Player(x, y);
		player.drawPlayer(mc);
	}

	/**
	 * add a new random drone
	 * 
	 * @param mc
	 */
	public void addDrone(MyCanvas mc) {
		if (drones.size() <= 15) {

			// if (mapFull() == false) {
			Drone newDrone = new Drone();
			Double xVal = rgen.nextDouble() * xCanvasSize;
			Double yVal = rgen.nextDouble() * yCanvasSize; // generate random
			boolean valid = false;
			do {
				newDrone.setXY(xVal, yVal); // sets drone x and y
				// checks if drone placement is valid
				if (newDrone.inBounds(mc) == true && getDroneAt(xVal, yVal, newDrone.getRadius()) == null) {
					newDrone.drawDrone(mc); // draws drone on canvas
					drones.add(newDrone); // adds drone to list
					// and its action to draw earth at random angle
					valid = true;
				} else {
					xVal = rgen.nextDouble() * xCanvasSize; // regen random number
					yVal = rgen.nextDouble() * yCanvasSize;
				}
			} while (valid == false);
		}
	}

	/**
	 * @param x
	 * @param y
	 * @param rad
	 * @return drone if there is a drone in that position, else null
	 */
	public Drone getDroneAt(double x, double y, double rad) {
		for (Drone aDrone : drones) {// << return either Drone at x,y or null >>
			if (aDrone.isHere(x, y, rad))
				return aDrone;
		}
		return null;
	}

	/**
	 * add a new bedrock obstacle
	 * 
	 * @param mc
	 *
	 */
	public void addBedrock(MyCanvas mc) {
		Bedrock newBedrock = new Bedrock();
		Double xVal = rgen.nextDouble() * xCanvasSize;
		Double yVal = rgen.nextDouble() * yCanvasSize; // new random coords
		int rVal = rgen.nextInt(7, 17);
		boolean valid = false;
		do {
			newBedrock.setAll(xVal, yVal, rVal); // sets position
			if (newBedrock.inBounds(mc) == true) {
				newBedrock.drawBedrock(mc);
				bedrocks.add(newBedrock); // adds new obstacle to list and draws it
				valid = true;
			} else {
				xVal = rgen.nextDouble() * xCanvasSize;
				yVal = rgen.nextDouble() * yCanvasSize; // regens position and size if bad
				rVal = rgen.nextInt(7, 17);
			}
		} while (valid == false);
	}

	/**
	 * @param x
	 * @param y
	 * @param rad
	 * @return Bedrock at that position
	 */
	public Bedrock getBedrockAt(double x, double y, double rad) {
		for (Bedrock aRock : bedrocks) {// << return either bedrock at x,y or null >>
			if (aRock.isHere(x, y, rad))
				return aRock;
		}
		return null;
	}

	/**
	 * @return string for everything in the arena
	 */
	public String toFile() {
		String output = xCanvasSize + "," + yCanvasSize + "\n";
		if (player != null)
			output += player.toFile();
		for (Drone aDrone : drones) {
			output += aDrone.toFile();
		}
		for (Bedrock aBedrock : bedrocks) {
			output += aBedrock.toFile();
		}
		return output;
	}

	/**
	 * sets key pressed
	 * 
	 * @param mc
	 * @param key
	 */
	public void keyInput(MyCanvas mc, KeyCode key) {
		keyPressed = key;
		// if (player != null)
		// player.updatePlayer(mc, drones, bedrocks, keyPressed);

	}

	/**
	 * draws all objects
	 * 
	 * @param mc
	 */
	public void drawAllObjects(MyCanvas mc) {
		player.drawPlayer(mc);
		for (Drone drones : drones) {
			drones.drawDrone(mc); // find new position of drone
		}
		for (Bedrock bedrocks : bedrocks) {
			bedrocks.drawBedrock(mc);
		}

	}

	/**
	 * moves all objects
	 * 
	 * @param mc
	 * @param droneSpeed
	 * @param playerSpeed
	 */
	public void moveAllObjects(MyCanvas mc, Double droneSpeed, Double playerSpeed) {
		for (Drone drones : drones) { // checks all drones
			drones.setSpeed(droneSpeed);
			drones.updateDrone(mc, bedrocks); // find new position of drone
		}
		for (Bedrock bedrocks : bedrocks) { // checks all obstacles
			bedrocks.drawBedrock(mc);
		}
		if (player != null) { // checks player 
			if (player.checkTouchingDrone(drones)) { // touching drone?
				player = null;
			}

			else {
				player.setSpeed(playerSpeed); // sets the speed
				if (keyPressed != null)
					player.updatePlayer(mc, drones, bedrocks, keyPressed); // updates the player pos
				player.drawPlayer(mc); // draws player
			}
		}
	}
 
	/**
	 * @param data
	 * @param mc
	 */
	public void loadObjects(List<String> data, MyCanvas mc) { // loads everything from file
		if (data.get(0).equals("P"))
			loadPlayer(data, mc);
		if (data.get(0).equals("D"))
			loadDrone(data, mc);
		if (data.get(0).equals("B"))
			loadBedrock(data, mc);
	}

	/**
	 * @param playerData
	 * @param mc
	 */
	private void loadPlayer(List<String> playerData, MyCanvas mc) { // loads player
		if (player == null)
			player = new Player(0, 0);
		player.setAll(Integer.parseInt(playerData.get(1)), Integer.parseInt(playerData.get(2)), // takes each attribute from list
				Integer.parseInt(playerData.get(3))); // sets drone attributes
		player.drawPlayer(mc);
	}

	/**
	 * loads a drone from a drone data list
	 * 
	 * @param droneData
	 */
	private void loadDrone(List<String> droneData, MyCanvas mc) {
		Drone newDrone = new Drone();
		newDrone.setAll(Integer.parseInt(droneData.get(1)), Integer.parseInt(droneData.get(2)),
				Integer.parseInt(droneData.get(3)), Integer.parseInt(droneData.get(4)),
				Integer.parseInt(droneData.get(5))); // sets drone attributes
		drones.add(newDrone); // adds drone to drones list
		newDrone.drawDrone(mc); // draws drone
	}

	/**
	 * load a bedrock from bedrock data list
	 * 
	 * @param bedrockData
	 * @param mc
	 */
	private void loadBedrock(List<String> bedrockData, MyCanvas mc) {
		Bedrock newBedrock = new Bedrock();
		newBedrock.setAll(Integer.parseInt(bedrockData.get(1)), Integer.parseInt(bedrockData.get(2)),
				Integer.parseInt(bedrockData.get(3))); // sets bedrock attributes
		bedrocks.add(newBedrock); // adds bedrock to bedrocks list
		newBedrock.drawBedrock(mc); // draws bedrock
	}

	public boolean isPlayerDead() {
		if (player != null)
			if (player.checkTouchingDrone(drones)) {
				return true;
			}
		return false;
	}
}
