package com.happyryan2.levels;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Micro2 extends Level {
	public Micro2() {
		super.content.add(new Retractor(0, 1, "right"));
		super.content.add(new Extender(0, 2, "up"));
		super.content.add(new Extender(1, 1, "down"));
		super.content.add(new Retractor(2, 1, "down"));
		super.content.add(new Extender(2, 2, "up"));
		super.content.add(new Goal(1, 1));
		super.content.add(new Player(1, 2));
	}
}
