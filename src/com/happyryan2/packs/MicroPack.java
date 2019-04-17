package com.happyryan2.packs;

import java.util.List;
import java.util.ArrayList;

import com.happyryan2.game.LevelPack;
import com.happyryan2.levels.Micro1;

public class MicroPack extends LevelPack {
	public MicroPack() {
		this.levels = new ArrayList();
		this.levels.add(new Micro1());
	}
}
