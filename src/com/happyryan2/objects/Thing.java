/*
This serves as the parent class for all in-game components so you can use ArrayList with all objects being the same type.
*/

package com.happyryan2.objects;

import java.awt.Graphics;

public class Thing {
	public float x;
	public float y;
	public float origX;
	public float origY;
	public float hoverY;
	public String dir;
	public double height;
	public boolean extending;
	public boolean retracting;
	public float extension;
	public boolean canExtend;
	public boolean ignoring = false;
	public boolean moved = false;
	public String moveDir = "none";
	public int timeMoving;
	public int color;
	public boolean winAnimation = false;
	public boolean deleted = false;
	public void display(Graphics g) { }
	public void update() { }
	public void checkMovement(String dir) {}
	public boolean canBePushed(String dir) { return false; }
}
