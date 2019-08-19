package com.happyryan2.levels;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Level12 extends Level {
	public Level12() {
		super.id = 12;
		super.x = 400;
		super.y = 200;
		super.requirements.add(10);

		super.content.add(new Extender(0, 3, "right", false));
		super.content.add(new Retractor(1, 3, "up", false));
		super.content.add(new Goal(1, 3));
		super.content.add(new Retractor(2, 1, "left", false));
		super.content.add(new Retractor(2, 2, "up", false));
		super.content.add(new Wall(1, 0));
		super.content.add(new Player(0, 2));
		super.content.add(new Retractor(0, 0, "down", true));
	}
}
