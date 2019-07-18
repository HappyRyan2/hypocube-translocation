package com.happyryan2.game;

import java.awt.Graphics;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

import com.happyryan2.objects.*;
import com.happyryan2.packs.*;
import com.happyryan2.utilities.*;
import com.happyryan2.levels.*;

public class Game {
	private static boolean initialized = false;
	public static String state = "level-select";
	public static List levelPacks = new ArrayList();
	public static List levels = new ArrayList();
	public static int packOpen = 0;
	public static int levelOpen = 0;
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
		// System.out.println("can you click? " + canClick);
		// System.out.println("undo stack size: " + Stack.stack.size());
		if(!initialized) {
			initialized = true;
			// levelPacks.add(new TutorialPack());
			// levelPacks.add(new IntroPack());
			// levelPacks.add(new ChallengePack());
			// levelPacks.add(new MicroPack());
			levels.add(new Tutorial1());
			levels.add(new Tutorial2());
			levels.add(new Tutorial3());
			levels.add(new Tutorial4());
			levels.add(new Tutorial5());
			levels.add(new Intro1());
			levels.add(new Intro2());
			levels.add(new Intro3());
		}
		if(state == "home") {
			// display the home page
		}
		else if(state == "select-levelpack") {
			for(byte i = 0; i < levelPacks.size(); i ++) {
				LevelPack pack = (LevelPack) levelPacks.get(i);
				pack.updateLevelInfo((i * 125) + 100);
				if(pack.playButton.pressed) {
					transition = 255;
					state = "select-level";
					packOpen = i;
				}
			}
		}
		else if(state == "select-level") {
			/* Old level select menu */
			LevelPack pack = (LevelPack) levelPacks.get(packOpen);
			pack.updateLevelSelect();
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
		else if(state == "select-levelpack") {
			// display the level pack selection screen
			for(byte i = 0; i < levelPacks.size(); i ++) {
				LevelPack pack = (LevelPack) levelPacks.get(i);
				pack.displayLevelInfo(g, (i * 125) + 100);
			}
		}
		else if(state == "select-level") {
			/* Old level select menu */
			LevelPack pack = (LevelPack) levelPacks.get(packOpen);
			pack.displayLevelSelect(g);
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
		g.fillRect(0, 0, 800, 800);
		transition -= (transition > 0) ? 10 : 0;
		transition = (transition < 0) ? 0 : transition;
	}
}
