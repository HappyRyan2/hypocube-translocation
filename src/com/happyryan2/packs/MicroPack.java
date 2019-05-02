package com.happyryan2.packs;

import com.happyryan2.game.LevelPack;
import com.happyryan2.levels.*;

public class MicroPack extends LevelPack {
	public MicroPack() {
		super.name = "Micro Pack";
		super.levels.add(new Micro1());
		super.levels.add(new Micro2());
		super.levels.add(new Micro3());
	}
}
