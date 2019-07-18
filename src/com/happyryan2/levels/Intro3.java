package com.happyryan2.levels;

import com.happyryan2.game.Level;
import com.happyryan2.objects.*;

public class Intro3 extends Level {
	public Intro3() {
		super.id = 8;
		super.requirements.add(6);
		super.requirements.add(7);
		super.x = 800;
		super.y = 0;

		super.content.add(new Wall(0, 0));
		super.content.add(new Wall(0, 1));
		super.content.add(new Wall(4, 2));
		super.content.add(new Extender(1, 0, "none"));
		super.content.add(new Retractor(0, 2, "right"));
		super.content.add(new Retractor(2, 2, "up"));
		super.content.add(new Player(2, 1));
		super.content.add(new Goal(4, 1));
	}
}
