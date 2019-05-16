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
	public Button pause = new Button(30, 30, 40, 40, new Color(175, 175, 175), new Color(255, 255, 255), "icon:2rects", "circle");
	public Button restart = new Button(300, 325, 200, 50, new Color(200, 200, 200), new Color(255, 255, 255), "Restart", "rect"); // retry button that shows when you are on the pause screen
	public Button exit = new Button(300, 400, 200, 50, new Color(200, 200, 200), new Color(255, 255, 255), "Exit", "rect");
	public Button undo = new Button(100, 30, 40, 40, new Color(175, 175, 175), new Color(255, 255, 255), "icon:arrowleft", "circle");
	public boolean lastLevel = false;
	public boolean paused = false;
	public boolean isForTesting = false;
	public int width = 0;
	public int height = 0;
	public int visualWidth = 0;
	public int visualHeight = 0;
	public int top = 0;
	public int left = 0;
	public Level() {
		this.content = new ArrayList();
	}
	public Level(int w, int h) {

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
		// Stack.resetStack();
		// initialize
		if(!resized) {
			resize();
		}
		if(!this.isForTesting) {
			LevelPack pack = (LevelPack) Game.levelPacks.get(Game.packOpen);
			int size = pack.levels.size();
			if(Game.levelOpen == size - 1) {
				this.lastLevel = true;
			}
		}
		if(this.isComplete() || Game.transition > 5) {
			Game.canClick = false;
		}
		if(Game.startingLevel && !MouseClick.mouseIsPressed) {
			System.out.println("clicking");
			Game.canClick = true;
		}
		for(byte i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			if((thing instanceof Extender || thing instanceof Retractor) && ((thing.extension != 0 && thing.extension != 1) || thing.extending || thing.retracting)) {
				Game.canClick = false;
				break;
			}
			if(thing instanceof Player && (thing.x != Math.round(thing.x) || thing.y != Math.round(thing.y))) {
				Game.canClick = false;
				break;
			}
		}
		if(this.paused) {
			Game.canClick = false;
			this.restart.update();
			this.exit.update();
			if(this.restart.pressed && !this.isForTesting) {
				Game.transition = 255;
				this.paused = false;
				this.reset();
			}
			if(this.exit.pressed && !this.isForTesting) {
				this.paused = false;
				Game.transition = 255;
				Game.state = "select-level";
				Game.canClick = false;
				Game.startingLevel = true;
			}
		}
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
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
			if(this.menu.pressed && !this.isForTesting) {
				Game.transition = 255;
				Game.state = "select-level";
			}
			if(this.next.pressed && !this.lastLevel && !this.isForTesting) {
				Game.transition = 255;
				Game.levelOpen ++;
				Game.startingLevel = true;
				// LevelPack pack = (LevelPack) Game.levelPacks.get(Game.packOpen);
				LevelPack pack = (LevelPack) Game.levelPacks.get(Game.packOpen);
				Level level = (Level) pack.levels.get(Game.levelOpen);
				level.reset();
				level.resize();
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
		this.undo.update();
		if(this.undo.pressed && !this.undo.pressedBefore) {
			Stack.undoAction();
		}
	}
	public void display(Graphics g) {
		// System.out.println("level size: (" + this.width + ", " + this.height + ")");
		// System.out.println("visual level size: (" + this.visualWidth + ", " + this.visualHeight + ")");
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
		g.translate(this.left, this.top);
		for(short i = 0; i < sorted.size(); i ++) {
			Thing thing = (Thing) sorted.get(i);
			thing.display(g);
		}
		g.translate(-this.left, -this.top);
		//gui box for winning
		if(this.winAnimationDone() && this.isComplete()) {
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
		g.setColor(new Color(200, 200, 200));
		g.fillRect(0, 0, 800, this.top);
		g.fillRect(0, 0, this.left, 800);
		g.fillRect(800 - this.left, 0, this.left, 800);
		g.fillRect(0, 800 - this.top, 800, this.top);
		// text for tutorials
		if(this.infoTextBottom != "" && !this.isComplete()) {
			g.setColor(new Color(200, 200, 200));
			g.setFont(Screen.fontRighteous.deriveFont(15f));
			Screen.centerText(g, 400, 725, this.infoTextTop);
			Screen.centerText(g, 400, 750, this.infoTextBottom);
		}
		// pause button
		this.pause.display(g);
		this.undo.display(g);
	}
	public void resize() {
		if(this.width != 0 || this.height != 0) {
			return;
		}
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
		// for(short i = 0; i < this.content.size(); i ++) {
		// 	Thing thing = (Thing) this.content.get(i);
		// 	thing.x -= left;
		// 	thing.y -= top;
		// }
		Game.levelSize = (right - left > bottom - top) ? (right - left + 1) : (bottom - top + 1);
		this.width = (int) (right - left + 1);
		this.height = (int) (bottom - top + 1);
		if(this.width < this.height) {
			this.visualHeight = 600;
			this.visualWidth = Math.round(600 * (float) ( (float) this.width / (float) this.height));
		}
		else {
			this.visualWidth = 600;
			this.visualHeight = Math.round(600 * ( (float) this.height / (float) this.width));
		}
		this.left = Math.round((800 - this.visualWidth) / 2.0f);
		this.top = Math.round((800 - this.visualHeight) / 2.0f);
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
