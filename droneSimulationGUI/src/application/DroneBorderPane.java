package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class DroneBorderPane extends Application {

	private int xCanvasSize = 400, yCanvasSize = 500; // size of canvas
	private MyCanvas mc; // canvas in which system drawn
	private static DroneArena droneArena = new DroneArena();
	private boolean animationOn = false; // are we animating?
	private ScrollPane rPane; // pane in which info on drone listed
	private VBox lPane; // pane for alerts
	private Slider droneSpeed; // slider for droneSpeed
	private Slider playerSpeed; // slider for playerSpeed

	Canvas canvas = new Canvas(xCanvasSize, yCanvasSize); // new canvas
	// static StopWatch timer;

//	public double getTimerTime() {
//		return timer.getTime(TimeUnit.SECONDS);
//	}

	/**
	 * Function to show a message,
	 * 
	 * @param TStr title of message block
	 * @param CStr content of message
	 */
	static public void showMessage(String TStr, String CStr) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(TStr);
		alert.setHeaderText(null);
		alert.setContentText(CStr);
		alert.show();
	}

	/**
	 * function to show in a box ABout the programme
	 */
	private void showAbout() {
		showMessage("About", "Luke Wilson's Drone Simulation");
	}

	/**
	 * function to show in a box Help the programme
	 */
	private void showHelp() {
		showMessage("Help", "Press the add drone button to place a drone randomly." + "\n"
				+ "Press the add obstacle button button to place an obstacle randomly." + "\n"
				+ "You can use the sliders on the left to control the speed of the drones." + "\n"
				+ "Press start/stop button to start and stop the animation." + "\n");
	}

	/**
	 * Function to set up the menu
	 */
	public MenuBar setMenu() {
		MenuBar menuBar = new MenuBar(); // create menu

		Menu mHelp = new Menu("Help"); // have entry for help
		// then add sub menus for About and Help
		// add the item and then the action to perform
		MenuItem mAbout = new MenuItem("About");
		mAbout.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				showAbout(); // show the about message
			}
		});

		MenuItem mfHelp = new MenuItem("Help"); // creates help menu part
		mfHelp.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				showHelp(); // show the about message
			}
		});

		mHelp.getItems().addAll(mAbout, mfHelp); // add submenu to Help

		// now add File menu, which here only has Exit
		Menu mFile = new Menu("File"); // create File Menu

		MenuItem mNew = new MenuItem("New Arena"); // add new submenu
		mNew.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) { // and add handler
				showMessage("Drone Arena", "New arena created!");
				newArena(); // creates new arena
			}
		});

		MenuItem mOpen = new MenuItem("Open"); // add open submenu
		mOpen.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) { // and add handler
				loadFile(); // loads file
			}
		});

		MenuItem mSave = new MenuItem("Save"); // add save submenu
		mSave.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) { // and add handler
				saveFile(); // saves file
			}
		});

		MenuItem mExit = new MenuItem("Exit"); // add exit submenu
		mExit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) { // and add handler
				System.exit(0); // quit program
			}
		});

		mFile.getItems().addAll(mNew, mOpen, mSave, mExit); // add Exit submenu to File

		menuBar.getMenus().addAll(mFile, mHelp); // menu has File and Help

		return menuBar; // return the menu, so can be added
	}

	/**
	 * creates a new arena
	 */
	public void newArena() {
		droneArena.resetArena(); // resets the arena
		mc.clearCanvas(); // clears the canvas
		drawStatus(); // redraws the stats on the right
		// timer = null;

	}

	/**
	 * saves current session to a file
	 */
	public void saveFile() {
		FileChooser chooser = new FileChooser(); // creates new FileChooser
		chooser.setTitle("Save"); // sets the title
		File chosenFile = chooser.showSaveDialog(null); // create file variable for file
		String selFile = chosenFile.getName(); // gets the file name
		String currDir = chosenFile.getAbsolutePath(); // gets filepath
		String filename = currDir + ".drn";

		try {
			File myObj = new File(filename); // creates new file
			if (myObj.createNewFile()) {
				System.out.println("File created: " + myObj.getName()); // error handling
			} else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		try {
			FileWriter myWriter = new FileWriter(filename);
			myWriter.write(droneArena.toFile()); // writes the arena data to the file
			myWriter.close(); // close filewriter
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		System.out.println("You chose to save into file: " + selFile + " in the dir " + currDir);
	}

	/**
	 * loads a previously saved arena from a file
	 */
	public void loadFile() {
		FileChooser chooser = new FileChooser(); // new FileChooser
		chooser.setTitle("Open"); // sets window title
		List<List<String>> records = new ArrayList<>(); // 2d list record for the data

		File selFile = chooser.showOpenDialog(null); // opens file chooser and sets file as selFile
		System.out.println("You chose to open this file: " + selFile.getName());
		if (selFile.isFile()) { // exists and is a file
			try (BufferedReader br = new BufferedReader(new FileReader(selFile.getAbsoluteFile()))) {
				String line;
				while ((line = br.readLine()) != null) { // loops through each line
					String[] values = line.split(","); // splits the data by a comma
					records.add(Arrays.asList(values)); // adds the data to the file
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		mc.clearCanvas(); // clears canvas
		newArena();
		for (int i = 1; i < records.size(); i++) // loops through all the drone data
			droneArena.loadObjects(records.get(i), mc);
		drawStatus(); // updates info on the right
	}

	/**
	 * show where drone is, in pane on right
	 */
	public void drawStatus() {
		rPane.setContent(null); // resets the panel on the right
		// now create label
		Label l = new Label("Arena size: " + droneArena.toFile()); // puts arena size on label
		l.setPrefWidth(152);
		l.setPadding(new Insets(4));
		rPane.setContent(l);
	}

	/**
	 * draws the left hand side panel
	 */
	private void drawAlerts() {
		lPane.getChildren().clear(); // clear lpane
		Label leftLbl = new Label();
		leftLbl.setMaxWidth(Double.MAX_VALUE); // set properties for label
		leftLbl.setPadding(new Insets(4));
		leftLbl.setText("Properties");
		leftLbl.setAlignment(Pos.CENTER);

		Label drnSpeedLbl = new Label();
		drnSpeedLbl.setMaxWidth(Double.MAX_VALUE);// set properties for label
		drnSpeedLbl.setPadding(new Insets(5));
		drnSpeedLbl.setText("Drone Speed");

		Label plySpeedLbl = new Label("Player Speed");// set properties for label
		plySpeedLbl.setMaxWidth(Double.MAX_VALUE);
		plySpeedLbl.setPadding(new Insets(5));

//		Label timerLbl = new Label(timer.toString());
//		timerLbl.setMaxWidth(Double.MAX_VALUE);
//		timerLbl.setPadding(new Insets(5));
		// adds the controls to the pane
		lPane.getChildren().addAll(leftLbl, new Separator(), drnSpeedLbl, droneSpeed, plySpeedLbl, playerSpeed);// ,
//				timerLbl);
	}

	/**
	 * set up the mouse event handler, so when click on canvas, put drone there
	 * 
	 * @param canvas
	 */
	private void setMouseEvents(Canvas canvas) {
		canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {

				mc.clearCanvas();
				droneArena.addPlayer(e.getX(), e.getY(), mc); // put drone at e.x, e.y
				droneArena.drawAllObjects(mc);
				// drones.drawWorld(mc); // draw world with drone where mouse clicked
				drawStatus(); // update panel

//				timer = null;
//				timer = new StopWatch();
				// resetTimer();

			}
		});
	}

	// when start button press reset timer
	/**
	 * set up the buttons and return so can add to borderpane
	 * 
	 * @return
	 */
	private HBox setButtons() {
		// create button
		Button btnAddDrone = new Button("Add Drone");
		// now add handler
		btnAddDrone.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				droneArena.addDrone(mc);
				drawStatus();
			}
		});

		// create button
		Button btnAddBedrock = new Button("Add Bedrock");
		// now add handler
		btnAddBedrock.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				droneArena.addBedrock(mc);
				drawStatus();
			}
		});

		// start the timer on start
		// stop the timer on stop
		// reset the timer when player spawned

		Button btnStartStop = new Button("Start/Stop");
		// now add handler
		btnStartStop.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				animationOn = !animationOn; // plays / pauses animation
//				if (animationOn) {
//					timer.start();
//				} else {
//					timer.stop();
//				}
			}
		});
		// adds controls to bottom panel
		HBox bottomHBox = new HBox(btnAddDrone, btnAddBedrock, new Label(" Animation: "), btnStartStop);
		bottomHBox.setPadding(new Insets(4));
		return bottomHBox;
	}

	@Override
	public void start(Stage stagePrimary) throws Exception {
		stagePrimary.setTitle("Luke's Drone Simulation");

		BorderPane bp = new BorderPane(); // create border pane

		Group root = new Group(); // create group
		mc = new MyCanvas(canvas.getGraphicsContext2D(), xCanvasSize, yCanvasSize); // create new canvas
		// create MyCanvas passing context
		// on canvas onto which images put
		// and canvas to draw in
		rPane = new ScrollPane(); // set vBox for listing data
		lPane = new VBox(); // new vbox for left panel
		droneSpeed = new Slider(0, 10, 3); // slider for drone speed
		playerSpeed = new Slider(0, 10, 3); // slider for player speed
		root.getChildren().add(canvas); // and add canvas to group

		bp.setTop(setMenu()); // create menu, add to top
		bp.setCenter(root); // put group in centre pane
		bp.setRight(rPane); // put in right pane
		bp.setLeft(lPane);
		bp.setBottom(setButtons()); /// add button to bottom
//		rPane.setStyle("-fx-border-color: red; -fx-background-color: lightgray;");
//		bp.setStyle("-fx-background-color: gray;");
		Scene scene = new Scene(bp, xCanvasSize * 2, yCanvasSize * 1.2);
		// scene.getStylesheets().add("dark-theme.css");

		drawStatus(); // draws the right label panel
		drawAlerts(); // draws everything on the left
		setMouseEvents(canvas); // set mouse handler
		scene.setOnKeyPressed(e -> {
			droneArena.keyInput(mc, e.getCode()); // gets user key input
		});

		// Configure the Label
		// Bind the timerLabel text property to the timeSeconds property

		// for animation, note start time
		new AnimationTimer() // create timer
		{
			public void handle(long currentNanoTime) {
				// define handle for what do at this time
				if (animationOn) {
					mc.clearCanvas();
					droneArena.moveAllObjects(mc, droneSpeed.getValue(), playerSpeed.getValue()); // moves all objects
//					if (droneArena.isPlayerDead())
//						timer = null;
					drawStatus(); // draws the right hand side panel
				}
			}
		}.start(); // start it

		// create scene so bigger than canvas,

		stagePrimary.setScene(scene);
		// stagePrimary.getScene().getStylesheets().add("/default.css");
		stagePrimary.show();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Application.launch(args); // launch the GUI
	}

}