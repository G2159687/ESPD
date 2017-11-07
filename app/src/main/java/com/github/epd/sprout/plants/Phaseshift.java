
package com.github.epd.sprout.plants;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.Blob;
import com.github.epd.sprout.actors.blobs.WaterOfTransmutation;
import com.github.epd.sprout.actors.blobs.WellWater;
import com.github.epd.sprout.items.potions.PotionOfMight;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class Phaseshift extends Plant {

	private static final String TXT_DESC = Messages.get(Phaseshift.class, "desc");

	{
		image = 14;
		plantName = Messages.get(this, "name");
	}

	@Override
	public void activate(Char ch) {
		if (ch == null) {
			if (WellWater.affectCellPlant(pos)) {
				super.activate(null);
			}
		}
	}

	@Override
	public String desc() {
		return TXT_DESC;
	}

	public static class Seed extends Plant.Seed {
		{
			plantName = Messages.get(Phaseshift.class, "name");

			name = Messages.get(this, "name");
			image = ItemSpriteSheet.SEED_PHASEPITCHER;

			plantClass = Phaseshift.class;
			alchemyClass = PotionOfMight.class;
		}

		@Override
		public String desc() {
			return Messages.get(Plant.class, "seeddesc", plantName);
		}

		@Override
		public Plant couch(int pos) {
			GameScene.add(Blob.seed(pos, 1, WaterOfTransmutation.class));
			return super.couch(pos);
		}
	}


	public static boolean checkWater() {

		WellWater water = (WellWater) Dungeon.level.blobs.get(WaterOfTransmutation.class);
		if (water == null) {
			return false;
		} else return !(water != null && water.volume == 0);
	}
}
