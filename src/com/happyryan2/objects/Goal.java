package com.happyryan2.objects;

import java.awt.Graphics;
import java.awt.Color;

import com.happyryan2.game.Game;
import com.happyryan2.game.Level;
import com.happyryan2.game.LevelPack;

public class Goal extends Thing {
	public Color darkRed = new Color(128, 0, 0);
	public Goal(float x, float y) {
		super.x = x;
		super.y = y;
		super.origX = x;
		super.origY = y;
	}
	public void update() {
		super.height = Game.sizes[(int) Game.levelSize - 2];
	}
	public void display(Graphics g) {
		int x = (int) (super.x * Game.tileSize);
		int y = (int) (super.y * Game.tileSize);
		int w = (int) (Game.tileSize);
		int h = (int) (Game.tileSize);
		g.setColor(darkRed);
		rect(g, (double) x, (double) y, (double) w, (double) h);
		darkRed = new Color(128, 0, super.color);
		if(super.winAnimation) {
			super.color += (super.color < 255) ? 5 : 0;
			if(super.color >= 255) {
				// Game.levelOpen ++;
				// Game.canClick = true;
			}
		}
	}
	public void rect(Graphics g, double x, double y, double w, double h) {
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
		g.fillRect(vX, (int) (vY + (super.height * Game.tileSize)), vW, (int) (vH));
	}
	public boolean canBePushed() {
		return true;
	}
}
