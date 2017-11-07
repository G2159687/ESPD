
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.BuffIndicator;

public class Fury extends Buff {

	public static float LEVEL = 0.4f;

	@Override
	public boolean act() {
		if (target.HP > target.HT * LEVEL) {
			detach();
		}

		spend(TICK);

		return true;
	}

	@Override
	public int icon() {
		return BuffIndicator.FURY;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}
