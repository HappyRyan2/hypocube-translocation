package com.happyryan2.levels;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Level10 extends Level {
	public Level10() {
		super.id = 10;
		super.x = 800;
		super.y = 200;
		super.requirements.add(8);

		super.content.add(new Retractor(0, 1, "right", false));
		super.content.add(new Extender(1, 1, "left", false));
		super.content.add(new Retractor(1, 0, "down", false));
		super.content.add(new Player(3, 2));
		super.content.add(new Goal(3, 1));
	}
}
