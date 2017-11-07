
package com.github.epd.sprout.plants;


import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Strength;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.items.potions.PotionOfExperience;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Starflower extends Plant {

	private static final String TXT_DESC = Messages.get(Starflower.class, "desc");

	{
		image = 11;
		plantName = Messages.get(Starflower.class, "name");
	}

	@Override
	public void activate(Char ch) {
		if (ch == null) {
			super.activate(ch);
		}
		if (ch instanceof Hero) {
			Buff.affect(ch, Strength.class);
		}
		if (Random.Int(3) == 0) {
			super.activate(ch);
		}
	}

	@Override
	public String desc() {
		return TXT_DESC;
	}

	public static class Seed extends Plant.Seed {

		{
			plantName = Messages.get(Starflower.class, "name");

			name = Messages.get(this, "name");
			image = ItemSpriteSheet.SEED_STARFLOWER;

			plantClass = Starflower.class;
			alchemyClass = PotionOfExperience.class;
		}

		@Override
		public String desc() {
			return Messages.get(Plant.class, "seeddesc", plantName);
		}
	}
}
