package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level23 extends Level {
	public Level23() {
		super.x = 400;
		super.y = 400;
		super.id = 26;
		super.requirements.add(21);

		super.content.add(new Wall(2, 4));
		super.content.add(new Wall(3, 4));
		super.content.add(new Wall(3, 3));
		super.content.add(new Wall(4, 3));
		super.content.add(new Wall(4, 2));
		super.content.add(new Wall(5, 2));
		super.content.add(new Wall(5, 1));
		super.content.add(new Wall(6, 1));
		super.content.add(new LongExtender(0, 4, "right", false));
		super.content.add(new Retractor(0, 3, "down", false));
		super.content.add(new Wall(0, 5));
		super.content.add(new Wall(1, 5));
		super.content.add(new Wall(2, 5));
		super.content.add(new Player(5, 0));
		super.content.add(new Goal(6, 0));
		super.content.add(new Wall(6, 2));
		super.content.add(new Wall(6, 3));
		super.content.add(new Wall(5, 3));
		super.content.add(new Wall(6, 4));
		super.content.add(new Wall(5, 4));
		super.content.add(new Wall(4, 4));
		super.content.add(new Wall(3, 5));
		super.content.add(new Wall(4, 5));
		super.content.add(new Wall(5, 5));
		super.content.add(new Wall(6, 5));
	}
}
