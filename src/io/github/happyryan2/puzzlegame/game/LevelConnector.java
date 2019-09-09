/*
This class represents the little white lines that connect levels on the level select.
*/
package io.github.happyryan2.puzzlegame.game;

import java.awt.*;

import io.github.happyryan2.puzzlegame.utilities.Screen;
import io.github.happyryan2.puzzlegame.utilities.Utils;

public class LevelConnector {
	public Point[] coords;
	public int previousLevel; // level that needs to be completed for this to show
	public int nextLevel;
	public int animationProgress = 0;
	public int size = 0;
	public boolean initialized = false;
	public String color = "white"; // "white" or "gray"
	public LevelConnector(Point[] coords, int previousLevel, int nextLevel) {
		this.coords = coords;
		this.previousLevel = previousLevel;
		this.nextLevel = nextLevel;
	}
	public LevelConnector(Point[] coords, int previousLevel, int nextLevel, String color) {
		this.coords = coords;
		this.previousLevel = previousLevel;
		this.nextLevel = nextLevel;
		this.color = color;
	}
	public void init() {
		if(this.initialized) {
			return;
		}
		this.initialized = true;
		/* Calculate total length */
		for(byte i = 0; i < this.coords.length - 1; i ++) {
			Point current = this.coords[i];
			Point next = this.coords[i + 1];
			float dist = (float) Utils.dist(current.x, current.y, next.x, next.y);
			this.size += dist;
		}
	}
	public void display(Graphics g) {
		if(!this.initialized) {
			this.init();
		}
		/* Don't display if previous level has not been won */
		for(byte i = 0; i < Game.levels.size(); i ++) {
			Level level = (Level) Game.levels.get(i);
			if(level.id == this.previousLevel && !level.completedBefore) {
				return;
			}
		}
		/* Update animation */
		if(Game.transition == 0) {
			this.animationProgress ++;
		}
		/* Draw the LevelConnector */
		Graphics2D g2 = (Graphics2D) g;
		if(this.color == "white") {
			g2.setColor(new Color(255, 255, 255));
		}
		else {
			g2.setColor(new Color(170, 170, 170));
		}
		g2.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		int lengthDrawn = 0;
		for(byte i = 0; i < coords.length - 1; i ++) {
			Point current = coords[i];
			Point next = coords[i + 1];
			int lengthRemaining = this.animationProgress - lengthDrawn;
			if(lengthRemaining <= 0) {
				break;
			}
			float dist = (float) Utils.dist(current.x, current.y, next.x, next.y);
			if(lengthRemaining >= dist) {
				/* Draw the whole segment */
				g2.drawLine(current.x, current.y, next.x, next.y);
			}
			else {
				/* Draw only a part of the segment */
				float dx = next.x - current.x;
				float dy = next.y - current.y;
				g2.drawLine(current.x, current.y, (int) (current.x + (dx * lengthRemaining / dist)), (int) (current.y + (dy * lengthRemaining / dist)));
			}
			lengthDrawn += dist;
		}
		/* Change color from gray to white */
		for(short i = 0; i < Game.levels.size(); i ++) {
			Level level = (Level) Game.levels.get(i);
			if(level.id == this.nextLevel) {
				if(level.requireAll && level.canPlay()) {
					this.color = "white";
				}
			}
		}
	}
}
