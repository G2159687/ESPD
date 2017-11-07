
package com.github.epd.sprout.items.potions;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.MindVision;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.items.misc.Spectacles.MagicSight;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.utils.GLog;

public class PotionOfMindVision extends Potion {

	{
		initials = 7;
		name = Messages.get(this, "name");
	}

	private static final String TXT_PREVENTING = Messages.get(PotionOfMindVision.class, "prevent");

	@Override
	public void apply(Hero hero) {
		setKnown();

		if (Dungeon.level.locked && Dungeon.depth > 50 && Dungeon.hero.buff(MagicSight.class) == null) {
			GLog.w(TXT_PREVENTING);
			return;
		}

		Buff.affect(hero, MindVision.class, Dungeon.hero.buff(MagicSight.class) != null ? MindVision.DURATION * 4 : MindVision.DURATION);
		Dungeon.observe();

		if (Dungeon.level.mobs.size() > 0) {
			GLog.i(Messages.get(this, "see_mobs"));
		} else {
			GLog.i(Messages.get(this, "see_none"));
		}
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	@Override
	public int price() {
		return isKnown() ? 35 * quantity : super.price();
	}
}
