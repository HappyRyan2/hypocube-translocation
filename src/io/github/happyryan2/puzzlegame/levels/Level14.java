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

		super.content.add(new LongExtender(2, 3, "up", false, 1));
		super.content.add(new Retractor(1, 3, "right", false));
		super.content.add(new Wall(0, 0));
		super.content.add(new Wall(3, 0));
		super.content.add(new Wall(4, 0));
		super.content.add(new Retractor(1, 2, "down", false));
		super.content.add(new Wall(0, 4));
		super.content.add(new Wall(1, 4));
		super.content.add(new Player(4, 3));
		super.content.add(new Retractor(3, 2, "left", false));
		super.content.add(new Goal(3, 3));
	}
}
