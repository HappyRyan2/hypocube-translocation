/*
This utility gets the mouse position on the screen.
*/

package io.github.happyryan2.puzzlegame.utilities;

import java.awt.MouseInfo;
import java.awt.PointerInfo;
import java.awt.Point;

public class MousePos {
	public static int x = 0;
	public static int y = 0;
	public static int preX = 0; // location of mouse on last call
	public static int preY = 0;

	public static void update() {
		PointerInfo mouse = MouseInfo.getPointerInfo();
		Point pos = mouse.getLocation();
		Point offset = Screen.canvas.getLocationOnScreen();
		preX = x;
		preY = y;
		x = pos.x - offset.x;
		y = pos.y - offset.y;
	}
}
