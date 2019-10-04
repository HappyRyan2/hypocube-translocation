package io.github.happyryan2.puzzlegame.levels;

import io.github.happyryan2.puzzlegame.game.Level;
import io.github.happyryan2.puzzlegame.objects.*;

public class Level18 extends Level {
	public Level18() {
		super.id = 18;
		super.requirements.add(16);
		super.x = 600;
		super.y = 200;
		
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
