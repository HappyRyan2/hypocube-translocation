/*
Represents a pack of levels.
*/

package com.happyryan2.game;

import java.util.List;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Color;

import com.happyryan2.utilities.Screen;
import com.happyryan2.game.Button;

public class LevelPack {
	private boolean initialized = false;
	public List levels = new ArrayList();
	public String name = "";
	public List buttons = new ArrayList();
	public Button playButton = new Button(450, 0, 100, 50, new Color(100, 100, 100), new Color(150, 150, 150), "play", "rect");
	public Button backButton = new Button(25, 25, 30, 30, new Color(0, 0, 255), new Color(100, 100, 255), "icon:arrowleft", "circle");
	public void displayLevelSelect(Graphics g) {
		// margins
		g.setColor(new Color(100, 100, 100));
		g.fillRect(0, 0, 800, 100);
		g.fillRect(0, 700, 800, 100);
		// title
		g.setColor(new Color(100, 100, 255));
		g.setFont(Screen.fontRighteous);
		Screen.centerText(g, 400, 200, this.name);
		// generate buttons + change graphics
		if(this.buttons.size() == 0) {
			for(byte i = 0; i < this.levels.size(); i ++) {
				Level level = (Level) this.levels.get(i);
				this.buttons.add(new Button(200 + (i * (400 / 4) - 15), 400, 30, 30, new Color(100, 100, 100), new Color(150, 150, 150), "" + (i + 1), "rect", (level.hasBeenCompleted || true ? new Color(0, 255, 255) : new Color(150, 150, 150))));
			}
		}
		for(byte i = 0; i < this.buttons.size(); i ++) {
			Button button = (Button) this.buttons.get(i);
			boolean canPlay = false;
			if(i == 0) {
				canPlay = true;
			}
			else {
				Level previous = (Level) this.levels.get(i - 1);
				canPlay = previous.hasBeenCompleted;
			}
			Level level = (Level) this.levels.get(i);
			if(!canPlay) {
				// you haven't unlocked the level, so gray it out
				button.textCol = new Color(150, 150, 150);
			}
			else if(level.hasBeenCompleted) {
				// you have already completed the level, so fill the text blue
				button.textCol = new Color(0, 255, 255);
			}
			else {
				// this is the level you have unlocked but not played, so highlight it
				g.setColor(new Color(0, 255, 255));
				g.drawRect(Math.round(button.x) - 15, Math.round(button.y) - 15 + Math.round(button.hoverH), Math.round(button.w) + 30, Math.round(button.h) + 30 - Math.round(button.hoverH));
			}
			button.display(g);
		}
		// back button
		this.backButton.display(g);
	}
	public void updateLevelSelect() {
		for(byte i = 0; i < this.buttons.size(); i ++) {
			if(i != 0) {
				Level previous = (Level) this.levels.get(i - 1);
				if(!previous.hasBeenCompleted) {
					continue;
				}
			}
			Button button = (Button) this.buttons.get(i);
			button.update();
			if(button.pressed) {
				Game.state = "play";
				Game.levelOpen = i;
				Game.startingLevel = true;
				Game.transition = 255;
				Level level = (Level) this.levels.get(i);
				level.reset();
			}
		}
		this.backButton.update();
		if(this.backButton.pressed) {
			Game.transition = 255;
			Game.state = "select-levelpack";
		}
	}
	public void displayLevelInfo(Graphics g, float y) {
		y = (int) Math.round(y);
		g.setColor(new Color(100, 100, 255));
		g.fillRect(200, (int) y, 400, 100);
		this.playButton.display(g);
		this.playButton.update();
		g.setColor(new Color(255, 255, 255));
		g.setFont(Screen.fontRighteous.deriveFont(20f));
		g.drawString(this.name, 220, (int) (y + 35));
		g.drawString(this.numCompleted() + " / " + this.levels.size(), 250, (int) (y + 75));
	}
	public void updateLevelInfo(float y) {
		y = (int) Math.round(y);
		this.playButton.update();
		playButton.y = y + 25;
	}
	public int numCompleted() {
		int numComplete = 0;
		for(byte i = 0; i < this.levels.size(); i ++) {
			Level level = (Level) this.levels.get(i);
			if(level.hasBeenCompleted) {
				numComplete ++;
			}
		}
		return numComplete;
	}
}
