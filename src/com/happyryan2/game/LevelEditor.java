package com.happyryan2.game;

import java.awt.Graphics;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

import com.happyryan2.utilities.*;
import com.happyryan2.objects.*;
import com.happyryan2.levels.*;

public class LevelEditor {
	public static Button mode = new Button(130, 10, 100, 30, new Color(100, 100, 100), new Color(150, 150, 150), "extend", "rect");
	public static Button isWeak = new Button(240, 10, 100, 30, new Color(100, 100, 100), new Color(150, 150, 150), "is weak", "rect");
	public static Button editing = new Button(350, 10, 100, 30, new Color(100, 100, 100), new Color(150, 150, 150), "editing", "rect");
	public static Button printCode = new Button(460, 10, 100, 30, new Color(100, 100, 100), new Color(150, 150, 150), "print code", "rect");
	public static Button checkSolvable = new Button(570, 10, 100, 30, new Color(100, 100, 100), new Color(150, 150, 150), "solve", "rect");
	public static Button crop = new Button(130, 50, 100, 30, new Color(100, 100, 100), new Color(150, 150, 150), "crop", "rect");
	public static Button uncrop = new Button(240, 50, 100, 30, new Color(100, 100, 100), new Color(150, 150, 150), "uncrop", "rect");
	public static Level level = new Level11();
	public static boolean initialized = false;
	public static List tree = new ArrayList(); // for checking whether it is solvable or not
	public static List solutionTree = new ArrayList();
	public static int solutionStep = -1;
	public static int cropX = 0;
	public static int cropY = 0;
	public static boolean cropping = false;
	public static void update() {
		if(MouseClick.mouseIsPressed && MousePos.x > level.left && MousePos.x < Screen.screenW - level.left && MousePos.y > level.top && MousePos.y < Screen.screenH - level.top && editing.text == "editing" && crop.text == "crop") {
			int x = MousePos.x;
			int y = MousePos.y;
			x -= level.left;
			y -= level.top;
			x = (int) Math.floor((float) x / (float) Game.tileSize);
			y = (int) Math.floor((float) y / (float) Game.tileSize);
			for(byte i = 0; i < level.content.size(); i ++) {
				Thing thing = (Thing) level.content.get(i);
				if(thing.x == x && thing.y == y) {
					level.content.remove(i);
					continue;
				}
			}
			String dir = (KeyInputs.keyA || KeyInputs.keyD) ? (KeyInputs.keyA ? "left" : "right") : ((KeyInputs.keyW || KeyInputs.keyS) ? (KeyInputs.keyW ? "up" : "down") : "none");
			if(mode.text == "extend") {
				solutionTree.clear();
				level.content.add(new Extender(x, y, dir, (isWeak.text == "is weak")));
			}
			else if(mode.text == "retract") {
				solutionTree.clear();
				level.content.add(new Retractor(x, y, dir, (isWeak.text == "is weak")));
			}
			else if(mode.text == "player") {
				solutionTree.clear();
				level.content.add(new Player(x, y));
			}
			else if(mode.text == "goal") {
				solutionTree.clear();
				level.content.add(new Goal(x, y));
			}
			else if(mode.text == "wall") {
				solutionTree.clear();
				level.content.add(new Wall(x, y));
			}
		}
		if(Game.levelSize < 3) {
			Game.levelSize = 3;
			Game.tileSize = 200;
		}
		Game.currentLevel = level;
		if(!initialized) {
			Game.tileSize = (800 - (level.top * 2)) / Game.levelSize;
			Game.levelSize = 10;
			level.isForTesting = true;
			level.width = 10;
			level.height = 10;
			level.left = 100;
			level.top = 100;
			level.resize();
			initialized = true;
		}
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
		crop.update();
		uncrop.update();
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
				mode.text = "wall";
			}
			else if(mode.text == "wall") {
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
				else if(thing instanceof Wall) {
					code += "super.content.add(new Wall(" + (int) thing.x + ", " + (int) thing.y + "));";
				}
			}
			System.out.println(code);
		}
		if(checkSolvable.pressed && !checkSolvable.pressedBefore) {
			if(solutionTree.size() == 0) {
				checkSolution();
			}
			solutionStep = solutionTree.size() - 1;
		}
		if(crop.pressed && !crop.pressedBefore) {
			crop.text = (crop.text == "crop") ? "cancel" : "crop";
			cropX = 0;
			cropY = 0;
			if(crop.text == "crop") {
				cropping = false;
			}
		}
		if(crop.text == "cancel") {
			if(MouseClick.mouseIsPressed && !crop.pressed) {
				if(!cropping) {
					cropX = MousePos.x;
					cropY = MousePos.y;
				}
				cropping = true;
			}
			else if(cropping) {
				cropping = false;
				crop.text = "crop";
				int left = (int) Math.ceil((cropX - level.left) / Game.tileSize);
				int right = (int) Math.floor((MousePos.x - level.left) / Game.tileSize);
				int top = (int) Math.ceil((cropY - level.top) / Game.tileSize);
				int bottom = (int) Math.floor((MousePos.y - level.top) / Game.tileSize);
				if(right < left) {
					int previousRight = right;
					right = left;
					left = previousRight;
				}
				if(bottom < top) {
					int previousBottom = bottom;
					bottom = top;
					top = previousBottom;
				}
				for(short i = 0; i < level.content.size(); i ++) {
					Thing thing = (Thing) level.content.get(i);
					if(thing.x < left || thing.y < top || thing.x > right || thing.y > bottom) {
						System.out.println("removed something at (" + thing.x + ", " + thing.y + ")");
						level.content.remove(i);
						i --;
						continue;
					}
					// System.out.println("not cropping out something");
					thing.x -= left;
					thing.y -= top;
					thing.origX -= left;
					thing.origY -= top;
				}
				level.manualSize = true;
				level.width = right - left;
				level.height = bottom - top;
				level.resize();
			}
		}
		if(uncrop.pressed && !uncrop.pressedBefore) {
			level.width = 10;
			level.height = 10;
			level.resize();
		}
		if(solutionStep != -1 && !level.transitioning()) {
			editing.text = "playing";
			Game.currentLevel = level;
			Level currentStep = (Level) solutionTree.get(solutionStep);
			Thing thing = (Thing) level.getAtPos(currentStep.preX, currentStep.preY);
			if(thing instanceof Extender) {
				Extender extender = (Extender) thing;
				extender.onClick();
			}
			else if(thing instanceof Retractor) {
				Retractor retractor = (Retractor) thing;
				retractor.onClick();
			}
			solutionStep --;
		}
	}
	public static void checkSolution() {
		/* First, clear the tree and set the root to be the current state */
		tree.clear();
		Level depth0 = level.copy();
		depth0.depth = 0;
		tree.add(depth0);

		for(int i = 0; i < tree.size(); i ++) {
			Level currentLevel = (Level) tree.get(i);
			Game.currentLevel = currentLevel;
			for(short x = 0; x < currentLevel.width; x ++) {
				yLoop: for(short y = 0; y < currentLevel.height; y ++) {
					/* Get the item at this position and verify that it exists and can do something when clicked */
					Thing thing = (Thing) currentLevel.getAtPos(x, y);
					if(thing == null || !thing.canDoSomething()) {
						continue yLoop;
					}
					/* Pretend that the user clicked on that item */
					if(thing instanceof Extender) {
						Extender extender = (Extender) thing;
						extender.onClick();
					}
					else if(thing instanceof Retractor) {
						Retractor retractor = (Retractor) thing;
						retractor.onClick();
					}
					// System.out.println("clicked at (" + x + ", " + y + ")");
					Stack.addAction();
					currentLevel.fastForward();

					/* Add the modified state to the tree */
					Level nextDepth = currentLevel.copy();
					nextDepth.depth = currentLevel.depth + 1;
					nextDepth.parentIndex = i;
					nextDepth.preX = x;
					nextDepth.preY = y;

					/* But first, check to make sure it isn't a duplicate */
					for(int j = 0; j < tree.size(); j ++) {
						Level duplicate = (Level) tree.get(j);
						if(nextDepth.equals(duplicate) && i != j) {
							// System.out.println("clicking at (" + x + ", " + y + ") will take you to a previous state");
							Stack.undoAction();
							currentLevel.fastForward();
							continue yLoop;
						}
					}
					tree.add(nextDepth);

					Stack.undoAction();
					currentLevel.fastForward();

					/* If the level has been won, terminate the algorithm and print the solution. */
					if(nextDepth.isComplete()) {
						printTree();
						System.out.println("------------------------------------------");
						System.out.println("FOUND THE SOLUTION (read the list backwards)");
						displayLevelMovePath(nextDepth);
						System.out.println("------------------------------------------");
						return;
					}
				}
			}
		}
		System.out.println("---------------------");
		System.out.println("the level cannot be solved.");
		System.out.println("---------------------");
	}
	public static void displayLevelMovePath(Level solution) {
		if(solution.depth == 0) {
			return;
		}
		System.out.println("click at (" + solution.preX + ", " + solution.preY + ")");
		solutionTree.add(solution);
		Level parent = (Level) tree.get(solution.parentIndex);
		displayLevelMovePath(parent);
	}
	public static void printTree() {
		if(true) { return; }
		for(int j = 0; j < tree.size(); j ++) {
			Level currentLevel = (Level) tree.get(j);
			System.out.println("Item at index " + j + ", depth " + currentLevel.depth + ", which can be gotten to from index " + currentLevel.parentIndex + " by clicking at (" + currentLevel.preX + ", " + currentLevel.preY + ")");
			for(int k = 0; k < currentLevel.content.size(); k ++) {
				Thing thing = (Thing) currentLevel.content.get(k);
				if(thing instanceof Extender) {
					System.out.println("an extender at (" + thing.x + ", " + thing.y + ") with an extension of " + thing.extension);
				}
				else if(thing instanceof Retractor) {
					System.out.println("a retractor at (" + thing.x + ", " + thing.y + ") with an extension of " + thing.extension);
				}
				else if(thing instanceof Player) {
					System.out.println("a player at (" + thing.x + ", " + thing.y + ")");
				}
				else if(thing instanceof Goal) {
					System.out.println("a goal at (" + thing.x + ", " + thing.y + ")");
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
		crop.display(g);
		uncrop.display(g);
		if(cropping) {
			g.setColor(new Color(100, 100, 100, 100));
			g.fillRect(level.left, level.top, 800 - (level.left * 2), cropY - level.top); // top
			g.fillRect(level.left, level.top, cropX - level.left, 800 - (level.top * 2)); // left
			g.fillRect(level.left, MousePos.y, 800 - (level.left * 2), 800 - level.top - MousePos.y); // bottom
			g.fillRect(MousePos.x, level.top, 800 - level.left - MousePos.x, 800 - (level.top * 2)); // right
		}
	}
}
