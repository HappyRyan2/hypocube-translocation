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
	public static List solutionTree = new ArrayList();
	public static void update() {
		// System.out.println("level size: " + level.content.size());
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
			tree = new ArrayList();
			Level depth0 = level.copy();
			depth0.depth = 0;
			tree.add(depth0);
			boolean solved = false;
			while(!solved) {
				treeLoop: for(int i = 0; i < tree.size(); i ++) {
					System.out.println("i is " + i + " and tree.size() is " + tree.size());
					Level currentLevel = (Level) tree.get(i);
					for(int x = 0; x < currentLevel.width; x ++) {
						yLoop: for(int y = 0; y < currentLevel.height; y ++) {
							System.out.println("searching position (" + x + ", " + y + ")");
							Thing thing = (Thing) currentLevel.getAtPos(x, y);
							if(!(thing instanceof Extender || thing instanceof Retractor) || thing == null) {
								// System.out.println("found a player / goal / empty space, skipping to next iteration");
								continue yLoop;
							}
							Level beforeAction = currentLevel.copy();
							Game.currentLevel = currentLevel;
							if(thing instanceof Extender) {
								Extender thing2 = (Extender) thing;
								if(!thing2.canDoSomething()) {
									// System.out.println("this one can't move, skipping to next iteration");
									continue yLoop;
								}
								thing2.onClick();
								currentLevel.fastForward();
							}
							else {
								Retractor thing2 = (Retractor) thing;
								System.out.println("this one can't move, skipping to next iteration");
								if(!thing2.canDoSomething()) {
									continue yLoop;
								}
								thing2.onClick();
								currentLevel.fastForward();
							}
							for(short j = 0; j < tree.size(); j ++) {
								Level previousState = (Level) tree.get(i);
								Level previousStateCopy = previousState.copy();
								if(previousStateCopy.equals(currentLevel)) {
									System.out.println("this move will take you to a previous position");
									tree.set(i, beforeAction);
									continue yLoop;
								}
							}
							Level nextDepth = currentLevel.copy();
							nextDepth.depth ++;
							nextDepth.preX = x;
							nextDepth.preY = y;
							nextDepth.parentIndex = i;
							tree.add(nextDepth);
							tree.set(i, beforeAction);
							currentLevel = beforeAction;
							if(nextDepth.isComplete()) {
								System.out.println("TREE:");
								System.out.println("------------------------------------------");
								printTree();
								System.out.println("------------------------------------------");
								System.out.println("FOUND A SOLUTION (takes " + nextDepth.depth + " moves)");
								System.out.println("remember to read the next list backwards");
								displayLevelMovePath(nextDepth);
								// printTree();
								System.out.println("------------------------------------------");
								solved = true;
								break treeLoop;
							}
						}
					}
					if(currentLevel.depth % 5 == 0) {
						System.out.println("------------------------------------------");
						System.out.println("the level cannot be solved in under " + level.depth + " moves.");
						System.out.println("progress so far:");
						printTree();
						System.out.println("------------------------------------------");
					}
				}
				solved = true;
			}
		}
	}
	// public static void addToList(Level solution) {
	// 	if(solution.depth == 0) {
	// 		System.out.println("found the beginning of time! return;");
	// 		return;
	// 	}
	// 	solutionTree.add(solution);
	// 	Level previousMove = (Level) tree.get(solution.parentIndex);
	// 	System.out.println("checking index " + solution.parentIndex + ", depth " + previousMove.depth);
	// 	solutionTree.add(previousMove);
	// 	addToList(previousMove);
	// }
	// public static void displayMovePath(Level solution) {
	// 	addToList(solution);
	// 	System.out.println("just to check, I now believe that it takes " + solutionTree.size() + " moves");
	// 	for(int i = solutionTree.size() - 1; i >= 0; i --) {
	// 		Level solutionPath = (Level) solutionTree.get(i);
	// 		System.out.println("click at (" + solutionPath.preX + ", " + solutionPath.preY + ")");
	// 	}
	// }
	public static void displayLevelMovePath(Level solution) {
		if(solution.depth == 0) {
			return;
		}
		System.out.println("click at (" + solution.preX + ", " + solution.preY + ")");
		Level parent = (Level) tree.get(solution.parentIndex);
		displayLevelMovePath(parent);
	}
	public static void printTree() {
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
	}
}
