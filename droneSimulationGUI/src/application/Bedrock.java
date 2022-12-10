/**
 * 
 */
package application;

/**
 * @author luke
 *
 */
public class Bedrock extends Object {
	/**
	 * draw the bedrock into the canvas mc
	 * 
	 * @param mc
	 */
	public void drawBedrock(MyCanvas mc) {
		// mc.clearCanvas();
		/*
		 * Image bedrockImage = new
		 * Image(getClass().getResourceAsStream("bedrock.png")); mc.drawIt(bedrockImage,
		 * getX(), getY(), getRad()); uncomment for picture
		 */
		mc.showCircle(getX(), getY(), getRadius(), 'b'); // call interface's routine
	}

	/**
	 * return string describing bedrock and its position
	 * 
	 * @returns String
	 */
	public String toString() {
		return "Bedrock at " + Math.round(getX()) + ", " + Math.round(getY());
	}

	/**
	 * returns bedrock data in csv format
	 * 
	 * @returns String
	 */
	public String toFile() {
		return "B" + "," + Math.round(getX()) + "," + Math.round(getY()) + "," + Math.round(getRadius()) + "\n";
	}

}
