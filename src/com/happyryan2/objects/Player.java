/*
This class is the 'player' (the blue square that you push around).
*/

package com.happyryan2.objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import com.happyryan2.game.Game;
import com.happyryan2.game.Level;
import com.happyryan2.utilities.ImageLoader;

public class Player extends Thing {
	private Color darkBlue = new Color(0, 0, 255);
	private Color lightBlue = new Color(0, 128, 255);
	public Image img = ImageLoader.loadImage("res/graphics/objects/player.png");
	public Player(float x, float y) {
		super.x = x;
		super.y = y;
		super.origX = x;
		super.origY = y;
	}
	public void update() {
		if(super.deleted) { return; }
		// super.height = Game.sizes[(int) Game.levelSize - 2];
		super.height = 0.1;
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
	public void raisedRect(Graphics g, double x, double y, double w, double h) {
		if(x + w >= (super.x * Game.tileSize) + Game.tileSize - Math.max(Game.tileSize * 0.03, 4) && super.extension == 0) {
			x = (super.x * Game.tileSize) + Game.tileSize - w;
		}
		if(y + h >= (super.y * Game.tileSize) + Game.tileSize - Math.max(Game.tileSize * 0.03, 4) && super.extension == 0) {
			y = (super.y * Game.tileSize) + Game.tileSize - h;
		}
		int vX = (int) (x);
		int vY = (int) (y);
		int vW = (int) (w);
		int vH = (int) (h);
		g.setColor(lightBlue);
		g.fillRect(vX, (int) (vY + vH + super.hoverY), vW, (int) (Game.tileSize * super.height - super.hoverY));
		g.setColor(darkBlue);
		g.fillRect(vX, (int) (vY + super.hoverY), vW, vH);
	}
	public boolean canBePushed(String dir) {
		if(super.deleted) { return true; }
		return !((dir == "left" && super.x == 0) || (dir == "right" && super.x == Game.currentLevel.width - 1) || (dir == "up" && super.y == 0) || (dir == "down" && super.y == Game.currentLevel.height - 1));
	}
	public void checkMovement(String dir) {
		Game.currentLevel.moveTile(super.x, super.y, dir);
	}
}
