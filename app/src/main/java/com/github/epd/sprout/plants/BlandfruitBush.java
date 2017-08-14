package com.github.epd.sprout.plants;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.items.food.Blandfruit;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class BlandfruitBush extends Plant {

	private static final String TXT_DESC = Messages.get(BlandfruitBush.class,"desc");

	{
		image = 8;
		plantName = Messages.get(BlandfruitBush.class,"name");
	}

	@Override
	public void activate(Char ch) {
		super.activate(ch);

		Dungeon.level.drop(new Blandfruit(), pos).sprite.drop();
	}

	@Override
	public String desc() {
		return TXT_DESC;
	}

	public static class Seed extends Plant.Seed {
		{
			plantName = Messages.get(BlandfruitBush.class,"name");

			name = Messages.get(this,"name");
			image = ItemSpriteSheet.SEED_BLANDFRUIT;

			plantClass = BlandfruitBush.class;
			alchemyClass = null;
		}

		@Override
		public String desc() {
			return Messages.get(Plant.class,"seeddesc", plantName);
		}	}
}
