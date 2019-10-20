package io.github.happyryan2.puzzlegame.objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import io.github.happyryan2.puzzlegame.game.Game;
import io.github.happyryan2.puzzlegame.game.UndoStack;
import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.utilities.MousePos;
import io.github.happyryan2.puzzlegame.utilities.MouseClick;
import io.github.happyryan2.puzzlegame.utilities.Screen;
import io.github.happyryan2.puzzlegame.utilities.ResourceLoader;

public class Extender extends Thing {
	public static Image block = ResourceLoader.loadImage("res/graphics/objects/emptyExtender.png");
	public static Image end = ResourceLoader.loadImage("res/graphics/objects/extenderEnd.png");
	public static Image middle = ResourceLoader.loadImage("res/graphics/objects/extenderMiddle.png");
	public static Image solidArrow = ResourceLoader.loadImage("res/graphics/objects/extender2.png");
	public static Image outlineArrow = ResourceLoader.loadImage("res/graphics/objects/extender3.png");

	public Extender(float x, float y, String dir) {
		super.x = x;
		super.y = y;
		super.origX = x;
		super.origY = y;
		super.dir = dir;
	}
	public Extender(float x, float y, String dir, boolean isWeak) {
		this(x, y, dir);
		super.isWeak = isWeak;
	}
	public Extender(float x, float y, String dir, boolean isWeak, boolean extended) {
		this(x, y, dir, isWeak);
		super.extension = extended ? 1 : 0;
		super.origExtension = super.extension;
	}
	public Extender(Extender e) {
		this(e.x, e.y, e.dir, e.isWeak);
		this.extension = e.extension;
		this.origExtension = e.origExtension;
	}

	public void update() {
		/* detect hovering + clicks */
		if(!Game.currentLevel.transitioning() && this.cursorHovered() && !Game.currentLevel.isComplete() && !Game.currentLevel.paused && super.dir != "none") {
			if(MouseClick.mouseIsPressed && !MouseClick.pressedBefore) {
				this.onClick();
			}
			if(this.canDoSomething()) {
				Screen.cursor = "hand";
			}
		}
	}
	public void onClick() {
		if(Game.currentLevel.transitioning() || Game.currentLevel.paused) {
			return;
		}
		super.ignoring = true;
		if(super.extension == 0) {
			if(this.canExtendForward()) {
				/* Push tiles in front of it forward */
				Game.animationSpeed = Game.defaultAnimationSpeed;
				super.extending = true;
				Game.currentLevel.moveSelected(super.dir);
				UndoStack.addAction();
			}
			else if(this.canExtendBackward()) {
				/* Push itself backward */
				Game.animationSpeed = Game.defaultAnimationSpeed;
				String backwards = (super.dir == "up" || super.dir == "down") ? (super.dir == "up" ? "down" : "up") : (super.dir == "left" ? "right" : "left");
				super.extending = true;
				super.moveDir = backwards;
				Game.currentLevel.moveSelected(backwards);
				UndoStack.addAction();
			}
		}
		else if(super.extension == 1 && this.canRetractForward()) {
			Game.animationSpeed = Game.defaultAnimationSpeed;
			String backwards = (super.dir == "up" || super.dir == "down") ? (super.dir == "up" ? "down" : "up") : (super.dir == "left" ? "right" : "left");
			super.retracting = true;
			Game.currentLevel.moveSelected(backwards);
			UndoStack.addAction();
		}
		super.ignoring = false;
	}
	public void move() {
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
		if((Game.currentLevel.numSelected() > 1 && super.isWeak) || !Game.currentLevel.canSelectedBePushed(super.dir)) {
			return false;
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
		/* If there are too many tiles than this can move, return false */
		if(Game.currentLevel.numSelected() > 1 && super.isWeak) {
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
		String backwards = (super.dir == "up" || super.dir == "down") ? (super.dir == "up" ? "down" : "up") : (super.dir == "left" ? "right" : "left");
		if((Game.currentLevel.numSelected() > 1) || !Game.currentLevel.canSelectedBePushed(backwards)) {
			return false;
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
	public boolean canDoSomething() {
		Game.currentLevel.clearSelected();
		if(super.dir == "none" || Game.currentLevel.transitioning()) {
			return false;
		}
		if(super.extension == 0) {
			return this.canExtendForward() || this.canExtendBackward();
		}
		else if(super.extension == 1) {
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
		/* Display non-directional extenders (basically just movable blocks) */
		if(super.dir == "none") {
			Graphics2D g2 = (Graphics2D) g;
			g2.translate(x, y);
			Screen.scaleImage(g2, block, w, h);
			g2.translate(-x, -y);
			return;
		}
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
		/* Translate to position (for triangle in the middle) */
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
		else {
			return false;
		}
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
				return Game.currentLevel.canTileBePushed(x, y, dir) && Game.currentLevel.canTileBePushed(x - 1, y, dir);
			}
			else if(super.dir == "right") {
				return Game.currentLevel.canTileBePushed(x, y, dir) && Game.currentLevel.canTileBePushed(x + 1, y, dir);
			}
			else if(super.dir == "up") {
				return Game.currentLevel.canTileBePushed(x, y, dir) && Game.currentLevel.canTileBePushed(x, y - 1, dir);
			}
			else {
				return Game.currentLevel.canTileBePushed(x, y, dir) && Game.currentLevel.canTileBePushed(x, y + 1, dir);
			}
		}
	}
}
