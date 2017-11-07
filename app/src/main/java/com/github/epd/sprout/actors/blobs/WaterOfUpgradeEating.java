
package com.github.epd.sprout.actors.blobs;

import com.github.epd.sprout.effects.BlobEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Generator.Category;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.Stylus;
import com.github.epd.sprout.items.UpgradeBlobRed;
import com.github.epd.sprout.items.UpgradeBlobViolet;
import com.github.epd.sprout.items.UpgradeBlobYellow;
import com.github.epd.sprout.items.potions.Potion;
import com.github.epd.sprout.items.scrolls.Scroll;
import com.github.epd.sprout.messages.Messages;
import com.watabou.utils.Random;

public class WaterOfUpgradeEating extends WellWater {

	@Override
	protected Item affectItem(Item item) {

		if (item.isUpgradable()) {
			item = eatUpgradable(item);
		} else if (item instanceof Scroll
				|| item instanceof Potion
				|| item instanceof Stylus) {
			item = eatStandard(item);
		} else {
			item = null;
		}

		return item;

	}

	@Override
	public void use(BlobEmitter emitter) {
		super.use(emitter);
		emitter.start(Speck.factory(Speck.CHANGE), 0.2f, 0);
	}

	private Item eatUpgradable(Item w) {

		int ups = w.level;

		Item n = null;

		if (Random.Float() < (ups / 10)) {

			n = new UpgradeBlobViolet();

		} else if (Random.Float() < (ups / 5)) {

			n = new UpgradeBlobRed();

		} else if (Random.Float() < (ups / 3)) {

			n = new UpgradeBlobYellow();

		} else {

			n = Generator.random(Category.SEEDRICH);
		}

		return n;
	}

	private Item eatStandard(Item w) {

		Item n = null;

		if (Random.Float() < 0.1f) {
			n = new UpgradeBlobYellow();
		} else {
			n = Generator.random(Category.SEEDRICH);
		}

		return n;
	}

	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}
}
