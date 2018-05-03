
package cs1302.arcade;

import java.util.Random;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * @author Adeeb Zaman & Manas Chakka
 *
 */
public class ArcadeApp extends Application {

	Random rng = new Random();

	@Override
	public void start(Stage stage) {

		BorderPane bp = new BorderPane();

		HBox gameSelection = new HBox(40);
		gameSelection.setPadding(new Insets(40));

		MenuBar menuBar = new MenuBar();
		// Create File menu
		Menu menu = new Menu("Options");

		// Create exit menu item
		MenuItem menuItem = new MenuItem("High Scores");

		// Set on action event handler
		menuItem.setOnAction(e -> HighscoreTable.displayHighscoreTable());

		// Add menu item to menu
		menu.getItems().add(menuItem);

		menuBar.getMenus().add(menu);

		bp.setTop(menuBar);

		Scene mainScene = new Scene(bp);

		Scene breakoutScene = BreakoutScene.createBreakoutScene(stage, mainScene);

		ImageView breakoutButton = new ImageView(new Image("breakout.png"));
		breakoutButton.setOnMouseClicked(e -> {
			stage.setScene(breakoutScene);
			stage.sizeToScene();
			BreakoutScene.loadingStage.show();
		});

		ImageView minesweeperButton = new ImageView(new Image("minesweeper.png"));
		minesweeperButton.setOnMouseClicked(e -> {

			stage.setScene(MinesweeperCell.createMinesweeperScene(stage, mainScene));
			stage.sizeToScene();

		});

		gameSelection.getChildren().addAll(breakoutButton, minesweeperButton);

		bp.setCenter(gameSelection);

		HighscoreTable.getScores();

		stage.setTitle("cs1302-arcade!");
		stage.setMinHeight(200);
		stage.setMinWidth(200);
		stage.setResizable(false);
		stage.show();

		// Animation
		animateStage(stage, mainScene);

		// the group must request input focus to receive key events
		// @see
		// https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Node.html#requestFocus--
		bp.requestFocus();

	} // start

	/**
	 * Plays the animation and goes
	 * to the game selection scene when it is done
	 * 
	 * @param stage - main stage
	 * @param mainScene - game selection scene
	 */
	public static void animateStage(Stage stage, Scene mainScene) {

		BorderPane outer = new BorderPane();

		ImageView intro = new ImageView(new Image("intro.gif"));

		outer.setCenter(intro);

		Scene animation = new Scene(outer);

		stage.setScene(animation);

		Thread t = new Thread(() -> {

			long start = System.currentTimeMillis();

			while (System.currentTimeMillis() - start < 4000) {
				System.out.print("");
			}

			Platform.runLater(() -> {
				stage.setScene(mainScene);
				stage.sizeToScene();
			});
		});

		t.start();

	}

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
