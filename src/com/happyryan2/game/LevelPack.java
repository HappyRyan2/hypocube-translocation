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
				this.buttons.add(new Button(200 + (i * (400 / 4) - 15), 400, 30, 30, new Color(100, 100, 100), new Color(150, 150, 150), "" + (i + 1), "rect", (level.hasBeenCompleted || true ? new Color(100, 100, 255) : new Color(150, 150, 150))));
			}
		}
		for(byte i = 0; i < this.buttons.size(); i ++) {
			Button button = (Button) this.buttons.get(i);
			button.display(g);
		}
	}
	public void updateLevelSelect() {
		for(byte i = 0; i < this.buttons.size(); i ++) {
			Button button = (Button) this.buttons.get(i);
			button.update();
		}
	}
	public void displayLevelInfo(Graphics g, float y) {
		y = (int) Math.round(y);
	}
}
