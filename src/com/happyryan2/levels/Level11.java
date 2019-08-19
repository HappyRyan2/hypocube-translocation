package com.happyryan2.levels;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Level11 extends Level {
	public Level11() {
		super.x = 1000;
		super.y = 200;
		super.requirements.add(9);
		super.id = 11;

		super.manualSize = true;
		super.width = 3;
		super.height = 5;

		super.content.add(new Retractor(0, 1, "down", true));
		super.content.add(new Extender(0, 2, "right"));
		super.content.add(new Retractor(1, 3, "up", true));
		super.content.add(new Player(1, 1));
		super.content.add(new Goal(2, 1));
		super.content.add(new Extender(2, 3, "none"));
	}
}
