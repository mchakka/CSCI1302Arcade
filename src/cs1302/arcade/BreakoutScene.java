package cs1302.arcade;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Bounds;
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
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class BreakoutScene {

	
	static boolean ready = true;
	
	public static Scene createBreakoutScene(Stage stage, Scene mainScene) {
		Group group = new Group();           // main container
		Scene scene = new Scene(group, 600, 600);

		Button backButton = new Button();
		backButton.setText("Back");
		backButton.setOnAction(e -> {
			stage.setScene(mainScene);
			stage.sizeToScene();
		});
		
		ArrayList<Rectangle> bricks = new ArrayList<Rectangle>();
		
		GridPane gPane = new GridPane();
		
		Rectangle rectangle = new Rectangle();
		rectangle.setX(250);
		rectangle.setY(580);
		group.getChildren().add(rectangle); 
		
		Circle ball = new Circle();
		ball.setCenterX(300);
		ball.setCenterY(569);
		ball.setRadius(10);
		group.getChildren().add(ball); 
		
		for(int i = 50; i <= 500; i = i + 50)
		{
			Rectangle r = new Rectangle();
			r.setX(i);
			r.setY(100);
			r.setWidth(50);
			r.setHeight(20);
			r.setStroke(Color.BLACK);
			r.setFill(Color.BLUE);
			group.getChildren().add(r);
			bricks.add(r);
		}
		
		for(int i = 50; i <= 500; i = i + 50)
		{
			Rectangle r = new Rectangle();
			r.setX(i);
			r.setY(120);
			r.setWidth(50);
			r.setHeight(20);
			r.setStroke(Color.BLACK);
			r.setFill(Color.YELLOW);
			group.getChildren().add(r);
			bricks.add(r);
		}
		
		for(int i = 50; i <= 500; i = i + 50)
		{
			Rectangle r = new Rectangle();
			r.setX(i);
			r.setY(140);
			r.setWidth(50);
			r.setHeight(20);
			r.setStroke(Color.BLACK);
			r.setFill(Color.RED);
			group.getChildren().add(r);
			bricks.add(r);
		}
		
		
		
		
		group.setOnKeyPressed(event -> {
			System.out.println(event);
			
			if (event.getCode() == KeyCode.LEFT && rectangle.getX() >= 10)
				{
					rectangle.setX(rectangle.getX() - 10.0);
					if (ready)
						ball.setCenterX(ball.getCenterX() - 10.0);
				}
			if (event.getCode() == KeyCode.RIGHT && rectangle.getX() <= 490)
				{
					rectangle.setX(rectangle.getX() + 10.0);
					if (ready)
						ball.setCenterX(ball.getCenterX() + 10.0);
				}
			if (event.getCode() == KeyCode.SPACE)
				{
					ready = false;
				}
		    });
		
		rectangle.setWidth(100);
		rectangle.setHeight(10);
		
		ball.setFill(Color.ORANGE);
		rectangle.setFill(Color.BLACK);

		 EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {    
	    		double dx = 10; //Step on x or velocity
	        	double dy = 4; //Step on
	    		public void handle(ActionEvent event) {        
	    			if (!ready) {//move the ball
	            	ball.setCenterX(ball.getCenterX() + dx);
	            	ball.setCenterY(ball.getCenterY() + dy);

	                int rightBound = 600;
	                int topBound = 0;
	                int leftBound = 0;
	                Bounds paddleEnds = rectangle.getBoundsInParent();
	     
	                for(int i =0; i < bricks.size(); i++)
	                {
	                	if (ball.intersects(bricks.get(i).getBoundsInParent()))
	                	{
	                		bricks.get(i).setFill(Color.WHITE);
	                		bricks.get(i).setStroke(Color.WHITE);
	                		bricks.remove(i);
	                		dy = -dy;
	                	}
	                }
	                
	                if((ball.getCenterX() + 10 >= rightBound) || (ball.getCenterX() - 10 <= leftBound))
	                {  
	                	dx = -dx;
	                }
	                
	                if((ball.getCenterY() - 10 <= topBound) || ball.intersects(paddleEnds))
	                {
	                	dy = -dy;
	                }
	    		}
	    	} // handle
	    };
	    
	    KeyFrame keyFrame = new KeyFrame(Duration.millis(39), handler);
	    	     
	    Timeline timeline = new Timeline();
	    timeline.setCycleCount(Timeline.INDEFINITE);
	    timeline.getKeyFrames().add(keyFrame);
	    timeline.play();
		group.requestFocus();
		return scene;
	}
	
	
	
}
