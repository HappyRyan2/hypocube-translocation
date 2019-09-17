/*
(Statically) this class represents the stack of game states. Making a move adds to the top of the stack, pressing 'undo' removes the one on top and reverts to that position.
Instances of this class represent a single move.
*/

package io.github.happyryan2.puzzlegame.game;

import java.util.List;
import java.util.ArrayList;

import io.github.happyryan2.puzzlegame.objects.*;

public class UndoStack {
	public static List stack = new ArrayList();
	public boolean chainAction;
	public boolean lastAction = false;
	public List movement = new ArrayList();

	public static void addAction() {
		addAction(false);
	}
	public static void addAction(boolean chainAction) {
		System.out.println("adding an action!");
		UndoStack action = new UndoStack();
		for(byte i = 0; i < Game.currentLevel.content.size(); i ++) {
			Thing thing = (Thing) Game.currentLevel.content.get(i);
			if(thing.moveDir == "none" && !thing.extending && !thing.retracting) {
				continue;
			}
			int x = Math.round(thing.x);
			int y = Math.round(thing.y);
			switch(thing.moveDir) {
				case "up":
					y --;
					break;
				case "down":
					y ++;
					break;
				case "left":
					x --;
					break;
				case "right":
					x ++;
					break;
			}
			if(thing.moveDir != "none") {
				// System.out.println("detected something moving to the " + thing.moveDir + " from position (" + thing.x + ", " + thing.y + ")");
				action.movement.add(new UndoStackItem(x, y, (thing.moveDir == "up" || thing.moveDir == "down") ? (thing.moveDir == "up" ? "down" : "up") : (thing.moveDir == "left" ? "right" : "left"), true));
			}
			if(thing.extending) {
				// System.out.println("detected something extending at (" + thing.x + ", " + thing.y + ")");
				action.movement.add(new UndoStackItem(x, y, "retract", false));
			}
			if(thing.retracting) {
				// System.out.println("detected something retracting at (" + thing.x + ", " + thing.y + ")");
				action.movement.add(new UndoStackItem(x, y, "extend", false));
			}
		}
		action.chainAction = chainAction;
		stack.add(action);
		// printStack();
	}
	public static void undoAction() {
		Game.currentLevel.snapToGrid();
		printStack();
		// System.out.println("Stack size: " + stack.size());
		if(stack.size() == 0) {
			// System.out.println("Stack size is 0");
			Game.chainUndo = false;
			return;
		}
		// Game.currentLevel.printContent();
		// System.out.println("UNDOING");
		if(Game.currentLevel.transitioning(true)) {
			return;
		}
		// System.out.println("undoing an action!");
		UndoStack actions = (UndoStack) stack.get(stack.size() - 1);
		for(byte i = 0; i < actions.movement.size(); i ++) {
			UndoStackItem action = (UndoStackItem) actions.movement.get(i);
			// System.out.println("the direction is: " + action.dir);
			// System.out.println("looking for something at (" + action.x + ", " + action.y + ")");
			itemLoop: for(byte j = 0; j < Game.currentLevel.content.size(); j ++) {
				Thing thing = (Thing) Game.currentLevel.content.get(j);
				// System.out.println("found something at (" + thing.x + ", " + thing.y);
				if(thing.x == action.x && thing.y == action.y) {
					// System.out.println("found something at the right position");
					if(action.moving) {
						if(thing instanceof LongExtender) {
							// System.out.println("moving the long extender " + action.dir);
						}
						else {
							// System.out.println("moving the retractor " + action.dir);
						}
						System.out.println("moving the thing at position (" + thing.x + ", " + thing.y + ") " + action.dir);
						thing.moveDir = action.dir;
						thing.timeMoving = 0;
					}
					if(action.dir == "retract") {
						if(thing instanceof LongExtender) {
							// System.out.println("retracting the long extender");
						}
						else if(thing instanceof Retractor) {
							// System.out.println("retracting the retractor");
						}
						// System.out.println("retracting the thing at position (" + thing.x + ", " + thing.y + ")");
						if(thing instanceof LongExtender) {
							Game.animationSpeed = Game.fastAnimationSpeed;
							((LongExtender) (thing)).timeRetracting = 0;
						}
						thing.retracting = true;
					}
					if(action.dir == "extend") {
						if(thing instanceof LongExtender) {
							// System.out.println("extending the long extender. Extension: " + thing.extension);
						}
						else if(thing instanceof Retractor) {
							// System.out.println("extending the retractor");
						}
						// System.out.println("extending the thing at position (" + thing.x + ", " + thing.y + ")");
						if(thing instanceof LongExtender) {
							((LongExtender) (thing)).timeExtending = 0;
							Game.animationSpeed = Game.fastAnimationSpeed;
						}
						thing.extending = true;
					}
					break itemLoop;
				}
			}
		}
		Game.chainUndo = actions.chainAction;
		Game.lastAction = actions.lastAction;
		if(actions.lastAction) {
			Game.timeSinceLastAction = 0;
		}
		stack.remove(stack.size() - 1);
		// System.out.println("After undoing, there are " + stack.size() + " items left in the stack");
	}

	public static void setLastChain(boolean chain) {
		((UndoStack) stack.get(stack.size() - 1)).chainAction = chain;
	}
	public static void setLastFinal(boolean last) {
		/* Sets the last item in the stack property 'lastAction' to be the argument */
		((UndoStack) stack.get(stack.size() - 1)).lastAction = last;
	}

	public static void printStack() {
		if(stack.size() == 0) {
			// System.out.println("The stack is empty.");
			return;
		}
		System.out.println("-------------------------------------");
		System.out.println("The stack has the following contents (" + stack.size() + " total):");
		for(short i = 0; i < stack.size(); i ++) {
			UndoStack item = (UndoStack) stack.get(i);
			item.printItem();
		}
		System.out.println("End printing for stack");
		System.out.println("-------------------------------------");
	}
	public void printItem() {
		System.out.println(" - A " + (this.lastAction ? "final " : "") + (this.chainAction ? "chain " : "") + "action with the following instructions:");
		for(short i = 0; i < movement.size(); i ++) {
			UndoStackItem item = (UndoStackItem) movement.get(i);
			item.print();
		}
	}

	public static void resetStack() {
		stack = new ArrayList();
	}
}
