package cs1302.arcade;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author Adeeb Zaman
 *
 */
/**
 * @author Adeeb
 *
 */
public class MinesweeperCell {

	static final int DIFFICULTY = 20; // Percent of the board to be filled with mines

	static final int WIDTH = 16;
	static final int HEIGHT = 16;

	static MinesweeperCell[][] buttons = new MinesweeperCell[WIDTH][HEIGHT]; // Array of cells in the board
	static GridPane grid = new GridPane(); // Gridpane to hold the buttons
	static ImageView smileButton; // Reset button
	static Text score; // Score

	/**
	 * Keeps track of time for timer object
	 * 
	 * @author Adeeb Zaman
	 *
	 */
	static class Timer {

		static Timer t;

		Text timerText = new Text();
		Timeline timeline = new Timeline();

		/**
		 * Creates a new Timer object
		 */
		public Timer() {
			KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
				timerText.setText((Integer.parseInt(timerText.getText()) + 1) + "");
			});
			timeline = new Timeline();
			timeline.setCycleCount(Timeline.INDEFINITE);
			timeline.getKeyFrames().add(keyFrame);

			t = this;
		}

		/**
		 * Set text of timer to new input
		 * 
		 * @param input
		 */
		public static void setText(String input) {
			t.timerText.setText(input);
		}

		/**
		 * Get current time from timer
		 * 
		 * @return time
		 */
		public static int getTime() {
			return Integer.parseInt(t.timerText.getText());
		}

		/**
		 * Turns timer on or off
		 * 
		 * @param on
		 *            - whether timer is on
		 */
		public static void setTimer(boolean on) {
			if (on)
				t.timeline.play();
			else
				t.timeline.pause();
		}

	}

	static int uncoveredNum; // Number of total uncovered blocks

	private ImageView iv; // ImageView for this cell

	private boolean covered = true; // Whether the cell is covered
	private boolean flagged = false; // Whether the cell is flagged

	private boolean containsMine; // Whether the cell contains a mine
	private int adjacentMines; // The number of adjacent mines
	private int listNum; // The number of the ImageView in the GridPane list
	private int row, col; // The row and col of the ImageView on the grid

	/**
	 * Constructs the minesweeper scene and starts a new game
	 * 
	 * @param stage
	 *            - Main stage to put minesweeper scene on
	 * @param mainScene
	 *            - main game selection scene
	 * @return
	 */
	public static Scene createMinesweeperScene(Stage stage, Scene mainScene) {
		BorderPane bp = new BorderPane();
		bp.setPadding(new Insets(10));

		// Make toolbar
		BorderPane toolbar = new BorderPane();
		toolbar.setPadding(new Insets(15));

		// Make timer
		Timer timer = new Timer();

		toolbar.setRight(timer.timerText);

		// Make restart button
		smileButton = new ImageView("smile.png");
		smileButton.setOnMouseClicked(e -> {

			if (e.getButton().equals(MouseButton.PRIMARY)) {
				startNewGame();
			}

		});

		// If this is being clicked
		smileButton.setOnMousePressed(e -> {

			if (e.getButton().equals(MouseButton.PRIMARY)) {
				smileButton.setImage(new Image("gasp.png"));
			}

		});

		smileButton.setOnMouseExited(e -> {
			smileButton.setImage(new Image("smile.png"));
		});

		toolbar.setCenter(smileButton);

		// Make score text
		score = new Text("0");

		toolbar.setLeft(score);

		bp.setCenter(toolbar);

		// Make back button
		Button backButton = new Button();
		backButton.setText("Back");
		backButton.setOnAction(e -> {
			stage.setScene(mainScene);
			stage.sizeToScene();
		});

		bp.setTop(backButton);
		bp.setBottom(grid);

		// Start the game
		MinesweeperCell.startNewGame();

		Scene MinesweeperCell = new Scene(bp);

		return MinesweeperCell;
	}

	/**
	 * Starts a new game of Minesweeper
	 * 
	 */
	public static void startNewGame() {

		// Reset number of mines uncovered
		uncoveredNum = 0;

		// Clear and fill grid with new buttons
		grid.getChildren().clear();

		int l = 0;
		for (int r = 0; r < buttons.length; r++) {
			for (int c = 0; c < buttons[0].length; c++) {
				int R = r, C = c;
				buttons[r][c] = new MinesweeperCell(l, R, C, e -> {
					grid.getChildren().set(buttons[R][C].listNum, buttons[R][C].iv);
				});

				grid.add(buttons[r][c].iv, r, c);

				l++;
			}
		}

		// Count adjacent mines for each button
		int count;
		for (int row = 0; row < buttons.length; row++) {
			for (int col = 0; col < buttons[0].length; col++) {

				count = 0;

				// Loop through each adjacent cell
				for (int r = Math.max(0, row - 1); r <= Math.min(buttons.length - 1, row + 1); r++) {

					for (int c = Math.max(0, col - 1); c <= Math.min(buttons[r].length - 1, col + 1); c++) {

						if (!(r == row && c == col) && buttons[r][c].containsMine) {
							count++;
						}

					}
				}

				buttons[row][col].adjacentMines = count;
			}
		}

		// Initialize variables
		smileButton.setImage(new Image("smile.png"));

		score.setText("0");

		Timer.setText("0");
		Timer.setTimer(true);
	}

	/**
	 * Creates a new cell object
	 * 
	 * @param l
	 *            - number of the ImageView in the gridpane list
	 * @param r
	 *            - row number of this cell
	 * @param c
	 *            - column number of this cell
	 * @param onClicked
	 *            - EventHandler to set new image in grid
	 */
	public MinesweeperCell(int l, int r, int c, EventHandler<ActionEvent> onClicked) {

		// Initialize cell image
		iv = new ImageView("cell.png");
		iv.setId(l + "");

		// If this cell is clicked
		iv.setOnMouseClicked(e -> {

			// If it is a left click uncover the cell, If it is a right click, flag the cell
			if (e.getButton().equals(MouseButton.PRIMARY)) {
				uncover(this);
			} else if (e.getButton().equals(MouseButton.SECONDARY)) {
				flag();
			}

			// Check if the player has won
			checkForWin();

			// Update cell image in grid
			onClicked.handle(new ActionEvent());

		});

		// If this is being clicked
		iv.setOnMousePressed(e -> {

			if (covered && !flagged && e.getButton().equals(MouseButton.PRIMARY)) {
				iv.setImage(new Image("0.png"));
				smileButton.setImage(new Image("gasp.png"));
			}

		});

		// Reset image when mouse moves
		iv.setOnMouseExited(e -> {
			if (covered) {
				if (flagged) {
					iv.setImage(new Image("flag.png"));
				} else {
					iv.setImage(new Image("cell.png"));
				}
			}
			smileButton.setImage(new Image("smile.png"));
		});

		// Place mines randomly
		if (Math.random() * 100 < DIFFICULTY) {
			containsMine = true;
		} else {
			containsMine = false;
		}

		listNum = l;
		row = r;
		col = c;

	}

	/**
	 * Uncovers the cell
	 * 
	 * @param MinesweeperCell
	 *            - the cell to uncover
	 */
	public static void uncover(MinesweeperCell MinesweeperCell) {

		// If it can be uncovered
		if (MinesweeperCell.covered && !MinesweeperCell.flagged) {

			MinesweeperCell.covered = false;

			// If it does not contain a mine
			if (!MinesweeperCell.containsMine) {

				// Uncover current cells
				MinesweeperCell.iv.setImage(new Image(MinesweeperCell.adjacentMines + ".png"));

				// Uncover adjacent cells recursively
				if (MinesweeperCell.adjacentMines == 0) {
					for (int r = Math.max(0, MinesweeperCell.row - 1); r <= Math.min(buttons.length - 1,
							MinesweeperCell.row + 1); r++) {
						for (int c = Math.max(0, MinesweeperCell.col - 1); c <= Math.min(buttons[r].length - 1,
								MinesweeperCell.col + 1); c++) {

							if (!(r == MinesweeperCell.row && c == MinesweeperCell.col)) {

								uncover(buttons[r][c]);

							}
						}
					}
				}

				// Reset reset button
				smileButton.setImage(new Image("smile.png"));

				// Increment uncovered
				uncoveredNum++;

				// Update score
				score.setText(calculateScore() + "");

			} else {

				// If it is a mine set image
				MinesweeperCell.iv.setImage(new Image("mine.png"));

				// Display game over screen
				endGame(false);

			}

		}
	}

	/**
	 * Flag current mine
	 * 
	 */
	public void flag() {

		// If it is covered
		if (covered) {

			if (flagged) {
				// If it is already flagged, then unflag
				iv.setImage(new Image("cell.png"));
				flagged = false;
			} else {
				// If it is not flagged, then flag
				iv.setImage(new Image("flag.png"));
				flagged = true;
			}

		}

	}

	/**
	 * Checks if the player has won
	 */
	public static void checkForWin() {
		// Loop through grid and check if every mine is marked
		for (int r = 0; r < buttons.length; r++) {

			for (int c = 0; c < buttons[0].length; c++) {

				// If a mine is not marked, or an empty cell has not been uncovered, return
				if ((buttons[r][c].containsMine && !buttons[r][c].flagged)
						|| (buttons[r][c].covered && !buttons[r][c].containsMine)) {

					return;

				}

			}

		}

		// If all cells have been uncovered, display win screen
		endGame(true);

	}

	/**
	 * Calculates current score
	 * 
	 * @return score
	 */
	public static int calculateScore() {
		return (int) (DIFFICULTY * WIDTH * HEIGHT * uncoveredNum / 100);
	}



	/**
	 * Displays end game screen and starts a new game
	 * 
	 * @param won - Whether the user won or not
	 */
	public static void endGame(boolean won) {

		// Turn off timer
		Timer.setTimer(false);

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Create new stage for game over screen
		Stage gameOverStage = new Stage();
		gameOverStage.setTitle(won ? "Congratulations" : "Game Over");

		VBox vbox = new VBox(won ? new Text("You won!") : new Text("Game Over!"),
				new Text("Your score was " + calculateScore()));
		vbox.setPadding(new Insets(20));

		vbox.setStyle("-fx-text-size:20");

		Button startOver = new Button();
		startOver.setText("Play Again");
		startOver.setOnAction(e -> {
			// Start a new game when play again button is pressed
			startNewGame();
			gameOverStage.close();

		});

		vbox.getChildren().add(startOver);

		Scene scene = new Scene(vbox);

		gameOverStage.setOnCloseRequest(e -> {
			// Start a new game when game over window is closed
			startNewGame();
		});

		// Prepare and show game over stage
		gameOverStage.initModality(Modality.APPLICATION_MODAL);
		gameOverStage.setScene(scene);
		gameOverStage.sizeToScene();
		gameOverStage.setResizable(false);
		gameOverStage.show();

		
		//If user won
		if (won) {
			// Calculate score and send to highscore table
			int score = calculateScore();

			HighscoreTable.newMinesweeperScore(score);
		} else {
			//If lost, set reset button to frown
			smileButton.setImage(new Image("frown.png"));
		}

	}
}
