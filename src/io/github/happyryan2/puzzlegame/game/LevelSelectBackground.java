/*
Statically, this class represents the entire background animation for the level select. Instances represent a single grid square.
*/
package io.github.happyryan2.puzzlegame.game;

import java.util.List;
import java.util.ArrayList;

public class LevelSelectBackground {
	public static final int GRID_SPACING = 20;
	public static List grid = new ArrayList();
	public int x;
	public int y;
	public int left = 0;
	public int right = GRID_SPACING;
	public int top = 0;
	public int bottom = GRID_SPACING;
	public boolean showLeft = true;
	public boolean showRight = true;
	public boolean showTop = true;
	public boolean showBottom = true;

	public LevelSelectBackground(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public static void init() {
		for(short x = 0; x < 2400; x += 20) {
			for(short y = 0; y < 2400; y += 20) {
				grid.add(new LevelSelectBackground(x, y));
			}
		}
	}
	public static void run() {

	}
	public static void displayBackground() {

	}
	public static LevelSelectBackground getAtPos(int x, int y) {
		for(short i = 0; i < grid.size(); i ++) {
			LevelSelectBackground gridSquare = (LevelSelectBackground) grid.get(i);
			if(gridSquare.x == x && gridSquare.y == y) {
				return gridSquare;
			}
		}
		return null;
	}

	public void display() {

	}
	public void update() {

	}
	public void extend(String dir) {
		switch(dir) {
			case "left":
				LevelSelectBackground toLeft = 
		}
	}
	public void retract(String dir) {

	}
	public String getShape() {
		if(this.transitioning()) {
			return "transitioning";
		}
		LevelSelectBackground toRight = getAtPos(this.x + 1, this.y);
		LevelSelectBackground toLeft = getAtPos(this.x - 1, this.y);
		LevelSelectBackground above = getAtPos(this.x, this.y - 1);
		LevelSelectBackground below = getAtPos(this.x, this.y + 1);
		boolean lineToLeft = ((this.left() == 0 && this.showLeft) || (toLeft.right() == GRID_SPACING && toLeft.showRight));
		boolean lineToRight = ((this.right() == GRID_SPACING && this.showRight) || (toRight.left() == 0 && toRight.showLeft));
		boolean lineAbove = ((this.top() == 0 && this.showTop) || (above.bottom() == GRID_SPACING && above.showBottom));
		boolean lineBelow = ((this.bottom() == GRID_SPACING && this.showBottom) || (below.top() == 0 && below.showTop));
		if(lineToLeft && lineToRight) {
			if(lineAbove && lineBelow) {
				return "square";
			}
			else if(lineAbove) {
				return "vertical-top";
			}
			else if(lineBelow) {
				return "vertical-bottom";
			}
			else {
				return "vertical";
			}
		}
		if(lineAbove && lineBelow) {
			if(lineToLeft && lineToRight) {
				return "square";
			}
			else if(lineToLeft) {
				return "horizontal-left";
			}
			else if(lineToRight) {
				return "horizontal-right";
			}
			else {
				return "horizontal";
			}
		}
		return null;
	}
	public int left() {
		return Math.min(this.left, this.right);
	}
	public int right() {
		return Math.max(this.left, this.right);
	}
	public int top() {
		return Math.min(this.top, this.bottom);
	}
	public int bottom() {
		return Math.max(this.top, this.bottom);
	}
	public boolean transitioning() {
		if(this.left() != 0 && this.left() != GRID_SPACING && this.showLeft) {
			return true;
		}
		if(this.right() != 0 && this.right() != GRID_SPACING && this.showRight) {
			return true;
		}
		if(this.top() != 0 && this.top() != GRID_SPACING && this.showTop) {
			return true;
		}
		if(this.bottom() != 0 && this.bottom() != GRID_SPACING && this.showBottom) {
			return true;
		}
	}
}
