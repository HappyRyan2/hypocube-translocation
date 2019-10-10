package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level14 extends Level {
	public Level14() {
		super.id = 14;
		super.requirements.add(12);
		super.requirements.add(15);
		super.x = 0;
		super.y = 600;

		super.content.add(new LongExtender(1, 2, "up", false));
		super.content.add(new Goal(1, 2));
		super.content.add(new Retractor(2, 1, "left", false));
		super.content.add(new Retractor(2, 3, "up", false));
		super.content.add(new Player(1, 1));
		super.content.add(new Retractor(2, 0, "down", false));
		super.content.add(new Wall(3, 2));
		super.content.add(new Wall(3, 3));
	}
}
