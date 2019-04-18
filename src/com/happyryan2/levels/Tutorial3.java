package com.happyryan2.levels;

import java.util.List;
import java.util.ArrayList;

import com.happyryan2.objects.*;
import com.happyryan2.game.Level;

public class Tutorial3 extends Level {
	List content = new ArrayList();
	public Tutorial3() {
		super.content.add(new Extender(3, 0, "left"));
		super.content.add(new Extender(2, 0, "down"));
		super.content.add(new Player(1, 1));
		super.content.add(new Goal(1, 3));
		super.content.add(new Retractor(2, 4, "up"));
		super.content.add(new Retractor(0, 4, "right"));
	}
}
