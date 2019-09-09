package io.github.happyryan2.puzzlegame.objects;

public class Wall extends Thing {
	public Wall(float x, float y) {
		super.x = x;
		super.y = y;
		super.origX = x;
		super.origY = y;
	}
	public Wall(Wall w) {
		this(w.x, w.y);
	}

	public boolean canBePushed() {
		return false;
	}
	public void checkMovement() {
		return;
	}
	public boolean canDoSomething() {
		return false;
	}
}
