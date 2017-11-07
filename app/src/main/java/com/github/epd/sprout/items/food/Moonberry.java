
package com.github.epd.sprout.items.food;

import com.github.epd.sprout.actors.buffs.Barkskin;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Hunger;
import com.github.epd.sprout.actors.buffs.Strength;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

public class Moonberry extends Food {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.SEED_MOONBERRY;
		energy = (Hunger.STARVING - Hunger.HUNGRY) / 10;
		message = Messages.get(Blackberry.class, "eat");
		bones = false;
	}

	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_EAT)) {

			switch (Random.Int(2)) {
				case 0:
					GLog.p(Messages.get(FullMoonberry.class, "effect"));
					Buff.affect(hero, Strength.class);
					break;
				case 1:
					GLog.p(Messages.get(FullMoonberry.class, "effect"));
					Buff.affect(hero, Strength.class);
					Buff.affect(hero, Barkskin.class).level(hero.HT);
					break;
			}
		}
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}

	@Override
	public int price() {
		return 20 * quantity;
	}

	public Moonberry() {
		this(1);
	}

	public Moonberry(int value) {
		this.quantity = value;
	}
}
