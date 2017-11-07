
package com.github.epd.sprout.items.food;

import com.github.epd.sprout.actors.buffs.Barkskin;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Hunger;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

public class ToastedNut extends Nut {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.SEED_TOASTEDDUNGEONNUT;
		energy = Hunger.STARVING - Hunger.HUNGRY;
		message = Messages.get(this, "eat");
	}

	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_EAT)) {

			switch (Random.Int(2)) {
				case 0:
					GLog.i(Messages.get(this, "effect"));
					Buff.affect(hero, Barkskin.class).level(hero.HT / 2);
					break;
				case 1:
					GLog.i(Messages.get(this, "effect"));
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

	public static Food cook(Nut ingredient) {
		ToastedNut result = new ToastedNut();
		result.quantity = ingredient.quantity();
		return result;
	}
}


