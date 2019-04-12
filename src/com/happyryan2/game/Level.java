package com.happyryan2.game;

import java.awt.Graphics;
import java.util.List;
import java.util.ArrayList;

import com.happyryan2.objects.Thing;

public class Level {
	public List content;
	private boolean resized = false;
	public Level() {
		this.content = new ArrayList();
	}
	public void update() {
		if(!resized) {
			resize();
		}
		for(byte i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			thing.update();
		}
	}
	public void display(Graphics g) {
		g.translate(200, 200);
		for(byte i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			thing.display(g);
		}
	}
	public void resize() {
		resized = true;
		float left = 0;
		float right = 0;
		float top = 0;
		float bottom = 0;
		for(byte i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			float x = (float) thing.x;
			float y = (float) thing.y;
			System.out.println("is x(" + x + ") greater than right(" + right + ")");
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
		for(byte i = 0; i < this.content.size(); i ++) {
			Thing thing = (Thing) this.content.get(i);
			thing.x -= left;
			thing.y -= top;
		}
		System.out.println("left: " + left);
		System.out.println("right: " + right);
		System.out.println("top: " + top);
		System.out.println("bottom: " + bottom);
		Game.levelSize = (right - left > bottom - top) ? (right - left + 1) : (bottom - top + 1);
		Game.tileSize = 400 / Game.levelSize;
		System.out.println(Game.levelSize);
	}
}
