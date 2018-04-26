
package cs1302.arcade;

import java.util.Random;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ArcadeApp extends Application {

	Random rng = new Random();

	@Override
	public void start(Stage stage) {

		BorderPane bp = new BorderPane();


		VBox gameSelection = new VBox(40);
		gameSelection.setPadding(new Insets(40));
		
		
		
		MenuBar menuBar = new MenuBar();
		// Create File menu
		Menu menu = new Menu("Options");

		//Create exit menu item
		MenuItem menuItem = new MenuItem("High Scores");
		
		//Set on action event handler
		menuItem.setOnAction(e -> HighscoreTable.displayHighscoreTable());
			
		//Add menu item to menu
		menu.getItems().add(menuItem);		

		menuBar.getMenus().add(menu);
			
		bp.setTop(menuBar);
				
				
				
		
		
		Scene mainScene = new Scene(bp);		
		
		
		
		Scene breakoutScene = BreakoutScene.createBreakoutScene(stage, mainScene);
		
		
		Button breakoutButton = new Button();
		breakoutButton.setText("Start a new game of Breakout");
		breakoutButton.setOnAction(e -> {			
			stage.setScene(breakoutScene);
			stage.sizeToScene();
		});
		
		Button minesweeperButton = new Button();
		minesweeperButton.setText("Start a new game of Minesweeper");
		minesweeperButton.setOnAction(e -> {
			stage.setScene(MinesweeperCell.createMinesweeperScene(stage, mainScene));
			stage.sizeToScene();
		});
		
		
		gameSelection.getChildren().addAll(breakoutButton, minesweeperButton);
		
		bp.setCenter(gameSelection);

		
		
		HighscoreTable.getScores();
			
		
		
		
		
		stage.setTitle("cs1302-arcade!");
		stage.setMinHeight(200);
		stage.setMinWidth(200);
		stage.setScene(mainScene);
		stage.sizeToScene();
		stage.show();

		// the group must request input focus to receive key events
		// @see
		// https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Node.html#requestFocus--
		bp.requestFocus();

		
		
	} // start

	public static void main(String[] args) {
		try {
			Application.launch(args);
		} catch (UnsupportedOperationException e) {
			System.out.println(e);
			System.err.println("If this is a DISPLAY problem, then your X server connection");
			System.err.println("has likely timed out. This can generally be fixed by logging");
			System.err.println("out and logging back in.");
			System.exit(1);
		} // try
	} // main

} // ArcadeApp
