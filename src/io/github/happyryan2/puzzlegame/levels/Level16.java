package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level16 extends Level {
	public Level16() {
		super.id = 16;
		super.requirements.add(15);
		super.x = 1200;
		super.y = 0;

		super.content.add(new Player(4, 1));
		super.content.add(new Goal(9, 1));
		super.content.add(new Wall(7, 2));
		super.content.add(new Wall(8, 2));
		super.content.add(new Wall(9, 2));
		super.content.add(new Retractor(1, 0, "down", false));
		super.content.add(new Retractor(0, 0, "right", true));
		super.content.add(new Retractor(0, 1, "right", true));
		super.content.add(new Retractor(2, 2, "left", true));
		super.content.add(new Wall(6, 2));
	}
}
