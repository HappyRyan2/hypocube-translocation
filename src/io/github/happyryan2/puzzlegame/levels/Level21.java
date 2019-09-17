package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level21 extends Level  {
	public Level21() {
		super.id = 21;
		super.requirements.add(17);
		super.x = 600;
		super.y = 400;

		super.content.add(new LongExtender(0, 1, "right"));
		super.content.add(new LongExtender(6, 1, "left"));
		super.content.add(new Extender(4, 1, "right", false));
		super.content.add(new Goal(5, 0));
		super.content.add(new Wall(0, 0));
		super.content.add(new Wall(6, 2));
		super.content.add(new Wall(5, 2));
		super.content.add(new Retractor(3, 2, "up", false));
		super.content.add(new Player(3, 0));
	}
}
