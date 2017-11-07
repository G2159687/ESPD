
package com.github.epd.sprout.items.scrolls;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.windows.WndBag;

public class ScrollOfMultiUpgrade extends InventoryScroll {

	{
		name = Messages.get(this, "name");
		inventoryTitle = Messages.get(this, "title");
		mode = WndBag.Mode.UPGRADEABLE;
		consumedValue = 10;

		bones = true;
	}

	@Override
	protected void onItemSelected(Item item) {

		ScrollOfRemoveCurse.uncurse(Dungeon.hero, item);
		item.upgrade();

		for (int i = 1; i < 6; i++) {
			upgrade(curUser);
		}
	}

	public static void upgrade(Hero hero) {
		hero.sprite.emitter().start(Speck.factory(Speck.UP), 0.2f, 3);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}
