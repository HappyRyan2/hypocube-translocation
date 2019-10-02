package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.objects.*;
import io.github.happyryan2.puzzlegame.game.Level;

public class Level6 extends Level {
	public Level6() {
		super.id = 6;
		super.requirements.add(4);
		super.x = 800;
		super.y = -200;

		// super.content.add(new Retractor(1, 3, "right", false));
		// super.content.add(new Retractor(3, 3, "left", false));
		// super.content.add(new Retractor(2, 2, "down", false));
		// super.content.add(new Wall(3, 4));
		// super.content.add(new Wall(4, 4));
		// super.content.add(new Player(3, 1));
		// super.content.add(new Goal(2, 1));
		// super.content.add(new Wall(0, 0));
		// super.content.add(new Wall(0, 1));
		// super.content.add(new Wall(4, 0));
		// super.content.add(new Wall(4, 1));

		super.content.add(new Retractor(0, 1, "right", false));
		super.content.add(new Extender(1, 1, "left", false));
		super.content.add(new Retractor(1, 0, "down", false));
		super.content.add(new Player(3, 2));
		super.content.add(new Goal(3, 1));
	}
}
