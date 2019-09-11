package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level17 extends Level {
	public Level17() {
		super.id = 17;
		super.requireAll = true;
		super.requirements.add(8);
		super.x = 800;
		super.y = 400;

		super.content.add(new Wall(4, 0));
		super.content.add(new Wall(2, 0));
		super.content.add(new Wall(0, 3));
		super.content.add(new Wall(5, 2));
		super.content.add(new Wall(5, 3));
		super.content.add(new Player(2, 2));
		super.content.add(new Goal(2, 3));
		super.content.add(new Extender(5, 0, "down", false));
		super.content.add(new Extender(3, 0, "down", false, true));
		super.content.add(new LongExtender(0, 1, "right", false));
	}
}
