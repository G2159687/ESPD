
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.scenes.GameScene;

public class Awareness extends FlavourBuff {

	public static final float DURATION = 2f;

	@Override
	public void detach() {
		super.detach();
		Dungeon.observe();
		GameScene.updateFog();
	}
}
