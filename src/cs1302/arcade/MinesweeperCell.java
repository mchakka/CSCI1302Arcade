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

public class MinesweeperCell {

	static final int DIFFICULTY = 20; // Percent of the board to be filled with mines

	static final int WIDTH = 16;
	static final int HEIGHT = 16;
	
	static MinesweeperCell[][] buttons = new MinesweeperCell[WIDTH][HEIGHT];
	static GridPane grid = new GridPane();
	static ImageView smileButton;
	static Text score;

	static class Timer {

		static Timer t;

		Text timerText = new Text();
		Timeline timeline = new Timeline();

		public Timer() {
			KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
				timerText.setText((Integer.parseInt(timerText.getText()) + 1) + "");
			});
			timeline = new Timeline();
			timeline.setCycleCount(Timeline.INDEFINITE);
			timeline.getKeyFrames().add(keyFrame);

			t = this;
		}

		public static void setText(String input) {
			t.timerText.setText(input);
		}

		public static int getTime() {
			return Integer.parseInt(t.timerText.getText());
		}

		public static void setTimer(boolean on) {
			if (on)
				t.timeline.play();
			else
				t.timeline.pause();
		}

	}

	static int uncovered;
	
	
	private ImageView iv;

	private boolean covered = true;
	private boolean flagged = false;

	private boolean containsMine;
	private int adjacentMines;
	private int listNum;
	private int row, col;

	public static Scene createMinesweeperScene(Stage stage, Scene mainScene) {
		BorderPane bp = new BorderPane();
		bp.setPadding(new Insets(10));

		// Make toolbar
		BorderPane toolbar = new BorderPane();
		toolbar.setPadding(new Insets(15));

		Timer timer = new Timer();

		toolbar.setRight(timer.timerText);
		
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
		
		score = new Text("0");
		
		toolbar.setLeft(score);

		bp.setCenter(toolbar);

		Button backButton = new Button();
		backButton.setText("Back");
		backButton.setOnAction(e -> {
			stage.setScene(mainScene);
			stage.sizeToScene();
		});

		bp.setTop(backButton);

		MinesweeperCell.startNewGame();

		bp.setBottom(grid);

		Scene MinesweeperCell = new Scene(bp);

		return MinesweeperCell;
	}

	public static void startNewGame() {

		uncovered = 0;
		
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

		// Count adjacent mines
		int count;
		for (int row = 0; row < buttons.length; row++) {
			for (int col = 0; col < buttons[0].length; col++) {

				count = 0;

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

		smileButton.setImage(new Image("smile.png"));
		
		score.setText("0");
		
		Timer.setText("0");
		Timer.setTimer(true);
	}

	public MinesweeperCell(int l, int r, int c, EventHandler<ActionEvent> onClicked) {

		// Initialize cell image
		iv = new ImageView("cell.png");
		iv.setId(l + "");

		// If this cell is clicked
		iv.setOnMouseClicked(e -> {

			if (e.getButton().equals(MouseButton.PRIMARY)) {
				uncover(this);
			} else if (e.getButton().equals(MouseButton.SECONDARY)) {
				flag();
			}

			checkForWin();

			onClicked.handle(new ActionEvent());

		});

		// If this is being clicked
		iv.setOnMousePressed(e -> {

			if (covered && !flagged && e.getButton().equals(MouseButton.PRIMARY)) {
				iv.setImage(new Image("0.png"));
				smileButton.setImage(new Image("gasp.png"));
			}

		});

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

	public static void uncover(MinesweeperCell MinesweeperCell) {

		if (MinesweeperCell.covered && !MinesweeperCell.flagged) {

			MinesweeperCell.covered = false;

			if (!MinesweeperCell.containsMine) {

				// Uncover current mine
				MinesweeperCell.iv.setImage(new Image(MinesweeperCell.adjacentMines + ".png"));

				// Uncover adjacent mines
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
				
				smileButton.setImage(new Image("smile.png"));
				uncovered++;
				score.setText(calculateScore() + "");

			} else {

				MinesweeperCell.iv.setImage(new Image("mine.png"));

				gameOver(calculateScore());

			}

		}
	}

	public void flag() {

		if (covered) {

			if (flagged) {
				iv.setImage(new Image("cell.png"));
				flagged = false;
			} else {
				iv.setImage(new Image("flag.png"));
				flagged = true;
			}

		}

	}

	public static void checkForWin() {
		// Loop through grid and check if every mine is marked
		for (int r = 0; r < buttons.length; r++) {

			for (int c = 0; c < buttons[0].length; c++) {

				if ((buttons[r][c].containsMine && !buttons[r][c].flagged) || (buttons[r][c].covered && !buttons[r][c].containsMine)) {

					return;

				}

			}

		}

		win();

	}
	
	public static int calculateScore() {
		return (int) (DIFFICULTY*WIDTH*HEIGHT*uncovered/100);
	}

	public static void win() {

		Timer.setTimer(false);
		
		int score = calculateScore();
		
		HighscoreTable.newMinesweeperScore(score);
		
		

		// Create new stage for win
		Stage winStage = new Stage();
		winStage.setTitle("Congratulations!");

		VBox vbox = new VBox();
		vbox.setPadding(new Insets(20));

		vbox.setStyle("-fx-text-size:20");

		Button startOver = new Button();
		startOver.setText("Play Again");
		startOver.setOnAction(e -> {

			startNewGame();
			winStage.close();

		});


		vbox.getChildren().addAll(new Text("You won! Your score is " + score), startOver);

		Scene scene = new Scene(vbox);

		winStage.setOnCloseRequest(e -> {
			startNewGame();
		});

		// Prepare and show about stage
		winStage.initModality(Modality.APPLICATION_MODAL);
		winStage.setScene(scene);
		winStage.sizeToScene();
		winStage.setResizable(false);
		winStage.show();
		
	}

	public static void gameOver(int score) {

		Timer.setTimer(false);
		
		

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Create new stage for game over screen
		Stage gameOverStage = new Stage();
		gameOverStage.setTitle("Game Over");

		VBox vbox = new VBox(new Text("Game Over!"), new Text("Your score was " + score));
		vbox.setPadding(new Insets(20));

		vbox.setStyle("-fx-text-size:20");

		Button startOver = new Button();
		startOver.setText("Play Again");
		startOver.setOnAction(e -> {

			startNewGame();
			gameOverStage.close();

		});

		// Add my personal info
		vbox.getChildren().add(startOver);

		Scene scene = new Scene(vbox);

		gameOverStage.setOnCloseRequest(e -> {
			startNewGame();
		});

		// Prepare and show about stage
		gameOverStage.initModality(Modality.APPLICATION_MODAL);
		gameOverStage.setScene(scene);
		gameOverStage.sizeToScene();
		gameOverStage.setResizable(false);
		gameOverStage.show();
		
		
		smileButton.setImage(new Image("frown.png"));
	}

}
