/*
Statically, this class represents the entire background animation for the level select. Instances represent a single grid square.
*/
package io.github.happyryan2.puzzlegame.game;

import java.util.List;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;

import io.github.happyryan2.puzzlegame.utilities.Screen;

public class LevelSelectBackground {
	public static final byte GRID_SPACING = 25;
	public static final byte ANIMATION_SPEED = 1;
	public static boolean initializedAll = false;
	public static List<List<LevelSelectBackground>> grid = new ArrayList<List<LevelSelectBackground>>();

	public int x;
	public int y;
	public boolean initialized = true;

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

	public String extendTop = "none";
	public String extendBottom = "none";
	public String extendLeft = "none";
	public String extendRight = "none";

	public static void init() {
		List<Point> uninitialized = new ArrayList();
		for(short y = -100; y < 1000; y += GRID_SPACING) {
			List row = new ArrayList();
			for(short x = -100; x < 1000; x += GRID_SPACING) {
				row.add(new LevelSelectBackground(x, y));
			}
			grid.add(row);
		}
		for(short i = 0; i < grid.size(); i ++) {
			List row = grid.get(i);
			for(short j = 0; j < row.size(); j ++) {
				LevelSelectBackground gridSquare = (LevelSelectBackground) row.get(j);
				uninitialized.add(new Point(gridSquare.x, gridSquare.y));
			}
		}
		while(uninitialized.size() > 0) {
			/* Pick an uninitialized location at random and initialize it */
			int randomIndex = (int) (Math.random() * (uninitialized.size() - 1));
			Point currentPoint = uninitialized.get(randomIndex);
			LevelSelectBackground gridSquare = getAtPos(currentPoint.x, currentPoint.y);
			if(!gridSquare.transitioning() && !gridSquare.adjacentTransitioning()) {
				gridSquare.move();
			}
			uninitialized.remove(randomIndex);
		}
		initializedAll = true;
	}
	public static void updateAll() {
		if(!initializedAll) {
			init();
		}
		for(int i = 0; i < grid.size(); i ++) {
			List row = grid.get(i);
			for(int j = 0; j < row.size(); j ++) {
				LevelSelectBackground gridSquare = (LevelSelectBackground) row.get(j);
				if(Math.random() < 0.005 && !gridSquare.transitioning() && !gridSquare.adjacentTransitioning()) {
					gridSquare.move();
				}
			}
		}
		for(int i = 0; i < grid.size(); i ++) {
			List row = grid.get(i);
			for(int j = 0; j < row.size(); j ++) {
				LevelSelectBackground gridSquare = (LevelSelectBackground) row.get(j);
				gridSquare.update();
			}
		}
	}
	public static void displayAll(Graphics g) {
		for(int i = 0; i < grid.size(); i ++) {
			List row = grid.get(i);
			for(int j = 0; j < row.size(); j ++) {
				LevelSelectBackground gridSquare = (LevelSelectBackground) row.get(j);
				gridSquare.display(g);
			}
		}
	}
	public static void fastForwardAll() {
		for(int i = 0; i < grid.size(); i ++) {
			List row = grid.get(i);
			for(int j = 0; j < row.size(); j ++) {
				LevelSelectBackground gridSquare = (LevelSelectBackground) row.get(j);
				gridSquare.fastForward();
			}
		}
	}
	public static void removeUnderLevel(Level level) {
		/* Removes the grid squares that would be covered up by the level for better performance */
		for(int i = 0; i < grid.size(); i ++) {
			List row = grid.get(i);
			for(int j = 0; j < row.size(); j ++) {
				LevelSelectBackground gridSquare = (LevelSelectBackground) row.get(j);
				if(gridSquare.x >= level.x && gridSquare.x + GRID_SPACING <= level.x + 100 && gridSquare.y >= level.y && gridSquare.y + GRID_SPACING <= level.y + 100) {
					row.remove(j);
					j --;
				}
			}
		}
	}
	public static LevelSelectBackground getAtPos(int x, int y) {
		for(short i = 0; i < grid.size(); i ++) {
			List row = grid.get(i);
			if(((LevelSelectBackground) row.get(0)).y == y) {
				for(short j = 0; j < row.size(); j ++) {
					LevelSelectBackground gridSquare = (LevelSelectBackground) row.get(j);
					if(gridSquare.x == x) {
						return gridSquare;
					}
				}
			}
		}
		return null;
	}

	public LevelSelectBackground(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public void display(Graphics g) {
		int x = (int) this.x + (LevelSelect.scrollX * 1) + (Screen.screenW / 2);
		int y = (int) this.y + (LevelSelect.scrollY * 1) + (Screen.screenH / 2);
		if(x < -GRID_SPACING - 5 || x > Screen.screenW + 5 || y < -GRID_SPACING - 5 || y > Screen.screenH + 5) {
			return;
		}
		g.setColor(new Color(150, 150, 150));
		if(this.showLeft) {
			switch (this.extendLeft) {
				case "none":
					g.drawLine(this.x + this.left, this.y, this.x + this.left, this.y + GRID_SPACING);
					break;
				case "up":
					g.drawLine(this.x + this.left, this.y, this.x + this.left, this.y + this.top);
					break;
				case "down":
					g.drawLine(this.x + this.left, this.y + this.bottom, this.x + this.left, this.y + GRID_SPACING);
					break;
			}
		}
		if(this.showTop) {
			switch(this.extendTop) {
				case "none":
					g.drawLine(this.x, this.y + this.top, this.x + GRID_SPACING, this.y + this.top);
					break;
				case "left":
					g.drawLine(this.x, this.y + this.top, this.x + this.left, this.y + this.top);
					break;
				case "right":
					g.drawLine(this.x + this.right, this.y + this.top, this.x + GRID_SPACING, this.y + this.top);
					break;
			}
		}
		if(this.showBottom) {
			switch(this.extendBottom) {
				case "none":
					g.drawLine(this.x, this.y + this.bottom, this.x + GRID_SPACING, this.y + this.bottom);
					break;
				case "left":
					g.drawLine(this.x, this.y + this.bottom, this.x + this.left, this.y + this.bottom);
					break;
				case "right":
					g.drawLine(this.x + this.right, this.y + this.bottom, this.x + GRID_SPACING, this.y + this.bottom);
					break;
			}
		}
		if(this.showRight) {
			switch (this.extendRight) {
				case "none":
					g.drawLine(this.x + this.right, this.y, this.x + this.right, this.y + GRID_SPACING);
					break;
				case "up":
					g.drawLine(this.x + this.right, this.y, this.x + this.right, this.y + this.top);
					break;
				case "down":
					g.drawLine(this.x + this.right, this.y + this.bottom, this.x + this.right, this.y + GRID_SPACING);
					break;
			}
		}
	}
	public void update() {
		this.left += this.dirLeft;
		this.right += this.dirRight;
		this.top += this.dirTop;
		this.bottom += this.dirBottom;
		if(this.left > GRID_SPACING) {
			this.left = GRID_SPACING;
			this.dirLeft = 0;
			this.showLeft = false;
			this.showRight = true;
			this.extendLeft = "none";
		}
		else if(this.left < 0) {
			this.left = 0;
			this.dirLeft = 0;
			this.extendLeft = "none";
		}
		if(this.right > GRID_SPACING) {
			this.right = GRID_SPACING;
			this.dirRight = 0;
			this.extendRight = "none";
		}
		else if(this.right < 0) {
			this.right = 0;
			this.dirRight = 0;
			this.showRight = false;
			this.showLeft = true;
			this.extendRight = "none";
		}
		if(this.top > GRID_SPACING) {
			this.top = GRID_SPACING;
			this.dirTop = 0;
			this.showTop = false;
			this.showBottom = true;
			this.extendTop = "none";
		}
		else if(this.top < 0) {
			this.top = 0;
			this.dirTop = 0;
			this.extendTop = "none";
		}
		if(this.bottom > GRID_SPACING) {
			this.bottom = GRID_SPACING;
			this.dirBottom = 0;
			this.extendBottom = "none";
		}
		else if(this.bottom < 0) {
			this.bottom = 0;
			this.dirBottom = 0;
			this.showBottom = false;
			this.showTop = true;
			this.extendBottom = "none";
		}
	}
	public void extend(String dir) {
		switch(dir) {
			case "left":
				LevelSelectBackground toLeft = getAtPos(this.x - GRID_SPACING, this.y);
				if(toLeft == null) {
					return;
				}
				toLeft.retract("right");

				this.showLeft = false;
				// this.showRight =
				this.showTop = true;
				this.showBottom = true;
				break;
			case "right":
				LevelSelectBackground toRight = getAtPos(this.x + GRID_SPACING, this.y);
				if(toRight == null) {
					return;
				}
				toRight.retract("left");

				this.showRight = false;
				this.showTop = true;
				this.showBottom = true;
				break;
			case "up":
				LevelSelectBackground above = getAtPos(this.x, this.y - GRID_SPACING);
				if(above == null) {
					return;
				}
				above.retract("down");
				this.showTop = false;

				this.showTop = false;
				this.showLeft = true;
				this.showRight = true;
				break;
			case "down":
				LevelSelectBackground below = getAtPos(this.x, this.y + GRID_SPACING);
				if(below == null) {
					return;
				}
				below.retract("up");

				this.showBottom = false;
				this.showLeft = true;
				this.showRight = true;
				break;
		}
	}
	public void retract(String dir) {
		switch(dir) {
			case "left":
			case "right":
				this.top = 0;
				this.bottom = GRID_SPACING;

				this.dirTop = 0;
				this.dirBottom = 0;

				this.extendTop = (this.showTop ? "none" : dir);
				this.extendBottom = (this.showBottom ? "none" : dir);

				this.showTop = true;
				this.showBottom = true;
				break;
			case "up":
			case "down":
				this.left = 0;
				this.right = GRID_SPACING;

				this.dirLeft = 0;
				this.dirRight = 0;

				this.extendLeft = (this.showLeft ? "none" : dir);
				this.extendRight = (this.showRight ? "none" : dir);

				this.showLeft = true;
				this.showRight = true;
				break;
		}
		switch(dir) {
			case "left":
				this.left = 0;
				this.right = GRID_SPACING;

				this.dirLeft = ANIMATION_SPEED;
				this.dirRight = 0;

				this.showLeft = true;
				// this.showRight = true;

				this.extendLeft = "none";
				this.extendRight = "none";
				break;
			case "right":
				this.left = 0;
				this.right = GRID_SPACING;

				this.dirLeft = 0;
				this.dirRight = -ANIMATION_SPEED;

				// this.showLeft = true;
				this.showRight = true;

				this.extendLeft = "none";
				this.extendRight = "none";
				break;
			case "up":
				this.top = 0;
				this.bottom = GRID_SPACING;

				this.dirTop = ANIMATION_SPEED;
				this.dirBottom = 0;

				this.showTop = true;
				// this.showBottom = true;

				this.extendTop = "none";
				this.extendBottom = "none";
				break;
			case "down":
				this.top = 0;
				this.bottom = GRID_SPACING;

				this.dirTop = 0;
				this.dirBottom = -ANIMATION_SPEED;

				// this.showTop = true;
				this.showBottom = true;

				this.extendTop = "none";
				this.extendBottom = "none";
				break;
		}
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
			float chooser = (float) Math.random();
			if(chooser < 0.25 && getAtPos(this.x - GRID_SPACING, this.y) != null) {
				this.extend("left");
			}
			else if(chooser < 0.5 && getAtPos(this.x + GRID_SPACING, this.y) != null) {
				this.extend("right");
			}
			else if(chooser < 0.75 && getAtPos(this.x, this.y - GRID_SPACING) != null) {
				this.extend("up");
			}
			else if(getAtPos(this.x, this.y + GRID_SPACING) != null) {
				this.extend("down");
			}
		}
		else if(shape == "horizontal") {
			if(Math.random() < 0.5 && getAtPos(this.x, this.y - GRID_SPACING) != null) {
				this.retract("up");
			}
			else if(getAtPos(this.x, this.y + GRID_SPACING) != null) {
				this.retract("down");
			}
		}
		else if(shape == "vertical") {
			if(Math.random() < 0.5 && getAtPos(this.x - GRID_SPACING, this.y) != null) {
				this.retract("left");
			}
			else if(getAtPos(this.x + GRID_SPACING, this.y) != null) {
				this.retract("right");
			}
		}
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
				// System.out.println("top: " + this.top());
				// System.out.println("bottom: " + this.bottom());
				// System.out.println("left: " + this.left());
				// System.out.println("right: " + this.right());
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
	public boolean adjacentTransitioning() {
		LevelSelectBackground toLeft = getAtPos(this.x - GRID_SPACING, this.y);
		LevelSelectBackground toRight = getAtPos(this.x + GRID_SPACING, this.y);
		LevelSelectBackground above = getAtPos(this.x, this.y - GRID_SPACING);
		LevelSelectBackground below = getAtPos(this.x, this.y + GRID_SPACING);
		if((toRight != null && toRight.transitioning()) || (toLeft != null && toLeft.transitioning()) || (above != null && above.transitioning()) || (below != null && below.transitioning())) {
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
		this.dirLeft = 0;
		if(this.dirRight < 0) {
			this.right = 0;
		}
		else if(this.dirRight > 0) {
			this.right = GRID_SPACING;
		}
		this.dirRight = 0;
		if(this.dirTop < 0) {
			this.top = 0;
		}
		else if(this.dirTop > 0) {
			this.top = GRID_SPACING;
		}
		this.dirTop = 0;
		if(this.dirBottom < 0) {
			this.bottom = 0;
		}
		else if(this.dirBottom > 0) {
			this.bottom = GRID_SPACING;
		}
		this.dirBottom = 0;
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
