package com.happyryan2.levels;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Level11 extends Level {
	public Level11() {
		super.x = 1000;
		super.y = 200;
		super.requirements.add(9);
		super.id = 11;

		// super.manualSize = true;
		// super.width = 3;
		// super.height = 5;

		// super.content.add(new Retractor(0, 1, "down", true));
		// super.content.add(new Extender(0, 2, "right"));
		// super.content.add(new Retractor(1, 3, "up", true));
		// super.content.add(new Player(1, 1));
		// super.content.add(new Goal(2, 1));
		// super.content.add(new Extender(2, 3, "none"));
		
		super.content.add(new Extender(3, 2, "right", false));
		super.content.add(new Retractor(4, 2, "down", false));
		super.content.add(new Goal(4, 2));
		super.content.add(new Retractor(5, 4, "up", false));
		super.content.add(new Retractor(3, 4, "right", false));
		super.content.add(new Player(5, 5));
		super.content.add(new Wall(2, 1));
		super.content.add(new Wall(3, 1));
		super.content.add(new Wall(4, 1));
		super.content.add(new Wall(5, 1));
		super.content.add(new Wall(6, 1));
		super.content.add(new Wall(6, 2));
		super.content.add(new Wall(6, 3));
		super.content.add(new Wall(6, 4));
		super.content.add(new Wall(6, 5));
		super.content.add(new Wall(6, 6));
		super.content.add(new Wall(5, 6));
		super.content.add(new Wall(4, 6));
		super.content.add(new Wall(3, 6));
		super.content.add(new Wall(2, 6));
		super.content.add(new Wall(2, 5));
		super.content.add(new Wall(2, 4));
		super.content.add(new Wall(2, 3));
		super.content.add(new Wall(2, 2));
		super.content.add(new Wall(3, 5));
	}
}
