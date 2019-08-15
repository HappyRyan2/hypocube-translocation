package com.happyryan2.levels;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Level13 extends Level {
	public Level13() {
		super.id = 13;
		super.x = 1000;
		super.y = 0;
		super.requirements.add(11);

		super.manualSize = true;
		super.width = 3;
		super.height = 3;
		
		super.content.add(new Player(0, 0));
		super.content.add(new Goal(2, 2));
		super.content.add(new Retractor(1, 2, "up", false));
		super.content.add(new Retractor(2, 1, "left", false));
		super.content.add(new Retractor(1, 1, "right", false));
		super.content.add(new Extender(0, 1, "right", false));
	}
}
