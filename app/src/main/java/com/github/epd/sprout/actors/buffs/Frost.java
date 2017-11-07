
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Thief;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.food.FrozenCarpaccio;
import com.github.epd.sprout.items.food.MysteryMeat;
import com.github.epd.sprout.items.potions.Potion;
import com.github.epd.sprout.items.rings.RingOfElements.Resistance;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.BuffIndicator;
import com.github.epd.sprout.utils.GLog;

public class Frost extends FlavourBuff {

	private static final float DURATION = 5f;

	@Override
	public boolean attachTo(Char target) {
		if (super.attachTo(target)) {

			target.paralysed++;
			Buff.detach(target, Burning.class);
			Buff.detach(target, Chill.class);

			if (target instanceof Hero) {

				Hero hero = (Hero) target;
				Item item = hero.belongings.randomUnequipped();
				if (item instanceof Potion) {

					item = item.detach(hero.belongings.backpack);
					GLog.w(Messages.get(this, "item"), item.toString());
					((Potion) item).shatter(hero.pos);

				} else if (item instanceof MysteryMeat) {

					item = item.detach(hero.belongings.backpack);
					FrozenCarpaccio carpaccio = new FrozenCarpaccio();
					if (!carpaccio.collect(hero.belongings.backpack)) {
						Dungeon.level.drop(carpaccio, target.pos).sprite.drop();
					}
					GLog.w(Messages.get(this, "item"), item.toString());

				}
			} else if (target instanceof Thief
					&& ((Thief) target).item instanceof Potion) {

				((Potion) ((Thief) target).item).shatter(target.pos);
				((Thief) target).item = null;

			}

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
		if (Level.water[target.pos]) {
			Buff.prolong(target, Chill.class, 6f);
		}
	}

	@Override
	public int icon() {
		return BuffIndicator.FROST;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	public static float duration(Char ch) {
		Resistance r = ch.buff(Resistance.class);
		return r != null ? r.durationFactor() * DURATION : DURATION;
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns());
	}
}
