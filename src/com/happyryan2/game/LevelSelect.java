package com.happyryan2.game;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

import com.happyryan2.utilities.ImageLoader;
import com.happyryan2.utilities.PerlinNoise;
import com.happyryan2.utilities.Screen;
import com.happyryan2.utilities.MouseClick;
import com.happyryan2.utilities.MousePos;

public class LevelSelect {
	public static int scrollX = 400;
	public static int scrollY = 400;

	public static Image completeLevel = ImageLoader.loadImage("res/graphics/levelselect/completeLevel.png");
	public static Image incompleteLevel = ImageLoader.loadImage("res/graphics/levelselect/incompleteLevel.png");
	public static Image inaccessibleLevel = ImageLoader.loadImage("res/graphics/levelselect/inaccessibleLevel.png");

	public static List levelConnectors = new ArrayList();

	private static boolean initialized = false;

	public static void init() {
		levelConnectors.add(new LevelConnector(new Point[] {new Point(50, -10), new Point(50, -90)}, 1));
		levelConnectors.add(new LevelConnector(new Point[] {new Point(110, -150), new Point(190, -150)}, 2));
		levelConnectors.add(new LevelConnector(new Point[] {new Point(310, -150), new Point(390, -150)}, 3));
		levelConnectors.add(new LevelConnector(new Point[] {new Point(510, -150), new Point(590, -150)}, 4));
		levelConnectors.add(new LevelConnector(new Point[] {new Point(650, -90), new Point(650, -10)}, 5));
		levelConnectors.add(new LevelConnector(new Point[] {new Point(710, -150), new Point(790, -150)}, 5));
		levelConnectors.add(new LevelConnector(new Point[] {new Point(710, 50), new Point(790, 50)}, 6));
		levelConnectors.add(new LevelConnector(new Point[] {new Point(850, -90), new Point(850, -10)}, 7));
	}
	public static void display(Graphics g) {
		/* animated noise background */
		try {
			double noiseZ = Screen.frameCount / 80.0f + 0.5;
			for(int x = 0; x < 800; x += 4) {
				for(int y = 0; y < 800; y += 4) {
					double noise = PerlinNoise.noise0To1((x - scrollX) / 40.0f + 0.5, (y - scrollY) / 40.0f + 0.5, noiseZ) * 30 + 120;
					int roundedNoise = (int) Math.round((double) noise);
					Color col = new Color(roundedNoise, roundedNoise, roundedNoise);
					g.setColor(col);
					g.fillRect(x, y, 4, 4);
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
			if(level.id <= 7) {
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
