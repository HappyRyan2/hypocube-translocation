package com.happyryan2.levels;

import com.happyryan2.game.Level;
import com.happyryan2.objects.*;

public class Level4 extends Level {
	public Level4() {
		super.requirements.add(3);
		super.id = 4;
		super.x = 400;
		super.y = -200;

		super.manualSize = true;
		super.width = 7;
		super.height = 1;

		super.content.add(new Extender(0, 0, "right", true));
		super.content.add(new Extender(1, 0, "right", true));
		super.content.add(new Extender(2, 0, "right", true));
		super.content.add(new Player(3, 0));
		super.content.add(new Goal(6, 0));
	}
}
