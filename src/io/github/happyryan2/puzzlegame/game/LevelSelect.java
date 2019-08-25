package io.github.happyryan2.puzzlegame.game;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

import io.github.happyryan2.puzzlegame.utilities.ImageLoader;
import io.github.happyryan2.puzzlegame.utilities.PerlinNoise;
import io.github.happyryan2.puzzlegame.utilities.Screen;
import io.github.happyryan2.puzzlegame.utilities.MouseClick;
import io.github.happyryan2.puzzlegame.utilities.MousePos;

public class LevelSelect {
	public static int scrollX = 400;
	public static int scrollY = 400;

	public static Image completeLevel = ImageLoader.loadImage("res/graphics/levelselect/completeLevel.png");
	public static Image incompleteLevel = ImageLoader.loadImage("res/graphics/levelselect/incompleteLevel.png");
	public static Image inaccessibleLevel = ImageLoader.loadImage("res/graphics/levelselect/inaccessibleLevel.png");

	public static List levelConnectors = new ArrayList();

	private static boolean initialized = false;

	public static void init() {
		levelConnectors.add(new LevelConnector(new Point[] {new Point(50, -10), new Point(50, -90)}, 1, 2));
		levelConnectors.add(new LevelConnector(new Point[] {new Point(110, -150), new Point(190, -150)}, 2, 3));
		levelConnectors.add(new LevelConnector(new Point[] {new Point(310, -150), new Point(390, -150)}, 3, 4));
		levelConnectors.add(new LevelConnector(new Point[] {new Point(510, -150), new Point(590, -150)}, 4, 5));
		levelConnectors.add(new LevelConnector(new Point[] {new Point(650, -90), new Point(650, -10)}, 5, 6));
		levelConnectors.add(new LevelConnector(new Point[] {new Point(710, -150), new Point(790, -150)}, 5, 7));
		levelConnectors.add(new LevelConnector(new Point[] {new Point(710, 50), new Point(790, 50)}, 6, 8));
		levelConnectors.add(new LevelConnector(new Point[] {new Point(850, -90), new Point(850, -10)}, 7, 8));
		levelConnectors.add(new LevelConnector(new Point[] {new Point(910, -150), new Point(990, -150)}, 7, 15, "gray"));
		levelConnectors.add(new LevelConnector(new Point[] {new Point(850, 110), new Point(850, 190)}, 8, 9));
		levelConnectors.add(new LevelConnector(new Point[] {new Point(790, 250), new Point(710, 250)}, 9, 10));
		levelConnectors.add(new LevelConnector(new Point[] {new Point(910, 250), new Point(990, 250)}, 9, 11));
		levelConnectors.add(new LevelConnector(new Point[] {new Point(590, 250), new Point(510, 250)}, 10, 12));
		levelConnectors.add(new LevelConnector(new Point[] {new Point(590, 50), new Point(510, 50)}, 6, 14, "gray"));
		levelConnectors.add(new LevelConnector(new Point[] {new Point(450, 190), new Point(450, 110)}, 12, 14, "gray"));
		levelConnectors.add(new LevelConnector(new Point[] {new Point(1050, 190), new Point(1050, 110)}, 11, 13));
		levelConnectors.add(new LevelConnector(new Point[] {new Point(1050, -10), new Point(1050, -90)}, 13, 15, "gray"));
	}
	public static void display(Graphics g) {
		/* animated noise background */
		try {
			double noiseZ = Screen.frameCount / 80.0f + 0.5;
			for(float x = 0; x < Screen.width(); x += Screen.width() / 200) {
				for(float y = 0; y < Screen.height(); y += Screen.height() / 200) {
					double noise = PerlinNoise.noise0To1((x - scrollX) / 40.0f + 0.5, (y - scrollY) / 40.0f + 0.5, noiseZ) * 30 + 120;
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
		g2.translate(scrollX, scrollY);
		for(short i = 0; i < Game.levels.size(); i ++) {
			Level level = (Level) Game.levels.get(i);
			level.displayLevelSelect(g);
		}
		/* display level connectors */
		for(short i = 0; i < levelConnectors.size(); i ++) {
			LevelConnector connector = (LevelConnector) levelConnectors.get(i);
			connector.display(g);
		}
		g2.translate(-scrollX, -scrollY);
		/* debug + hax */
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
			initialized = true;
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
