/*
This utility does all the work necessary for importing the Java timer and setting the fps.
*/

package com.happyryan2.utilities;

import java.util.TimerTask;
import java.awt.Cursor;

import com.happyryan2.game.Game;
import com.happyryan2.game.Level;
import com.happyryan2.utilities.MouseClick;

public class Delay extends TimerTask {
	private boolean workingOnFrame = false;

	public void run() {
		if(workingOnFrame) {
			System.out.println("A frame is taking too long to render.");
			return;
		}
		workingOnFrame = true;
		Screen.frameCount ++;
		Screen.cursor = "default";

		//update screen size
		Screen.screenW = Screen.canvas.getWidth();
		Screen.screenH = Screen.canvas.getHeight();

		//get inputs for this frame
		MousePos.update();

		//run the game
		Game.run();

		//repaint to show changes
		Screen.canvas.repaint();

		//update mouse type
		if(Screen.cursor == "hand") {
			Screen.canvas.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		else {
			Screen.canvas.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
		if(true) {
			workingOnFrame = false;
		}
		MouseClick.pressedBefore = MouseClick.mouseIsPressed;
	}

}
