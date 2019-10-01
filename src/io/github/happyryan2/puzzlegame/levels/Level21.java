package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level21 extends Level  {
	public Level21() {
		super.id = 21;
		super.requirements.add(17);
		super.x = 600;
		super.y = 400;

		super.content.add(new LongExtender(0, 1, "right", false));
		super.content.add(new Wall(0, 0));
		super.content.add(new LongExtender(8, 1, "left", false));
		super.content.add(new Wall(0, 2));
		super.content.add(new Wall(1, 2));
		super.content.add(new Wall(7, 2));
		super.content.add(new Wall(8, 2));
		super.content.add(new Player(5, 0));
		super.content.add(new Goal(6, 0));
		super.content.add(new Extender(5, 1, "right", false));
		super.content.add(new Retractor(4, 3, "up", false));
	}
}
