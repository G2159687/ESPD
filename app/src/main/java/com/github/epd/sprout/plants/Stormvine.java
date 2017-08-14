package com.github.epd.sprout.plants;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Vertigo;
import com.github.epd.sprout.items.potions.PotionOfLevitation;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class Stormvine extends Plant {

	private static final String TXT_DESC = Messages.get(Stormvine.class,"desc");

	{
		image = 9;
		plantName = Messages.get(Stormvine.class,"name");
	}

	@Override
	public void activate(Char ch) {
		super.activate(ch);

		if (ch != null) {
			Buff.affect(ch, Vertigo.class, Vertigo.duration(ch));
		}
	}

	@Override
	public String desc() {
		return TXT_DESC;
	}

	public static class Seed extends Plant.Seed {
		{
			plantName = Messages.get(Stormvine.class,"name");

			name = Messages.get(this,"name");
			image = ItemSpriteSheet.SEED_STORMVINE;

			plantClass = Stormvine.class;
			alchemyClass = PotionOfLevitation.class;
		}

		@Override
		public String desc() {
			return Messages.get(Plant.class,"seeddesc", plantName);
		}
	}
}
