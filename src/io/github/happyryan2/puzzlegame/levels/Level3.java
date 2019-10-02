package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level3 extends Level {
	public Level3() {
		super.requirements.add(2);
		super.id = 3;
		super.x = 0;
		super.y = 200;

		super.manualSize = true;
		super.width = 3;
		super.height = 3;

		super.content.add(new Player(1, 1));
		super.content.add(new Goal(2, 1));
		super.content.add(new Retractor(0, 2, "right"));
		super.content.add(new Retractor(1, 2, "up"));
		super.content.add(new Retractor(0, 0, "right"));
	}
}
