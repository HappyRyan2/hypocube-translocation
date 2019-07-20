package com.happyryan2.objects;

public class Wall extends Thing {
	public Wall(float x, float y) {
		super.x = x;
		super.y = y;
		super.origX = x;
		super.origY = y;
	}

	public boolean canBePushed() {
		return true;
	}
	public void checkMovement() {
		return;
	}
	public boolean canDoSomething() {
		return false;
	}
}
