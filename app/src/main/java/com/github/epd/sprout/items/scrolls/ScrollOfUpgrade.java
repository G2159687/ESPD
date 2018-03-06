
package com.github.epd.sprout.items.scrolls;

import com.github.epd.sprout.Challenges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.artifacts.Artifact;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.windows.WndBag;

public class ScrollOfUpgrade extends InventoryScroll {

	{
		initials = 11;
		name = Messages.get(this, "name");
		inventoryTitle = Messages.get(this, "inv_title");
		mode = WndBag.Mode.UPGRADEABLE;
		consumedValue = 15;

		bones = true;
	}

	@Override
	protected void onItemSelected(Item item) {

		ScrollOfRemoveCurse.uncurse(Dungeon.hero, item);

		if (item instanceof Artifact) {
			if (item.level < ((Artifact) item).levelCap || item.level >= 50) {
				GLog.w(Messages.get(this, "cannot"));
				new ScrollOfUpgrade().collect();
				return;
			}
		}

		if (Dungeon.upgradeTweaks) {
			item.upgrade(10);
		} else {
			item.upgrade();
		}

		upgrade(curUser);

		if (item instanceof Artifact && item.level > 50){
			item.level = 50;
		}
	}

	public static void upgrade(Hero hero) {
		hero.sprite.emitter().start(Speck.factory(Speck.UP), 0.2f, 3);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}


	@Override
	public int price() {
		return 100 * quantity;
	}
}
