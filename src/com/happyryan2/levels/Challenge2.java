package com.happyryan2.levels;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Challenge2 extends Level {
	public Challenge2() {
		super.manualSize = true;
		super.width = 4;
		super.height = 5;
		super.content.add(new Retractor(0, 0, "right", true));
		super.content.add(new Extender(0, 2, "right", false));
		super.content.add(new Player(0, 3));
		// super.content.add(new Retractor(0, 1, "up", true));
		super.content.add(new Retractor(1, 2, "up", true));
		super.content.add(new Retractor(2, 1, "left", false));
		super.content.add(new Retractor(2, 4, "up", false));
		super.content.add(new Retractor(3, 4, "left", true));
		super.content.add(new Goal(1, 2));
		super.content.add(new Extender(0, 1, "none"));
	}
}
