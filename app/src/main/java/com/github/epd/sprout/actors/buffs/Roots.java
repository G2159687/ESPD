
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.BuffIndicator;

public class Roots extends FlavourBuff {

	@Override
	public boolean attachTo(Char target) {
		if (!target.flying && super.attachTo(target)) {
			target.rooted = true;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void detach() {
		target.rooted = false;
		super.detach();
	}

	@Override
	public int icon() {
		return BuffIndicator.ROOTS;
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
