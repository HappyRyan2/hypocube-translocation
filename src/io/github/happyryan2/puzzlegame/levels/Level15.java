package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level15 extends Level {
	public Level15() {
		super.id = 15;
		super.requirements.add(14);
		super.requirements.add(13);
		super.x = 0;
		super.y = 800;

		// super.content.add(new LongExtender(0, 2, "right", false));
		// super.content.add(new LongExtender(8, 2, "left", false));
		// super.content.add(new Wall(8, 0));
		// super.content.add(new Wall(2, 2));
		// super.content.add(new Wall(8, 4));
		// super.content.add(new Goal(7, 4));
		// super.content.add(new Player(5, 4));
		// super.content.add(new Extender(4, 2, "right", false));
		// super.content.add(new Retractor(4, 1, "down", false));
		
		super.content.add(new Player(1, 3));
		super.content.add(new Goal(3, 3));
		super.content.add(new Retractor(1, 1, "down", false));
		super.content.add(new LongExtender(4, 2, "left", false));
		super.content.add(new Wall(4, 0));
		super.content.add(new LongExtender(0, 2, "right", false));
	}
}
