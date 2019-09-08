package io.github.happyryan2.puzzlegame.game;

import java.awt.Graphics;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

import java.nio.charset.StandardCharsets;
import java.nio.*;
import java.nio.file.*;
import java.io.*;

import io.github.happyryan2.puzzlegame.objects.*;
import io.github.happyryan2.puzzlegame.utilities.*;
import io.github.happyryan2.puzzlegame.levels.*;

public class Game {
	public static boolean debugged = false;
	private static boolean initialized = false;
	public static String state = "start";
	public static List levels = new ArrayList();
	public static int levelOpen = 20;
	public static float levelSize = 0;
	public static float tileSize = 0;
	public static double[] sizes = {0.0625, 0.08, 0.095, 0.1, 0.104, 0.108, 0.111, 0.113, 0.115, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116};
	public static boolean clickBefore = false;
	public static boolean startingLevel = false;
	public static Level currentLevel;
	public static int transition = 255;
	public static int scrollY = 0;
	public static String saveDest = System.getProperty("user.home") + "\\AppData\\Local\\HypocubeTranslocation\\progress.txt";
	public static ImageButton playButton = new ImageButton(Screen.screenW / 2, Screen.screenH / 2, 100, "res/graphics/buttons/play.png", new Color(59, 67, 70), new Color(150, 150, 150)); // (59, 67, 70)
	public static float defaultAnimationSpeed = 0.05f;
	public static float fastAnimationSpeed = 0.1f;
	public static float animationSpeed = defaultAnimationSpeed;
	public static boolean chainUndo = false;
	public static boolean chainUndoLastFrame = false;
	public static boolean lastAction = false;
	public static int timeSinceLastAction = 0;
	public static void updateProgress() {
		// System.out.println("user home directory: " + System.getProperty("user.home"));
		String progress = " ";
		for(short i = 0; i < levels.size(); i ++) {
			Level level = (Level) levels.get(i);
			if(level.completedBefore) {
				progress += level.id + " ";
			}
		}
		// progress = "foobar";
		System.out.println("Writing string to file: \"" + progress + "\"");
		try {
			/* Create the folder if it doesn't already exist */
			if(!Files.exists(Paths.get(System.getProperty("user.home") + "\\AppData\\Local\\HypocubeTranslocation"))) {
				new File(System.getProperty("user.home") + "\\AppData\\Local\\HypocubeTranslocation").mkdirs();
			}
			/* Create the file if it doesn't already exist */
			File file = new File(saveDest);
			file.setWritable(true);
			if(file.createNewFile()) {
				System.out.println("File created");
			}
			else {
				System.out.println("File already exists");
			}
			/* Write progress to file */
			BufferedWriter writer = new BufferedWriter(new FileWriter(saveDest));
		    writer.write(progress);
		    writer.close();
		}
		catch(Exception e) {
			System.out.println("unable to save progress - the following error occured:");
			e.printStackTrace();
		}
	}
	public static void run() {
		if(!initialized) {
			/* Initialize levels */
			initialized = true;
			levels.add(new Level1());
			levels.add(new Level2());
			levels.add(new Level3());
			levels.add(new Level4());
			levels.add(new Level5());
			levels.add(new Level6());
			levels.add(new Level7());
			levels.add(new Level8());
			levels.add(new Level9());
			levels.add(new Level10());
			levels.add(new Level11());
			levels.add(new Level12());
			levels.add(new Level13());
			levels.add(new Level14());
			levels.add(new Level15());
			levels.add(new Level16());
			levels.add(new Level17());
			levels.add(new Level18());
			// levels.add(new Level20());
			/* Load user progress from the file */
			String progress = "";
			try {
				if(Files.exists(Paths.get(saveDest))) {
					System.out.println("File exists!");
					String path = saveDest;
					byte[] encoded = Files.readAllBytes(Paths.get(path));
					progress = new String(encoded, StandardCharsets.US_ASCII);
				}
			}
			catch(Exception e) {
				System.out.println("Progress file not found.");
				// e.printStackTrace();
			}
			System.out.println("Progress: \"" + progress + "\"");
			loop1: for(short i = 0; i < progress.length(); i ++) {
				if(progress.substring(i, i + 1).equals(" ")) {
					loop2: for(short j = (short) (i + 1); j < progress.length(); j ++) {
						if(progress.substring(j, j + 1).equals(" ")) {
							int num = Integer.parseInt(progress.substring(i + 1, j));
							loop3: for(short k = 0; k < levels.size(); k ++) {
								Level level = (Level) levels.get(k);
								if(level.id == num) {
									level.completedBefore = true;
									continue loop1;
								}
							}
						}
					}
				}
			}
			/* Update level connectors */
			LevelSelect.init();
			for(short i = 0; i < LevelSelect.levelConnectors.size(); i ++) {
				LevelConnector connector = (LevelConnector) LevelSelect.levelConnectors.get(i);
				connector.init();
				for(short j = 0; j < levels.size(); j ++) {
					Level level = (Level) levels.get(j);
					if(level.id == connector.previousLevel && level.completedBefore) {
						connector.animationProgress = connector.size;
					}
				}
			}
			chainUndoLastFrame = chainUndo;
		}
		if(state == "start") {
			/* update the home page */
			playButton.update();
			playButton.x = Screen.screenW / 2;
			playButton.y = Screen.screenH / 2;
			if(playButton.pressed) {
				/* Move to the level select screen */
				transition = 255;
				state = "level-select";
				/* Find out where to start the screen scrolling */
				boolean completedAll = true;
				for(short i = 0; i < levels.size(); i ++) {
					Level level = (Level) levels.get(i);
					if(!level.completedBefore) {
						System.out.println("User has not completed level " + level.id);
						completedAll = false;
						break;
					}
				}
				if(completedAll) {
					int highestIndex = 0;
					for(short i = 0; i < levels.size(); i ++) {
						Level highest = (Level) levels.get(highestIndex);
						Level level = (Level) levels.get(i);
						if(level.id > highest.id) {
							highestIndex = i;
						}
					}
					Level lastLevel = (Level) levels.get(highestIndex);
					LevelSelect.scrollX = -lastLevel.x - 50;
					LevelSelect.scrollY = -lastLevel.y - 50;
				}
				else {
					int lowestIncompleteIndex = levels.size() - 1;
					for(short i = 0; i < levels.size(); i ++) {
						Level lowest = (Level) levels.get(lowestIncompleteIndex);
						Level level = (Level) levels.get(i);
						if(!level.completedBefore && level.id < lowest.id) {
							lowestIncompleteIndex = i;
						}
					}
					Level lowestIncomplete = (Level) levels.get(lowestIncompleteIndex);
					System.out.println("Lowest level: " + lowestIncomplete.id);
					LevelSelect.scrollX = -lowestIncomplete.x - 50;
					LevelSelect.scrollY = -lowestIncomplete.y - 50;
				}
			}
		}
		else if(state == "level-select") {
			/* New level select menu */
			LevelSelect.update();
		}
		else if(state == "play") {
			for(short i = 0; i < levels.size(); i ++) {
				Level level = (Level) levels.get(i);
				if(level.id == levelOpen) {
					currentLevel = level;
					level.update();
				}
			}
		}
		else if(state == "level-editor") {
			LevelEditor.update();
		}
		clickBefore = MouseClick.mouseIsPressed;
	}
	public static void display(Graphics g) {
		if(!initialized) {
			return;
		}
		if(state == "start") {
			/* Main box for GUI */
			g.setColor(new Color(190, 190, 190));
			g.fillRect(Screen.screenW / 4, 0, Screen.screenW / 2, Screen.screenH);
			/* Title */
			g.setColor(new Color(59, 67, 70));
			g.setFont(Screen.fontOxygen.deriveFont(50f));
			if(g.getFontMetrics().stringWidth("Hypocube Translocation") > Screen.screenW / 2 - 25) {
				Screen.centerText(g, Screen.screenW / 2, Screen.screenH / 4 - 40, "Hypocube");
				Screen.centerText(g, Screen.screenW / 2, Screen.screenH / 4 + 40, "Translocation");
			}
			else {
				Screen.centerText(g, Screen.screenW / 2, Screen.screenH / 4, "Hypocube Translocation");
			}
			/* Play button */
			playButton.display(g);
		}
		else if(state == "level-select") {
			/* New level select menu */
			LevelSelect.display(g);
		}
		else if(state == "play") {
			for(short i = 0; i < levels.size(); i ++) {
				Level level = (Level) levels.get(i);
				if(level.id == levelOpen) {
					level.display(g);
				}
			}
		}
		else if(state == "level-editor") {
			LevelEditor.display(g);
		}
		// transitions
		g.setColor(new Color(255, 255, 255, transition));
		g.fillRect(0, 0, Screen.screenW, Screen.screenH);
		transition -= (transition > 0) ? 10 : 0;
		transition = (transition < 0) ? 0 : transition;
	}
}
