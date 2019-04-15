package com.happyryan2.levels;

import java.util.List;
import java.util.ArrayList;

import com.happyryan2.objects.Extender;
import com.happyryan2.game.Level;

public class Tutorial1 extends Level {
	List content = new ArrayList();
	public Tutorial1() {
		super.content.add(new Extender(0, 0, "up"));
		super.content.add(new Extender(1, 1, "up"));
		super.content.add(new Extender(0, 2, "right"));
		super.content.add(new Extender(2, 1, "down"));
		super.content.add(new Extender(2, 0, "left"));
	}
}
