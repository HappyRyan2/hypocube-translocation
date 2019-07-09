/*
(Statically) this class represents the stack of game states. Making a move adds to the top of the stack, pressing 'undo' removes the one on top and reverts to that position.
Instances of this class represent a single move.
*/

package com.happyryan2.game;

import java.util.List;
import java.util.ArrayList;

import com.happyryan2.objects.*;

public class Stack {
    public static List stack = new ArrayList();
    public static void addAction() {
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
        stack.add(action);
    }
    public static void undoAction() {
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
				// System.out.println("found something at (" + thing.x + ", " + thing.y);
				if(thing.x == action.x && thing.y == action.y) {
					// System.out.println("found something at the right position");
					if(action.moving) {
						// System.out.println("moving the thing at position (" + thing.x + ", " + thing.y + ") " + action.dir);
						thing.moveDir = action.dir;
						thing.timeMoving = 0;
					}
					if(action.dir == "retract") {
						// System.out.println("retracting the thing at position (" + thing.x + ", " + thing.y + ")");
						thing.retracting = true;
					}
					if(action.dir == "extend") {
						// System.out.println("extending the thing at position (" + thing.x + ", " + thing.y + ")");
						thing.extending = true;
					}
				}
			}
		}
		stack.remove(stack.size() - 1);
    }
    public static void resetStack() {
		stack = new ArrayList();
    }

    public List movement = new ArrayList();
}
