package io.github.happyryan2.puzzlegame.levels;

import java.util.List;
import java.util.ArrayList;

import io.github.happyryan2.puzzlegame.objects.*;
import io.github.happyryan2.puzzlegame.game.Level;

public class Level2 extends Level {
	List content = new ArrayList();
	public Level2() {
		super.requirements.add(1);
		super.id = 2;
		super.x = 0;
		super.y = -200;

		super.content.add(new Retractor(0, 1, "right"));
		super.content.add(new Retractor(2, 0, "down"));
		super.content.add(new Player(2, 2));
		super.content.add(new Goal(1, 1));
	}
}
