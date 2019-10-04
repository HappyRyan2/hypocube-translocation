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
	}
}
