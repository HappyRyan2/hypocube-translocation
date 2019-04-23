package com.happyryan2.levels;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Intro1 extends Level {
	public Intro1() {
		super.content.add(new Extender(0, 1, "right"));
		super.content.add(new Extender(2, 0, "down"));
		super.content.add(new Extender(3, 2, "left"));
		super.content.add(new Retractor(1, 5, "up"));
		super.content.add(new Retractor(1, 6, "up"));
		super.content.add(new Player(1, 1));
		super.content.add(new Goal(1, 4));
	}
}
