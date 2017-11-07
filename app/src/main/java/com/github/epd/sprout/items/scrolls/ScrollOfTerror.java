
package com.github.epd.sprout.items.scrolls;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Invisibility;
import com.github.epd.sprout.actors.buffs.Terror;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.effects.Flare;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfTerror extends Scroll {

	{
		initials = 10;
		name = Messages.get(this, "name");
		consumedValue = 5;
	}

	@Override
	protected void doRead() {

		new Flare(5, 32).color(0xFF0000, true).show(curUser.sprite, 2f);
		Sample.INSTANCE.play(Assets.SND_READ);
		Invisibility.dispel();

		int count = 0;
		Mob affected = null;
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
			if (Level.fieldOfView[mob.pos]) {
				Buff.affect(mob, Terror.class, Terror.DURATION).object = curUser
						.id();

				if (mob.buff(Terror.class) != null) {
					count++;
					affected = mob;
				}
			}
		}

		switch (count) {
			case 0:
				GLog.i(Messages.get(this, "none"));
				break;
			case 1:
				GLog.i(Messages.get(this, "one", affected.name));
				break;
			default:
				GLog.i(Messages.get(this, "many"));
		}
		setKnown();

		curUser.spendAndNext(TIME_TO_READ);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	@Override
	public int price() {
		return isKnown() ? 50 * quantity : super.price();
	}
}
