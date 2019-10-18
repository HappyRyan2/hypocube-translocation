package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.objects.*;
import io.github.happyryan2.puzzlegame.game.Level;

public class Level11 extends Level {
	public Level11() {
		super.id = 11;
		super.x = 400;
		super.y = 800;
		super.requirements.add(10);

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
