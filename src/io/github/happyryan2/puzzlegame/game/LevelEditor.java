package io.github.happyryan2.puzzlegame.game;

import java.awt.Graphics;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

import io.github.happyryan2.puzzlegame.utilities.*;
import io.github.happyryan2.puzzlegame.objects.*;
import io.github.happyryan2.puzzlegame.levels.*;

public class LevelEditor {

	public static TextButton mode = new TextButton(240, 10, 100, 30, "extend", new Color(150, 150, 150), new Color(100, 100, 100));
	public static TextButton isWeak = new TextButton(350, 10, 100, 30, "is weak", new Color(150, 150, 150), new Color(100, 100, 100));
	public static TextButton editing = new TextButton(460, 10, 100, 30, "editing", new Color(150, 150, 150), new Color(100, 100, 100));
	public static TextButton printCode = new TextButton(570, 10, 100, 30, "print code", new Color(150, 150, 150), new Color(100, 100, 100));
	public static TextButton checkSolvable = new TextButton(240, 50, 100, 30, "solve", new Color(150, 150, 150), new Color(100, 100, 100));
	public static TextButton crop = new TextButton(350, 50, 100, 30, "crop", new Color(150, 150, 150), new Color(100, 100, 100));
	public static TextButton uncrop = new TextButton(460, 50, 100, 30, "uncrop", new Color(150, 150, 150), new Color(100, 100, 100));

	public static Level level = new Level24();

	public static boolean initialized = false;

	public static List tree = new ArrayList(); // Not a tree, just an ArrayList. Still functions as a tree. Used for solving.
	public static List solutionTree = new ArrayList(); // Definitely not a tree at all. Doesn't function as a tree. Used to remember the solution so it can play the solution back to you multiple times.
	public static int solutionStep = -1;

	public static int cropX = 0;
	public static int cropY = 0;
	public static boolean cropping = false;

	public static void init() {
		Game.tileSize = (800 - (level.top * 2)) / Game.levelSize;
		Game.levelSize = 10;
		level.isForTesting = true;
		if(level.width == 0 && level.height == 0) {
			level.width = 10;
			level.height = 10;
		}
		level.left = 100;
		level.top = 100;
		initialized = true;
	}

	public static void update() {
		/* Initialize */
		if(!initialized) {
			init();
		}
		level.resize();
		Game.currentLevel = level;
		/* Click to add items to the level */
		if(MouseClick.mouseIsPressed && MousePos.x > level.left && MousePos.x < Screen.screenW - level.left && MousePos.y > level.top && MousePos.y < Screen.screenH - level.top && editing.text == "editing" && crop.text == "crop") {
			/* Calculate position */
			int x = MousePos.x;
			int y = MousePos.y;
			x -= level.left;
			y -= level.top;
			x = (int) Math.floor((float) x / (float) Game.tileSize);
			y = (int) Math.floor((float) y / (float) Game.tileSize);
			/* Remove what was previously there */
			for(byte i = 0; i < level.content.size(); i ++) {
				Thing thing = (Thing) level.content.get(i);
				if(thing.x == x && thing.y == y) {
					level.content.remove(i);
					continue;
				}
			}
			/* Add the new object */
			String dir = (KeyInputs.keyA || KeyInputs.keyD) ? (KeyInputs.keyA ? "left" : "right") : ((KeyInputs.keyW || KeyInputs.keyS) ? (KeyInputs.keyW ? "up" : "down") : "none");
			if(mode.text == "extend") {
				level.content.add(new Extender(x, y, dir, (isWeak.text == "is weak")));
			}
			else if(mode.text == "retract") {
				level.content.add(new Retractor(x, y, dir, (isWeak.text == "is weak")));
			}
			else if(mode.text == "long extend") {
				level.content.add(new LongExtender(x, y, dir, (isWeak.text == "is weak")));
			}
			else if(mode.text == "player") {
				level.content.add(new Player(x, y));
			}
			else if(mode.text == "goal") {
				level.content.add(new Goal(x, y));
			}
			else if(mode.text == "wall") {
				level.content.add(new Wall(x, y));
			}
			solutionTree.clear();
		}
		/* Cap level size at 3 */
		if(Game.levelSize < 3) {
			Game.levelSize = 3;
			Game.tileSize = 200;
		}
		/* Update level */
		if(editing.text == "playing" && !cropping) {
			level.update();
		}
		else {
			level.completionY = -1000;
		}
		/* Update buttons */
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
				mode.text = "long extend";
			}
			else if(mode.text == "long extend") {
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
				else if(thing instanceof LongExtender) {
					code += "super.content.add(new LongExtender(" + (int) thing.x + ", " + (int) thing.y + ", \"" + thing.dir + "\", " + thing.isWeak + "));";
				}
				else if(thing instanceof Wall) {
					code += "super.content.add(new Wall(" + (int) thing.x + ", " + (int) thing.y + "));";
				}
			}
			System.out.println(code);
		}
		if(checkSolvable.pressed && !checkSolvable.pressedBefore) {
			if(solutionTree.size() == 0) {
				try {
					checkSolution();
				}
				catch(OutOfMemoryError err) {
					System.gc();
					System.out.println("----------------------------------------");
					System.out.println("Ran out of memory while solving.");
					System.out.println("----------------------------------------");
				}
				System.out.println("level complexity: " + tree.size());
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
		/* Replay solution */
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
			else if(thing instanceof LongExtender) {
				LongExtender longExtender = (LongExtender) thing;
				longExtender.onClick();
			}
			solutionStep --;
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

	public static void checkSolution() throws OutOfMemoryError {
		/* Clear "tree" + set root to current state */
		tree.clear();
		Level depth0 = new Level(level);
		depth0.depth = 0;
		tree.add(depth0);
		/* Go through the "tree", trying all possible moves */
		for(int i = 0; i < tree.size(); i ++) {
			System.out.println("Checking index " + i + " / " + tree.size());
			Level level = (Level) tree.get(i);
			moveLoop: for(int j = 0; j < level.content.size(); j ++) {
				/* Copy the level */
				level.snapToGrid();
				Level nextLevel = new Level(level);
				Game.currentLevel = nextLevel;
				nextLevel.depth ++;
				nextLevel.parentIndex = i;
				/* Get the thing at this index */
				Thing thing = (Thing) nextLevel.content.get(j);
				if(!thing.canDoSomething()) {
					continue;
				}
				nextLevel.preX = (int) thing.x;
				nextLevel.preY = (int) thing.y;
				// System.out.println("Found something at (" + thing.x + ", " + thing.y + "). Index " + j + " at level " + i);
				/* Simulate a click on that object */
				// System.out.println("Before clicking:");
				// nextLevel.printContent();
				if(thing instanceof Extender) {
					Extender e = (Extender) thing;
					e.onClick();
				}
				else if(thing instanceof Retractor) {
					Retractor r = (Retractor) thing;
					r.onClick();
				}
				else if(thing instanceof LongExtender) {
					// System.out.println("It's a long extender"); // expected output recieved
					LongExtender le = (LongExtender) thing;
					le.onClick();
				}
				nextLevel.fastForward();
				// System.out.println("After clicking:");
				// nextLevel.printContent();
				/* Make sure it isn't a duplicate state + add to tree */
				for(int k = 0; k < tree.size(); k ++) {
					Level duplicate = (Level) tree.get(k);
					if(duplicate.equals(nextLevel)) {
						// System.out.println("Already been there at index " + k);
						// System.out.println("Duplicate state:");
						// duplicate.printContent();
						// System.out.println("Potential new state:");
						// nextLevel.printContent();
						continue moveLoop;
					}
				}
				tree.add(nextLevel);
				/* Check if the level is complete */
				if(nextLevel.isComplete()) {
					System.out.println("------------------------------------------");
					System.out.println("FOUND THE SOLUTION (read the list backwards)");
					displayLevelMovePath(nextLevel);
					System.out.println("------------------------------------------");
					return;
				}
			}
		}
		System.out.println("------------------------------------------");
		System.out.println("The level cannot be solved.");
		System.out.println("------------------------------------------");
		printTree();
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
		for(int i = 0; i < tree.size(); i ++) {
			Level currentLevel = (Level) tree.get(i);
			System.out.println("Level at index " + i + ", depth: " + currentLevel.depth + ", parent: " + currentLevel.parentIndex);
			currentLevel.printContent();
		}
	}
}
