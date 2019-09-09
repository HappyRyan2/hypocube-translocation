/*
This utility does all the work necessary for importing the Java timer and setting the fps.
*/

package io.github.happyryan2.puzzlegame.utilities;

import java.util.TimerTask;
import java.awt.Cursor;

import io.github.happyryan2.puzzlegame.game.Game;
import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.utilities.MouseClick;

public class Delay extends TimerTask {
	private boolean workingOnFrame = false;

	public void run() {
		if(workingOnFrame && false) {
			System.out.println("A frame is taking too long to render.");
			return;
		}
		workingOnFrame = true;
		Screen.frameCount ++;
		Screen.cursor = "default";
		Screen.screenW = Screen.width();
		Screen.screenH = Screen.height();

		//update screen size
		Screen.screenW = Screen.canvas.getWidth();
		Screen.screenH = Screen.canvas.getHeight();

		//get inputs for this frame
		MousePos.update();

		//run the game
		Game.update();

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
