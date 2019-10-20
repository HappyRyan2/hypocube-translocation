package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level25 extends Level  {
	public Level25() {
		super.id = 25;
		super.x = 800;
		super.y = 800;
		super.requirements.add(24);
		super.requirements.add(23);

		super.content.add(new LongExtender(1, 0, "down", false));
		super.content.add(new Wall(7, 1));
		super.content.add(new Retractor(2, 0, "left", true));
		super.content.add(new Wall(0, 0));
		super.content.add(new Wall(0, 4));
		super.content.add(new Retractor(2, 5, "left", true));
		super.content.add(new Wall(0, 5));
		super.content.add(new LongExtender(1, 5, "up", false));
		super.content.add(new Retractor(7, 3, "left", false));
		super.content.add(new Player(6, 4));
		super.content.add(new Goal(7, 4));
		super.content.add(new Wall(6, 2));
		super.content.add(new Wall(7, 2));
		super.content.add(new Wall(7, 0));
		super.content.add(new Wall(6, 1));
		super.content.add(new Wall(6, 0));
		super.content.add(new Retractor(0, 3, "right", true));
	}
}
