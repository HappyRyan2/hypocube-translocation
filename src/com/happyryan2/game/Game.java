package com.happyryan2.game;

import java.awt.Graphics;
import java.util.List;
import java.util.ArrayList;

import com.happyryan2.objects.*;
import com.happyryan2.packs.*;
import com.happyryan2.utilities.*;
import com.happyryan2.levels.*;

public class Game {
	private static boolean initialized = false;
	public static String state = "play";
	public static List levelPacks = new ArrayList();
	public static int packOpen = 0;
	public static int levelOpen = 0;
	public static float levelSize = 0;
	public static float tileSize = 0;
	public static double[] sizes = {0.0625, 0.08, 0.095, 0.1, 0.104, 0.108, 0.111, 0.113, 0.115, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116, 0.116};
	public static boolean clickBefore = false;
	public static boolean canClick = true;
	public static Level currentLevel;
	public static void run() {
		if(!initialized) {
			initialized = true;
			levelPacks.add(new TutorialPack());
			levelPacks.add(new MicroPack());
		}
		if(state == "home") {
			// display the home page
		}
		else if(state == "play") {
			// display the world
			LevelPack pack = (LevelPack) levelPacks.get(packOpen);
			currentLevel = (Level) pack.levels.get(levelOpen);
			currentLevel.update();
			System.out.println("updating the level");
		}
		clickBefore = MouseClick.mouseIsPressed;
	}
	public static void display(Graphics g) {
		if(!initialized) {
			return;
		}
		LevelPack pack = (LevelPack) levelPacks.get(packOpen);
		Level level = (Level) pack.levels.get(levelOpen);
		level.display(g);
	}
}
