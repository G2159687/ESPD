
package com.github.epd.sprout.plants;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Blindness;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Cripple;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.potions.PotionOfInvisibility;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Blindweed extends Plant {

	private static final String TXT_DESC = Messages.get(Blindweed.class, "desc");

	{
		image = 3;
		plantName = Messages.get(this, "name");
	}

	@Override
	public void activate(Char ch) {
		super.activate(ch);

		if (ch != null) {
			int len = Random.Int(5, 10);
			Buff.prolong(ch, Blindness.class, len);
			Buff.prolong(ch, Cripple.class, len);
			if (ch instanceof Mob) {
				if (((Mob) ch).state == ((Mob) ch).HUNTING) ((Mob) ch).state = ((Mob) ch).WANDERING;
				((Mob) ch).beckon(Dungeon.level.randomDestination());
			}
		}

		if (Dungeon.visible[pos]) {
			CellEmitter.get(pos).burst(Speck.factory(Speck.LIGHT), 4);
		}
	}

	@Override
	public String desc() {
		return TXT_DESC;
	}

	public static class Seed extends Plant.Seed {
		{
			plantName = Messages.get(Blindweed.class, "name");

			name = Messages.get(this, "name");
			image = ItemSpriteSheet.SEED_BLINDWEED;

			plantClass = Blindweed.class;
			alchemyClass = PotionOfInvisibility.class;
		}

		@Override
		public String desc() {
			return Messages.get(Plant.class, "seeddesc", plantName);
		}
	}
}
