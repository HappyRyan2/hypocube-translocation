package com.happyryan2.game;

import java.awt.Graphics;
import java.awt.Color;
import java.lang.Math;

import com.happyryan2.utilities.*;
import com.happyryan2.objects.*;

public class LevelEditor {
	public static String mode = "extender";
	public static Button extender = new Button(130, 10, 100, 30, new Color(100, 100, 100), new Color(150, 150, 150), "extend", "rect");
	public static Button isWeak = new Button(240, 10, 100, 30, new Color(100, 100, 100), new Color(150, 150, 150), "is weak", "rect");
	public static Button editing = new Button(350, 10, 100, 30, new Color(100, 100, 100), new Color(150, 150, 150), "editing", "rect");
	public static Level level = new Level();
	public static void update() {
		if(MouseClick.mouseIsPressed && MousePos.x > 100 && MousePos.x < 700 && MousePos.y > 100 && MousePos.y < 700) {
			int x = MousePos.x;
			int y = MousePos.y;
			x -= 100;
			y -= 100;
			x = Math.round(x / 10);
			y = Math.round(y / 10);
			y += 100;
			String dir = (KeyInputs.keyA || KeyInputs.keyD) ? (KeyInputs.keyA ? "left" : "right") : ((KeyInputs.keyW || KeyInputs.keyS) ? (KeyInputs.keyW ? "up" : "down") : "none");
			if(extender.text == "extender") {
				level.content.add(new Extender(x, y, dir, (isWeak.text == "is weak")));
			}
			else {
				level.content.add(new Retractor(x, y, dir, (isWeak.text == "is weak")));
			}
		}
		if(Game.levelSize < 3) {
			Game.levelSize = 3;
			Game.tileSize = 200;
		}
		Game.currentLevel = level;
		level.isForTesting = true;
		level.update();
		extender.update();
		isWeak.update();
		editing.update();
		if(isWeak.pressed && !isWeak.pressedBefore) {
			isWeak.text = (isWeak.text == "is weak") ? "is not weak" : "is weak";
		}
		if(extender.pressed && !extender.pressedBefore) {
			extender.text = (extender.text == "extend") ? "retract" : "extend";
		}
		if(editing.pressed && !editing.pressedBefore) {
			if(editing.text == "editing") {
				level.reset();
			}
			editing.text = (editing.text == "editing") ? "playing" : "editing";
		}
	}
	public static void display(Graphics g) {
		level.display(g);
		extender.display(g);
		isWeak.display(g);
		editing.display(g);
	}
}