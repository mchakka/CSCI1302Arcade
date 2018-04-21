package cs1302.arcade;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class HighscoreTable {

	static int[] breakoutScores = new int[5]; // High scores from Breakout
	static int[] minesweeperScores = new int[5]; // High scores from Minesweeper

	public static void getScores() {

		File hsFile = new File("src/highscores.txt");

		try {

			if (hsFile.exists()) {

				// Get high scores from file
				Scanner file = new Scanner(hsFile);

				for (int i = 0; i < breakoutScores.length; i++) {

					breakoutScores[i] = file.nextInt();

				}

				for (int i = 0; i < minesweeperScores.length; i++) {

					minesweeperScores[i] = file.nextInt();

				}

				file.close();

			} else {

				// Create file and initialize high scores to zero
				BufferedWriter writer = new BufferedWriter(new FileWriter(hsFile));

				for (int i = 0; i < 10; i++) {
					writer.write("0\n");
				}

				writer.close();

				getScores();

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void writeScoresToFile() {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(new File("src/highscores.txt")));

			for(int x : breakoutScores) {
				writer.write(x + "\n");
			}
			
			for(int x : minesweeperScores) {
				writer.write(x + "\n");
			}

			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void newBreakoutScore(int score) {

		for (int i = breakoutScores.length - 1; i >= 0; i--) {

			if (score > breakoutScores[i]) {
				if ( (i == 0) || score <= breakoutScores[i - 1]) {
					
					breakoutScores[i] = score;
					break;
					
				}
			}

		}
		
		writeScoresToFile();

	}

	public static void newMinesweeperScore(int score) {

		for (int i = minesweeperScores.length - 1; i >= 0; i--) {

			if (score > minesweeperScores[i]) {
				if ( (i == 0) || score <= minesweeperScores[i - 1]) {
					
					
					for(int i2 = minesweeperScores.length - 2; i2 >= i; i2--) {
						
						minesweeperScores[i2 + 1] = minesweeperScores[i2];
					
					}
					
					minesweeperScores[i] = score;
					
					break;
					
				}
			}

		}
		
		
		writeScoresToFile();

	}

}
