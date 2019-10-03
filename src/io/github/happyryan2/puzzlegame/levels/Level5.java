package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.objects.*;
import io.github.happyryan2.puzzlegame.game.Level;

public class Level5 extends Level {
	public Level5() {
		super.id = 5;
		super.requirements.add(3);
		super.x = 0;
		super.y = 400;

		super.content.add(new Retractor(4, 2, "left", false));
		super.content.add(new Extender(2, 2, "up", false));
		super.content.add(new Retractor(3, 0, "down", false));
		super.content.add(new Goal(0, 2));
		super.content.add(new Player(1, 2));
	}
}
