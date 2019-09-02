package io.github.happyryan2.puzzlegame.game;

import java.awt.*;
import javax.swing.*;

import io.github.happyryan2.puzzlegame.utilities.ResourceLoader;
import io.github.happyryan2.puzzlegame.utilities.Screen;
import io.github.happyryan2.puzzlegame.utilities.Utils;
import io.github.happyryan2.puzzlegame.utilities.MousePos;
import io.github.happyryan2.puzzlegame.utilities.MouseClick;

public class ImageButton {
	/*
	Represents a circular button with an icon on it.
	*/

	public int x; // (x, y) is the center
	public int y;
	public int size;

	public String imagePath;
	public Image image;
	public Color sideCol;
	public Color frontCol;

	public final int hoverH = 5;
	public int hoverY = 0;

	public boolean pressed = false;
	public boolean pressedBefore = false; // whether the button was being pressed the previous frame

	public ImageButton(int x, int y, int size, String imagePath, Color sideCol, Color frontCol) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.imagePath = imagePath;
		this.sideCol = sideCol;
		this.frontCol = frontCol;
		this.image = ResourceLoader.loadImage(imagePath);
	}

	public void display(Graphics g) {
		int size2 = (int) Math.round(this.size / 2);
		/* Button side */
		g.setColor(this.sideCol);
		g.fillRect(this.x - size2, this.y + this.hoverY, this.size, this.hoverH - this.hoverY);
		g.fillOval(this.x - size2, this.y - size2 + this.hoverH, this.size, this.size);
		/* Button top */
		g.setColor(this.frontCol);
		g.fillOval(this.x - size2, this.y - size2 + this.hoverY, this.size, this.size);
		/* Button image */
		Graphics2D g2 = (Graphics2D) g;
		g2.translate(this.x - size2, this.y - size2 + this.hoverY);
		Screen.scaleImage(g2, this.image, this.size, this.size);
		g2.translate(-(this.x - size2), -(this.y - size2 + this.hoverY));
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
		return Utils.distSq(MousePos.x, MousePos.y, this.x, this.y) < (this.size / 2) * (this.size / 2);
	}
	public boolean isPressed() {
		return this.cursorHovered() && MouseClick.mouseIsPressed;
	}
}
