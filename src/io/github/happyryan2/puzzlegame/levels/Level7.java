package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level7 extends Level {
	public Level7() {
		super.id = 7;
		super.requirements.add(5);
		super.requirements.add(6);
		super.x = 400;
		super.y = 400;

		super.content.add(new Retractor(1, 3, "right", false));
		super.content.add(new Retractor(3, 3, "left", false));
		super.content.add(new Retractor(2, 2, "down", false));
		super.content.add(new Wall(3, 4));
		super.content.add(new Wall(4, 4));
		super.content.add(new Player(3, 1));
		super.content.add(new Goal(2, 1));
		super.content.add(new Wall(0, 0));
		super.content.add(new Wall(0, 1));
		super.content.add(new Wall(4, 0));
		super.content.add(new Wall(4, 1));
	}
}
