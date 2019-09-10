package io.github.happyryan2.puzzlegame.objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import io.github.happyryan2.puzzlegame.game.Game;
import io.github.happyryan2.puzzlegame.game.UndoStack;
import io.github.happyryan2.puzzlegame.game.UndoStackItem;
import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.utilities.MousePos;
import io.github.happyryan2.puzzlegame.utilities.MouseClick;
import io.github.happyryan2.puzzlegame.utilities.Screen;
import io.github.happyryan2.puzzlegame.utilities.ResourceLoader;

public class LongExtender extends Thing {
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
	public LongExtender(float x, float y, String dir, boolean isWeak, float extension) {
		this(x, y, dir, isWeak);
		super.extension = extension;
		super.origExtension = super.extension;
	}
	public LongExtender(LongExtender le) {
		this(le.x, le.y, le.dir, le.isWeak, le.extension);
	}

	public void update() {
		// detect hovering + clicks
		if(!Game.currentLevel.transitioning() && this.cursorHovered() && !Game.currentLevel.isComplete() && !Game.currentLevel.paused) {
			if(MouseClick.mouseIsPressed) {
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
		int x = (int) (super.x * Game.tileSize) + Game.currentLevel.left;
		int y = (int) (super.y * Game.tileSize) + Game.currentLevel.top;
		int w = (int) (Game.tileSize);
		int h = (int) (Game.tileSize);
		this.ignoring = true;
		if(super.extension == 0) {
			if(this.canExtendForward()) {
				Game.animationSpeed = Game.fastAnimationSpeed;
				super.extending = true;
				Game.currentLevel.moveSelected(super.dir);
				UndoStack.addAction();
				this.timeExtending = 0;
			}
			else if(this.canExtendBackward()) {
				String backwards = (super.dir == "up" || super.dir == "down") ? (super.dir == "up" ? "down" : "up") : (super.dir == "left" ? "right" : "left");
				super.moveDir = backwards;
				Game.currentLevel.moveSelected(backwards);
				Game.animationSpeed = Game.fastAnimationSpeed;
				super.extending = true;
				this.timeExtending = 0;
				UndoStack.addAction();
			}
		}
		else {
			if(this.canRetractForward()) {
				String backwards = (super.dir == "up" || super.dir == "down") ? (super.dir == "up" ? "down" : "up") : (super.dir == "left" ? "right" : "left");
				Game.currentLevel.moveSelected(backwards);
				Game.animationSpeed = Game.fastAnimationSpeed;
				super.retracting = true;
				this.timeRetracting = 0;
				UndoStack.addAction();
				UndoStack.setLastFinal(true);
				UndoStack.setLastChain(true);
			}
			else if(this.canRetractBackward()) {
				String backwards = (super.dir == "up" || super.dir == "down") ? (super.dir == "up" ? "down" : "up") : (super.dir == "left" ? "right" : "left");
				Game.currentLevel.moveSelected(backwards);
				Game.animationSpeed = Game.fastAnimationSpeed;
				super.retracting = true;
				this.timeRetracting = 0;
				super.moveDir = super.dir;
				UndoStack.addAction();
				UndoStack.setLastFinal(true);
				UndoStack.setLastChain(true);
			}
		}
		super.ignoring = false;
	}
	public void move() {
		if(super.extending) {
			this.timeExtending ++;
			super.extension += Game.animationSpeed;
			super.extension = Math.round(super.extension / (float) (Game.animationSpeed)) * Game.animationSpeed;
			/* Pause every integer extension to see if it should continue extending */
			if(this.timeExtending >= (1 / Game.animationSpeed)) {
				Game.currentLevel.snapToGrid();
				if(Game.chainUndo) {
					super.extending = false;
					this.timeExtending = 0;
					return;
				}
				if(this.canExtendForward()) {
					if(!Game.chainUndo && !Game.chainUndoLastFrame && Game.timeSinceLastAction >= 3 && super.extension >= 1) {
						Game.currentLevel.moveSelected(super.dir);
						this.timeExtending = 0;
						UndoStack.addAction(true);
					}
					else {
						/* Wait for the chain undo to tell it to keep moving */
						this.timeExtending = 0;
						super.extending = false;
					}
				}
				else if(this.canExtendBackward()) {
					if(!Game.chainUndo && super.extension >= 1) {
						String backwards = (super.dir == "right" || super.dir == "left") ? (super.dir == "right" ? "left" : "right") : (super.dir == "up" ? "down" : "up");
						Game.currentLevel.moveSelected(backwards);
						this.timeExtending = 0;
						super.timeMoving = 0;
						super.moveDir = backwards;
						UndoStack.addAction(true);
					}
					else {
						this.timeRetracting = 0;
						super.retracting = false;
					}
				}
				else {
					Game.animationSpeed = Game.defaultAnimationSpeed;
					super.extending = false;
				}
			}
		}
		else if(super.retracting) {
			this.timeRetracting ++;
			super.extension -= Game.animationSpeed;
			super.extension = Math.round(super.extension / (float) (Game.animationSpeed)) * Game.animationSpeed;
			/* Pause every integer extension to see if it should continue retracting */
			if(this.timeRetracting >= 1 / Game.animationSpeed) {
				Game.currentLevel.snapToGrid();
				this.timeRetracting = 0;
				if(super.extension >= 1) {
					if(Game.chainUndo) {
						/* Wait for the chain undo to tell it to keep moving */
						super.retracting = false;
					}
					else {
						super.retracting = false;
						if(this.canRetractForward()) {
							super.retracting = true;
							String backwards = (super.dir == "left" || super.dir == "right") ? (super.dir == "left" ? "right" : "left") : (super.dir == "up" ? "down" : "up");
							Game.currentLevel.moveSelected(backwards);
							UndoStack.addAction(true);
						}
						else if(this.canRetractBackward()) {
							super.retracting = true;
							super.moveDir = super.dir;
							super.timeMoving = 0;
							Game.currentLevel.moveSelected(super.dir);
							UndoStack.addAction(true);
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
		if((Game.currentLevel.numSelected() > 1 && super.isWeak) || !Game.currentLevel.canSelectedBePushed(super.dir)) {
			return false;
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
		if((Game.currentLevel.numSelected() > 1 && super.isWeak) || !Game.currentLevel.canSelectedBePushed(backwards)) {
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
	public boolean canRetractBackward() {
		Game.currentLevel.clearSelected();
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
		else {
			return false;
		}
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
