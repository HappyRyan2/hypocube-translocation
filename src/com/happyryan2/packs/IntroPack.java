package com.happyryan2.packs;

import com.happyryan2.game.LevelPack;
import com.happyryan2.levels.*;

public class IntroPack extends LevelPack {
	public IntroPack() {
		super.name = "Introductory Pack";
		super.levels.add(new Intro1());
	}
}
