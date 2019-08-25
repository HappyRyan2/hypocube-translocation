package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.objects.*;
import io.github.happyryan2.puzzlegame.game.Level;

public class Level14 extends Level {
	public Level14() {
		super.id = 14;
		super.x = 400;
		super.y = 0;
		super.requirements.add(6);
		super.requirements.add(12);
		super.requireAll = true;

		super.manualSize = true;
		super.width = 3;
		super.height = 5;

		super.content.add(new Retractor(1, 4, "up", true));
		super.content.add(new Retractor(1, 3, "up"));
		super.content.add(new Retractor(0, 1, "right"));
		super.content.add(new Player(1, 2));
		super.content.add(new Goal(1, 0));
	}
}
