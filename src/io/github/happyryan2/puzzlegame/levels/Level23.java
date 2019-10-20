package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level23 extends Level {
	public Level23() {
		super.x = 600;
		super.y = 800;
		super.id = 23;
		super.requirements.add(22);

		super.content.add(new Retractor(2, 4, "down", false));
		super.content.add(new Retractor(2, 5, "left", false));
		super.content.add(new Goal(1, 1));
		super.content.add(new Player(1, 5));
		super.content.add(new Wall(3, 5));
		super.content.add(new Wall(3, 6));
		super.content.add(new LongExtender(1, 6, "up", true));
		super.content.add(new Wall(2, 6));
	}
}
