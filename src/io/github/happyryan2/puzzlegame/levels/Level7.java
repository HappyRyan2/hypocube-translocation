package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level7 extends Level {
	public Level7() {
		super.id = 7;
		super.requirements.add(5);
		super.requirements.add(6);
		super.x = 800;
		super.y = 0;

		super.content.add(new Wall(0, 0));
		super.content.add(new Wall(0, 1));
		super.content.add(new Wall(4, 2));
		super.content.add(new Extender(1, 0, "none"));
		super.content.add(new Retractor(0, 2, "right"));
		super.content.add(new Retractor(2, 1, "up"));
		super.content.add(new Player(2, 0));
		super.content.add(new Goal(4, 1));
	}
}
