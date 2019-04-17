/*
This class is the 'player' (the blue square that you push around).
*/

package com.happyryan2.objects;

import java.awt.Graphics;
import java.awt.Color;

import com.happyryan2.game.Game;
import com.happyryan2.game.Level;
import com.happyryan2.game.LevelPack;

public class Player extends Thing {
	private Color darkBlue = new Color(0, 0, 255);
	private Color lightBlue = new Color(0, 128, 255);
	public Player(float x, float y) {
		super.x = x;
		super.y = y;
	}
	public void update() {
		super.height = Game.sizes[(int) Game.levelSize - 2];
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
		int x = (int) (super.x * Game.tileSize);
		int y = (int) (super.y * Game.tileSize);
		int w = (int) (Game.tileSize);
		int h = (int) (Game.tileSize);
		//debug
		if(super.moved && false) {
			g.setColor(new Color(255, 0, 0));
			g.fillRect(x, y, w, h);
		}
		g.setColor(darkBlue);
		//horizontal
		raisedRect(g, (double) x, (double) y, (double) w / 3, (double) h / 6);
		raisedRect(g, (double) (x + w - (w / 3)), (double) y, (double) w / 3, (double) h / 6);
		raisedRect(g, (double) x, (double) y + (h / 6 * 5), (double) w / 3, (double) h / 6);
		raisedRect(g, (double) (x + w - (w / 3)), (double) y + (h / 6 * 5), (double) w / 3, (double) h / 6);
		//vertical
		raisedRect(g, (double) x, (double) y, (double) w / 6, (double) h / 3);
		raisedRect(g, (double) x, (double) y + h - (h / 3), (double) w / 6, (double) h / 3);
		raisedRect(g, (double) (x + (w / 6 * 5)), (double) y, (double) w / 6, (double) h / 3);
		raisedRect(g, (double) (x + (w / 6 * 5)), (double) y + h - (h / 3), (double) w / 6, (double) h / 3);
		//circle
		for(short i = 0; i < Game.tileSize * super.height; i ++) {
			g.setColor(lightBlue);
			g.fillOval(x + (w / 3), y + (h / 3) + i, (w / 3), (h / 3));
		}
		g.setColor(darkBlue);
		g.fillOval(x + (w / 3), y + (h / 3), (w / 3), (h / 3));
		//win animation
		if(Game.currentLevel.isComplete()) {
			if(super.hoverY < h * super.height) {
				super.hoverY ++;
			}
		}
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
		return !((dir == "left" && super.x == 0) || (dir == "right" && super.x == Game.levelSize - 1) || (dir == "up" && super.y == 0) || (dir == "down" && super.y == Game.levelSize - 1));
	}
	public void checkMovement(String dir) {
		switch(dir) {
			case "up":
				Game.currentLevel.setMoved(super.x, super.y - 1, dir);
				break;
			case "down":
				Game.currentLevel.setMoved(super.x, super.y + 1, dir);
				break;
			case "left":
				Game.currentLevel.setMoved(super.x - 1, super.y, dir);
				break;
			case "right":
				Game.currentLevel.setMoved(super.x + 1, super.y, dir);
				break;
		}
	}
}
