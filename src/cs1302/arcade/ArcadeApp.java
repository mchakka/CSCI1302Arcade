
package cs1302.arcade;

import java.util.Random;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ArcadeApp extends Application {

	Random rng = new Random();

	@Override
	public void start(Stage stage) {

		Group gameSelectionGroup = new Group();


		VBox gameSelection = new VBox(40);
		gameSelection.setPadding(new Insets(40));
		
		
		Scene mainScene = new Scene(gameSelectionGroup);		
		
		
		
		Scene tetrisScene = TetrisScene.createTetrisScene(stage, mainScene);
		Scene minesweeperScene = MinesweeperScene.createMinesweeperScene(stage, mainScene);
		
		
		Button tetrisButton = new Button();
		tetrisButton.setText("Start a new game of Tetris");
		tetrisButton.setOnAction(e -> {			
			stage.setScene(tetrisScene);
			stage.sizeToScene();
		});
		
		Button minesweeperButton = new Button();
		minesweeperButton.setText("Start a new game of Minesweeper");
		minesweeperButton.setOnAction(e -> {
			stage.setScene(minesweeperScene);
			stage.sizeToScene();
		});
		
		
		gameSelection.getChildren().addAll(tetrisButton, minesweeperButton);
		
		gameSelectionGroup.getChildren().add(gameSelection);

		
		
		
		stage.setTitle("cs1302-arcade!");
		stage.setMinHeight(200);
		stage.setMinWidth(200);
		stage.setScene(mainScene);
		stage.sizeToScene();
		stage.show();

		// the group must request input focus to receive key events
		// @see
		// https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Node.html#requestFocus--
		gameSelectionGroup.requestFocus();

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
