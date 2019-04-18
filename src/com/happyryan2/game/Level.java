package com.happyryan2.game;

import java.awt.Graphics;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;

import com.happyryan2.objects.*;

public class Level {
	public List content;
	public boolean hasBeenCompleted = false;
	public boolean completeNow = false;
	private boolean resized = false;
	public String infoText = "";
	public int completionY = -500;
	public Level() {
		this.content = new ArrayList();
	}
	public void update() {
		System.out.println("-----------------------------");
		if(!resized) {
			resize();
		}
		if(this.isComplete()) {
			Game.canClick = false;
		}
		for(short i = 0; i < Game.currentLevel.content.size(); i ++) {
			Thing thing = (Thing) Game.currentLevel.content.get(i);
			thing.moved = false;
		}
		for(short i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			thing.update();
		}
		if(this.isComplete()) {
			hasBeenCompleted = true;
			completeNow = true;
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
		g.translate(200, 200);
		for(short i = 0; i < sorted.size(); i ++) {
			Thing thing = (Thing) sorted.get(i);
			thing.display(g);
		}
		g.translate(-200, -200);
		// border
		g.setColor(new Color(125, 125, 125));
		g.fillRect(0, 0, 800, 200);
		g.fillRect(0, 0, 200, 800);
		g.fillRect(600, 0, 200, 800);
		g.fillRect(0, 600, 800, 200);
		//gui box for winning
		if(this.isComplete()) {
			this.completionY += Math.min(Math.abs(this.completionY, 200) / 10, 1);
			g.setColor(new Color(100, 100, 100));
			g.fillRect(200, this.completionY, 400, 400);
			g.setColor(new Color(0, 0, 255));
			g.drawString("Level Complete", 400, this.completionY + 100);
		}
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
		Game.tileSize = 400 / Game.levelSize;
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
		System.out.println("checking the coords (" + x + ", " + y + ")");
		Thing thing = this.getAtPos(x, y);
		if(thing == null || thing.moved || thing.ignoring || thing instanceof Goal) {
			System.out.println("and it's empty! (terminate branch)");
			return;
		}
		System.out.println("it is an extender at coords (" + thing.x + ", " + thing.y + ") with an extension of " + thing.extension);
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
		return complete;
	}
}
