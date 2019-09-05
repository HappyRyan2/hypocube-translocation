/*
(Statically) this class represents the stack of game states. Making a move adds to the top of the stack, pressing 'undo' removes the one on top and reverts to that position.
Instances of this class represent a single move.
*/

package io.github.happyryan2.puzzlegame.game;

import java.util.List;
import java.util.ArrayList;

import io.github.happyryan2.puzzlegame.objects.*;

public class Stack {
    public static List stack = new ArrayList();
	public boolean chainAction;
    public List movement = new ArrayList();

	public static void addAction() {
		addAction(false);
	}
    public static void addAction(boolean chainAction) {
        // System.out.println("adding an action!");
        Stack action = new Stack();
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
                action.movement.add(new StackItem(x, y, (thing.moveDir == "up" || thing.moveDir == "down") ? (thing.moveDir == "up" ? "down" : "up") : (thing.moveDir == "left" ? "right" : "left"), true));
            }
            if(thing.extending) {
				// System.out.println("detected something extending at (" + thing.x + ", " + thing.y + ")");
                action.movement.add(new StackItem(x, y, "retract", false));
            }
            if(thing.retracting) {
				// System.out.println("detected something retracting at (" + thing.x + ", " + thing.y + ")");
                action.movement.add(new StackItem(x, y, "extend", false));
            }
        }
		action.chainAction = chainAction;
        stack.add(action);
    }
    public static void undoAction() {
		printStack();
		Game.currentLevel.printContent();
		// System.out.println("UNDOING");
		if(stack.size() == 0 || Game.currentLevel.transitioning()) {
			return;
		}
        // System.out.println("undoing an action!");
		Stack actions = (Stack) stack.get(stack.size() - 1);
		for(byte i = 0; i < actions.movement.size(); i ++) {
			StackItem action = (StackItem) actions.movement.get(i);
			// System.out.println("the direction is: " + action.dir);
			// System.out.println("looking for something at (" + action.x + ", " + action.y + ")");
			for(byte j = 0; j < Game.currentLevel.content.size(); j ++) {
				Thing thing = (Thing) Game.currentLevel.content.get(j);
				System.out.println("found something at (" + thing.x + ", " + thing.y);
				if(thing.x == action.x && thing.y == action.y) {
					System.out.println("found something at the right position");
					if(action.moving) {
						// System.out.println("moving the thing at position (" + thing.x + ", " + thing.y + ") " + action.dir);
						thing.moveDir = action.dir;
						thing.timeMoving = 0;
						System.out.println("Started moving to undo");
					}
					if(action.dir == "retract") {
						// System.out.println("retracting the thing at position (" + thing.x + ", " + thing.y + ")");
						if(thing instanceof LongExtender) {
							Game.animationSpeed = Game.fastAnimationSpeed;
							((LongExtender) (thing)).timeRetracting = 0;
						}
						thing.retracting = true;
					}
					if(action.dir == "extend") {
						// System.out.println("extending the thing at position (" + thing.x + ", " + thing.y + ")");
						if(thing instanceof LongExtender) {
							((LongExtender) (thing)).timeExtending = 0;
							Game.animationSpeed = Game.fastAnimationSpeed;
						}
						thing.extending = true;
					}
				}
			}
		}
		Game.chainUndo = actions.chainAction;
		stack.remove(stack.size() - 1);
    }

	public static void printStack() {
		if(stack.size() == 0) {
			// System.out.println("The stack is empty.");
			return;
		}
		System.out.println("-------------------------------------");
		System.out.println("The stack has the following contents:");
		for(short i = 0; i < stack.size(); i ++) {
			Stack item = (Stack) stack.get(i);
			item.printItem();
		}
		System.out.println("End printing for stack");
		System.out.println("-------------------------------------");
	}
	public void printItem() {
		System.out.println(" - A " + (this.chainAction ? "chain " : "") + "action with the following instructions:");
		for(short i = 0; i < movement.size(); i ++) {
			StackItem item = (StackItem) movement.get(i);
			item.print();
		}
	}

	public static void resetStack() {
		stack = new ArrayList();
	}
}
