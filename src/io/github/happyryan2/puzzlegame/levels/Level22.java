package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level22 extends Level  {
	public Level22() {
		super.id = 22;
		super.requirements.add(10);
		super.requirements.add(16);
		super.requireAll = true;
		super.x = 600;
		super.y = 600;

		// super.content.add(new LongExtender(0, 1, "right", true));
		// super.content.add(new Retractor(6, 2, "left", true));
		// super.content.add(new Retractor(7, 1, "left", true));
		// super.content.add(new Retractor(5, 0, "down", false));
		// super.content.add(new Goal(0, 2));
		// super.content.add(new Player(3, 2));
		// super.content.add(new Wall(0, 0));
		// super.content.add(new Wall(7, 2));
		// super.content.add(new Wall(7, 0));

		super.content.add(new Retractor(0, 2, "right", true));
		super.content.add(new LongExtender(1, 0, "down", false));
		super.content.add(new LongExtender(1, 4, "up", false, 2));
		super.content.add(new Wall(6, 0));
		super.content.add(new Wall(6, 1));
		super.content.add(new Wall(7, 1));
		super.content.add(new Wall(7, 0));
		super.content.add(new Player(6, 3));
		super.content.add(new Goal(7, 3));
		super.content.add(new Retractor(7, 2, "left", false));
		super.content.add(new Retractor(2, 0, "left", true));
		super.content.add(new Retractor(2, 4, "left", true));
		super.content.add(new Wall(0, 0));
		super.content.add(new Wall(0, 4));
	}
}
