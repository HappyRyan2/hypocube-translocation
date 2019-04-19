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

public class Retractor extends Thing {
	private static Color darkGreen = new Color(0, 128, 50);
	private static Color lightGreen = new Color(50, 200, 150);
	public Retractor(float x, float y, String dir) {
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
		int x = (int) (super.x * Game.tileSize);
		int y = (int) (super.y * Game.tileSize);
		int w = (int) (Game.tileSize);
		int h = (int) (Game.tileSize);
		// detect hovering + clicks
		if(this.cursorHovered() && Game.canClick && super.extension <= 0) {
			// decide which tiles will be moved when it extends forward
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
				if(thing.moved && !thing.canBePushed(super.dir)) {
					canExtend = false;
					break;
				}
			}
			if(!canExtend || ((super.dir == "up" && super.y == 0) || (super.dir == "down" && super.y == Game.levelSize - 1) || (super.dir == "left" && super.x == 0) || (super.dir == "right" && super.x == Game.levelSize - 1))) {
				// decide which tiles will be affected by pushing itself backwards
				for(short i = 0; i < Game.currentLevel.content.size(); i ++) {
					Thing thing = (Thing) Game.currentLevel.content.get(i);
					thing.moved = false;
				}
				switch(super.dir) {
					case "up":
						Game.currentLevel.setMoved(super.x, super.y + 1, "down");
						break;
					case "down":
						Game.currentLevel.setMoved(super.x, super.y - 1, "up");
						break;
					case "left":
						Game.currentLevel.setMoved(super.x + 1, super.y, "right");
						break;
					case "right":
						Game.currentLevel.setMoved(super.x - 1, super.y, "left");
						break;
				}
				// decide whether it can extend (by pushing itself backwards)
				canExtend = true;
				for(short i = 0; i < Game.currentLevel.content.size(); i ++) {
					Thing thing = (Thing) Game.currentLevel.content.get(i);
					if(!thing.moved) {
						continue;
					}
					if((super.dir == "up" && !thing.canBePushed("down")) || (super.dir == "down" && !thing.canBePushed("up")) || (super.dir == "left" && !thing.canBePushed("right")) || (super.dir == "right" && !thing.canBePushed("left"))) {
						canExtend = false;
					}
				}
				if((super.dir == "up" && super.y == Game.levelSize - 1) || (super.dir == "down" && super.y == 0) || (super.dir == "left" && super.x == Game.levelSize - 1) || (super.dir == "right" && super.x == 0)) {
					canExtend = false;
				}
				if(canExtend) {
					Screen.cursor = "hand";
					if(super.hoverY < h * super.height) {
						super.hoverY ++;
					}
					if(MouseClick.mouseIsPressed) {
						Game.canClick = false;
						super.extending = true;
						if(super.dir == "up") {
							super.moveDir = "down";
						}
						else if(super.dir == "down") {
							super.moveDir = "up";
						}
						else if(super.dir == "left") {
							super.moveDir = "right";
						}
						else if(super.dir == "right") {
							super.moveDir = "left";
						}
						for(short i = 0; i < Game.currentLevel.content.size(); i ++) {
							Thing thing = (Thing) Game.currentLevel.content.get(i);
							if(thing.moved) {
								if(super.dir == "up") {
									thing.moveDir = "down";
								}
								else if(super.dir == "down") {
									thing.moveDir = "up";
								}
								else if(super.dir == "left") {
									thing.moveDir = "right";
								}
								else if(super.dir == "right") {
									thing.moveDir = "left";
								}
								thing.timeMoving = 0;
							}
						}
					}
				}
				canExtend = false;
			}
			if(canExtend && !((super.x == 0 && super.dir == "left") || (super.y == 0 && super.dir == "up") || (super.x == Game.levelSize - 1 && super.dir == "right") || (super.y == Game.levelSize - 1 && super.dir == "down"))) {
				Screen.cursor = "hand";
				if(super.hoverY < h * super.height) {
					super.hoverY ++;
				}
				if(MouseClick.mouseIsPressed) {
					Game.canClick = false;
					super.extending = true;
					for(short i = 0; i < Game.currentLevel.content.size(); i ++) {
						Thing thing = (Thing) Game.currentLevel.content.get(i);
						if(thing.moved) {
							thing.moveDir = super.dir;
							thing.timeMoving = 0;
						}
					}
				}
			}
		}
		else if(this.cursorHovered() && Game.canClick) {
			super.ignoring = true;
			// decide which tiles will be moved when it retracts
			switch(super.dir) {
				case "up":
					Game.currentLevel.setMoved(super.x, super.y - 2, "down");
					break;
				case "down":
					Game.currentLevel.setMoved(super.x, super.y + 2, "up");
					break;
				case "left":
					Game.currentLevel.setMoved(super.x - 2, super.y, "right");
					break;
				case "right":
					Game.currentLevel.setMoved(super.x + 2, super.y, "left");
					break;
			}
			// decide whether it can retract or not
			boolean canRetract = true;
			for(short i = 0; i < Game.currentLevel.content.size(); i ++) {
				Thing thing = (Thing) Game.currentLevel.content.get(i);
				if(!thing.moved) {
					continue;
				}
				System.out.println("can (" + thing.x + ", " + thing.y + ") be pushed right? " + thing.canBePushed("right"));
				if((super.dir == "left" && !thing.canBePushed("right") || (super.dir == "right" && !thing.canBePushed("left")) || super.dir == "up" && !thing.canBePushed("down")) || (super.dir == "down" && !thing.canBePushed("up"))) {
					canRetract = false;
					break;
				}
			}
			// decide whether it can pull itself forward
			boolean pullingSelf = false;
			if(!canRetract) {
				pullingSelf = true;
				System.out.println("just pulling myself along...");
			}
			if((super.dir == "up" && super.y == 1) || (super.dir == "down" && super.y == Game.levelSize - 2) || (super.dir == "left" && super.x == 1) || (super.dir == "right" && super.x == Game.levelSize - 2)) {
				pullingSelf = true;
			}
			// hovering animations
			Screen.cursor = "hand";
			if(super.hoverY < h * super.height) {
				super.hoverY ++;
			}
			// clicking
			if(MouseClick.mouseIsPressed) {
				Game.canClick = false;
				super.retracting = true;
				if(pullingSelf) {
					super.moveDir = super.dir;
					super.timeMoving = 0;
				}
				else {
					for(short i = 0; i < Game.currentLevel.content.size(); i ++) {
						Thing thing = (Thing) Game.currentLevel.content.get(i);
						if(!thing.moved) {
							continue;
						}
						switch(super.dir) {
							case "up":
								thing.moveDir = "down";
								break;
							case "down":
								thing.moveDir = "up";
								break;
							case "left":
								thing.moveDir = "right";
								break;
							case "right":
								thing.moveDir = "left";
								break;
						}
					}
				}
			}
			super.ignoring = false;
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
		g.setColor(darkGreen);
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
			triangle.addPoint((int) x + (w / 3), (int) ((y + h - (h / 3) + super.hoverY - (super.extension * Game.tileSize / 2))));
			triangle.addPoint((int) x + w - (w / 3), (int) ((y + h - (h / 3) + super.hoverY) - (super.extension * Game.tileSize / 2)));
			triangle.addPoint((int) x + (w / 2), (int) ((y + (h / 3) + super.hoverY) - (super.extension * Game.tileSize / 2)));
			raisedRect(g, (double) x + (w / 3), (double) y + h - (h / 3) - (super.extension * Game.tileSize / 2), (double) w / 3, (double) 1.0);
		}
		else if(super.dir == "down") {
			g.setColor(lightGreen);
			Polygon side = new Polygon();
			side.addPoint((int) x + (w / 3), (int) (y + h / 3 + super.hoverY + (super.extension * Game.tileSize / 2)));
			side.addPoint((int) x + w - (w / 3), (int) (y + (h / 3) + super.hoverY + (super.extension * Game.tileSize / 2)));
			side.addPoint((int) x + w - (w / 3), (int) (y + (h / 3) + (super.extension * Game.tileSize / 2) + (Game.tileSize * super.height)));
			side.addPoint((int) (x + (w / 2)), (int) (y + h - (h / 3) + (super.extension * Game.tileSize / 2) + (Game.tileSize * super.height)));
			side.addPoint((int) x + (w / 3), (int) (y + (h / 3) + (super.extension * Game.tileSize / 2) + (Game.tileSize * super.height)));
			g.fillPolygon(side);
			g.setColor(darkGreen);
			triangle.addPoint((int) x + (w / 3), (int) ((y + (h / 3) + super.hoverY + (super.extension * Game.tileSize / 2))));
			triangle.addPoint((int) x + w - (w / 3), (int) ((y + (h / 3) + super.hoverY + (super.extension * Game.tileSize / 2))));
			triangle.addPoint((int) x + (w / 2), (int) ((y + h - (h / 3) + super.hoverY + (super.extension * Game.tileSize / 2))));
		}
		else if(super.dir == "right") {
			g.setColor(lightGreen);
			Polygon side = new Polygon();
			side.addPoint((int) (x + (w / 3) + (super.extension * Game.tileSize / 2)), (int) (y + h - (h / 3) + super.hoverY));
			side.addPoint((int) (x + w - (w / 3) + (super.extension * Game.tileSize / 2)), (int) (y + (h / 2) + super.hoverY));
			side.addPoint((int) (x + w - (w / 3) + (super.extension * Game.tileSize / 2)), (int) (y + (h / 2) + (super.height * Game.tileSize)));
			side.addPoint((int) (x + (w / 3) + (super.extension * Game.tileSize / 2)), (int) (y + h - (h / 3) + (super.height * Game.tileSize)));
			g.fillPolygon(side);
			g.setColor(darkGreen);
			triangle.addPoint((int) (x + (w / 3) + (super.extension * Game.tileSize / 2)), (int) (y + (h / 3) + super.hoverY));
			triangle.addPoint((int) (x + (w / 3) + (super.extension * Game.tileSize / 2)), (int) (y + h - (h / 3) + super.hoverY));
			triangle.addPoint((int) (x + w - (w / 3) + (super.extension * Game.tileSize / 2)), (int) (y + (h / 2) + super.hoverY));
		}
		else if(super.dir == "left") {
			g.setColor(lightGreen);
			Polygon side = new Polygon();
			side.addPoint((int) (x + w - (w / 3) - (super.extension * Game.tileSize / 2)), (int) (y + h - (h / 3) + super.hoverY));
			side.addPoint((int) (x + (w / 3) - (super.extension * Game.tileSize / 2)), (int) (y + (h / 2) + super.hoverY));
			side.addPoint((int) (x + (w / 3) - (super.extension * Game.tileSize / 2)), (int) (y + (h / 2) + (super.height * Game.tileSize)));
			side.addPoint((int) (x + w - (w / 3) - (super.extension * Game.tileSize / 2)), (int) (y + h - (h / 3) + (super.height * Game.tileSize)));
			g.fillPolygon(side);
			g.setColor(darkGreen);
			triangle.addPoint((int) (x + w - (w / 3) - (super.extension * Game.tileSize / 2)), (int) (y + (h / 3) + super.hoverY));
			triangle.addPoint((int) (x + w - (w / 3) - (super.extension * Game.tileSize / 2)), (int) (y + h - (h / 3) + super.hoverY));
			triangle.addPoint((int) (x + (w / 3) - (super.extension * Game.tileSize / 2)), (int) (y + (h / 2) + super.hoverY));
		}
		g.fillPolygon(triangle);
	}
	public boolean cursorHovered() {
		int x = (int) (super.x * Game.tileSize) + 200;
		int y = (int) (super.y * Game.tileSize) + 200;
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
							Game.currentLevel.setMoved(super.x, super.y + 1, dir);
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
							Game.currentLevel.setMoved(super.x - 1, super.y, dir);
							Game.currentLevel.setMoved(super.x - 1, super.y - 1, dir);
							break;
						case "down":
							Game.currentLevel.setMoved(super.x - 1, super.y, dir);
							Game.currentLevel.setMoved(super.x - 1, super.y + 1, dir);
							break;
						case "left":
							Game.currentLevel.setMoved(super.x - 2, super.y, dir);
							break;
						case "right":
							Game.currentLevel.setMoved(super.x - 1, super.y, dir);
							break;
						}
					break;
				case "right":
					switch(super.dir) {
						case "up":
							Game.currentLevel.setMoved(super.x + 1, super.y, dir);
							Game.currentLevel.setMoved(super.x + 1, super.y - 1, dir);
							break;
						case "down":
							Game.currentLevel.setMoved(super.x + 1, super.y, dir);
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
	public boolean canBePushed(String dir) {
		/*
		Returns whether this can be pushed without colliding with a wall. Assumes no other extenders exist.
		*/
		if(((dir == "left" && super.x == 0) || (dir == "right" && super.x == Game.levelSize - 1) || (dir == "up" && super.y == 0) || (dir == "down" && super.y == Game.levelSize - 1))) {
			return false;
		}
		if(super.extension != 0) {
			if(dir == "left" && super.dir == "left" && super.x <= 1) {
				return false;
			}
			if(dir == "right" && super.dir == "right" && super.x >= Game.levelSize - 2) {
				return false;
			}
			if(dir == "up" && super.dir == "up" && super.y <= 1) {
				return false;
			}
			if(dir == "down" && super.dir == "down" && super.y >= Game.levelSize - 2) {
				return false;
			}
		}
		return true;
	}
}