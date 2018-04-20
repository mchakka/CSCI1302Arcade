package cs1302.arcade;

import java.awt.event.KeyEvent;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

public class BreakoutScene {

	
	
	
	public static Scene createBreakoutScene(Stage stage, Scene mainScene) {
		Group group = new Group();           // main container
		Scene scene = new Scene(group, 600, 600);

		Button backButton = new Button();
		backButton.setText("Back");
		backButton.setOnAction(e -> {
			stage.setScene(mainScene);
			stage.sizeToScene();
		});
		
		GridPane gPane = new GridPane();
		
		
		
		
		Rectangle rectangle = new Rectangle();
		rectangle.setX(250);
		rectangle.setY(580);
		group.getChildren().add(rectangle); 
		
		Circle ball = new Circle();
		ball.setCenterX(300);
		ball.setCenterY(570);
		ball.setRadius(10);
		group.getChildren().add(ball); 
		boolean ready = false;
		
		group.setOnKeyPressed(event -> {
			System.out.println(event);
			if (event.getCode() == KeyCode.LEFT && rectangle.getX() >= 10)
				{
					rectangle.setX(rectangle.getX() - 10.0);
					ball.setCenterX(ball.getCenterX() - 10.0);
				}
			if (event.getCode() == KeyCode.RIGHT && rectangle.getX() <= 490)
				{
					rectangle.setX(rectangle.getX() + 10.0);
					ball.setCenterX(ball.getCenterX() + 10.0);
				}
			if (event.getCode() == KeyCode.RIGHT)
				{
					//hh
				}
		    });
		
		rectangle.setWidth(100);
		rectangle.setHeight(10);
		
		ball.setFill(Color.RED);
		rectangle.setFill(Color.BLACK);
		  
		
		
		group.requestFocus();
		return scene;
	}
}
