package io.github.happyryan2.puzzlegame.objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import io.github.happyryan2.puzzlegame.game.Game;
import io.github.happyryan2.puzzlegame.game.Stack;
import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.utilities.MousePos;
import io.github.happyryan2.puzzlegame.utilities.MouseClick;
import io.github.happyryan2.puzzlegame.utilities.Screen;
import io.github.happyryan2.puzzlegame.utilities.ResourceLoader;

public class Retractor extends Thing {
	private static Color darkGreen = new Color(0, 128, 50);
	private static Color lightGreen = new Color(50, 200, 150);

	public static Image end = ResourceLoader.loadImage("res/graphics/objects/retractorEnd.png");
	public static Image middle = ResourceLoader.loadImage("res/graphics/objects/retractorMiddle.png");
	public static Image solidArrow = ResourceLoader.loadImage("res/graphics/objects/retractor2.png");
	public static Image outlineArrow = ResourceLoader.loadImage("res/graphics/objects/retractor3.png");

	public Retractor(float x, float y, String dir) {
		super.x = x;
		super.y = y;
		super.origX = x;
		super.origY = y;
		super.dir = dir;
	}
	public Retractor(float x, float y, String dir, boolean isWeak) {
		this(x, y, dir);
		super.isWeak = isWeak;
	}
	public Retractor(float x, float y, String dir, boolean isWeak, boolean extended) {
		this(x, y, dir, isWeak);
		super.extension = extended ? 1 : 0;
		super.origExtension = super.extension;
	}

	public void update() {
		if(super.dir == "none") {
			throw new IllegalStateException("retractor.dir cannot be \"none\"");
		}
		// System.out.println("time moving: " + this.timeMoving + " and is it moving? " + (super.moveDir != "none"));
		// calculate visual position for hitboxes
		// super.height = Game.sizes[(int) Game.levelSize - 2];
		super.height = 0.1;
		int x = (int) (super.x * Game.tileSize) + Game.currentLevel.left;
		int y = (int) (super.y * Game.tileSize) + Game.currentLevel.top;
		int w = (int) (Game.tileSize);
		int h = (int) (Game.tileSize);
		// detect hovering + clicks
		// System.out.println("is it transitioning? " + Game.currentLevel.transitioning());
		if(this.cursorHovered() && !Game.currentLevel.transitioning() && !Game.currentLevel.isComplete()) {
			if(this.canDoSomething() && !Game.currentLevel.paused) {
				Screen.cursor = "hand";
			}
			if(MouseClick.mouseIsPressed) {
				this.onClick();
			}
			if(super.hoverY < Game.tileSize * super.height) {
				super.hoverY ++;
			}
		}
		else if(this.hoverY > 0) {
			super.hoverY --;
		}
		// debug
	}
	public void onClick() {
		if(Game.currentLevel.transitioning() || Game.currentLevel.paused) {
			return;
		}
		Game.currentLevel.clearSelected();
		if(super.extension == 0) {
			if(this.canExtendForward()) { // push something forward
				super.extending = true;
				Game.currentLevel.moveSelected(super.dir);
				Stack.addAction();
			}
			else if(this.canExtendBackward()) { // push itself backward
				super.extending = true;
				super.moveDir = (super.dir == "up" || super.dir == "down") ? (super.dir == "up" ? "down" : "up") : (super.dir == "left" ? "right" : "left");
				Game.currentLevel.moveSelected(super.moveDir);
				Stack.addAction();
			}
		}
		else if(super.extension == 1) {
			if(this.canRetractForward()) { // pull something towards itself
				super.retracting = true;
				Game.currentLevel.moveSelected((super.dir == "up" || super.dir == "down") ? (super.dir == "up" ? "down" : "up") : (super.dir == "left" ? "right" : "left"));
				Stack.addAction();
			}
			else if(this.canRetractBackward()) { // pull itself towards something
				super.retracting = true;
				super.moveDir = super.dir;
				Game.currentLevel.moveSelected(super.dir);
				Stack.addAction();
			}
		}
	}
	public void move() {
		/* Update position */
		if(super.extending) {
			super.extension += Game.animationSpeed;
			if(super.extension >= 1) {
				super.extending = false;
				super.extension = 1;
			}
		}
		else if(super.retracting) {
			super.extension -= Game.animationSpeed;
			if(super.extension <= 0) {
				super.retracting = false;
				super.extension = 0;
			}
		}
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

	public boolean canExtendForward() {
		/* Find out which tiles will be moved */
		Game.currentLevel.clearSelected();
		super.ignoring = true;
		if(super.dir == "left") {
			Game.currentLevel.moveObject(super.x - 1, super.y, "left");
		}
		else if(super.dir == "right") {
			Game.currentLevel.moveObject(super.x + 1, super.y, "right");
		}
		else if(super.dir == "up") {
			Game.currentLevel.moveObject(super.x, super.y - 1, "up");
		}
		else if(super.dir == "down") {
			Game.currentLevel.moveObject(super.x, super.y + 1, "down");
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
			(super.x == 0 && super.dir == "left") ||
			(super.x == Game.currentLevel.width - 1 && super.dir == "right") ||
			(super.y == 0 && super.dir == "up") ||
			(super.y == Game.currentLevel.height - 1 && super.dir == "down")
		) {
			return false;
		}
		return true;
	}
	public boolean canRetractForward() {
		/* Find out which tiles will be moved */
		Game.currentLevel.clearSelected();
		super.ignoring = true;
		if(super.dir == "left") {
			Game.currentLevel.moveObject(super.x - 2, super.y, "right");
		}
		else if(super.dir == "right") {
			Game.currentLevel.moveObject(super.x + 2, super.y, "left");
		}
		else if(super.dir == "up") {
			Game.currentLevel.moveObject(super.x, super.y - 2, "down");
		}
		else if(super.dir == "down") {
			Game.currentLevel.moveObject(super.x, super.y + 2, "up");
		}
		/* Find out if any of the selected tiles cannot be moved */
		String backward = (super.dir == "left" || super.dir == "right") ? (super.dir == "left" ? "right" : "left") : (super.dir == "up" ? "down" : "up");
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
		/* Find out if this is directly in front of a wall */
		if(
			(super.x == 1 && super.dir == "left") ||
			(super.x == Game.currentLevel.width - 2 && super.dir == "right") ||
			(super.y == 1 && super.dir == "up") ||
			(super.y == Game.currentLevel.height - 2 && super.dir == "down")
		) {
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
		/* Find out which tiles will be moved by retracting backward */
		Game.currentLevel.clearSelected();
		super.ignoring = true;
		Game.currentLevel.moveTile(super.x, super.y, super.dir);
		/* Find out if any of the selected tiles cannot be moved */
		boolean foundOne = false;
		for(short i = 0; i < Game.currentLevel.content.size(); i ++) {
			Thing thing = (Thing) Game.currentLevel.content.get(i);
			if(thing.selected) {
				if(foundOne && super.isWeak) {
					return false;
				}
				foundOne = true;
			}
		}
		return true;
	}

	public boolean canDoSomething() {
		if(this.extension == 0) {
			return this.canExtendForward() || this.canExtendBackward();
		}
		else if(this.extension == 1) {
			return this.canRetractForward() || this.canRetractBackward();
		}
		else {
			return false;
		}
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
		g.setColor(lightGreen);
		g.fillRect(vX, (int) (vY + vH + super.hoverY), vW, (int) (Game.tileSize * super.height - super.hoverY));
		g.setColor(darkGreen);
		g.fillRect(vX, (int) (vY + super.hoverY), vW, vH);
	}

	@Override
	public void checkMovement(String dir) {
		/*
		This function selects all the tiles that would be moved if this was pushed in a certain direction.
		*/
		Game.currentLevel.moveTile(super.x, super.y, dir);
		if(super.extension != 0) {
			if(super.dir == "left") {
				Game.currentLevel.moveTile(super.x - 1, super.y, dir);
			}
			else if(super.dir == "right") {
				Game.currentLevel.moveTile(super.x + 1, super.y, dir);
			}
			else if(super.dir == "up") {
				Game.currentLevel.moveTile(super.x, super.y - 1, dir);
			}
			else if(super.dir == "down") {
				Game.currentLevel.moveTile(super.x, super.y + 1, dir);
			}
		}
		String backward = (super.dir == "right" || super.dir == "left") ? (super.dir == "right" ? "left" : "right") : (super.dir == "up" ? "down" : "up");
		if(dir == backward) {
			if(dir == "left") {
				Game.currentLevel.moveObject(super.x + super.extension + 1, super.y, "right");
			}
			else if(dir == "right") {
				Game.currentLevel.moveObject(super.x - super.extension - 1, super.y, "left");
			}
			else if(dir == "up") {
				Game.currentLevel.moveObject(super.x, super.y + super.extension + 1, "down");
			}
			else if(dir == "down") {
				Game.currentLevel.moveObject(super.x, super.y - super.extension - 1, "up");
			}
		}
	}
	public boolean canBePushed(String dir) {
		/* Return false if it is directly next to a wall */
		if(((dir == "left" && super.x == 0) || (dir == "right" && super.x == Game.currentLevel.width - 1) || (dir == "up" && super.y == 0) || (dir == "down" && super.y == Game.currentLevel.height - 1))) {
			return false;
		}
		/* Return false if it is extending near a wall */
		if(super.extension != 0) {
			if(dir == "left" && super.dir == "left" && super.x <= 1) {
				return false;
			}
			if(dir == "right" && super.dir == "right" && super.x >= Game.currentLevel.width - 2) {
				return false;
			}
			if(dir == "up" && super.dir == "up" && super.y <= 1) {
				return false;
			}
			if(dir == "down" && super.dir == "down" && super.y >= Game.currentLevel.height - 2) {
				return false;
			}
		}
		/* Return false if it is stuck to a wall */
		if(dir == "right" && super.dir == "left" && super.x - super.extension == 0) {
			return false;
		}
		if(dir == "left" && super.dir == "right" && super.x + super.extension == Game.levelSize - 1) {
			return false;
		}
		if(dir == "down" && super.dir == "up" && super.y - super.extension == 0) {
			return false;
		}
		if(dir == "up" && super.dir == "down" && super.y + super.extension == Game.levelSize - 1) {
			return false;
		}
		return true;
	}
}
