
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.ui.BuffIndicator;

public class MindVision extends FlavourBuff {

	public static final float DURATION = 40f;

	public int distance = 2;

	@Override
	public int icon() {
		return BuffIndicator.MIND_VISION;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public void detach() {
		super.detach();
		Dungeon.observe();
		GameScene.updateFog();
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns());
	}
}
