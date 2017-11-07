
package com.github.epd.sprout.items.food;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.buffs.Barkskin;
import com.github.epd.sprout.actors.buffs.Bleeding;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Cripple;
import com.github.epd.sprout.actors.buffs.Drowsy;
import com.github.epd.sprout.actors.buffs.Hunger;
import com.github.epd.sprout.actors.buffs.Invisibility;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.actors.buffs.Slow;
import com.github.epd.sprout.actors.buffs.Vertigo;
import com.github.epd.sprout.actors.buffs.Weakness;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

public class FrozenCarpaccio extends Food {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.CARPACCIO;
		energy = Hunger.STARVING - Hunger.HUNGRY;
	}

	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_EAT)) {

			switch (Random.Int(5)) {
				case 0:
					if (Dungeon.depth != 29) {
						GLog.i(Messages.get(this, "invis"));
						Buff.affect(hero, Invisibility.class, Invisibility.DURATION);
					}
					break;
				case 1:
					GLog.i(Messages.get(this, "hard"));
					Buff.affect(hero, Barkskin.class).level(hero.HT / 4);
					break;
				case 2:
					GLog.i(Messages.get(this, "refresh"));
					Buff.detach(hero, Poison.class);
					Buff.detach(hero, Cripple.class);
					Buff.detach(hero, Weakness.class);
					Buff.detach(hero, Bleeding.class);
					Buff.detach(hero, Drowsy.class);
					Buff.detach(hero, Slow.class);
					Buff.detach(hero, Vertigo.class);
					break;
				case 3:
					GLog.i(Messages.get(this, "better"));
					if (hero.HP < hero.HT) {
						hero.HP = Math.min(hero.HP + hero.HT / 4, hero.HT);
						hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
					}
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
		return 10 * quantity;
	}

	public static Food cook(MysteryMeat ingredient) {
		FrozenCarpaccio result = new FrozenCarpaccio();
		result.quantity = ingredient.quantity();
		return result;
	}

	public static Food cook(Meat ingredient) {
		FrozenCarpaccio result = new FrozenCarpaccio();
		result.quantity = ingredient.quantity();
		return result;
	}
}
