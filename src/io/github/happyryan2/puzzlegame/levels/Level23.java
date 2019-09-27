package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level23 extends Level  {
	public Level23() {
		super.id = 23;
		super.requirements.add(10);
		super.x = 600;
		super.y = 600;

		super.content.add(new LongExtender(0, 2, "right"));
		super.content.add(new LongExtender(5, 2, "left"));
		super.content.add(new Retractor(2, 1, "down", false));
		super.content.add(new Player(3, 0));
		super.content.add(new Goal(5, 0));
		super.content.add(new Wall(5, 3));
		super.content.add(new Wall(0, 0));
	}
}
