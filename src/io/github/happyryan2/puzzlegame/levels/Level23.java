package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level23 extends Level {
	public Level23() {
		super.x = 600;
		super.y = 800;
		super.id = 23;
		super.requirements.add(22);

		super.content.add(new LongExtender(0, 2, "right", true));
		super.content.add(new Wall(6, 4));
		super.content.add(new Wall(5, 4));
		super.content.add(new Wall(5, 3));
		super.content.add(new Wall(6, 3));
		super.content.add(new Wall(4, 0));
		super.content.add(new Wall(5, 0));
		super.content.add(new Wall(6, 0));
		super.content.add(new Player(0, 3));
		super.content.add(new Goal(0, 4));
		super.content.add(new Wall(4, 3));
		super.content.add(new Wall(4, 4));
		super.content.add(new Wall(7, 0));
		super.content.add(new Wall(7, 2));
		super.content.add(new Wall(7, 3));
		super.content.add(new Wall(7, 4));
		super.content.add(new Retractor(2, 0, "left", false));
		super.content.add(new Wall(3, 0));
		super.content.add(new Retractor(1, 1, "up", false));
	}
}
