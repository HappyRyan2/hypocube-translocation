package com.happyryan2.levels;

public class Tutorial4 extends Level {
	public Tutorial4() {
		super.infoText = "you can push multiple objects with one extender";
		super.content.add(new Retractor(3, 2, "left"));
		super.content.add(new Retractor(2, 2, "up"));
		super.content.add(new Player(1, 1));
		super.content.add(new Goal(0, 1));
	}
}