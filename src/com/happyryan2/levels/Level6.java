package com.happyryan2.levels;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Level6 extends Level {
	public Level6() {
		super.id = 6;
		super.requirements.add(5);
		super.x = 600;
		super.y = 0;

		super.content.add(new Retractor(4, 2, "left", false));
		super.content.add(new Extender(2, 2, "up", false));
		super.content.add(new Retractor(3, 0, "down", false));
		super.content.add(new Goal(0, 2));
		super.content.add(new Player(1, 2));
	}
}
