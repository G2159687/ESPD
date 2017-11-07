
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

public class PotionOfMending extends Potion {

	{
		name = Messages.get(this, "name");
		initials = 12;
		bones = true;
	}

	@Override
	public void apply(Hero hero) {
		setKnown();
		heal(Dungeon.hero);
		GLog.p(Messages.get(this, "effect"));
	}

	public static void heal(Hero hero) {

		if (Dungeon.hero.HP < Dungeon.hero.HT) {
			hero.HP = hero.HP + Math.min((hero.HT / 4), hero.HT - hero.HP);
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
		return isKnown() ? 20 * quantity : super.price();
	}
}
