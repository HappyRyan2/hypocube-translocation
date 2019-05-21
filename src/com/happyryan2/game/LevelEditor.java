package com.happyryan2.game;

import java.awt.Graphics;
import java.awt.Color;
import java.lang.Math;
import java.util.List;
import java.util.ArrayList;

import com.happyryan2.utilities.*;
import com.happyryan2.objects.*;

public class LevelEditor {
	public static Button mode = new Button(130, 10, 100, 30, new Color(100, 100, 100), new Color(150, 150, 150), "extend", "rect");
	public static Button isWeak = new Button(240, 10, 100, 30, new Color(100, 100, 100), new Color(150, 150, 150), "is weak", "rect");
	public static Button editing = new Button(350, 10, 100, 30, new Color(100, 100, 100), new Color(150, 150, 150), "editing", "rect");
	public static Button printCode = new Button(460, 10, 100, 30, new Color(100, 100, 100), new Color(150, 150, 150), "print code", "rect");
	public static Button checkSolvable = new Button(570, 10, 100, 30, new Color(100, 100, 100), new Color(150, 150, 150), "check solvability", "rect");
	public static Level level = new Level();
	public static List tree = new ArrayList(); // for checking whether it is solvable or not
	public static void update() {
		if(MouseClick.mouseIsPressed && MousePos.x > 100 && MousePos.x < 700 && MousePos.y > 100 && MousePos.y < 700 && editing.text == "editing") {
			int x = MousePos.x;
			int y = MousePos.y;
			x -= 100;
			y -= 100;
			x = Math.round(x / 60);
			y = Math.round(y / 60);
			for(byte i = 0; i < level.content.size(); i ++) {
				Thing thing = (Thing) level.content.get(i);
				if(thing.x == x && thing.y == y) {
					level.content.remove(i);
					continue;
				}
			}
			String dir = (KeyInputs.keyA || KeyInputs.keyD) ? (KeyInputs.keyA ? "left" : "right") : ((KeyInputs.keyW || KeyInputs.keyS) ? (KeyInputs.keyW ? "up" : "down") : "none");
			if(mode.text == "extend") {
				System.out.println("adding an extender");
				level.content.add(new Extender(x, y, dir, (isWeak.text == "is weak")));
			}
			else if(mode.text == "retract") {
				System.out.println("adding a retractor");
				level.content.add(new Retractor(x, y, dir, (isWeak.text == "is weak")));
			}
			else if(mode.text == "player") {
				level.content.add(new Player(x, y));
			}
			else if(mode.text == "goal") {
				level.content.add(new Goal(x, y));
			}
		}
		if(Game.levelSize < 3) {
			Game.levelSize = 3;
			Game.tileSize = 200;
		}
		Game.currentLevel = level;
		Game.tileSize = 60;
		Game.levelSize = 10;
		level.isForTesting = true;
		level.width = 10;
		level.height = 10;
		level.left = 100;
		level.top = 100;
		if(editing.text == "playing") {
			level.update();
		}
		else {
			level.completionY = -1000;
		}
		mode.update();
		isWeak.update();
		editing.update();
		printCode.update();
		checkSolvable.update();
		if(isWeak.pressed && !isWeak.pressedBefore) {
			isWeak.text = (isWeak.text == "is weak") ? "is not weak" : "is weak";
		}
		if(mode.pressed && !mode.pressedBefore) {
			if(mode.text == "extend") {
				mode.text = "retract";
			}
			else if(mode.text == "retract") {
				mode.text = "player";
			}
			else if(mode.text == "player") {
				mode.text = "goal";
			}
			else if(mode.text == "goal") {
				mode.text = "eraser";
			}
			else if(mode.text == "eraser") {
				mode.text = "extend";
			}
		}
		if(editing.pressed && !editing.pressedBefore) {
			if(editing.text == "editing") {
				level.reset();
				Game.startingLevel = true;
			}
			editing.text = (editing.text == "editing") ? "playing" : "editing";
		}
		if(printCode.pressed && !printCode.pressedBefore && editing.text == "editing") {
			String code = "";
			for(byte i = 0; i < level.content.size(); i ++) {
				code += "\n";
				Thing thing = (Thing) level.content.get(i);
				if(thing instanceof Player) {
					code += "super.content.add(new Player(" + (int) thing.x + ", " + (int) thing.y + "));";
				}
				else if(thing instanceof Goal) {
					code += "super.content.add(new Goal(" + (int) thing.x + ", " + (int) thing.y + "));";
				}
				else if(thing instanceof Extender) {
					code += "super.content.add(new Extender(" + (int) thing.x + ", " + (int) thing.y + ", \"" + thing.dir + "\", " + thing.isWeak + "));";
				}
				else if(thing instanceof Retractor) {
					code += "super.content.add(new Retractor(" + (int) thing.x + ", " + (int) thing.y + ", \"" + thing.dir + "\", " + thing.isWeak + "));";
				}
			}
			System.out.println(code);
		}
		if(checkSolvable.pressed && !checkSolvable.pressedBefore) {
			Level depth0 = ((Level)level).clone();
			depth0.depth = 0;
			tree.add(depth0);
			boolean solved = false;
			while(!solved) {
				for(int i = 0; i < tree.size(); i ++) {
					Level level = (Level) tree.get(i);
					for(int x = 0; x < level.width; x ++) {
						yLoop: for(int y = 0; y < level.height; y ++) {
							Thing thing = (Thing) level.getAtPos(x, y);
							if(!(thing instanceof Extender || thing instanceof Retractor)) {
								continue yLoop;
							}
							if(thing instanceof Extender) {
								Extender extender = (Extender) thing;
								extender.onClick();
							}
							else if(thing instanceof Retractor) {
								Retractor retractor = (Retractor) thing;
								retractor.onClick();
							}
							boolean canDoSomething = false;
							for(int j = 0; j < level.content.size(); j ++) {
								Thing thing2 = (Thing) level.content.get(j);
								if((thing2.extension != 0 && thing2.extension != 1) || thing2.moveDir != "none") {
									canDoSomething = true;
								}
								if(thing2.extending) {
									thing2.extension = 1;
								}
								else if(thing2.retracting) {
									thing2.extension = 0;
								}
								if(thing2.moveDir == "up") {
									thing2.y = Math.round(thing2.y - 1);
								}
								else if(thing2.moveDir == "down") {
									thing2.y = Math.round(thing2.y + 1);
								}
								else if(thing2.moveDir == "left") {
									thing2.x = Math.round(thing2.x - 1);
								}
								else if(thing2.moveDir == "right") {
									thing2.x = Math.round(thing2.x + 1);
								}
							}
							if(canDoSomething) {
								
							}
						}
					}
				}
			}
		}
	}
	public static void display(Graphics g) {
		level.display(g);
		mode.display(g);
		isWeak.display(g);
		editing.display(g);
		printCode.display(g);
		checkSolvable.display(g);
	}
}