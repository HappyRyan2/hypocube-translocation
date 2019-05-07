package com.happyryan2.levels;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Challenge1 extends Level {
	public Challenge1() {
        super.content.add(new Player(1, 0));
        super.content.add(new Retractor(3, 0, "left"));
        super.content.add(new Retractor(1, 1, "down", true));
        super.content.add(new Retractor(1, 2, "right"));
        super.content.add(new Retractor(2, 3, "up"));
        super.content.add(new Retractor(2, 4, "up"));
        super.content.add(new Goal(3, 1));
        // super.content.add(new Extender(3, 4, "up"));
	}
}
