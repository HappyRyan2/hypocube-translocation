/*
Statically, this class represents the entire background animation for the level select. Instances represent a single grid square.
*/
package io.github.happyryan2.puzzlegame.game;

import java.util.List;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;

public class LevelSelectBackground {
	public static final int GRID_SPACING = 20;
	public static final int ANIMATION_SPEED = 2;
	public static boolean initialized = false;
	public static List grid = new ArrayList();

	public int x;
	public int y;

	public int left = 0;
	public int right = GRID_SPACING;
	public int top = 0;
	public int bottom = GRID_SPACING;

	public int dirLeft = 0;
	public int dirRight = 0;
	public int dirTop = 0;
	public int dirBottom = 0;

	public boolean showLeft = true;
	public boolean showRight = true;
	public boolean showTop = true;
	public boolean showBottom = true;

	public LevelSelectBackground(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public static void init() {
		for(short x = -100; x < 1000; x += GRID_SPACING) {
			for(short y = -100; y < 1000; y += GRID_SPACING) {
				grid.add(new LevelSelectBackground(x, y));
			}
		}
		for(short i = 0; i < grid.size(); i ++) {
			LevelSelectBackground gridSquare = (LevelSelectBackground) grid.get(i);
			if(gridSquare.canMoveRandomly()) {
				// System.out.println("It can move!"); // Run many times
				gridSquare.move();
				gridSquare.fastForward();
			}
		}
		initialized = true;
	}
	public static void updateAll() {
		if(!initialized) {
			init();
		}
		for(int i = 0; i < grid.size(); i ++) {
			LevelSelectBackground gridSquare = (LevelSelectBackground) grid.get(i);
			// gridSquare.update();
		}
	}
	public static void displayAll(Graphics g) {
		for(int i = 0; i < grid.size(); i ++) {
			LevelSelectBackground gridSquare = (LevelSelectBackground) grid.get(i);
			gridSquare.display(g);
		}
	}
	public static void fastForwardAll() {
		for(int i = 0; i < grid.size(); i ++) {
			LevelSelectBackground gridSquare = (LevelSelectBackground) grid.get(i);
			gridSquare.fastForward();
		}
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

	public void display(Graphics g) {
		g.setColor(new Color(150, 150, 150));
		if(this.showTop) {
			g.drawLine(this.x, this.y + this.top, this.x + GRID_SPACING, this.y + this.top);
		}
		if(this.showBottom) {
			g.drawLine(this.x, this.y + this.bottom, this.x + GRID_SPACING, this.y + this.bottom);
		}
		if(this.showLeft) {
			g.drawLine(this.x + this.left, this.y, this.x + this.left, this.y + GRID_SPACING);
		}
		if(this.showRight) {
			g.drawLine(this.x + this.right, this.y, this.x + this.right, this.y + GRID_SPACING);
		}
	}
	public void update() {
		this.left += this.dirLeft;
		this.right += this.dirRight;
		this.top += this.dirTop;
		this.bottom += this.dirBottom;
		if(this.left >= GRID_SPACING) {
			this.left = GRID_SPACING;
			this.dirLeft = 0;
		}
		else if(this.left <= 0) {
			this.left = 0;
			this.dirLeft = 0;
		}
		if(this.right >= GRID_SPACING) {
			this.right = GRID_SPACING;
			this.dirRight = 0;
		}
		else if(this.right <= 0) {
			this.right = 0;
			this.dirRight = 0;
		}
		if(this.top >= GRID_SPACING) {
			this.top = GRID_SPACING;
			this.dirTop = 0;
		}
		else if(this.top <= 0) {
			this.top = 0;
			this.dirTop = 0;
		}
		if(this.bottom >= GRID_SPACING) {
			this.bottom = GRID_SPACING;
			this.dirBottom = 0;
		}
		else if(this.bottom <= 0) {
			this.bottom = 0;
			this.dirBottom = 0;
		}
	}
	public void extend(String dir) {
		System.out.println("dir is " + dir);
		switch(dir) {
			case "left":
				System.out.println("Extending left");
				LevelSelectBackground toLeft = getAtPos(this.x - GRID_SPACING, this.y);
				if(toLeft == null) {
					return;
				}
				toLeft.retract("right");
				this.showLeft = false;
				System.out.println("Not showing left side");
				break;
			case "right":
				System.out.println("Extending right");
				LevelSelectBackground toRight = getAtPos(this.x + GRID_SPACING, this.y);
				if(toRight == null) {
					return;
				}
				toRight.retract("left");
				this.showRight = false;
				System.out.println("Not showing right side");
				break;
			case "up":
				System.out.println("Extending up");
				LevelSelectBackground above = getAtPos(this.x, this.y - GRID_SPACING);
				if(above == null) {
					return;
				}
				above.retract("down");
				this.showTop = false;
				System.out.println("Not showing top");
				break;
			case "below":
				System.out.println("Extending down");
				LevelSelectBackground below = getAtPos(this.x, this.y + GRID_SPACING);
				if(below == null) {
					return;
				}
				below.retract("up");
				this.showBottom = false;
				System.out.println("Not showing bottom");
				break;
		}
	}
	public void retract(String dir) {

	}
	public void move() {
		String shape = this.getShape();
		if(shape == "horizontal-left") {
			this.extend("left");
		}
		else if(shape == "horizontal-right") {
			this.extend("right");
		}
		else if(shape == "vertical-top") {
			this.extend("up");
		}
		else if(shape == "vertical-bottom") {
			this.extend("down");
		}
		else if(shape == "square") {
			System.out.println("It's a square!");
			float chooser = (float) Math.random();
			if(chooser < 0.25 && getAtPos(this.x - GRID_SPACING, this.y) != null) {
				System.out.println("left");
				this.extend("left");
			}
			else if(chooser < 0.5 && getAtPos(this.x + GRID_SPACING, this.y) != null) {
				System.out.println("right");
				this.extend("right");
			}
			else if(chooser < 0.75 && getAtPos(this.x, this.y - GRID_SPACING) != null) {
				System.out.println("up");
				this.extend("up");
			}
			else if(getAtPos(this.x, this.y + GRID_SPACING) != null) {
				System.out.println("down");
				this.extend("down");
			}
		}
	}
	public boolean canMoveRandomly() {
		if(this.getShape() == "horizontal" || this.getShape() == "vertical") {
			return false;
		}
		LevelSelectBackground toRight = getAtPos(this.x + GRID_SPACING, this.y);
		LevelSelectBackground toLeft = getAtPos(this.x - GRID_SPACING, this.y);
		LevelSelectBackground above = getAtPos(this.x, this.y - GRID_SPACING);
		LevelSelectBackground below = getAtPos(this.x, this.y + GRID_SPACING);
		if((toRight != null && toRight.transitioning()) || (toLeft != null && toLeft.transitioning()) || (above != null && above.transitioning()) || (below != null && below.transitioning())) {
			return false;
		}
		return true;
	}
	public String getShape() {
		if(this.transitioning()) {
			return "transitioning";
		}
		LevelSelectBackground toRight = getAtPos(this.x + GRID_SPACING, this.y);
		LevelSelectBackground toLeft = getAtPos(this.x - GRID_SPACING, this.y);
		LevelSelectBackground above = getAtPos(this.x, this.y - GRID_SPACING);
		LevelSelectBackground below = getAtPos(this.x, this.y + GRID_SPACING);
		boolean lineToLeft = ((this.left() == 0 && this.showLeft) || (toLeft != null && toLeft.right() == GRID_SPACING && toLeft.showRight));
		boolean lineToRight = ((this.right() == GRID_SPACING && this.showRight) || (toRight != null && toRight.left() == 0 && toRight.showLeft));
		boolean lineAbove = ((this.top() == 0 && this.showTop) || (above != null && above.bottom() == GRID_SPACING && above.showBottom));
		boolean lineBelow = ((this.bottom() == GRID_SPACING && this.showBottom) || (below != null && below.top() == 0 && below.showTop));
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
		return false;
	}
	public void fastForward() {
		if(this.dirLeft < 0) {
			this.left = 0;
		}
		else if(this.dirLeft > 0) {
			this.left = GRID_SPACING;
		}
		if(this.dirRight < 0) {
			this.right = 0;
		}
		else if(this.dirRight > 0) {
			this.right = GRID_SPACING;
		}
		if(this.dirTop < 0) {
			this.top = 0;
		}
		else if(this.dirTop > 0) {
			this.top = GRID_SPACING;
		}
		if(this.dirBottom < 0) {
			this.bottom = 0;
		}
		else if(this.dirBottom > 0) {
			this.bottom = GRID_SPACING;
		}
	}
	public void fastForwardAdjacent() {
		LevelSelectBackground toLeft = getAtPos(this.x - GRID_SPACING, this.y);
		LevelSelectBackground toRight = getAtPos(this.x + GRID_SPACING, this.y);
		LevelSelectBackground above = getAtPos(this.x, this.y - GRID_SPACING);
		LevelSelectBackground below = getAtPos(this.x, this.y + GRID_SPACING);
		if(toLeft != null) {
			toLeft.fastForward();
		}
		if(toRight != null) {
			toRight.fastForward();
		}
		if(above != null) {
			above.fastForward();
		}
		if(below != null) {
			below.fastForward();
		}
	}
}
