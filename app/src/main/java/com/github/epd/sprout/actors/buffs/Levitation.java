
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.BuffIndicator;

public class Levitation extends FlavourBuff {

	public static final float DURATION = 20f;

	@Override
	public boolean attachTo(Char target) {
		if (super.attachTo(target)) {
			target.flying = true;
			Buff.detach(target, Roots.class);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void detach() {
		target.flying = false;
		Dungeon.level.press(target.pos, target);
		super.detach();
	}

	@Override
	public int icon() {
		return BuffIndicator.LEVITATION;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns());
	}
}
