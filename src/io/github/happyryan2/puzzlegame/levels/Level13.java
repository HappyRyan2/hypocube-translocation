package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.objects.*;
import io.github.happyryan2.puzzlegame.game.Level;

public class Level13 extends Level {
	public Level13() {
		super.id = 13;
		super.x = 200;
		super.y = 800;
		super.requirements.add(11);
		super.requirements.add(12);

		super.content.add(new Player(3, 2));
		super.content.add(new Goal(3, 0));
		super.content.add(new Wall(2, 0));
		super.content.add(new Wall(2, 1));
		super.content.add(new Wall(1, 0));
		super.content.add(new Wall(0, 0));
		super.content.add(new LongExtender(0, 2, "right"));
		super.content.add(new LongExtender(5, 3, "left"));
		super.content.add(new Retractor(5, 4, "up", false));
		super.content.add(new Retractor(0, 4, "up", false));
		super.content.add(new Wall(2, 5));
		super.content.add(new Wall(2, 4));
		super.content.add(new Wall(1, 5));
		super.content.add(new Wall(0, 5));
	}
}
