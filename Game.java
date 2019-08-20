package com.happyryan2.game;

import java.awt.Graphics;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.io.IOException;

import com.happyryan2.objects.*;
import com.happyryan2.utilities.*;
import com.happyryan2.levels.*;

public class Game {
	private static boolean initialized = false;
	public static String state = "level-editor";
	public static List levels = new ArrayList();
	public static int levelOpen = 20;
	public static float levelSize = 0;
	public static float tileSize = 0;
	public static double[] sizes = {0.0625, 0.08, 0.095, 0.1, 0.104, 0.108, 0.111, 0.113, 0.115, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116};
	public static boolean clickBefore = false;
	public static boolean canClick = true;
	public static boolean startingLevel = false;
	public static Level currentLevel;
	public static int transition = 255;
	public static int scrollY = 0;
	public static void run() {
		if(!initialized) {
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
			// levels.add(new Level20());
			try {
				String path = "progress.txt";
				String progress = Files.readString(Paths.get(path), StandardCharsets.US_ASCII);
				System.out.println("user progress: " + progress);
				for(int i = 0; i < progress.length(); i ++) {
					/* Find a space */
					if(progress.substring(i, i + 1) == " ") {
						for(int j = i; i < progress.length(); j ++) {
							if(progress.substring(j, j + 1) == " ") {
								int levelId = Integer.parseInt(progress.substring(i, j));
								System.out.println("user completed level " + levelId);
								for(int k = 0; k < levels.size(); k ++) {
									Level level = (Level) levels.get(k);
									int levelNum = level.id;
									if(levelNum == levelId) {
										level.completedBefore = true;
									}
								}
							}
						}
					}
				}
			}
			catch(IOException e) {
				System.out.println("Could not find progress file");
			}
		}
		if(state == "home") {
			// display the home page
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
		if(state == "home") {

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
