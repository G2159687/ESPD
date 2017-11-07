
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.BuffIndicator;

public class Strength extends Buff {

	public static float LEVEL = 0.4f;

	@Override
	public int icon() {
		return BuffIndicator.MOON_FURY;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", LEVEL);
	}

	@Override
	public void detach() {

		Buff buff = Dungeon.hero.buff(FullMoonStrength.class);
		if (buff != null) {

			buff.detach();
		} else {
			super.detach();
		}

	}
}
