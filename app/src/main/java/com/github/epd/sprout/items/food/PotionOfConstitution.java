
package com.github.epd.sprout.items.food;

import com.github.epd.sprout.actors.buffs.Bleeding;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Cripple;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.actors.buffs.Weakness;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class PotionOfConstitution extends Food {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.POTION_HONEY;
		bones = true;
		message = Messages.get(this, "eat");
	}

	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_EAT)) {
			hero.HT = hero.HT + (Random.Int(5, 20));
			hero.HP = hero.HP + Math.min(((hero.HT - hero.HP) / 2), hero.HT - hero.HP);
			Buff.detach(hero, Poison.class);
			Buff.detach(hero, Cripple.class);
			Buff.detach(hero, Weakness.class);
			Buff.detach(hero, Bleeding.class);

			hero.sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f, 4);

		}
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}


	@Override
	public int price() {
		return 900 * quantity;
	}
}
