
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.items.rings.RingOfElements.Resistance;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.BuffIndicator;

public class Paralysis extends FlavourBuff {

	private static final float DURATION = 10f;

	@Override
	public boolean attachTo(Char target) {
		if (super.attachTo(target)) {
			target.paralysed++;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void detach() {
		super.detach();
		if (target.paralysed > 0)
			target.paralysed--;
	}

	@Override
	public int icon() {
		return BuffIndicator.PARALYSIS;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns());
	}

	public static float duration(Char ch) {
		Resistance r = ch.buff(Resistance.class);
		return r != null ? r.durationFactor() * DURATION : DURATION;
	}
}
