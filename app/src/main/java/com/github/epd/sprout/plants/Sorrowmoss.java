
package com.github.epd.sprout.plants;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.PoisonParticle;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.potions.PotionOfToxicGas;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class Sorrowmoss extends Plant {

	private static final String TXT_DESC = Messages.get(Sorrowmoss.class, "desc");

	{
		image = 2;
		plantName = Messages.get(this, "name");
	}

	@Override
	public void activate(Char ch) {
		super.activate(ch);

		if (ch != null) {
			Buff.affect(ch, Poison.class).set(
					Poison.durationFactor(ch) * (4 + Dungeon.depth / 2));
		}

		if (Dungeon.visible[pos]) {
			CellEmitter.center(pos).burst(PoisonParticle.SPLASH, 3);
		}
		Heap heap = Dungeon.level.heaps.get(pos);
		if (heap != null) {
			heap.poison();
		}
	}

	@Override
	public String desc() {
		return TXT_DESC;
	}

	public static class Seed extends Plant.Seed {
		{
			plantName = Messages.get(Sorrowmoss.class, "name");

			name = Messages.get(this, "name");
			image = ItemSpriteSheet.SEED_SORROWMOSS;

			plantClass = Sorrowmoss.class;
			alchemyClass = PotionOfToxicGas.class;
		}

		@Override
		public String desc() {
			return Messages.get(Plant.class, "seeddesc", plantName);
		}
	}
}
