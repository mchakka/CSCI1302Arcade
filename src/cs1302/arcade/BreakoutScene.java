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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class BreakoutScene {

	static Circle ball = new Circle();
	static boolean ready = true;
	static double dx = 10; //Step on x or velocity
	static double dy = 4;
	static int counter=0;
	static Rectangle rectangle = new Rectangle();
	static ArrayList<Rectangle> bricks = new ArrayList<Rectangle>();
	static Group group = new Group();
	static Button backButton = new Button();
	static Button easyButton = new Button();
	static Button hardButton = new Button();
	static Text newone = new Text("Score: " + counter);
	static Timeline timeline = new Timeline();
	public static Stage loadingStage = new Stage();
	static Text difficultyLevel = new Text("Difficulty Level:");
	
	
	public static Scene createBreakoutScene(Stage stage, Scene mainScene) {
		group.getChildren().clear();
		Scene scene = new Scene(group, 600, 600);
		
		easyButton.setLayoutX(400);
		hardButton.setLayoutX(500);
		easyButton.setText("Easy");
		hardButton.setText("Hard");
		Text text2 = new Text("Choose one of the difficulty levels before starting. To release the ball, press spacebar on your keyboard!");
		
		difficultyLevel.setX(400);
		difficultyLevel.setY(50);
		
		newone.setX(200);
		newone.setY(50);
		
		VBox vbox2 = new VBox();
    	vbox2.getChildren().addAll(easyButton, hardButton, text2);
    	Scene scene2 = new Scene(vbox2);
		loadingStage.initModality(Modality.APPLICATION_MODAL);
		loadingStage.setScene(scene2);
		loadingStage.sizeToScene();
		loadingStage.setResizable(false);
	
		
		easyButton.setOnAction( e -> {
			dx =10;
			dy =4;
			difficultyLevel.setText("Difficulty Level: Easy");
			loadingStage.close();
		});
		
		hardButton.setOnAction( e -> {
			dx =30;
			dy =12;
			difficultyLevel.setText("Difficulty Level: Hard");
			loadingStage.close();
		});
		
		 backButton.setText("Back");
			backButton.setOnAction(e -> {
				timeline.pause();
				newone.setText("Score: " + 0);
				newScene();
			

				
				stage.setScene(mainScene);
				stage.sizeToScene();
			});
			
			Stage winStage = new Stage();
			EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {    
	    		 //Step on
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
	                	if (ball.getBoundsInParent().intersects(bricks.get(i).getBoundsInParent()))
	                	{
	                		
	                		
	                	
	                	if(ball.getCenterY()  <= bricks.get(i).getY()  - (20/2))
	                	{
	                		dy = -dy;
	                		
	                	}
	                		  //Hit was from below the brick
	                		if(ball.getCenterY() >= bricks.get(i).getY()  + (20/2))
	                		{
	                			dy = -dy;
	                		
	                		}
	                		  //Hit was from above the brick
	                		if(ball.getCenterX() < bricks.get(i).getX())
	                		{
	                			dx = -dx;
	                			
	                		}
	                		  //Hit was on left
	                		if(ball.getCenterX() > bricks.get(i).getX())
	                		{
	                			dx = -dx;
	                			
	                		}
	                		
	                		
	                		bricks.get(i).setFill(Color.WHITE);
	                		bricks.get(i).setStroke(Color.WHITE);
	                		bricks.remove(i);
	        
	                		int newScore = counter++;
	                		
	                		newone.setText("Score: " + (newScore + 1));
	                		
	                		break;
	                	}
	                	
	                	
	                }
	               
	                if (ball.getCenterY() >= 600)
	        		{
	                	timeline.pause();
	                	VBox vbox = new VBox();
	            		vbox.setPadding(new Insets(20));

	            		vbox.setStyle("-fx-text-size:20");

	            		Button startOver = new Button();
	            		startOver.setText("Play Again");
	            		startOver.setOnAction(e -> {
	            			group.getChildren().clear();
	            			newone.setText("Score: " + 0);
	            			newScene();
	            			winStage.close();
	            			BreakoutScene.loadingStage.show();

	            		});
	            		
	                	vbox.getChildren().addAll(new Text("Game Over! Your score is " + counter), startOver);
	                	
	                	HighscoreTable.newBreakoutScore(counter);

	            		Scene scene = new Scene(vbox);

	            		winStage.setOnCloseRequest(e -> {
	            			newScene();
	            			BreakoutScene.loadingStage.show();
	            		});

	            		// Prepare and show about stage

	            		winStage.setScene(scene);
	            		winStage.sizeToScene();
	            		winStage.setResizable(false);
	            		winStage.show();
	        		}
	                
	                if (counter == 30)
	                {
	                	timeline.pause();
	                	VBox vbox3 = new VBox();
	            		vbox3.setPadding(new Insets(20));

	            		vbox3.setStyle("-fx-text-size:20");

	            		Button startOver = new Button();
	            		startOver.setText("Play Again");
	            		startOver.setOnAction(e -> {
	            			group.getChildren().clear();
	            			newone.setText("Score: " + 0);
	            			newScene();
	            			winStage.close();
	            			BreakoutScene.loadingStage.show();

	            		});
	            		
	                	vbox3.getChildren().addAll(new Text("You Won! Your score is " + counter), startOver);

	            		Scene scene = new Scene(vbox3);
	            		HighscoreTable.newBreakoutScore(counter);
	            		winStage.setOnCloseRequest(e -> {
	            			newScene();
	            			BreakoutScene.loadingStage.show();
	            		});

	            		// Prepare and show about stage
	            		winStage.initModality(Modality.APPLICATION_MODAL);
	            		winStage.setScene(scene);
	            		winStage.sizeToScene();
	            		winStage.setResizable(false);
	            		winStage.show();
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
		    timeline.setCycleCount(Timeline.INDEFINITE);
		    timeline.getKeyFrames().add(keyFrame);
		    
			newScene();	
			
    
		group.requestFocus();
		
		return scene;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	public static void resetBricks()
	{
		group.getChildren().clear();
		bricks.clear();
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
		group.getChildren().addAll(newone);
		group.getChildren().addAll(backButton);
		rectangle.setX(250);
		rectangle.setY(580);
		group.getChildren().add(rectangle); 
		
		ball.setCenterX(300);
		ball.setCenterY(569);
		ball.setRadius(10);
		group.getChildren().add(ball); 
		
		rectangle.setWidth(100);
		rectangle.setHeight(10);
		ball.setFill(Color.ORANGE);
		rectangle.setFill(Color.BLACK);

		group.getChildren().add(difficultyLevel);
		
	}

	
	
	public static void timelinemethod()
	{
	    timeline.play();
	}
	
	public static void newScene()
	{
		timeline.stop();
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
		
 
		resetBricks();
		
		
		ball.setCenterX(300);
		ball.setCenterY(569);
		rectangle.setX(250);
		rectangle.setY(580);
		ready = true;
		counter = 0;
		
		Stage winStage = new Stage();
		 EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {    
    		 //Step on
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
                	if (ball.getBoundsInParent().intersects(bricks.get(i).getBoundsInParent()))
                	{
                		
                		
                	
                	if(ball.getCenterY()  <= bricks.get(i).getY()  - (20/2))
                	{
                		dy = -dy;
                		
                	}
                		  //Hit was from below the brick
                		if(ball.getCenterY() >= bricks.get(i).getY()  + (20/2))
                		{
                			dy = -dy;
                		
                		}
                		  //Hit was from above the brick
                		if(ball.getCenterX() < bricks.get(i).getX())
                		{
                			dx = -dx;
                			
                		}
                		  //Hit was on left
                		if(ball.getCenterX() > bricks.get(i).getX())
                		{
                			dx = -dx;
                			
                		}
                		
                		
                		bricks.get(i).setFill(Color.WHITE);
                		bricks.get(i).setStroke(Color.WHITE);
                		bricks.remove(i);
        
                		int newScore = counter++;
                		
                		newone.setText("Score: " + newScore);
                		
                		break;
                	}
                	
                	
                }
               
                if (ball.getCenterY() >= 600)
        		{
                	timeline.pause();
                	VBox vbox = new VBox();
            		vbox.setPadding(new Insets(20));

            		vbox.setStyle("-fx-text-size:20");

            		Button startOver = new Button();
            		startOver.setText("Play Again");
            		startOver.setOnAction(e -> {
            			group.getChildren().clear();
            			newone.setText("Score: " + 0);
            			
            			newScene();
            			winStage.close();
            			BreakoutScene.loadingStage.show();

            		});
            		
                	vbox.getChildren().addAll(new Text("Game Over! Your score is " + counter), startOver);

            		Scene scene = new Scene(vbox);
            		HighscoreTable.newBreakoutScore(counter);
            		winStage.setOnCloseRequest(e -> {
            			newScene();
            			BreakoutScene.loadingStage.show();
            		});

            		// Prepare and show about stage
            		winStage.initModality(Modality.APPLICATION_MODAL);
            		winStage.setScene(scene);
            		winStage.sizeToScene();
            		winStage.setResizable(false);
            		winStage.show();
        		}
                
                if (counter == 30)
                {
                	timeline.pause();
                	VBox vbox3 = new VBox();
            		vbox3.setPadding(new Insets(20));

            		vbox3.setStyle("-fx-text-size:20");

            		Button startOver = new Button();
            		startOver.setText("Play Again");
            		startOver.setOnAction(e -> {
            			group.getChildren().clear();
            			newone.setText("Score: " + 0);
            			
            			newScene();
            			winStage.close();
            			BreakoutScene.loadingStage.show();

            		});
            		
                	vbox3.getChildren().addAll(new Text("You Won! Your score is " + counter), startOver);

            		Scene scene = new Scene(vbox3);
            		HighscoreTable.newBreakoutScore(counter);
            		winStage.setOnCloseRequest(e -> {
            			newScene();
            			BreakoutScene.loadingStage.show();
            		});

            		// Prepare and show about stage
            		winStage.initModality(Modality.APPLICATION_MODAL);
            		winStage.setScene(scene);
            		winStage.sizeToScene();
            		winStage.setResizable(false);
            		winStage.show();
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
    
    
timelinemethod();

	group.requestFocus();
	
	}

	
	
}
