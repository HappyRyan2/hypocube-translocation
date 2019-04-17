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
	}
	public void update() {
		super.height = Game.sizes[(int) Game.levelSize - 2];
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
		g.setColor(darkRed);
		//horizontal
		rect(g, (int) x, (int) (y + (super.height * Game.tileSize)), (int) w / 3, (int) h / 6);
		rect(g, (int) (x + w - (w / 3)), (int) (y + (super.height * Game.tileSize)), (int) w / 3, (int) h / 6);
		rect(g, (int) x, (int) (y + (super.height * Game.tileSize) + (h / 6 * 5)), (int) w / 3, (int) h / 6);
		rect(g, (int) (x + w - (w / 3)), (int) (y + (super.height * Game.tileSize)) + (h / 6 * 5), (int) w / 3, (int) h / 6);
		//vertical
		rect(g, (int) x, (int) (y + (super.height * Game.tileSize)), (int) w / 6, (int) h / 3);
		rect(g, (int) x, (int) (y + (super.height * Game.tileSize)) + h - (h / 3), (int) w / 6, (int) h / 3);
		rect(g, (int) (x + (w / 6 * 5)), (int) (y + (super.height * Game.tileSize)), (int) w / 6, (int) h / 3);
		rect(g, (int) (x + (w / 6 * 5)), (int) (y + (super.height * Game.tileSize)) + h - (h / 3), (int) w / 6, (int) h / 3);
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
		g.fillRect(vX, (int) (vY + (super.height * Game.tileSize * 0)), vW, (int) (vH));
	}
	public boolean canBePushed() {
		return true;
	}
}
