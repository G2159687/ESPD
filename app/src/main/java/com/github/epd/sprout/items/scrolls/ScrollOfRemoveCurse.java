
package com.github.epd.sprout.items.scrolls;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Invisibility;
import com.github.epd.sprout.actors.buffs.Weakness;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.effects.Flare;
import com.github.epd.sprout.effects.particles.ShadowParticle;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.bags.Bag;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;

public class ScrollOfRemoveCurse extends Scroll {

	private static final String TXT_PROCCED = Messages.get(ScrollOfRemoveCurse.class, "cleansed");
	private static final String TXT_NOT_PROCCED = Messages.get(ScrollOfRemoveCurse.class, "not_cleansed");

	{
		initials = 8;
		name = Messages.get(this, "name");
		consumedValue = 15;
	}

	@Override
	protected void doRead() {

		new Flare(6, 32).show(curUser.sprite, 2f);
		Sample.INSTANCE.play(Assets.SND_READ);
		Invisibility.dispel();

		boolean procced = uncurse(curUser,
				curUser.belongings.backpack.items.toArray(new Item[0]));
		procced = uncurse(curUser, curUser.belongings.weapon,
				curUser.belongings.armor, curUser.belongings.misc1,
				curUser.belongings.misc2, curUser.belongings.misc3,
				curUser.belongings.misc4)
				|| procced;

		Buff.detach(curUser, Weakness.class);

		if (procced) {
			GLog.p(TXT_PROCCED);
		} else {
			GLog.i(TXT_NOT_PROCCED);
		}

		setKnown();

		curUser.spendAndNext(TIME_TO_READ);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	public static boolean uncurse(Hero hero, Item... items) {

		boolean procced = false;
		for (int i = 0; i < items.length; i++) {
			Item item = items[i];
			if (item != null && item.cursed) {
				item.uncurse();
				if (item.level < 0) {
					item.upgrade((-item.level) * 2);
				} //upgrade to reverse of negatives
				if (!item.cursed) {
					procced = true;
				}
			}
			if (item instanceof Bag) {
				for (Item bagItem : ((Bag) item).items) {
					if (bagItem != null && bagItem.cursed) {
						bagItem.uncurse();
						if (bagItem.level < 0) {
							bagItem.upgrade((-bagItem.level) * 2);
						}
						if (!bagItem.cursed) {
							procced = true;
						}
					}
				}
			}
		}

		if (procced) {
			hero.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);
		}

		return procced;
	}

	@Override
	public int price() {
		return isKnown() ? 30 * quantity : super.price();
	}
}
