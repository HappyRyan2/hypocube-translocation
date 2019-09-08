package io.github.happyryan2.puzzlegame.objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import io.github.happyryan2.puzzlegame.game.Game;
import io.github.happyryan2.puzzlegame.game.Stack;
import io.github.happyryan2.puzzlegame.game.StackItem;
import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.utilities.MousePos;
import io.github.happyryan2.puzzlegame.utilities.MouseClick;
import io.github.happyryan2.puzzlegame.utilities.Screen;
import io.github.happyryan2.puzzlegame.utilities.ResourceLoader;

public class LongExtender extends Thing {
	public float x;
	public float y;
	public String dir;
	public int timeExtending = 0;
	public int timeRetracting = 0;

	public static Image end = ResourceLoader.loadImage("res/graphics/objects/longExtenderEnd.png");
	public static Image middle = ResourceLoader.loadImage("res/graphics/objects/longExtenderMiddle.png");
	public static Image solidArrow = ResourceLoader.loadImage("res/graphics/objects/longExtender2.png");
	public static Image outlineArrow = ResourceLoader.loadImage("res/graphics/objects/longExtender3.png");

	public LongExtender(float x, float y, String dir) {
		super.x = x;
		super.y = y;
		super.origX = x;
		super.origY = y;
		super.dir = dir;
	}
	public LongExtender(float x, float y, String dir, boolean isWeak) {
		this(x, y, dir);
		super.isWeak = isWeak;
	}
	public LongExtender(float x, float y, String dir, boolean isWeak, int extension) {
		this(x, y, dir, isWeak);
		super.extension = extension;
		super.origExtension = super.extension;
	}

	public void update() {
		// calculate visual position for hitboxes
		super.height = 0.1;
		int x = (int) (super.x * Game.tileSize) + Game.currentLevel.left;
		int y = (int) (super.y * Game.tileSize) + Game.currentLevel.top;
		int w = (int) (Game.tileSize);
		int h = (int) (Game.tileSize);
		// detect hovering + clicks
		if(!Game.currentLevel.transitioning() && this.cursorHovered() && !Game.currentLevel.isComplete() && !Game.currentLevel.paused) {
			if(MouseClick.mouseIsPressed) {
				this.onClick();
			}
			// System.out.println("Can it do something? " + this.canDoSomething());
			if(this.canDoSomething()) {
				Screen.cursor = "hand";
			}
		}
		else if(this.hoverY > 0) {
			super.hoverY --;
		}
	}
	public void onClick() {
		if(Game.currentLevel.transitioning() || Game.currentLevel.paused) {
			return;
		}
		int x = (int) (super.x * Game.tileSize) + Game.currentLevel.left;
		int y = (int) (super.y * Game.tileSize) + Game.currentLevel.top;
		int w = (int) (Game.tileSize);
		int h = (int) (Game.tileSize);
		this.ignoring = true;
		if(super.extension == 0) {
			System.out.println("Clicked - Extending");
			boolean forwards = this.canExtendForward();
			boolean backwards = this.canExtendBackward();
			if(this.canExtendForward()) {
				Game.animationSpeed = Game.fastAnimationSpeed;
				super.extending = true;
				Game.currentLevel.moveSelected(super.dir);
				Stack.addAction();
				this.timeExtending = 0;
			}
			else if(this.canExtendBackward()) {
				this.timeExtending = 0;
				Game.animationSpeed = Game.fastAnimationSpeed;
				super.extending = true;
				super.moveDir = (super.dir == "up" || super.dir == "down") ? (super.dir == "up" ? "down" : "up") : (super.dir == "left" ? "right" : "left");
				Game.currentLevel.moveSelected((super.dir == "up" || super.dir == "down") ? (super.dir == "up" ? "down" : "up") : (super.dir == "left" ? "right" : "left"));
				Stack.addAction();
				// System.out.println("Clicked - adding an action");
			}
		}
		else if(this.canRetractForward()) {
			System.out.println("Clicked - Retracting");
			Game.animationSpeed = Game.fastAnimationSpeed;
			super.retracting = true;
			this.timeRetracting = 0;
			Game.currentLevel.moveSelected((super.dir == "up" || super.dir == "down") ? (super.dir == "up" ? "down" : "up") : (super.dir == "left" ? "right" : "left")); // move selected tiles in the opposite direction of super.dir
			Stack.addAction();
			((Stack) Stack.stack.get(Stack.stack.size() - 1)).lastAction = true;
			Stack.setLastFinal(true);
			Stack.setLastChain(true);
			System.out.println("That action was the last");
			// System.out.println("Clicked - adding an action");
		}
		this.ignoring = false;
	}
	public void move() {
		if(super.extending) {
			this.timeExtending ++;
			super.extension += Game.animationSpeed;
			super.extension = Math.round(super.extension / (float) (Game.animationSpeed)) * Game.animationSpeed;
			if(Game.chainUndo) {
				// System.out.println("Extension: " + super.extension + ", Time Extending: " + this.timeExtending);
			}
			if(this.timeExtending >= (1 / Game.animationSpeed)) {
				// System.out.println("Stopping at extension " + super.extension + ". Have been extending for " + );
				for(short i = 0; i < Game.currentLevel.content.size(); i ++) {
					Thing thing = (Thing) Game.currentLevel.content.get(i);
					thing.x = Math.round((float) thing.x);
					thing.y = Math.round((float) thing.y);
				}
				super.extension = Math.round(super.extension);
				if(this.canExtendForward()) {
					// System.out.println("  Can extend forward");
					if(!Game.chainUndo && !Game.chainUndoLastFrame && Game.timeSinceLastAction >= 3 && super.extension >= 1) {
						// System.out.println("  Not doing a chain undo");
						// System.out.println("Extending forward - adding action with extension " + super.extension);
						Game.currentLevel.moveSelected(super.dir);
						this.timeExtending = 0;
						System.out.println("Extending, so added an action at extension " + super.extension + ". Time since last final action: " + Game.timeSinceLastAction);
						Stack.addAction(true);
					}
					else {
						// System.out.println("  Doing a chain undo");
						this.timeExtending = 0;
						super.extending = false;
					}
				}
				else if(this.canExtendBackward()) {
					if(!Game.chainUndo && super.extension >= 1) {
						String backward = (super.dir == "right" || super.dir == "left") ? (super.dir == "right" ? "left" : "right") : (super.dir == "up" ? "down" : "up");
						Game.currentLevel.moveSelected(backward);
						this.timeExtending = 0;
						super.timeMoving = 0;
						super.moveDir = backward;
						// System.out.println("Extending backward - adding action with extension " + super.extension);
						Stack.addAction(true);
					}
					else {
						this.timeRetracting = 0;
						super.retracting = false;
					}
				}
				else {
					// System.out.println("  Cannot extend at all");
					Game.animationSpeed = Game.defaultAnimationSpeed;
					super.extending = false;
					// Stack.printStack();
				}
			}
		}
		else if(super.retracting) {
			this.timeRetracting ++;
			super.extension -= Game.animationSpeed;
			super.extension = Math.round(super.extension / (float) (Game.animationSpeed)) * Game.animationSpeed;
			/* Pause every integer extension to see if it should continue retracting */
			if(this.timeRetracting >= 1 / Game.animationSpeed) {
				// System.out.println("Hit an integer point");
				this.timeRetracting = 0;
				if(super.extension >= 1) {
					if(Game.chainUndo) {
						System.out.println("Hit an integer point (" + super.extension + ") while doing a chain undo");
						super.retracting = false;
					}
					else {
						super.retracting = false;
						if(this.canRetractForward()) {
							super.retracting = true;
							Stack.addAction(true);
							// System.out.println("Retracting forward - adding action with extension " + super.extension);
						}
						else if(this.canRetractBackward()) {
							super.retracting = true;
							super.moveDir = super.dir;
							super.timeMoving = 0;
							Stack.addAction(true);
							// System.out.println("Retracting backward - adding action with extension " + super.extension);
						}
					}
				}
			}
			if(super.extension <= 0) {
				super.retracting = false;
				super.extension = 0;
				Game.animationSpeed = Game.defaultAnimationSpeed;
			}
		}
		if(super.timeMoving > 0 || !super.extending) {
			if(super.moveDir == "up") {
				super.y -= Game.animationSpeed;
			}
			else if(super.moveDir == "down") {
				super.y += Game.animationSpeed;
			}
			else if(super.moveDir == "left") {
				super.x -= Game.animationSpeed;
			}
			else if(super.moveDir == "right") {
				super.x += Game.animationSpeed;
			}
		}
		if(super.moveDir != "none") {
			super.timeMoving ++;
			if(super.timeMoving >= 1 / Game.animationSpeed) {
				super.x = Math.round(super.x);
				super.y = Math.round(super.y);
				super.moveDir = "none";
				super.timeMoving = 0;
			}
		}
	}

	public boolean canExtendForward() {
		/* Find out which tiles will be moved */
		Game.currentLevel.clearSelected();
		super.ignoring = true;
		if(super.dir == "left") {
			Game.currentLevel.moveObject(super.x - 1 - super.extension, super.y, "left");
		}
		else if(super.dir == "right") {
			Game.currentLevel.moveObject(super.x + 1 + super.extension, super.y, "right");
		}
		else if(super.dir == "up") {
			Game.currentLevel.moveObject(super.x, super.y - 1 - super.extension, "up");
		}
		else if(super.dir == "down") {
			Game.currentLevel.moveObject(super.x, super.y + 1 + super.extension, "down");
		}
		/* Find out if any of the selected tiles cannot be moved */
		boolean foundOne = false;
		for(short i = 0; i < Game.currentLevel.content.size(); i ++) {
			Thing thing = (Thing) Game.currentLevel.content.get(i);
			if(thing.selected) {
				if(foundOne && super.isWeak) {
					return false;
				}
				if(!thing.canBePushed(super.dir)) {
					return false;
				}
				foundOne = true;
			}
		}
		/* Find out if this is in front of a wall */
		if(
			(super.x <= super.extension && super.dir == "left") ||
			(super.x >= Game.currentLevel.width - 1 - super.extension && super.dir == "right") ||
			(super.y <= super.extension && super.dir == "up") ||
			(super.y == Game.currentLevel.height - 1 - super.extension && super.dir == "down")
		) {
			return false;
		}
		return true;
	}
	public boolean canRetractForward() {
		Game.currentLevel.clearSelected();
		super.ignoring = true;
		if(super.dir == "left") {
			Game.currentLevel.moveTile(super.x - super.extension, super.y, "right");
		}
		else if(super.dir == "right") {
			Game.currentLevel.moveTile(super.x + super.extension, super.y, "left");
		}
		else if(super.dir == "up") {
			Game.currentLevel.moveTile(super.x, super.y - super.extension, "down");
		}
		else if(super.dir == "down") {
			Game.currentLevel.moveTile(super.x, super.y + super.extension, "up");
		}
		if(Game.currentLevel.numSelected() > 1 && super.isWeak) {
			return false;
		}
		String backwards = (super.dir == "left" || super.dir == "right") ? (super.dir == "left" ? "right" : "left") : (super.dir == "up" ? "down" : "up");
		if(!Game.currentLevel.canSelectedBePushed(backwards)) {
			System.out.println("Cannot retract because of walls");
			System.out.println("Number of tiles selected: " + Game.currentLevel.numSelected());
			System.out.println("This is what is selected: " + Game.currentLevel.selectedToString());
			return false;
		}
		return true;
	}
	public boolean canExtendBackward() {
		/* Find out which tiles will be moved */
		Game.currentLevel.clearSelected();
		super.ignoring = true;
		if(super.dir == "left") {
			Game.currentLevel.moveObject(super.x + 1, super.y, "right");
		}
		else if(super.dir == "right") {
			Game.currentLevel.moveObject(super.x - 1, super.y, "left");
		}
		else if(super.dir == "up") {
			Game.currentLevel.moveObject(super.x, super.y + 1, "down");
		}
		else if(super.dir == "down") {
			Game.currentLevel.moveObject(super.x, super.y - 1, "up");
		}
		/* Find out if any of the selected tiles cannot be moved */
		String backward = (super.dir == "up" || super.dir == "down") ? (super.dir == "up" ? "down" : "up") : (super.dir == "left" ? "right" : "left");
		boolean foundOne = false;
		for(short i = 0; i < Game.currentLevel.content.size(); i ++) {
			Thing thing = (Thing) Game.currentLevel.content.get(i);
			if(thing.selected) {
				if(foundOne && super.isWeak) {
					return false;
				}
				if(!thing.canBePushed(backward)) {
					return false;
				}
				foundOne = true;
			}
		}
		/* Find out if this is in front of a wall */
		if(
			(super.x == 0 && super.dir == "right") ||
			(super.x == Game.currentLevel.width - 1 && super.dir == "left") ||
			(super.y == 0 && super.dir == "down") ||
			(super.y == Game.currentLevel.height - 1 && super.dir == "up")
		) {
			return false;
		}
		return true;
	}
	public boolean canRetractBackward() {
		Game.currentLevel.moveTile(super.x, super.y, super.dir);
		if(Game.currentLevel.numSelected() > 1 && super.isWeak) {
			return false;
		}
		return true;
	}
	public boolean canDoSomething() {
		Game.currentLevel.clearSelected();
		if(super.dir == "none" || Game.currentLevel.transitioning()) {
			return false;
		}
		if(super.extension == 0) {
			return this.canExtendForward() || this.canExtendBackward();
		}
		else if(super.extension % 1 <= 0.01) {
			return this.canRetractForward();
		}
		else {
			return false;
		}
	}

	public void display(Graphics g) {
		/* Calculate position */
		int x = (int) (super.x * Game.tileSize);
		int y = (int) (super.y * Game.tileSize);
		int w = (int) (Game.tileSize);
		int h = (int) (Game.tileSize);
		/* Translate to position */
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform beforeRotation = g2.getTransform();
		g2.translate(x + (w / 2), y + (h / 2));
		/* Rotate to orientation */
		if(super.dir == "right") {
			g2.rotate(Math.toRadians(90));
		}
		else if(super.dir == "left") {
			g2.rotate(Math.toRadians(-90));
		}
		else if(super.dir == "down") {
			g2.rotate(Math.toRadians(180));
		}
		/* Display front + back */
		g2.translate(-(w / 2), 0);
		Screen.scaleImage(g, end, w, h / 2);
		g2.translate(0, -1 * super.extension * Game.tileSize);
		g2.scale(1, -1);
		Screen.scaleImage(g, end, w, h / 2);
		g2.scale(1, -1);
		/* Display middle */
		Screen.scaleImage(g2, middle, w, (int) Math.round(super.extension * Game.tileSize));
		g2.setTransform(beforeRotation);
		/* Translate to position */
		beforeRotation = g2.getTransform();
		g2.translate(x + (w / 2), y + (h / 2));
		if(super.dir == "up") {
			g2.translate(0, super.extension * h / -2);
		}
		else if(super.dir == "down") {
			g2.translate(0, super.extension * h / 2);
		}
		else if(super.dir == "left") {
			g2.translate(super.extension * w / -2, 0);
		}
		else if(super.dir == "right") {
			g2.translate(super.extension * w / 2, 0);
		}
		/* Rotate for different orientations */
		if(super.dir == "right") {
			g2.rotate(Math.toRadians(90));
		}
		else if(super.dir == "down") {
			g2.rotate(Math.toRadians(180));
		}
		else if(super.dir == "left") {
			g2.rotate(Math.toRadians(270));
		}
		/* Display triangle */
		g2.translate(-(w / 2), -(h / 2));
		if(super.isWeak) {
			Screen.scaleImage(g2, outlineArrow, w, h);
		}
		else {
			Screen.scaleImage(g2, solidArrow, w, h);
		}
		g2.setTransform(beforeRotation);
	}
	public boolean cursorHovered() {
		int x = (int) (super.x * Game.tileSize) + Game.currentLevel.left;
		int y = (int) (super.y * Game.tileSize) + Game.currentLevel.top;
		int w = (int) (Game.tileSize);
		int h = (int) (Game.tileSize);
		if(super.dir == "left") {
			return (MousePos.x > x - (Game.tileSize * super.extension) && MousePos.x < x + w && MousePos.y > y && MousePos.y < y + h);
		}
		else if(super.dir == "right") {
			return (MousePos.x > x && MousePos.x < x + w + (Game.tileSize * super.extension) && MousePos.y > y && MousePos.y < y + h);
		}
		else if(super.dir == "up") {
			return (MousePos.x > x && MousePos.x < x + w && MousePos.y > y - (Game.tileSize * super.extension) && MousePos.y < y + h);
		}
		else if(super.dir == "down") {
			return (MousePos.x > x && MousePos.x < x + w && MousePos.y > y && MousePos.y < y + h + (Game.tileSize * super.extension));
		}
		return true; // (just to appease the compiler)
	}
	public void raisedRect(Graphics g, double x, double y, double w, double h) {
		if((super.dir == "right") && x + w >= (super.x * Game.tileSize) + (super.extension * Game.tileSize) + Game.tileSize - Math.max(Game.tileSize * 0.03, 4)) {
			x = (super.x * Game.tileSize) + (super.extension * Game.tileSize) + Game.tileSize - w;
		}
		else if(x + w >= (super.x * Game.tileSize) + Game.tileSize - Math.max(Game.tileSize * 0.03, 4)) {
			x = (super.x * Game.tileSize) + Game.tileSize - w;
		}
		if((super.dir == "down") && y + h >= (super.y * Game.tileSize) + (super.extension * Game.tileSize) + Game.tileSize - Math.max(Game.tileSize * 0.03, 4)) {
			y = (super.y * Game.tileSize) + (super.extension * Game.tileSize) + Game.tileSize - h;
		}
		else if(y + h >= (super.y * Game.tileSize) + Game.tileSize - Math.max(Game.tileSize * 0.03, 4)) {
			y = (super.y * Game.tileSize) + Game.tileSize - h;
		}
		int vX = (int) (x);
		int vY = (int) (y);
		int vW = (int) (w);
		int vH = (int) (h);
		g.setColor(new Color(150, 150, 150));
		g.fillRect(vX, (int) (vY + vH + super.hoverY), vW, (int) (Game.tileSize * super.height - super.hoverY));
		g.setColor(new Color(100, 100, 100));
		g.fillRect(vX, (int) (vY + super.hoverY), vW, vH);
	}
	@Override
	public void checkMovement(String dir) {
		/*
		This function selects all the tiles that would be moved if this was pushed in a certain direction.
		*/
		if(super.dir == "left") {
			for(int i = 0; i < super.extension + 1; i ++) {
				Game.currentLevel.moveTile(super.x - i, super.y, dir);
			}
		}
		else if(super.dir == "right") {
			for(int i = 0; i < super.extension + 1; i ++) {
				Game.currentLevel.moveTile(super.x + i, super.y, dir);
				// System.out.println("Moving: (" + (super.x + i) + ", " + super.y + ")");
			}
		}
		else if(super.dir == "up") {
			for(int i = 0; i < super.extension + 1; i ++) {
				Game.currentLevel.moveTile(super.x, super.y - i, dir);
			}
		}
		else if(super.dir == "down") {
			for(int i = 0; i < super.extension + 1; i ++) {
				Game.currentLevel.moveTile(super.x, super.y + i, dir);
			}
		}
	}
	public boolean canBePushed(String dir) {
		/*
		Returns whether this can be pushed without colliding with a wall. Assumes no other extenders exist.
		*/
		int x = (int) super.x;
		int y = (int) super.y;
		if(super.extension == 0 || super.dir == "none") {
			return Game.currentLevel.canTileBePushed(x, y, dir);
		}
		else {
			if(super.dir == "left") {
				for(int x2 = x; x2 >= x - super.extension; x2 --) {
					if(!Game.currentLevel.canTileBePushed(x2, y, dir)) {
						return false;
					}
				}
			}
			else if(super.dir == "right") {
				for(int x2 = x; x2 <= x + super.extension; x2 ++) {
					if(!Game.currentLevel.canTileBePushed(x2, y, dir)) {
						return false;
					}
				}
			}
			else if(super.dir == "up") {
				for(int y2 = y; y2 >= y - super.extension; y2 --) {
					if(!Game.currentLevel.canTileBePushed(x, y2, dir)) {
						return false;
					}
				}
			}
			else {
				for(int y2 = y; y2 <= y + super.extension; y2 ++) {
					if(!Game.currentLevel.canTileBePushed(x, y2, dir)) {
						return false;
					}
				}
			}
			return true;
		}
	}
}
