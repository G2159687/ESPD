
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.items.rings.RingOfElements.Resistance;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class Charm extends FlavourBuff {

	public int object = 0;

	private static final String OBJECT = "object";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(OBJECT, object);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		object = bundle.getInt(OBJECT);
	}

	@Override
	public int icon() {
		return BuffIndicator.HEART;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	public static float durationFactor(Char ch) {
		Resistance r = ch.buff(Resistance.class);
		return r != null ? r.durationFactor() : 1;
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns());
	}
}
