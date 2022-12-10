package application;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

public class DroneAnimation extends Application {

	private int xCanvasSize = 400, yCanvasSize = 500;	// size of canvas
    private MyCanvas mc; 								// canvas in which system drawn
    private Drone odw;							// world with a bouncing drone

	@Override
	public void start(Stage stagePrimary) throws Exception {
		stagePrimary.setTitle("RJMs Ball Animation");
		
	    Group root = new Group();					// for group of what is shown
	    Scene scene = new Scene( root );			// put it in a scene
	    stagePrimary.setScene( scene );				// apply the scene to the stage
	 
	    Canvas canvas = new Canvas( xCanvasSize, yCanvasSize );
	    											// create canvas onto which animation shown
	    root.getChildren().add( canvas );			// add to root and hence stage
	 
	    mc = new MyCanvas(canvas.getGraphicsContext2D(), xCanvasSize, yCanvasSize);
	    								// create MyCanvas passing context on canvas onto which images put
	    
	    odw = new Drone();					// create object for world with a drone
		
	    new AnimationTimer()						// create timer
	    	{
	    		public void handle(long currentNanoTime) {
	    				// define handle for what do at this time ... here dont use time
	    			//odw.updateDrone(mc);			// find new position of drone 
	    			odw.drawDrone(mc);				// draw drone in new position
	    		}
	    	}.start();							// start animation
	    
		stagePrimary.show();						// show scene

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Application.launch(args);			// launch the GUI

	}


}
