/*
This class is the 'player' (the blue square that you push around).
*/

package io.github.happyryan2.puzzlegame.objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import io.github.happyryan2.puzzlegame.game.Game;
import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.utilities.ResourceLoader;

public class Player extends Thing {
	private Color darkBlue = new Color(0, 0, 255);
	private Color lightBlue = new Color(0, 128, 255);
	public Image img = ResourceLoader.loadImage("res/graphics/objects/player.png");

	public Player(float x, float y) {
		super.x = x;
		super.y = y;
		super.origX = x;
		super.origY = y;
	}

	public void update() {
		if(super.deleted) { return; }
	}
	public void move() {
		//movement
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
		/* Display + undo transformations */
		g2.drawImage(img, Math.round(-(w / 2) / xScale), Math.round(-(h / 2) / yScale), null);
		g2.setTransform(at);
	}

	public boolean canBePushed(String dir) {
		if(super.deleted) { return true; }
		return !((dir == "left" && super.x == 0) || (dir == "right" && super.x == Game.currentLevel.width - 1) || (dir == "up" && super.y == 0) || (dir == "down" && super.y == Game.currentLevel.height - 1));
	}
	public void checkMovement(String dir) {
		Game.currentLevel.moveTile(super.x, super.y, dir);
	}
}
