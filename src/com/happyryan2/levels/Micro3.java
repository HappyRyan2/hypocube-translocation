package com.happyryan2.levels;

import com.happyryan2.game.Level;
import com.happyryan2.objects.*;

public class Micro3 extends Level {
	public Micro3() {
		super.content.add(new Retractor(0, 0, "right"));
		super.content.add(new Retractor(0, 1, "right"));
		super.content.add(new Retractor(2, 0, "down"));
		super.content.add(new Retractor(3, 0, "down"));
		super.content.add(new Retractor(3, 2, "left"));
		super.content.add(new Retractor(3, 3, "left"));
		super.content.add(new Retractor(0, 3, "up"));
		super.content.add(new Retractor(1, 3, "up"));
		super.content.add(new Goal(1, 0));
		super.content.add(new Goal(2, 3));
		super.content.add(new Player(1, 1));
		super.content.add(new Player(2, 2));
	}
}
