
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.BuffIndicator;
import com.github.epd.sprout.utils.GLog;

public class Combo extends Buff {

	private static String TXT_COMBO = Messages.get(Combo.class, "combo");

	public int count = 0;

	@Override
	public int icon() {
		return BuffIndicator.COMBO;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	public int hit(Char enemy, int damage) {

		count++;

		if (count >= 3) {
			GLog.p(TXT_COMBO, count);
			postpone(1.41f - count / 10f);
			return (int) (damage * (count - 2) / 5f);

		} else {

			postpone(1.1f);
			return 0;

		}
	}

	@Override
	public boolean act() {
		detach();
		return true;
	}

}
