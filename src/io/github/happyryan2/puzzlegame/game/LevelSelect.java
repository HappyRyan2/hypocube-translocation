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
		levelConnectors.add(new LevelConnector(4, 5));
		levelConnectors.add(new LevelConnector(4, 6));
		levelConnectors.add(new LevelConnector(5, 7));
		levelConnectors.add(new LevelConnector(6, 7));
		levelConnectors.add(new LevelConnector(7, 8));
		levelConnectors.add(new LevelConnector(8, 9));
		levelConnectors.add(new LevelConnector(9, 11));
		levelConnectors.add(new LevelConnector(11, 13, "gray"));
		levelConnectors.add(new LevelConnector(5, 13, "gray"));
		levelConnectors.add(new LevelConnector(8, 10));
		levelConnectors.add(new LevelConnector(10, 12));
		levelConnectors.add(new LevelConnector(10, 22));
		levelConnectors.add(new LevelConnector(10, 19, "gray"));
		levelConnectors.add(new LevelConnector(12, 14));
		levelConnectors.add(new LevelConnector(14, 15));
		levelConnectors.add(new LevelConnector(15, 16));
		levelConnectors.add(new LevelConnector(8, 17));
		levelConnectors.add(new LevelConnector(17, 21));
		levelConnectors.add(new LevelConnector(17, 18));
		levelConnectors.add(new LevelConnector(17, 19, "gray"));
		levelConnectors.add(new LevelConnector(18, 23));
		levelConnectors.add(new LevelConnector(23, 24));
		levelConnectors.add(new LevelConnector(19, 20));
		levelConnectors.add(new LevelConnector(19, 25));
	}
	public static void display(Graphics g) {
		/* animated noise background */
		try {
			double noiseZ = Screen.frameCount / 80.0f + 0.5;
			for(float x = 0; x < Screen.width(); x += Screen.width() / 200) {
				for(float y = 0; y < Screen.height(); y += Screen.height() / 200) {
					double noise = PerlinNoise.noise0To1((x - (scrollX + (Screen.screenW / 2))) / 40.0f + 0.5, (y - (scrollY + (Screen.screenH / 2))) / 40.0f + 0.5, noiseZ) * 30 + 120;
					int roundedNoise = (int) Math.round((double) noise);
					Color col = new Color(roundedNoise, roundedNoise, roundedNoise);
					g.setColor(col);
					g.fillRect((int) x, (int) y, Screen.width() / 200, Screen.height() / 200);
				}
			}
		}
		catch(Exception e) { }
		/* display levels */
		Graphics2D g2 = (Graphics2D) g;
		g2.translate(scrollX + (Screen.screenW / 2), scrollY + (Screen.screenH / 2));
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
