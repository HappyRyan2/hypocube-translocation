/*
This utility gets whether or not the user is pressing a mouse button.
*/

package com.happyryan2.utilities;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class MouseClick implements MouseListener {
	public static boolean mouseIsPressed = false;
	public static boolean pressedBefore = false;
	public void mousePressed(MouseEvent event) {
		mouseIsPressed = true;
	}
	public void mouseReleased(MouseEvent event) {
		mouseIsPressed = false;
	}

	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	public void mouseClicked(MouseEvent e) { }
}
