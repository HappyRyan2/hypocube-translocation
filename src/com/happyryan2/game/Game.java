package com.happyryan2.game;

import java.awt.Graphics;
import java.util.List;
import java.util.ArrayList;

// import com.happyryan2.levels.*;
import com.happyryan2.packs.*;

public class Game {
	private static boolean initialized = false;
	public static String state = "play";
	public static List levelPacks = new ArrayList();
	public static int packOpen = 0;
	public static int levelOpen = 0;
	public static float levelSize = 0;
	public static float tileSize = 0;
	public static void run() {
		if(!initialized) {
			initialized = true;
			levelPacks.add(new TutorialPack());
		}
		if(state == "home") {
			// display the home page
		}
		else if(state == "play") {
			// display the world
			LevelPack pack = (LevelPack) levelPacks.get(packOpen);
			Level level = (Level) pack.levels.get(levelOpen);
			level.update();
		}
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
