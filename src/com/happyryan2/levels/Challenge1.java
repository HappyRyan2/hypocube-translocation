package com.happyryan2.levels;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Challenge1 extends Level {
	public Challenge1() {
        super.content.add(new Player(1, 0));
        // super.content.add(new Retractor(2, 0, "left"));
        super.content.add(new Retractor(0, 1, "down", true));
        super.content.add(new Retractor(0, 2, "right"));
        super.content.add(new Retractor(1, 3, "up"));
        super.content.add(new Retractor(1, 4, "up"));
        super.content.add(new Goal(2, 1));
        // super.content.add(new Extender(3, 4, "up"));
	}
}
