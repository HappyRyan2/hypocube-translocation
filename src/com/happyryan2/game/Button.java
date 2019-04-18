package com.happyryan2.game;

public class Button {
	public Button(x, y, w, h, frontCol, sideCol, text) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.frontCol = frontCol || new Color(100, 100, 100);
		this.sideCol = sideCol 
	}
}