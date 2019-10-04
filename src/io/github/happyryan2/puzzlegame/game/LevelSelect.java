package io.github.happyryan2.puzzlegame.game;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

import io.github.happyryan2.puzzlegame.utilities.ResourceLoader;
import io.github.happyryan2.puzzlegame.utilities.PerlinNoise;
import io.github.happyryan2.puzzlegame.utilities.Screen;
import io.github.happyryan2.puzzlegame.utilities.MouseClick;
import io.github.happyryan2.puzzlegame.utilities.MousePos;

public class LevelSelect {
	public static int scrollX = 400;
	public static int scrollY = 400;

	public static Image completeLevel = ResourceLoader.loadImage("res/graphics/levelselect/completeLevel.png");
	public static Image incompleteLevel = ResourceLoader.loadImage("res/graphics/levelselect/incompleteLevel.png");
	public static Image inaccessibleLevel = ResourceLoader.loadImage("res/graphics/levelselect/inaccessibleLevel.png");

	public static List levelConnectors = new ArrayList();

	private static boolean initialized = false;

	public static void init() {
		if(initialized) {
			return;
		}
		initialized = true;
		levelConnectors.add(new LevelConnector(1, 2));
		levelConnectors.add(new LevelConnector(2, 3));
		levelConnectors.add(new LevelConnector(3, 4));
		levelConnectors.add(new LevelConnector(3, 5));
		levelConnectors.add(new LevelConnector(4, 6));
		levelConnectors.add(new LevelConnector(5, 6));
		levelConnectors.add(new LevelConnector(6, 7));
		levelConnectors.add(new LevelConnector(7, 8));
		levelConnectors.add(new LevelConnector(8, 9));
		levelConnectors.add(new LevelConnector(7, 10));
		levelConnectors.add(new LevelConnector(10, 11));
		levelConnectors.add(new LevelConnector(10, 12));
		levelConnectors.add(new LevelConnector(11, 13));
		levelConnectors.add(new LevelConnector(13, 11));
		levelConnectors.add(new LevelConnector(12, 13));
		levelConnectors.add(new LevelConnector(13, 12));
		levelConnectors.add(new LevelConnector(12, 14));
		levelConnectors.add(new LevelConnector(14, 15));
		levelConnectors.add(new LevelConnector(15, 14));
		levelConnectors.add(new LevelConnector(13, 15));
		levelConnectors.add(new LevelConnector(15, 13));
		levelConnectors.add(new LevelConnector(7, 16));
		levelConnectors.add(new LevelConnector(16, 17));
		levelConnectors.add(new LevelConnector(16, 18));
		levelConnectors.add(new LevelConnector(18, 19));
		levelConnectors.add(new LevelConnector(19, 20));
		levelConnectors.add(new LevelConnector(20, 21));
		levelConnectors.add(new LevelConnector(16, 22));
		levelConnectors.add(new LevelConnector(10, 22));
		levelConnectors.add(new LevelConnector(22, 23));
		levelConnectors.add(new LevelConnector(22, 24));
		levelConnectors.add(new LevelConnector(23, 25));
		levelConnectors.add(new LevelConnector(24, 25));
	}
	public static void display(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.translate(scrollX + (Screen.screenW / 2), scrollY + (Screen.screenH / 2));
		/* animated background */
		LevelSelectBackground.displayAll(g2);
		/* display levels */
		for(short i = 0; i < Game.levels.size(); i ++) {
			Level level = (Level) Game.levels.get(i);
			level.displayLevelSelect(g);
		}
		/* display level connectors */
		for(short i = 0; i < levelConnectors.size(); i ++) {
			LevelConnector connector = (LevelConnector) levelConnectors.get(i);
			connector.display(g);
		}
		g2.translate(-scrollX - (Screen.screenW / 2), -scrollY - (Screen.screenH / 2));
		/* make all levels completed for dev */
		for(short i = 0; i < Game.levels.size(); i ++) {
			Level level = (Level) Game.levels.get(i);
			if(level.id <= 15 && false) {
				level.completedBefore = true;
			}
		}
	}
	public static void update() {
		if(!initialized) {
			init();
		}
		LevelSelectBackground.updateAll();
		for(short i = 0; i < Game.levels.size(); i ++) {
			Level level = (Level) Game.levels.get(i);
			level.updateLevelSelect();
		}
		if(MouseClick.mouseIsPressed) {
			scrollX += (MousePos.x - MousePos.preX);
			scrollY += (MousePos.y - MousePos.preY);
		}
	}

}
