/*
This serves as the parent class for all in-game components so you can use ArrayList with all objects being the same type.
*/

package io.github.happyryan2.puzzlegame.objects;

import java.awt.Graphics;

public class Thing {
	public float x;
	public float y;
	public float origX;
	public float origY;
	public float origExtension;
	public String dir;
	public double height;
	public boolean extending = false;
	public boolean retracting = false;
	public float extension = 0;
	public boolean canExtend;
	public boolean isWeak = false;
	public boolean ignoring = false;
	public boolean selected = false;
	public String moveDir = "none";
	public int timeMoving;
	public int color;
	public boolean winAnimation = false;
	public boolean deleted = false;
	public void display(Graphics g) { }
	public void update() { }
	public void move() { }
	public void checkMovement(String dir) {}
	public boolean canBePushed(String dir) { return false; }
	public boolean canDoSomething() { return false; }
}
