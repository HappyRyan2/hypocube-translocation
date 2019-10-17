package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level7 extends Level {
	public Level7() {
		super.id = 7;
		super.requirements.add(6);
		super.x = 400;
		super.y = 400;

		super.content.add(new Extender(2, 2, "left", false));
		super.content.add(new Player(2, 1));
		super.content.add(new Goal(3, 1));
		super.content.add(new Wall(3, 2));
		super.content.add(new Wall(3, 3));
		super.content.add(new Retractor(1, 1, "down", false));
		super.content.add(new Extender(0, 3, "right", false));
		super.content.add(new Wall(2, 3));
	}
}
