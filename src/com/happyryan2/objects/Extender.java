package com.happyryan2.objects;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Polygon;
import java.lang.Math;

import com.happyryan2.game.Game;
import com.happyryan2.game.Level;
import com.happyryan2.game.LevelPack;
import com.happyryan2.utilities.MousePos;
import com.happyryan2.utilities.MouseClick;
import com.happyryan2.utilities.Screen;

public class Extender extends Thing {
	public float x;
	public float y;
	public String dir;
	public Extender(float x, float y, String dir) {
		super.x = x;
		super.y = y;
		super.dir = dir;
		super.extending = false;
		super.retracting = false;
		super.hoverY = 0;
		super.extension = 0;
	}
	public void update() {
		// calculate visual position for hitboxes
		super.height = Game.sizes[(int) Game.levelSize - 2];
		int x = (int) Math.round(super.x * Game.tileSize);
		int y = (int) Math.round(super.y * Game.tileSize);
		int w = (int) Math.round(Game.tileSize);
		int h = (int) Math.round(Game.tileSize);
		// detect hovering + clicks
		if(this.cursorHovered() && Game.canClick && super.extension <= 0) {
			// decide which tiles will be moved when it extends
			switch(super.dir) {
				case "up":
					Game.currentLevel.setMoved(super.x, super.y - 1, "up");
					break;
				case "down":
					Game.currentLevel.setMoved(super.x, super.y + 1, "down");
					break;
				case "left":
					Game.currentLevel.setMoved(super.x - 1, super.y, "left");
					break;
				case "right":
					Game.currentLevel.setMoved(super.x + 1, super.y, "right");
					break;
			}
			// decide whether it can extend or not (by pushing the tiles in front of it)
			boolean canExtend = true;
			for(short i = 0; i < Game.currentLevel.content.size(); i ++) {
				Thing thing = (Thing) Game.currentLevel.content.get(i);
				if(!thing.moved) {
					continue;
				}
				if((thing.x == 0 && super.dir == "left") || (thing.y == 0 && super.dir == "up") || (thing.x == Game.levelSize - 1 && super.dir == "right") || (thing.y == Game.levelSize - 1 && super.dir == "down")) {
					canExtend = false;
					break;
				}
			}
			// decide whether it can extend or not (by pushing itself backward)
			if(canExtend && !((super.x == 0 && super.dir == "left") || (super.y == 0 && super.dir == "up") || (super.x == Game.levelSize - 1 && super.dir == "right") || (super.y == Game.levelSize - 1 && super.dir == "down"))) {
				Screen.cursor = "hand";
				if(super.hoverY < h * super.height) {
					super.hoverY ++;
				}
				if(MouseClick.mouseIsPressed) {
					Game.canClick = false;
					super.extending = true;
				}
			}
		}
		else if(this.cursorHovered() && Game.canClick) {
			Screen.cursor = "hand";
			if(super.hoverY < h * super.height) {
				super.hoverY ++;
			}
			if(MouseClick.mouseIsPressed) {
				Game.canClick = false;
				super.retracting = true;
			}
		}
		else if(super.hoverY > 0 && !super.extending && !super.retracting) {
			super.hoverY --;
		}
		// extension + pushing tiles
		if(super.extending) {
			super.extension += 0.05;
			if(super.extension >= 1) {
				Game.canClick = true;
				super.extending = false;
				super.extension = 1;
			}
		}
		else if(super.retracting) {
			super.extension -= 0.05;
			if(super.extension <= 0) {
				Game.canClick = true;
				super.retracting = false;
				super.extension = 0;
			}
		}
		if((super.extending || super.retracting) && super.hoverY < h * super.height) {
			super.hoverY ++;
		}
	}
	public void display(Graphics g) {
		int x = (int) Math.round(super.x * Game.tileSize);
		int y = (int) Math.round(super.y * Game.tileSize);
		int w = (int) Math.round(Game.tileSize);
		int h = (int) Math.round(Game.tileSize);
		//debug
		if(super.moved) {
			g.setColor(new Color(255, 0, 0));
			g.fillRect(x, y, w, h);
		}
		g.setColor(new Color(100, 100, 100));
		//horizontal
		raisedRect(g, (double) x - (super.dir == "left" ? super.extension * h : 0), (double) y - (super.dir == "up" ? super.extension * h : 0), (double) w / 3, (double) h / 6);
		raisedRect(g, (double) (x + w - (w / 3) + (super.dir == "right" ? super.extension * w : 0)), (double) y - (super.dir == "up" ? super.extension * Game.tileSize : 0), (double) w / 3, (double) h / 6);
		raisedRect(g, (double) x - (super.dir == "left" ? super.extension * h : 0), (double) y + (h / 6 * 5) + (super.dir == "down" ? super.extension * h : 0), (double) w / 3, (double) h / 6);
		raisedRect(g, (double) (x + w - (w / 3) + (super.dir == "right" ? super.extension * Game.tileSize : 0)), (double) y + (h / 6 * 5) + (super.dir == "down" ? super.extension * h : 0), (double) w / 3, (double) h / 6);
		//vertical
		raisedRect(g, (double) x - (super.dir == "left" ? super.extension * h : 0), (double) y - (super.dir == "up" ? super.extension * Game.tileSize : 0), (double) w / 6, (double) h / 3);
		raisedRect(g, (double) x - (super.dir == "left" ? super.extension * h : 0), (double) y + h - (h / 3) + (super.dir == "down" ? super.extension * h : 0), (double) w / 6, (double) h / 3);
		raisedRect(g, (double) (x + (w / 6 * 5) + (super.dir == "right" ? super.extension * Game.tileSize : 0)), (double) y - (super.dir == "up" ? super.extension * Game.tileSize : 0), (double) w / 6, (double) h / 3);
		raisedRect(g, (double) (x + (w / 6 * 5) + (super.dir == "right" ? super.extension * Game.tileSize : 0)), (double) y + h - (h / 3) + (super.dir == "down" ? super.extension * h : 0), (double) w / 6, (double) h / 3);
		//triangle
		Polygon triangle = new Polygon();
		if(super.dir == "up") {
			triangle.addPoint((int) x + (w / 3), (int) (Math.round(y + h - (h / 3) + super.hoverY - (super.extension * Game.tileSize / 2))));
			triangle.addPoint((int) x + w - (w / 3), (int) (Math.round(y + h - (h / 3) + super.hoverY) - (super.extension * Game.tileSize / 2)));
			triangle.addPoint((int) x + (w / 2), (int) (Math.round(y + (h / 3) + super.hoverY) - (super.extension * Game.tileSize / 2)));
			raisedRect(g, (double) x + (w / 3), (double) y + h - (h / 3) - (super.extension * Game.tileSize / 2), (double) w / 3, (double) 1.0);
		}
		else if(super.dir == "down") {
			triangle.addPoint((int) x + (w / 3), (int) (Math.round(y + (h / 3) + super.hoverY + (super.extension * Game.tileSize / 2))));
			triangle.addPoint((int) x + w - (w / 3), (int) (Math.round(y + (h / 3) + super.hoverY + (super.extension * Game.tileSize / 2))));
			triangle.addPoint((int) x + (w / 2), (int) (Math.round(y + h - (h / 3) + super.hoverY + (super.extension * Game.tileSize / 2))));
			Polygon side = new Polygon();
			// side.addPoint((int) x + (w / 3), (int) y + Math.round(y + h / 3) + super.hoverY);
		}
		else if(super.dir == "right") {
			triangle.addPoint((int) (x + (w / 3) + (super.extension * Game.tileSize / 2)), (int) Math.round(y + (h / 3) + super.hoverY));
			triangle.addPoint((int) (x + (w / 3) + (super.extension * Game.tileSize / 2)), (int) Math.round(y + h - (h / 3) + super.hoverY));
			triangle.addPoint((int) (x + w - (w / 3) + (super.extension * Game.tileSize / 2)), (int) Math.round(y + (h / 2) + super.hoverY));
		}
		else if(super.dir == "left") {
			triangle.addPoint((int) (x + w - (w / 3) - (super.extension * Game.tileSize / 2)), (int) Math.round(y + (h / 3) + super.hoverY));
			triangle.addPoint((int) (x + w - (w / 3) - (super.extension * Game.tileSize / 2)), (int) Math.round(y + h - (h / 3) + super.hoverY));
			triangle.addPoint((int) (x + (w / 3) - (super.extension * Game.tileSize / 2)), (int) Math.round(y + (h / 2) + super.hoverY));
		}
		g.fillPolygon(triangle);
	}
	public boolean cursorHovered() {
		int x = (int) Math.round(super.x * Game.tileSize) + 200;
		int y = (int) Math.round(super.y * Game.tileSize) + 200;
		int w = (int) Math.round(Game.tileSize);
		int h = (int) Math.round(Game.tileSize);
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
		if(x + w >= (super.x * Game.tileSize) + Game.tileSize - Math.max(Game.tileSize * 0.03, 4) && super.extension == 0) {
			x = (super.x * Game.tileSize) + Game.tileSize - w;
		}
		if(y + h >= (super.y * Game.tileSize) + Game.tileSize - Math.max(Game.tileSize * 0.03, 4) && super.extension == 0) {
			y = (super.y * Game.tileSize) + Game.tileSize - h;
		}
		int vX = (int) Math.round(x);
		int vY = (int) Math.round(y);
		int vW = (int) Math.round(w);
		int vH = (int) Math.round(h);
		g.setColor(new Color(150, 150, 150, 255));
		g.fillRect(vX, (int) Math.round(vY + vH + super.hoverY), vW, (int) Math.round(Game.tileSize * super.height - super.hoverY));
		g.setColor(new Color(100, 100, 100, 255));
		g.fillRect(vX, (int) Math.round(vY + super.hoverY), vW, vH);
	}
	@Override
	public void checkMovement(String dir) {
		/*
		This function sets all of the tiles that would be moved to be 'moved' depending on which direction it was pushed.
		*/
		System.out.println("deciding which tiles are affected by (" + super.x + ", " + super.y + ")");
		if(super.extension == 0) {
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
		else {
			switch(dir) {
				case "up":
					switch(super.dir) {
						case "up":
							Game.currentLevel.setMoved(super.x, super.y - 2, dir);
							break;
						case "down":
							Game.currentLevel.setMoved(super.x, super.y - 1, dir);
							break;
						case "left":
							Game.currentLevel.setMoved(super.x, super.y - 1, dir);
							Game.currentLevel.setMoved(super.x - 1, super.y - 1, dir);
							break;
						case "right":
							Game.currentLevel.setMoved(super.x, super.y - 1, dir);
							Game.currentLevel.setMoved(super.x + 1, super.y - 1, dir);
							break;
						}
					break;
				case "down":
					switch(super.dir) {
						case "up":
							Game.currentLevel.setMoved(super.x, super.y - 1, dir);
							break;
						case "down":
							Game.currentLevel.setMoved(super.x, super.y + 2, dir);
							break;
						case "left":
							Game.currentLevel.setMoved(super.x, super.y + 1, dir);
							Game.currentLevel.setMoved(super.x - 1, super.y + 1, dir);
							break;
						case "right":
							Game.currentLevel.setMoved(super.x, super.y + 1, dir);
							Game.currentLevel.setMoved(super.x + 1, super.y + 1, dir);
							break;
						}
					break;
				case "left":
					switch(super.dir) {
						case "up":
							Game.currentLevel.setMoved(super.x, super.y - 1, dir);
							Game.currentLevel.setMoved(super.x - 1, super.y - 1, dir);
							break;
						case "down":
							Game.currentLevel.setMoved(super.x, super.y + 1, dir);
							Game.currentLevel.setMoved(super.x - 1, super.y + 1, dir);
							break;
						case "left":
							Game.currentLevel.setMoved(super.x - 2, super.y, dir);
							break;
						case "right":
							Game.currentLevel.setMoved(super.x + 1, super.y, dir);
							break;
						}
					break;
				case "right":
					switch(super.dir) {
						case "up":
							Game.currentLevel.setMoved(super.x, super.y - 1, dir);
							Game.currentLevel.setMoved(super.x + 1, super.y - 1, dir);
							break;
						case "down":
							Game.currentLevel.setMoved(super.x, super.y + 1, dir);
							Game.currentLevel.setMoved(super.x + 1, super.y + 1, dir);
							break;
						case "left":
							Game.currentLevel.setMoved(super.x - 1, super.y, dir);
							break;
						case "right":
							Game.currentLevel.setMoved(super.x + 2, super.y, dir);
							break;
					}
					break;
				}
			}
	}
}
