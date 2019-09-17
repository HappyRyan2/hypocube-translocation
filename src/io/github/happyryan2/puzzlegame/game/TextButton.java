package io.github.happyryan2.puzzlegame.game;

import java.awt.*;
import javax.swing.*;

import io.github.happyryan2.puzzlegame.utilities.Screen;
import io.github.happyryan2.puzzlegame.utilities.MousePos;
import io.github.happyryan2.puzzlegame.utilities.MouseClick;

public class TextButton {
	/*
	Represents a rectangular button with some text on it.
	*/

	public int x; // (x, y) is the top left corner
	public int y;
	public int w;
	public int h;

	public String text;
	public Color sideCol; // also the color for the text
	public Color frontCol;

	public final int hoverH = 5;
	public int hoverY = 0;

	public boolean pressed = false;
	public boolean pressedBefore = false;

	public TextButton(int x, int y, int w, int h, String text, Color sideCol, Color frontCol) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.text = text;
		this.sideCol = sideCol;
		this.frontCol = frontCol;
	}

	public void display(Graphics g) {
		/* Button side */
		g.setColor(this.sideCol);
		g.fillRect(this.x, this.y + this.hoverY, this.w, this.h + this.hoverH - this.hoverY);
		/* Button top */
		g.setColor(this.frontCol);
		g.fillRect(this.x, this.y + this.hoverY, this.w, this.h);
		/* Button text */
		g.setColor(this.sideCol);
		g.setFont(Screen.fontRighteous.deriveFont(15f));
		Screen.centerText(g, this.x + (int) (this.w / 2), this.y + (this.h / 2) + this.hoverY + 7, this.text);
	}
	public void update() {
		this.pressedBefore = this.pressed;
		this.pressed = this.isPressed();
		/* Cursor hovering */
		if(this.cursorHovered()) {
			Screen.cursor = "hand";
			if(this.hoverY < this.hoverH) {
				this.hoverY ++;
			}
		}
		else if(this.hoverY > 0) {
			this.hoverY --;
		}
	}

	public boolean cursorHovered() {
		return (MousePos.x > this.x && MousePos.x < this.x + this.w && MousePos.y > this.y && MousePos.y < this.y + this.h);
	}
	public boolean isPressed() {
		return this.cursorHovered() && MouseClick.mouseIsPressed;
	}
}
