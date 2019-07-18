package com.happyryan2.objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import com.happyryan2.game.Game;
import com.happyryan2.game.Stack;
import com.happyryan2.game.Level;
import com.happyryan2.game.LevelPack;
import com.happyryan2.utilities.MousePos;
import com.happyryan2.utilities.MouseClick;
import com.happyryan2.utilities.Screen;
import com.happyryan2.utilities.ImageLoader;

public class Extender extends Thing {
	public float x;
	public float y;
	public String dir;
	public static Image img = ImageLoader.loadImage("res/graphics/objects/extender.png");
	public static Image img2 = ImageLoader.loadImage("res/graphics/objects/extender2.png");
	public static Image img3 = ImageLoader.loadImage("res/graphics/objects/extender3.png");
	public Extender(float x, float y, String dir) {
		super.x = x;
		super.y = y;
		super.origX = x;
		super.origY = y;
		super.dir = dir;
	}
	public Extender(float x, float y, String dir, boolean isWeak) {
		super.x = x;
		super.y = y;
		super.origX = x;
		super.origY = y;
		super.dir = dir;
		super.isWeak = isWeak;
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
		if(!transitioning && this.cursorHovered() && !Game.currentLevel.isComplete() && super.dir != "none") {
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
		if(!Game.canClick || Game.currentLevel.transitioning()) {
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
			Stack.addAction();
			// Game.currentLevel.moveSelected((super.dir == "up" || super.dir == "down") ? (super.dir == "up" ? "down" : "up") : (super.dir == "left" ? "right" : "left")); // move selected tiles in the opposite direction of super.dir
		}
		this.ignoring = false;
	}

	public boolean canExtendForward() {
		Game.currentLevel.clearSelected();
		super.ignoring = true;
		switch(super.dir) {
			case "up":
				Game.currentLevel.select(super.x, super.y - 1, "up");
				break;
			case "down":
				Game.currentLevel.select(super.x, super.y + 1, "down");
				break;
			case "left":
				Game.currentLevel.select(super.x - 1, super.y, "left");
				break;
			case "right":
				Game.currentLevel.select(super.x + 1, super.y, "right");
				break;
		}
		for(short i = 0; i < Game.currentLevel.content.size(); i ++) {
			Thing thing = (Thing) Game.currentLevel.content.get(i);
			if(thing.selected && !thing.canBePushed(super.dir)) {
				return false;
			}
		}
		boolean foundOne = false;
		for(short i = 0; i < Game.currentLevel.content.size(); i ++) {
			Thing thing = (Thing) Game.currentLevel.content.get(i);
			if(thing.selected) {
				if(foundOne && super.isWeak) {
					return false;
				}
				foundOne = true;
				if(!thing.canBePushed(super.dir)) {
					return false;
				}
			}
		}
		if((super.x == 0 && super.dir == "left") || (super.x == Game.currentLevel.width - 1 && super.dir == "right") || (super.y == 0 && super.dir == "up") || (super.y == Game.currentLevel.height - 1 && super.dir == "down")) {
			return false;
		}
		return true;
	}
	public boolean canRetractForward() {
		Game.currentLevel.clearSelected();
		return true;
	}
	public boolean canExtendBackward() {
		Game.currentLevel.clearSelected();
		switch(super.dir) {
			case "up":
				Game.currentLevel.select(super.x, super.y + 1, "down");
				break;
			case "down":
				Game.currentLevel.select(super.x, super.y - 1, "up");
				break;
			case "left":
				Game.currentLevel.select(super.x + 1, super.y, "right");
				break;
			case "right":
				Game.currentLevel.select(super.x - 1, super.y, "left");
				break;
		}
		for(short i = 0; i < Game.currentLevel.content.size(); i ++) {
			Thing thing = (Thing) Game.currentLevel.content.get(i);
			if(thing.selected && !thing.canBePushed((super.dir == "up" || super.dir == "down") ? (super.dir == "up" ? "down" : "up") : (super.dir == "left" ? "right" : "left"))) {
				return false;
			}
		}
		boolean foundOne = false;
		for(short i = 0; i < Game.currentLevel.content.size(); i ++) {
			Thing thing = (Thing) Game.currentLevel.content.get(i);
			if(thing.selected) {
				if(foundOne && super.isWeak) {
					return false;
				}
				foundOne = true;
				if(!thing.canBePushed((super.dir == "up" || super.dir == "down") ? (super.dir == "up" ? "down" : "up") : (super.dir == "left" ? "right" : "left"))) {
					return false;
				}
			}
		}
		if((super.y == 0 && super.dir == "down") || (super.y == Game.currentLevel.height - 1 && super.dir == "up") || (super.x == 0 && super.dir == "right") || (super.x == Game.currentLevel.width - 1 && super.dir == "left")) {
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
		g2.drawImage(img, Math.round(-(w / 2) / xScale), Math.round(-(h / 2) / yScale), null);
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
		This function sets all of the tiles that would be moved to be 'moved' depending on which direction it was pushed.
		*/
		if(super.extension == 0) {
			switch(dir) {
				case "up":
					Game.currentLevel.select(super.x, super.y - 1, dir);
					break;
				case "down":
					Game.currentLevel.select(super.x, super.y + 1, dir);
					break;
				case "left":
					Game.currentLevel.select(super.x - 1, super.y, dir);
					break;
				case "right":
					Game.currentLevel.select(super.x + 1, super.y, dir);
					break;
			}
		}
		else {
			switch(dir) {
				case "up":
					switch(super.dir) {
						case "up":
							Game.currentLevel.select(super.x, super.y - 2, dir);
							break;
						case "down":
							Game.currentLevel.select(super.x, super.y - 1, dir);
							break;
						case "left":
							Game.currentLevel.select(super.x, super.y - 1, dir);
							Game.currentLevel.select(super.x - 1, super.y - 1, dir);
							break;
						case "right":
							Game.currentLevel.select(super.x, super.y - 1, dir);
							Game.currentLevel.select(super.x + 1, super.y - 1, dir);
							break;
						}
					break;
				case "down":
					switch(super.dir) {
						case "up":
							Game.currentLevel.select(super.x, super.y + 1, dir);
							break;
						case "down":
							Game.currentLevel.select(super.x, super.y + 2, dir);
							break;
						case "left":
							Game.currentLevel.select(super.x, super.y + 1, dir);
							Game.currentLevel.select(super.x - 1, super.y + 1, dir);
							break;
						case "right":
							Game.currentLevel.select(super.x, super.y + 1, dir);
							Game.currentLevel.select(super.x + 1, super.y + 1, dir);
							break;
						}
					break;
				case "left":
					switch(super.dir) {
						case "up":
							Game.currentLevel.select(super.x - 1, super.y, dir);
							Game.currentLevel.select(super.x - 1, super.y - 1, dir);
							break;
						case "down":
							Game.currentLevel.select(super.x - 1, super.y, dir);
							Game.currentLevel.select(super.x - 1, super.y + 1, dir);
							break;
						case "left":
							Game.currentLevel.select(super.x - 2, super.y, dir);
							break;
						case "right":
							Game.currentLevel.select(super.x - 1, super.y, dir);
							break;
						}
					break;
				case "right":
					switch(super.dir) {
						case "up":
							Game.currentLevel.select(super.x + 1, super.y, dir);
							Game.currentLevel.select(super.x + 1, super.y - 1, dir);
							break;
						case "down":
							Game.currentLevel.select(super.x + 1, super.y, dir);
							Game.currentLevel.select(super.x + 1, super.y + 1, dir);
							break;
						case "left":
							Game.currentLevel.select(super.x - 1, super.y, dir);
							break;
						case "right":
							Game.currentLevel.select(super.x + 2, super.y, dir);
							break;
					}
					break;
				}
			}
	}
	public boolean canBePushed(String dir) {
		/*
		Returns whether this can be pushed without colliding with a wall. Assumes no other extenders exist.
		*/
		if(((dir == "left" && super.x == 0) || (dir == "right" && super.x == Game.currentLevel.width - 1) || (dir == "up" && super.y == 0) || (dir == "down" && super.y == Game.currentLevel.height - 1))) {
			return false;
		}
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
		return true;
	}
}
