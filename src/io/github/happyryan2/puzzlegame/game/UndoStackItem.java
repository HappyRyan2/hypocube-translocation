/*
Within each undo/redo action, this class represents a single game object that is to be moved.
*/

package io.github.happyryan2.puzzlegame.game;

public class UndoStackItem {
    public int x;
    public int y;
    public String dir;
    public boolean moving;
    public UndoStackItem(int x, int y, String dir, boolean moving) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.moving = moving;
    }

	/* Debug */
	public void print() {
		if(this.dir == "extend") {
			System.out.println("    - Extend something at (" + this.x + ", " + this.y + ")");
		}
		else if(this.dir == "retract") {
			System.out.println("    - Retract something at (" + this.x + ", " + this.y + ")");
		}
		else {
			System.out.println("    - Move something at (" + this.x + ", " + this.y + ") " + this.dir);
		}
	}
}
