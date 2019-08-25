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
import io.github.happyryan2.puzzlegame.utilities.ImageLoader;

public class Extender extends Thing {
	public float x;
	public float y;
	public String dir;
	public static Image img = ImageLoader.loadImage("res/graphics/objects/extender.png");
	public static Image img2 = ImageLoader.loadImage("res/graphics/objects/extender2.png");
	public static Image img3 = ImageLoader.loadImage("res/graphics/objects/extender3.png");
	public static Image img4 = ImageLoader.loadImage("res/graphics/objects/emptyExtender.png");
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
	public void update() {
		// calculate visual position for hitboxes
		// super.height = Game.sizes[(int) Game.levelSize - 2];
		super.height = 0.1;
		int x = (int) (super.x * Game.tileSize) + Game.currentLevel.left;
		int y = (int) (super.y * Game.tileSize) + Game.currentLevel.top;
		int w = (int) (Game.tileSize);
		int h = (int) (Game.tileSize);
		// detect hovering + clicks
		boolean transitioning = false;
		for(int i = 0; i < Game.currentLevel.content.size(); i ++) {
			Thing thing = (Thing) Game.currentLevel.content.get(i);
			if((thing instanceof Extender || thing instanceof Retractor) && thing.extension != 0 && thing.extension != 1) {
				transitioning = true;
				break;
			}
		}
		if(!transitioning && this.cursorHovered() && !Game.currentLevel.isComplete() && !Game.currentLevel.paused && super.dir != "none") {
			if(MouseClick.mouseIsPressed) {
				this.onClick();
			}
			if(this.canDoSomething()) {
				Screen.cursor = "hand";
			}
			if(super.hoverY < Game.tileSize * super.height) {
				super.hoverY ++;
			}
		}
		else if(this.hoverY > 0) {
			super.hoverY --;
		}
		// extension + pushing tiles
		if(super.extending) {
			super.extension += 0.05;
			if(super.extension >= 1) {
				Game.canClick = !Game.currentLevel.isComplete();
				super.extending = false;
				super.extension = 1;
			}
		}
		else if(super.retracting) {
			super.extension -= 0.05;
			if(super.extension <= 0) {
				Game.canClick = !Game.currentLevel.isComplete();
				super.retracting = false;
				super.extension = 0;
			}
		}
		if((super.extending || super.retracting) && super.hoverY < h * super.height) {
			super.hoverY ++;
		}
		//movement
		if(super.moveDir == "up") {
			super.y -= 0.05;
		}
		else if(super.moveDir == "down") {
			super.y += 0.05;
		}
		else if(super.moveDir == "left") {
			super.x -= 0.05;
		}
		else if(super.moveDir == "right") {
			super.x += 0.05;
		}
		if(super.moveDir != "none") {
			super.timeMoving ++;
			if(super.timeMoving >= 20) {
				super.x = Math.round(super.x);
				super.y = Math.round(super.y);
				super.moveDir = "none";
				super.timeMoving = 0;
			}
		}
		// debug
	}
	public void onClick() {
		if(!Game.canClick || Game.currentLevel.transitioning() || Game.currentLevel.paused) {
			return;
		}
		int x = (int) (super.x * Game.tileSize) + Game.currentLevel.left;
		int y = (int) (super.y * Game.tileSize) + Game.currentLevel.top;
		int w = (int) (Game.tileSize);
		int h = (int) (Game.tileSize);
		this.ignoring = true;
		if(super.extension == 0) {
			boolean forwards = this.canExtendForward();
			boolean backwards = this.canExtendBackward();
			if(this.canExtendForward()) {
				super.extending = true;
				Game.currentLevel.moveSelected(super.dir);
				Stack.addAction();
			}
			else if(this.canExtendBackward()) {
				super.extending = true;
				super.moveDir = (super.dir == "up" || super.dir == "down") ? (super.dir == "up" ? "down" : "up") : (super.dir == "left" ? "right" : "left");
				Game.currentLevel.moveSelected((super.dir == "up" || super.dir == "down") ? (super.dir == "up" ? "down" : "up") : (super.dir == "left" ? "right" : "left"));
				Stack.addAction();
			}
		}
		else if(super.extension == 1 && this.canRetractForward()) {
			super.retracting = true;
			Game.currentLevel.moveSelected((super.dir == "up" || super.dir == "down") ? (super.dir == "up" ? "down" : "up") : (super.dir == "left" ? "right" : "left")); // move selected tiles in the opposite direction of super.dir
			Stack.addAction();
		}
		this.ignoring = false;
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
		Game.currentLevel.clearSelected();
		super.ignoring = true;
		if(super.dir == "left") {
			for(short i = 0; i < Game.currentLevel.content.size(); i ++) {
				Thing thing = (Thing) Game.currentLevel.content.get(i);
				if(thing != null && thing instanceof Retractor && thing.dir == "right" && thing.y == super.y && thing.x + thing.extension == super.x - 2) {
					Game.currentLevel.moveObject(thing.x, thing.y, "right");
					break;
				}
			}
		}
		else if(super.dir == "right") {
			for(short i = 0; i < Game.currentLevel.content.size(); i ++) {
				Thing thing = (Thing) Game.currentLevel.content.get(i);
				if(thing != null && thing instanceof Retractor && thing.dir == "left" && thing.y == super.y && thing.x - thing.extension == super.x + 2) {
					Game.currentLevel.moveObject(thing.x, thing.y, "left");
					break;
				}
			}
		}
		else if(super.dir == "up") {
			for(short i = 0; i < Game.currentLevel.content.size(); i ++) {
				Thing thing = (Thing) Game.currentLevel.content.get(i);
				if(thing != null && thing instanceof Retractor && thing.dir == "down" && thing.x == super.x && thing.y + thing.extension == super.y - 2) {
					Game.currentLevel.moveObject(thing.x, thing.y, "down");
				}
			}
		}
		else if(super.dir == "down") {
			for(short i = 0; i < Game.currentLevel.content.size(); i ++) {
				Thing thing = (Thing) Game.currentLevel.content.get(i);
				if(thing != null && thing instanceof Retractor && thing.dir == "up" && thing.x == super.x && thing.y - thing.extension == super.y + 2) {
					Game.currentLevel.moveObject(thing.x, thing.y, "up");
				}
			}
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
		/* Debug */
		if(super.selected && false) {
			g.setColor(new Color(255, 0, 0));
			g.fillRect(x, y, w, h);
		}
		/* Translate to position + scale to size */
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform at = g2.getTransform();
		g2.translate(x + (w / 2), y + (h / 2));
		float xScale = ((float) w) / ((float) img.getWidth(null));
		float yScale = ((float) h) / ((float) img.getHeight(null));
		g2.scale(xScale, yScale);
		/* Scale for when extended */
		if(super.dir == "right") {
			g2.translate(w / -2 / xScale, 0);
			g2.scale(super.extension + 1, 1);
			g2.translate(w / 2 / xScale, 0);
		}
		else if(super.dir == "left") {
			g2.translate(w / 2 / xScale, 0);
			g2.scale(super.extension + 1, 1);
			g2.translate(w / -2 / xScale, 0);
		}
		else if(super.dir == "up") {
			g2.translate(0, h / 2 / yScale);
			g2.scale(1, super.extension + 1);
			g2.translate(0, h / -2 / yScale);
		}
		else if(super.dir == "down") {
			g2.translate(0, h / -2 / yScale);
			g2.scale(1, super.extension + 1);
			g2.translate(0, h / 2 / yScale);
		}
		/* Rotate for different orientations */
		if(super.dir == "right") {
			g2.rotate(Math.toRadians(90));
		}
		else if(super.dir == "left") {
			g2.rotate(Math.toRadians(-90));
		}
		else if(super.dir == "down") {
			g2.rotate(Math.toRadians(180));
		}
		/* Display + undo transformations */
		if(super.dir != "none") {
			g2.drawImage(img, Math.round(-(w / 2) / xScale), Math.round(-(h / 2) / yScale), null);
		}
		else {
			g2.drawImage(img4, Math.round(-(w / 2) / xScale), Math.round(-(h / 2) / yScale), null);
		}
		g2.setTransform(at);
		if(super.dir == "none") {
			return;
		}
		/* Translate to position */
		at = g2.getTransform();
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
		/* Scale to correct size */
		g2.scale(xScale, yScale);
		/* Display + undo transformations */
		if(super.isWeak) {
			g2.drawImage(img3, Math.round(-(w / 2) / xScale), Math.round(-(h / 2) / yScale), null);
		}
		else {
			g2.drawImage(img2, Math.round(-(w / 2) / xScale), Math.round(-(h / 2) / yScale), null);
		}
		g2.setTransform(at);
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
