package com.happyryan2.levels;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Level7 extends Level {
	public Level7() {
		super.id = 7;
		super.requirements.add(5);
		super.x = 800;
		super.y = -200;

		super.manualSize = true;
		super.width = 8;
		super.height = 5;

		super.content.add(new Retractor(2, 2, "up", false));
		super.content.add(new Wall(1, 3));
		super.content.add(new Wall(2, 1));
		super.content.add(new Wall(3, 3));
		super.content.add(new Wall(4, 1));
		super.content.add(new Wall(5, 3));
		super.content.add(new Wall(6, 1));
		super.content.add(new Player(7, 0));
		super.content.add(new Goal(7, 1));
		super.content.add(new Retractor(0, 2, "right", false));
	}
}
