package com.happyryan2.game;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Polygon;

import com.happyryan2.utilities.MousePos;
import com.happyryan2.utilities.MouseClick;
import com.happyryan2.utilities.Screen;

public class Button {
	public float x;
	public float y;
	public float w;
	public float h;
	public Color frontCol;
	public Color sideCol;
	public Color textCol = null;
	public float hoverY;
	public final float hoverH = 6;
	public String text;
	public String type;
	public boolean pressed = false;
	public boolean pressedBefore = false;
	public Button(float x, float y, float w, float h, Color frontCol, Color sideCol, String text, String type) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.frontCol = frontCol;
		this.sideCol = sideCol;
		this.text = text;
		this.type = type;
		this.hoverY = 0;
	}
	public Button(float x, float y, float w, float h, Color frontCol, Color sideCol, String text, String type, Color textCol) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.frontCol = frontCol;
		this.sideCol = sideCol;
		this.textCol = textCol;
		this.text = text;
		this.type = type;
		this.hoverY = 0;
	}
	public void display(Graphics g) {
		// button side
		g.setColor(this.sideCol);
		if(this.type == "circle") {
			for(int i = (int) this.hoverY; i < this.hoverH; i ++) {
				g.fillOval((int) (this.x - (this.w / 2)), (int) (this.y - (this.h / 2) + i), (int) this.w, (int) this.h);
			}
		}
		else {
			g.fillRect((int) this.x, (int) (this.y + this.hoverY), (int) this.w, (int) (this.h + this.hoverH - this.hoverY));
		}
		// button top
		g.setColor(this.frontCol);
		if(this.type == "circle") {
			g.fillOval((int) (this.x - (this.w / 2)), (int) (this.y - (this.h / 2) + this.hoverY), (int) this.w, (int) this.h);
		}
		else {
			g.fillRect((int) this.x, (int) (this.y + this.hoverY), (int) this.w, (int) this.h);
		}
		// text + icons
		g.setColor(this.sideCol);
		if(this.text == "icon:arrowright") {
			g.translate((int) this.w / 20, (int) this.hoverY);
			Polygon triangle = new Polygon();
			triangle.addPoint((int) (this.x - (this.w / 6)), (int) (this.y - (this.w / 6)));
			triangle.addPoint((int) (this.x - (this.w / 6)), (int) (this.y + (this.w / 6)));
			triangle.addPoint((int) (this.x + (this.w / 6)), (int) this.y);
			g.fillPolygon(triangle);
			g.translate((int) -this.w / 20, (int) -this.hoverY);
		}
		else if(this.text == "icon:arrowleft") {
			g.translate((int) -this.w / 20, (int) this.hoverY);
			Polygon triangle = new Polygon();
			triangle.addPoint((int) (this.x + (this.w / 6)), (int) (this.y - (this.w / 6)));
			triangle.addPoint((int) (this.x + (this.w / 6)), (int) (this.y + (this.w / 6)));
			triangle.addPoint((int) (this.x - (this.w / 6)), (int) this.y);
			g.fillPolygon(triangle);
			g.translate((int) this.w / 20, (int) -this.hoverY);
		}
		else if(this.text == "icon:3rects") {
			g.translate(0, (int) this.hoverY);
			for(byte i = -1; i <= 1; i ++) {
				g.fillRect((int) Math.round(this.x - (this.w / 6)), (int) (this.y + (i * this.h / 6) - (this.h / 20)), (int) Math.round(this.w / 3), (int) (this.h / 10));
			}
			g.translate(0, (int) -this.hoverY);
		}
		else if(this.text == "icon:2rects") {
			g.translate(0, (int) this.hoverY);
			g.fillRect((int) Math.round(this.x - (this.w / 4)), (int) Math.round(this.y - (this.h / 4)), (int) Math.round(this.w / 8), (int) Math.round(this.h / 2));
			g.fillRect((int) Math.round(this.x + (this.w / 8)), (int) Math.round(this.y - (this.h / 4)), (int) Math.round(this.w / 8), (int) Math.round(this.h / 2));
			g.translate(0, (int) -this.hoverY);
		}
		else {
			if(this.textCol != null) {
				g.setColor(this.textCol);
			}
			g.setFont(Screen.fontRighteous.deriveFont(15f));
			Screen.centerText(g, this.x + (this.w / 2), this.y + (this.h / 2) + 7 + this.hoverY, this.text);
		}
	}
	public void update() {
		this.pressedBefore = this.pressed;
		this.pressed = false;
		if((this.type == "circle" && Math.hypot(this.x - MousePos.x, this.y - MousePos.y) <= this.w / 2) || (this.type == "rect" && MousePos.x > this.x && MousePos.x < this.x + this.w && MousePos.y > this.y && MousePos.y < this.y + this.h)) {
			Screen.cursor = "hand";
			if(this.hoverY < this.hoverH) {
				this.hoverY ++;
			}
			if(MouseClick.mouseIsPressed && Game.transition == 0) {
				this.pressed = true;
			}
		}
		else if(this.hoverY > 0) {
			this.hoverY --;
		}
	}
}
