package com.happyryan2.levels;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Challenge2 extends Level {
	public Challenge2() {
        super.content.add(new Goal(2, 0));
        super.content.add(new Goal(0, 2));
		super.content.add(new Retractor(0, 0, "down"));
        super.content.add(new Retractor(1, 1, "down"));
        super.content.add(new Retractor(4, 0, "left"));
        super.content.add(new Retractor(3, 1, "left"));
        super.content.add(new Retractor(1, 3, "right"));
        super.content.add(new Retractor(0, 4, "right"));
        super.content.add(new Retractor(3, 3, "up"));
        super.content.add(new Retractor(4, 4, "up"));
        super.content.add(new Player(4, 2));
        super.content.add(new Player(2, 4));
	}
}
