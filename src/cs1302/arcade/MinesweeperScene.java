package cs1302.arcade;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MinesweeperScene {

	static MinesweeperScene[][] buttons = new MinesweeperScene[16][16];

	private ImageView iv;
	private boolean covered = true;

	private boolean containsMine;
	private boolean flagged = false;
	private int adjacentMines;
	private int listNum;
	private int row, col;

	public static void initializeButtons(GridPane grid) {
		int l = 0;
		for (int r = 0; r < buttons.length; r++) {
			for (int c = 0; c < buttons[0].length; c++) {
				int R = r, C = c;
				buttons[r][c] = new MinesweeperScene(l, R, C, e -> {
					System.out.println(R + ", " + C);
					grid.getChildren().set(buttons[R][C].listNum, buttons[R][C].iv);
				});

				grid.add(buttons[r][c].getImageView(), r, c);

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
	}

	public MinesweeperScene(int l, int r, int c, EventHandler<ActionEvent> onClicked) {

		if (Math.random() * 100 < 10) {
			containsMine = true;
		}

		iv = new ImageView("cell.png");
		iv.setId(l + "");
		iv.setOnMouseClicked(e -> {
			if (e.getButton().equals(MouseButton.PRIMARY)) {
				uncover(this);
			} else if (e.getButton().equals(MouseButton.SECONDARY)) {
				flag();
			}
			
			onClicked.handle(new ActionEvent());
		});

		listNum = l;

		row = r;
		col = c;

	}

	public ImageView getImageView() {
		return iv;
	}

	public static void uncover(MinesweeperScene minesweeperScene) {
		if (minesweeperScene.covered) {

			minesweeperScene.covered = false;

			if (!minesweeperScene.containsMine) {

				minesweeperScene.iv.setImage(new Image(minesweeperScene.adjacentMines + ".png"));

				if (minesweeperScene.adjacentMines == 0) {
					for (int r = Math.max(0, minesweeperScene.row - 1); r <= Math.min(buttons.length - 1, minesweeperScene.row + 1); r++) {

						for (int c = Math.max(0, minesweeperScene.col - 1); c <= Math.min(buttons[r].length - 1,
								minesweeperScene.col + 1); c++) {

							if (!(r == minesweeperScene.row && c == minesweeperScene.col)) {

								uncover(buttons[r][c]);

							}

						}
					}
				}

			} else {

				minesweeperScene.iv.setImage(new Image("mine.png"));

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Create new stage for about
				Stage gameOverStage = new Stage();
				gameOverStage.setTitle("Game Over");

				VBox vbox = new VBox(new Text("Game Over!"));
				vbox.setPadding(new Insets(20));

				vbox.setStyle("-fx-text-size:20");

				Button startOver = new Button();
				startOver.setText("Start Over");
				startOver.setOnAction(e -> {
					gameOverStage.close();
				});

				// Add my personal info
				vbox.getChildren().add(startOver);

				Scene scene = new Scene(vbox);

				// Prepare and show about stage
				gameOverStage.initModality(Modality.APPLICATION_MODAL);
				gameOverStage.setScene(scene);
				gameOverStage.sizeToScene();
				gameOverStage.setResizable(false);
				gameOverStage.show();

			}

		}
	}

	public void flag() {

		if (covered) {

			if (flagged) {
				iv.setImage(new Image("cell.png"));
			} else {
				iv.setImage(new Image("flag.png"));
			}

		}

	}

	public static Scene createMinesweeperScene(Stage stage, Scene mainScene) {
		BorderPane bp = new BorderPane();
		bp.setPadding(new Insets(20));

		// Make toolbar
		HBox toolbar = new HBox();

		Button backButton = new Button();
		backButton.setText("Back");
		backButton.setOnAction(e -> {
			stage.setScene(mainScene);
			stage.sizeToScene();
		});

		toolbar.getChildren().add(backButton);

		bp.setTop(toolbar);

		// Grid
		GridPane grid = new GridPane();

		MinesweeperScene.initializeButtons(grid);

		bp.setCenter(grid);

		Scene minesweeperScene = new Scene(bp);

		return minesweeperScene;
	}

}
