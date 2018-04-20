package cs1302.arcade;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TetrisScene {
	
	
	
	
	public static Scene createTetrisScene(Stage stage, Scene mainScene) {
		BorderPane bp = new BorderPane();
		bp.setPadding(new Insets(40));
				

		Button backButton = new Button();
		backButton.setText("Back");
		backButton.setOnAction(e -> {
			stage.setScene(mainScene);
			stage.sizeToScene();
		});
		
		bp.setTop(backButton);
		
		Scene tetrisScene = new Scene(bp);
		
		
		return tetrisScene;
	}

}
