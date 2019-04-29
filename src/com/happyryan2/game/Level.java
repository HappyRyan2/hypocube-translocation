package com.happyryan2.game;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;

import com.happyryan2.objects.*;
import com.happyryan2.utilities.Screen;
import com.happyryan2.utilities.MouseClick;
import com.happyryan2.game.Button;

public class Level {
	public List content;
	public boolean hasBeenCompleted = true;
	public boolean completeNow = false;
	private boolean resized = false;
	public String infoTextTop = "";
	public String infoTextBottom = "";
	public int completionY = -500;
	public Button next = new Button(500, 100, 50, 50, new Color(200, 200, 200), new Color(255, 255, 255), "icon:arrowright", "circle");
	public Button menu = new Button(400, 100, 50, 50, new Color(200, 200, 200), new Color(255, 255, 255), "icon:3rects", "circle");
	public Button retry = new Button(300, 100, 50, 50, new Color(200, 200, 200), new Color(255, 255, 255), "icon:arrowleft", "circle"); // retry button that shows when you have completed the level
	public Button pause = new Button(30, 30, 40, 40, new Color(200, 200, 200), new Color(255, 255, 255), "icon:2rects", "circle");
	public Button restart = new Button(300, 325, 200, 50, new Color(200, 200, 200), new Color(255, 255, 255), "Restart", "rect"); // retry button that shows when you are on the pause screen
	public Button exit = new Button(300, 400, 200, 50, new Color(200, 200, 200), new Color(255, 255, 255), "Exit", "rect");
	public boolean lastLevel = false;
	public boolean paused = false;
	public Level() {
		this.content = new ArrayList();
	}
	public void reset() {
		this.completionY = -500;
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			thing.x = thing.origX;
			thing.y = thing.origY;
			if(thing instanceof Extender || thing instanceof Retractor) {
				thing.extending = false;
				thing.retracting = false;
				thing.extension = 0;
			}
			else if(thing instanceof Player || thing instanceof Goal) {
				thing.deleted = false;
				thing.hoverY = 0;
				thing.color = 0;
				thing.winAnimation = false;
			}
		}
	}
	public void update() {
		// initialize
		if(!resized) {
			resize();
		}
		LevelPack pack = (LevelPack) Game.levelPacks.get(Game.packOpen);
		int size = pack.levels.size();
		if(Game.levelOpen == size - 1) {
			this.lastLevel = true;
		}
		if(this.isComplete() || Game.transition > 5) {
			Game.canClick = false;
		}
		if(Game.startingLevel && !MouseClick.mouseIsPressed) {
			Game.canClick = true;
		}
		if(this.paused) {
			Game.canClick = false;
			this.restart.update();
			this.exit.update();
			if(this.restart.pressed) {
				Game.transition = 255;
				this.paused = false;
				this.reset();
			}
			if(this.exit.pressed) {
				this.paused = false;
				Game.transition = 255;
				Game.state = "select-level";
				Game.canClick = false;
				Game.startingLevel = true;
			}
		}
		for(short i = 0; i < Game.currentLevel.content.size(); i ++) {
			Thing thing = (Thing) Game.currentLevel.content.get(i);
			thing.moved = false;
		}
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			thing.update();
		}
		if(this.winAnimationDone()) {
			hasBeenCompleted = true;
			completeNow = true;
			this.retry.update();
			this.menu.update();
			if(!this.lastLevel) {
				this.next.update();
			}
			if(this.menu.pressed) {
				Game.transition = 255;
				Game.state = "select-level";
			}
			if(this.next.pressed && !this.lastLevel) {
				Game.transition = 255;
				Game.levelOpen ++;
				Game.startingLevel = true;
				// LevelPack pack = (LevelPack) Game.levelPacks.get(Game.packOpen);
				Level level = (Level) pack.levels.get(Game.levelOpen);
				level.reset();
			}
			if(this.retry.pressed) {
				Game.transition = 255;
				Game.startingLevel = true;
				this.reset();
			}
		}
		this.pause.update();
		if(this.pause.pressed && !this.pause.pressedBefore) {
			this.paused = !this.paused;
		}
	}
	public void display(Graphics g) {
		// sort by y-value (display top ones first)
		List sorted = new ArrayList();
		List unsorted = new ArrayList();
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			unsorted.add(thing);
		}
		while(unsorted.size() > 0) {
			int highestIndex = 0;
			for(short i = 0; i < unsorted.size(); i ++) {
				Thing thing = (Thing) unsorted.get(i);
				Thing highest = (Thing) unsorted.get(highestIndex);
				if(thing.y < highest.y && !(highest instanceof Goal) || thing instanceof Goal) {
					highestIndex = i;
				}
			}
			Thing thing = (Thing) unsorted.get(highestIndex);
			sorted.add(thing);
			unsorted.remove(highestIndex);
		}
		// display objects
		g.translate(100, 100);
		for(short i = 0; i < sorted.size(); i ++) {
			Thing thing = (Thing) sorted.get(i);
			thing.display(g);
		}
		g.translate(-100, -100);
		//gui box for winning
		if(this.winAnimationDone()) {
			if(this.completionY < 100) {
				this.completionY += Math.max((100 - this.completionY) / 15, 1);
			}
			g.setColor(new Color(100, 100, 100, 150));
			g.fillRect(200, this.completionY, 400, 600);
			g.setFont(Screen.fontRighteous);
			g.setColor(new Color(255, 255, 255));
			Screen.centerText(g, 400, this.completionY + 200, "Level Complete");
			this.retry.y = this.completionY + 400;
			this.menu.y = this.completionY + 400;
			this.next.y = this.completionY + 400;
			this.retry.display(g);
			this.menu.display(g);
			if(!this.lastLevel) {
				this.next.display(g);
			}
			else {
				this.retry.x = 350;
				this.menu.x = 450;
			}
		}
		if(this.paused) {
			g.setColor(new Color(100, 100, 100, 150));
			g.fillRect(200, 0, 400, 800);
			g.setFont(Screen.fontRighteous);
			g.setColor(new Color(255, 255, 255));
			Screen.centerText(g, 400, 300, "Menu");
			this.restart.display(g);
			this.exit.display(g);
		}
		// border
		g.setColor(new Color(125, 125, 125));
		g.fillRect(0, 0, 800, 100);
		g.fillRect(0, 0, 100, 800);
		g.fillRect(700, 0, 100, 800);
		g.fillRect(0, 700, 800, 100);
		// text for tutorials
		if(this.infoTextBottom != "" && !this.isComplete()) {
			g.setColor(new Color(200, 200, 200));
			g.setFont(Screen.fontRighteous.deriveFont(15f));
			Screen.centerText(g, 400, 725, this.infoTextTop);
			Screen.centerText(g, 400, 750, this.infoTextBottom);
		}
		// pause button
		this.pause.display(g);
	}
	public void resize() {
		resized = true;
		float left = 0;
		float right = 0;
		float top = 0;
		float bottom = 0;
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			float x = (float) thing.x;
			float y = (float) thing.y;
			if(x < left) {
				left = x;
			}
			else if(x > right) {
				right = x;
			}
			if(y < top) {
				top = y;
			}
			else if(y > bottom) {
				bottom = y;
			}
		}
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			thing.x -= left;
			thing.y -= top;
		}
		Game.levelSize = (right - left > bottom - top) ? (right - left + 1) : (bottom - top + 1);
		Game.tileSize = 600 / Game.levelSize;
	}
	public Thing getAtPos(float x, float y) {
		// return if there is an extender in that position
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			if(thing instanceof Goal) {
				continue;
			}
			if(thing.x == x && thing.y == y) {
				return thing;
			}
		}
		// return if there is an extender pushing out into that position
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			if(thing.extension == 0 || thing instanceof Goal) {
				continue;
			}
			if((thing.x == x && thing.y == y - 1 && thing.dir == "down") || (thing.x == x && thing.y == y + 1 && thing.dir == "up") || (thing.y == y && thing.x == x - 1 && thing.dir == "right") || (thing.y == y && thing.x == x + 1 && thing.dir == "left")) {
				return thing;
			}
		}
		// if you haven't found anything by now, there must not be anything there
		return null;
	};
	public void setMoved(float x, float y, String dir) {
		Thing thing = this.getAtPos(x, y);
		if(thing == null || thing.moved || thing.ignoring || thing instanceof Goal) {
			return;
		}
		thing.moved = true;
		thing.checkMovement(dir);
	}
	public boolean isComplete() {
		boolean complete = true;
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			if(thing instanceof Goal) {
				boolean occupied = false;
				for(short j = 0; j < this.content.size(); j ++) {
					Thing thing2 = (Thing) this.content.get(j);
					if(thing2 instanceof Player && thing2.x == thing.x && thing2.y == thing.y) {
						occupied = true;
						break;
					}
				}
				if(!occupied) {
					complete = false;
					break;
				}
			}
		}
		boolean hasAGoal = false;
		for(byte i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			if(thing instanceof Goal) {
				hasAGoal = true;
			}
		}
		if(!hasAGoal) {
			return false; // level is under construction
		}
		return complete;
	}
	public boolean winAnimationDone() {
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			if(thing instanceof Goal && thing.color < 255) {
				return false;
			}
		}
		return true;
	}
}
