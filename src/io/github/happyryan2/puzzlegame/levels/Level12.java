package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.objects.*;
import io.github.happyryan2.puzzlegame.game.Level;

public class Level12 extends Level {
	public Level12() {
		super.id = 12;
		super.x = 1000;
		super.y = 0;
		super.requirements.add(10);

		super.manualSize = true;
		super.width = 3;
		super.height = 5;

		super.content.add(new Retractor(0, 1, "down", true));
		super.content.add(new Retractor(0, 2, "right"));
		super.content.add(new Retractor(1, 3, "up", true));
		super.content.add(new Player(1, 1));
		super.content.add(new Goal(2, 1));
	}
}
