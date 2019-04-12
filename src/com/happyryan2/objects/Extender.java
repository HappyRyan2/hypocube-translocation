package com.happyryan2.objects;

import java.awt.Graphics;
import java.awt.Color;

import com.happyryan2.game.Game;
import com.happyryan2.utilities.MousePos;
import com.happyryan2.utilities.Screen;

public class Extender extends Thing {
	public float x;
	public float y;
	public String dir;
	public Extender(float x, float y, String dir) {
		super.x = x;
		super.y = y;
		super.dir = dir;
		super.hoverY = 0;
	}
	public void update() {
		System.out.println("updating");
		int x = (int) Math.round(super.x * Game.tileSize);
		int y = (int) Math.round(super.y * Game.tileSize);
		int w = (int) Math.round(Game.tileSize);
		int h = (int) Math.round(Game.tileSize);
		if(MousePos.x > x + 200 && MousePos.x < x + w + 200 && MousePos.y > y + 200 && MousePos.y < y + h + 200) {
			System.out.println("hovering");
			Screen.cursor = "hand";
			if(super.hoverY < 5) {
				super.hoverY ++;
			}
		}
		else if(super.hoverY > 0) {
			super.hoverY --;
		}
	}
	public void display(Graphics g) {
		g.setColor(new Color(100, 100, 100));
		int x = (int) Math.round(super.x * Game.tileSize);
		int y = (int) Math.round(super.y * Game.tileSize);
		int w = (int) Math.round(Game.tileSize);
		int h = (int) Math.round(Game.tileSize);
		//horizontal
		raisedRect(g, (float) x, (float) y, (float) w / 3, (float) h / 6);
		raisedRect(g, (float) x + (w / 3 * 2), (float) y, (float) w / 3, (float) h / 6);
		raisedRect(g, (float) x, (float) y + (h / 6 * 5), (float) w / 3, (float) h / 6);
		raisedRect(g, (float) x + (w / 3 * 2), (float) y + (h / 6 * 5), (float) w / 3, (float) h / 6);
		//vertical
		raisedRect(g, (float) x, (float) y, (float) w / 6, (float) h / 3);
		raisedRect(g, (float) x, (float) y + (h / 3 * 2), (float) w / 6, (float) h / 3);
		raisedRect(g, (float) x + (w / 6 * 5), (float) y, (float) w / 6, (float) h / 3);
		raisedRect(g, (float) x + (w / 6 * 5), (float) y + (h / 3 * 2), (float) w / 6, (float) h / 3);
	}
	public void raisedRect(Graphics g, float x, float y, float w, float h) {
		int vX = (int) Math.round(x);
		int vY = (int) Math.round(y);
		int vW = (int) Math.round(w);
		int vH = (int) Math.round(h);
		g.setColor(new Color(150, 150, 150, 255));
		g.fillRect(vX, vY + vH, vW, 5);
		g.setColor(new Color(100, 100, 100, 255));
		g.fillRect(vX, vY + Math.round(super.hoverY), vW, vH);
	}
}
