package com.happyryan2.levels;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Micro1 extends Level {
	public Micro1() {
		super.content.add(new Extender(0, 0, "up"));
		super.content.add(new Retractor(1, 1, "up"));
		super.content.add(new Retractor(0, 2, "right"));
		super.content.add(new Retractor(2, 1, "down"));
		super.content.add(new Retractor(2, 0, "left"));
		super.content.add(new Player(2, 2));
		super.content.add(new Goal(3, 1));
		super.content.add(new Extender(3, 2, "left"));
		super.content.add(new Extender(3, 3, "left"));
	}
}
