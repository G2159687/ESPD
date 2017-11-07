
package com.github.epd.sprout.items.food;

import com.github.epd.sprout.actors.buffs.Hunger;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

public class GoldenNut extends Nut {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.SEED_GOLDENDUNGEONNUT;
		energy = Hunger.STARVING;
		message = Messages.get(this, "eat");
	}

	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_EAT)) {

			switch (Random.Int(2)) {
				case 0:
					GLog.p(Messages.get(this, "effect1"));

					hero.HT += 20;
					hero.STR += 2;
					hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "effect2"));
					GLog.p(Messages.get(this, "effect3"));
					break;
				case 1:
					GLog.p(Messages.get(this, "effect4"));

					hero.HT += 50;
					hero.STR += 5;
					hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "effect5"));
					GLog.p(Messages.get(this, "effect3"));
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

}


