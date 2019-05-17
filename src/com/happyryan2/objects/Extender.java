package com.happyryan2.objects;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Polygon;
import java.lang.Math;

import com.happyryan2.game.Game;
import com.happyryan2.game.Stack;
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
		if(!transitioning) {
			this.ignoring = true;
			if(this.cursorHovered() && Game.canClick && !Game.currentLevel.isComplete() && super.extension <= 0 && super.dir != "none") {
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
				int numAffected = 0;
				for(short i = 0; i < Game.currentLevel.content.size(); i ++) {
					Thing thing = (Thing) Game.currentLevel.content.get(i);
					if(!thing.moved) {
						continue;
					}
					if((thing.x == 0 && super.dir == "left") || (thing.y == 0 && super.dir == "up") || (thing.x == Game.currentLevel.width - 1 && super.dir == "right") || (thing.y == Game.currentLevel.height - 1 && super.dir == "down")) {
						canExtend = false;
						break;
					}
					numAffected ++;
				}
				if(super.isWeak && numAffected > 1) {
					canExtend = false;
				}
				if(!canExtend || ((super.dir == "up" && super.y == 0) || (super.dir == "down" && super.y == Game.currentLevel.height - 1) || (super.dir == "left" && super.x == 0) || (super.dir == "right" && super.x == Game.currentLevel.width - 1))) {
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
					numAffected = 0;
					for(short i = 0; i < Game.currentLevel.content.size(); i ++) {
						Thing thing = (Thing) Game.currentLevel.content.get(i);
						if(thing.moved && !thing.canBePushed(super.dir)) {
							canExtend = false;
							break;
						}
						if(thing.moved) {
							numAffected ++;
						}
					}
					if(super.isWeak && numAffected > 1) {
						canExtend = false;
					}
					if((super.dir == "up" && super.y == Game.currentLevel.height - 1) || (super.dir == "down" && super.y == 0) || (super.dir == "left" && super.x == Game.currentLevel.width - 1) || (super.dir == "right" && super.x == 0)) {
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
							Stack.addAction();
						}
					}
					canExtend = false;
				}
				if(canExtend && !((super.x == 0 && super.dir == "left") || (super.y == 0 && super.dir == "up") || (super.x == Game.currentLevel.width - 1 && super.dir == "right") || (super.y == Game.currentLevel.height - 1 && super.dir == "down"))) {
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
						Stack.addAction();
					}
				}
			}
			else if(this.cursorHovered() && Game.canClick && !Game.currentLevel.isComplete() && super.dir != "none") {
				Screen.cursor = "hand";
				if(super.hoverY < h * super.height) {
					super.hoverY ++;
				}
				if(MouseClick.mouseIsPressed) {
					Game.canClick = false;
					super.retracting = true;
					Stack.addAction();
				}
			}
			else if(super.hoverY > 0 && !super.extending && !super.retracting) {
				super.hoverY --;
			}
			this.ignoring = false;
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
		if(super.retracting) {
			// System.out.println("retracting!");
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
			triangle.addPoint((int) x + (w / 3), (int) ((y + h - (h / 3) + super.hoverY - (super.extension * Game.tileSize / 2))));
			triangle.addPoint((int) x + w - (w / 3), (int) ((y + h - (h / 3) + super.hoverY) - (super.extension * Game.tileSize / 2)));
			triangle.addPoint((int) x + (w / 2), (int) ((y + (h / 3) + super.hoverY) - (super.extension * Game.tileSize / 2)));
			raisedRect(g, (double) x + (w / 3), (double) y + h - (h / 3) - (super.extension * Game.tileSize / 2), (double) w / 3, (double) 1.0);
		}
		else if(super.dir == "down") {
			g.setColor(new Color(150, 150, 150));
			Polygon side = new Polygon();
			side.addPoint((int) x + (w / 3), (int) (y + h / 3 + super.hoverY + (super.extension * Game.tileSize / 2)));
			side.addPoint((int) x + w - (w / 3), (int) (y + (h / 3) + super.hoverY + (super.extension * Game.tileSize / 2)));
			side.addPoint((int) x + w - (w / 3), (int) (y + (h / 3) + (super.extension * Game.tileSize / 2) + (Game.tileSize * super.height)));
			side.addPoint((int) (x + (w / 2)), (int) (y + h - (h / 3) + (super.extension * Game.tileSize / 2) + (Game.tileSize * super.height)));
			side.addPoint((int) x + (w / 3), (int) (y + (h / 3) + (super.extension * Game.tileSize / 2) + (Game.tileSize * super.height)));
			g.fillPolygon(side);
			g.setColor(new Color(100, 100, 100));
			triangle.addPoint((int) x + (w / 3), (int) ((y + (h / 3) + super.hoverY + (super.extension * Game.tileSize / 2))));
			triangle.addPoint((int) x + w - (w / 3), (int) ((y + (h / 3) + super.hoverY + (super.extension * Game.tileSize / 2))));
			triangle.addPoint((int) x + (w / 2), (int) ((y + h - (h / 3) + super.hoverY + (super.extension * Game.tileSize / 2))));
		}
		else if(super.dir == "right") {
			g.setColor(new Color(150, 150, 150));
			Polygon side = new Polygon();
			side.addPoint((int) (x + (w / 3) + (super.extension * Game.tileSize / 2)), (int) (y + h - (h / 3) + super.hoverY));
			side.addPoint((int) (x + w - (w / 3) + (super.extension * Game.tileSize / 2)), (int) (y + (h / 2) + super.hoverY));
			side.addPoint((int) (x + w - (w / 3) + (super.extension * Game.tileSize / 2)), (int) (y + (h / 2) + (super.height * Game.tileSize)));
			side.addPoint((int) (x + (w / 3) + (super.extension * Game.tileSize / 2)), (int) (y + h - (h / 3) + (super.height * Game.tileSize)));
			g.fillPolygon(side);
			g.setColor(new Color(100, 100, 100));
			triangle.addPoint((int) (x + (w / 3) + (super.extension * Game.tileSize / 2)), (int) (y + (h / 3) + super.hoverY));
			triangle.addPoint((int) (x + (w / 3) + (super.extension * Game.tileSize / 2)), (int) (y + h - (h / 3) + super.hoverY));
			triangle.addPoint((int) (x + w - (w / 3) + (super.extension * Game.tileSize / 2)), (int) (y + (h / 2) + super.hoverY));
		}
		else if(super.dir == "left") {
			g.setColor(new Color(150, 150, 150));
			Polygon side = new Polygon();
			side.addPoint((int) (x + w - (w / 3) - (super.extension * Game.tileSize / 2)), (int) (y + h - (h / 3) + super.hoverY));
			side.addPoint((int) (x + (w / 3) - (super.extension * Game.tileSize / 2)), (int) (y + (h / 2) + super.hoverY));
			side.addPoint((int) (x + (w / 3) - (super.extension * Game.tileSize / 2)), (int) (y + (h / 2) + (super.height * Game.tileSize)));
			side.addPoint((int) (x + w - (w / 3) - (super.extension * Game.tileSize / 2)), (int) (y + h - (h / 3) + (super.height * Game.tileSize)));
			g.fillPolygon(side);
			g.setColor(new Color(100, 100, 100));
			triangle.addPoint((int) (x + w - (w / 3) - (super.extension * Game.tileSize / 2)), (int) (y + (h / 3) + super.hoverY));
			triangle.addPoint((int) (x + w - (w / 3) - (super.extension * Game.tileSize / 2)), (int) (y + h - (h / 3) + super.hoverY));
			triangle.addPoint((int) (x + (w / 3) - (super.extension * Game.tileSize / 2)), (int) (y + (h / 2) + super.hoverY));
		}
		g.fillPolygon(triangle);
		//cutout for single-tile extenders
		if(super.isWeak) {
			Polygon cutout = new Polygon();
			if(super.dir == "up") {
				cutout.addPoint((int) Math.round(x + (w / 3) + (w / 18)), (int) Math.round((y + h - (h / 3) - (h / 36) + super.hoverY - (super.extension * Game.tileSize / 2)))); // left
				cutout.addPoint((int) Math.round(x + (w / 2)), (int) Math.round((y + (h / 3) + (h / 9) + super.hoverY) - (super.extension * Game.tileSize / 2))); // middle
				cutout.addPoint((int) Math.round(x + w - (w / 3) - (w / 18)), (int) Math.round((y + h - (h / 3) - (h / 36) + super.hoverY) - (super.extension * Game.tileSize / 2))); // right
			}
			else if(super.dir == "down") {
				cutout.addPoint((int) Math.round(x + (w / 3) + (w / 18)), (int) Math.round((y + (h / 3) + (h / 36) + super.hoverY + (super.extension * Game.tileSize / 2)))); // left
				cutout.addPoint((int) Math.round(x + (w / 2)), (int) Math.round((y + h - (h / 3) - (h / 9) + super.hoverY) + (super.extension * Game.tileSize / 2))); // middle
				cutout.addPoint((int) Math.round(x + w - (w / 3) - (w / 18)), (int) Math.round((y + (h / 3) + (h / 36) + super.hoverY) + (super.extension * Game.tileSize / 2))); // right
			}
			else if(super.dir == "left") {
				cutout.addPoint((int) Math.round(x + w - (w / 3) - (w / 36) - (super.extension * Game.tileSize / 2)), (int) Math.round(y + h - (h / 3) - (h / 18) + super.hoverY)); // bottom
				cutout.addPoint((int) Math.round(x + (w / 3) + (w / 9) - (super.extension * Game.tileSize / 2)), (int) Math.round(y + (h / 2) + super.hoverY)); // middle
				cutout.addPoint((int) Math.round(x + w - (w / 3) - (w / 36) - (super.extension * Game.tileSize / 2)), (int) Math.round(y + (h / 3) + (h / 18) + super.hoverY)); // top
			}
			else if(super.dir == "right") {
				cutout.addPoint((int) Math.round(x + (w / 3) + (w / 36) + (super.extension * Game.tileSize / 2)), (int) Math.round(y + h - (h / 3) - (h / 18) + super.hoverY)); // bottom
				cutout.addPoint((int) Math.round(x + w - (w / 3) - (w / 9) + (super.extension * Game.tileSize / 2)), (int) Math.round(y + (h / 2) + super.hoverY)); // middle
				cutout.addPoint((int) Math.round(x + (w / 3) + (w / 36) + (super.extension * Game.tileSize / 2)), (int) Math.round(y + (h / 3) + (h / 18) + super.hoverY)); // top
			}
			g.setColor(new Color(255, 255, 255));
			g.fillPolygon(cutout);
			g.setClip(cutout);
			Polygon inside = new Polygon();
			if(super.dir == "up") {
				inside.addPoint((int) Math.round(x + (w / 3) + (w / 18)), (int) Math.round((y + h - (h / 3) - (h / 36) + super.hoverY - (super.extension * Game.tileSize / 2)))); // left
				inside.addPoint((int) Math.round(x + (w / 2)), (int) Math.round((y + (h / 3) + (h / 9) + super.hoverY) - (super.extension * Game.tileSize / 2))); // middle
				inside.addPoint((int) Math.round(x + w - (w / 3) - (w / 18)), (int) Math.round((y + h - (h / 3) - (h / 36) + super.hoverY) - (super.extension * Game.tileSize / 2))); // right
				inside.addPoint((int) Math.round(x + w - (w / 3) - (w / 18)), (int) Math.round((y + h - (h / 3) - (h / 36) + (Game.tileSize * super.height)) - (super.extension * Game.tileSize / 2))); // right bottom
				inside.addPoint((int) Math.round(x + (w / 2)), (int) Math.round((y + (h / 3) + (h / 9) + (Game.tileSize * super.height)) - (super.extension * Game.tileSize / 2))); // middle
				inside.addPoint((int) Math.round(x + (w / 3) + (w / 18)), (int) Math.round((y + h - (h / 3) - (h / 36) + (Game.tileSize * super.height) - (super.extension * Game.tileSize / 2)))); // left
			}
			else if(super.dir == "down") {
				raisedRect(g, x + (w / 3) + (w / 18), y + (h / 3) + (h / 36) + (super.extension * Game.tileSize / 2), (w / 3), 1);
			}
			else if(super.dir == "left") {
				inside.addPoint((int) Math.round(x + (w / 3) + (w / 9) - (super.extension * Game.tileSize / 2)), (int) Math.round(y + (h / 2) + super.hoverY)); // middle
				inside.addPoint((int) Math.round(x + w - (w / 3) - (w / 36) - (super.extension * Game.tileSize / 2)), (int) Math.round(y + (h / 3) + (h / 18) + super.hoverY)); // right
				inside.addPoint((int) Math.round(x + w - (w / 3) - (w / 36) - (super.extension * Game.tileSize / 2)), (int) Math.round(y + (h / 3) + (h / 18) + (super.height * Game.tileSize))); // right
				inside.addPoint((int) Math.round(x + (w / 3) + (w / 9) - (super.extension * Game.tileSize / 2)), (int) Math.round(y + (h / 2) + (super.height * Game.tileSize))); // middle
			}
			else if(super.dir == "right") {
				inside.addPoint((int) Math.round(x + w - (w / 3) - (w / 9) + (super.extension * Game.tileSize / 2)), (int) Math.round(y + (h / 2) + super.hoverY)); // middle
				inside.addPoint((int) Math.round(x + (w / 3) + (w / 36) + (super.extension * Game.tileSize / 2)), (int) Math.round(y + (h / 3) + (h / 18) + super.hoverY)); // right
				inside.addPoint((int) Math.round(x + (w / 3) + (w / 36) + (super.extension * Game.tileSize / 2)), (int) Math.round(y + (h / 3) + (h / 18) + (super.height * Game.tileSize))); // right
				inside.addPoint((int) Math.round(x + w - (w / 3) - (w / 9) + (super.extension * Game.tileSize / 2)), (int) Math.round(y + (h / 2) + (super.height * Game.tileSize))); // middle
			}
			g.setColor(new Color(150, 150, 150));
			g.fillPolygon(inside);
			g.setClip(null);
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
