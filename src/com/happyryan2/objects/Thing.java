/*
This serves as the parent class for all in-game components so you can use ArrayList with all objects being the same type.
*/

package com.happyryan2.objects;

import java.awt.Graphics;

public class Thing {
	public float x;
	public float y;
	public float hoverY;
	public String dir;
	public double height;
	public boolean extending;
	public boolean retracting;
	public float extension;
	public boolean canExtend;
	public boolean moved = false;
	public String moveDir = "none";
	public int timeMoving;
	public void display(Graphics g) { }
	public void update() { }
	public boolean canBePushed(String dir) { return false; }
	public void checkMovement(String dir) {}
}
