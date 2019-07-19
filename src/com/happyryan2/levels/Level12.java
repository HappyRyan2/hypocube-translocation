package com.happyryan2.levels;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Level12 extends Level {
	public Level12() {
		super.manualSize = true;
		super.width = 3;
		super.height = 4;
		super.content.add(new Retractor(0, 0, "down"));
		super.content.add(new Retractor(2, 0, "left", true));
		super.content.add(new Retractor(1, 2, "up", true));
		super.content.add(new Extender(0, 2, "right"));
		super.content.add(new Goal(1, 2));
		super.content.add(new Player(1, 3));
	}
}
