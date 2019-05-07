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
        Stack action = new Stack();
        for(byte i = 0; i < Game.currentLevel.content.size(); i ++) {
            Thing thing = (Thing) Game.currentLevel.content.get(i);
            if(thing.moveDir == "none" && !thing.extending && !thing.retracting) {
                continue;
            }
            if(thing.moveDir != "none") {
                action.movement.add(new StackItem(Math.round(thing.x), Math.round(thing.y), (thing.moveDir == "up" || thing.moveDir == "down") ? (thing.moveDir == "up" ? "down" : "up") : (thing.moveDir == "left" ? "right" : "left"), true));
            }
            if(thing.extending) {
                action.movement.add(new StackItem(Math.round(thing.x), Math.round(thing.y), "retract", false));
            }
            if(thing.retracting) {
                action.movement.add(new StackItem(Math.round(thing.x), Math.round(thing.y), "extend", false));
            }
        }
        stack.add(action);
    }
    public static void undoAction() {
		if(stack.size() == 0) {
			return;
		}
		Stack actions = (Stack) stack.get(stack.size() - 1);
		for(byte i = 0; i < actions.movement.size(); i ++) {
			StackItem action = (StackItem) actions.movement.get(i);
			for(byte j = 0; j < Game.currentLevel.content.size(); j ++) {
				Thing thing = (Thing) Game.currentLevel.content.get(i);
				if(thing.x == action.x && thing.y == action.y) {
					if(action.moving) {
						thing.moveDir = action.dir;
					}
					if(action.dir == "retract") {
						thing.retracting = true;
					}
					if(action.dir == "extend") {
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
