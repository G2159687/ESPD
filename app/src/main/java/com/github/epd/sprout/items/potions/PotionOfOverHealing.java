
package com.github.epd.sprout.items.potions;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.buffs.Bleeding;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Cripple;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.actors.buffs.Weakness;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.utils.GLog;

public class PotionOfOverHealing extends Potion {

	{
		name = Messages.get(this, "name");
		initials = 13;
		bones = true;
	}

	@Override
	public void apply(Hero hero) {
		setKnown();
		heal(Dungeon.hero);
		//GLog.p("Your wounds heal completely.");
	}

	public static void heal(Hero hero) {

		if (Dungeon.hero.HP < hero.HT + (hero.lvl * 2)) {
			hero.HP = hero.HT + (hero.lvl * 2);
			GLog.p(Messages.get(PotionOfOverHealing.class, "effect"));
			GLog.p(Messages.get(PotionOfOverHealing.class, "fill", hero.HP - hero.HT));

			hero.sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f, 4);
		}

		Buff.detach(hero, Poison.class);
		Buff.detach(hero, Cripple.class);
		Buff.detach(hero, Weakness.class);
		Buff.detach(hero, Bleeding.class);


	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	@Override
	public int price() {
		return isKnown() ? 30 * quantity : super.price();
	}
}
