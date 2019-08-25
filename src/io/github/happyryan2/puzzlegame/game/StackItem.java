/*
Within each undo/redo action, this class represents a single game object that is to be moved.
*/

package io.github.happyryan2.puzzlegame.game;

public class StackItem {
    public int x;
    public int y;
    public String dir;
    public boolean moving;
    public StackItem(int x, int y, String dir, boolean moving) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.moving = moving;
    }
}
