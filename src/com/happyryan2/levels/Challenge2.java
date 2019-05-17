package com.happyryan2.levels;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Challenge2 extends Level {
	public Challenge2() {
        super.content.add(new Extender(3, 0, "left", true));
		super.content.add(new Retractor(3, 2, "up", false));
		super.content.add(new Retractor(0, 2, "right", false));
		super.content.add(new Retractor(0, 0, "down", false));
		super.content.add(new Retractor(1, 1, "up", false));
		super.content.add(new Goal(1, 1));
		super.content.add(new Player(2, 1));
	}
}
