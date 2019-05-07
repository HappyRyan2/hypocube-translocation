/*
(Statically) this class represents the stack of game states. Making a move adds to the top of the stack, pressing 'undo' removes the one on top and reverts to that position.
Instances of this class represent a single move.
*/

package com.happyryan2.game;

import java.util.List;
import java.util.ArrayList;

public class Stack {
    public static List stack = new ArrayList();
    public static void addAction() {
        Stack action = new Stack();
        for(byte i = 0; i < Game.currentLevel.content.size(); i ++) {
            Thing thing = (Thing) Game.currentLevel.content.get(i);
            if(!thing.moving && !thing.extending && !thing.retracting) {
                continue;
            }
            if(thing.moving) {
                action.movement.add(new StackItem(Math.round(thing.x), Math.round(thing.y), (thing.moveDir == "up" || thing.moveDir == "down") ? (thing.moveDir == "up" ? "down" : "up") : (thing.moveDir == "left" ? "right" : "left"), true));
            }
            else if(thing.extending) {
                action.movement.add(new StackItem(Math.round(thing.x), Math.round(thing.y), "retract", false));
            }
            else if(thing.retracting) {
                action.movement.add(new StackItem(Math.round(thing.x), Math.round(thing.y), "extend", false);
            }
        }
        stack.add(action);
    }
    public static void undoAction() {

    }
    public static void resetStack() {

    }

    public List movement = new ArrayList();
}
