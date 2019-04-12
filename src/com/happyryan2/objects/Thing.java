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
	public void display(Graphics g) { }
	public void update() { }
}
