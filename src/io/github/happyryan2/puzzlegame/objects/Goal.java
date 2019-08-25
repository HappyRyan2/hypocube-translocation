package io.github.happyryan2.puzzlegame.objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Image;
import java.awt.geom.AffineTransform;

import io.github.happyryan2.puzzlegame.game.Game;
import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.utilities.Screen;
import io.github.happyryan2.puzzlegame.utilities.ImageLoader;

public class Goal extends Thing {
	public Color darkRed = new Color(128, 0, 0);
	public static Image img = ImageLoader.loadImage("res/graphics/objects/goal.png");
	public Goal(float x, float y) {
		super.x = x;
		super.y = y;
		super.origX = x;
		super.origY = y;
	}
	public void update() {
		super.extending = false;
		super.retracting = false;
		super.moveDir = "none";
	}
	public void display(Graphics g) {
		int x = (int) (super.x * Game.tileSize);
		int y = (int) (super.y * Game.tileSize);
		int w = (int) (Game.tileSize);
		int h = (int) (Game.tileSize);
		/* Translate to position */
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform at = g2.getTransform();
		g2.translate(x, y);
		/* Display + undo transformations */
		Screen.scaleImage(g2, img, w, h);
		g2.setTransform(at);
	}
	public boolean canBePushed() {
		return true;
	}
}
