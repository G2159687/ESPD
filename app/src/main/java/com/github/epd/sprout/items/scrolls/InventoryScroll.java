/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.github.epd.sprout.items.scrolls;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.actors.buffs.Invisibility;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.windows.WndBag;
import com.github.epd.sprout.windows.WndOptions;
import com.watabou.noosa.audio.Sample;

public abstract class InventoryScroll extends Scroll {

	protected String inventoryTitle = Messages.get(InventoryScroll.class, "title");
	protected WndBag.Mode mode = WndBag.Mode.ALL;

	private static final String TXT_WARNING = Messages.get(InventoryScroll.class, "warning");
	private static final String TXT_YES = Messages.get(InventoryScroll.class, "yes");
	private static final String TXT_NO = Messages.get(InventoryScroll.class, "no");

	@Override
	protected void doRead() {

		if (!isKnown()) {
			setKnown();
			identifiedByUse = true;
		} else {
			identifiedByUse = false;
		}

		GameScene.selectItem(itemSelector, mode, inventoryTitle);
	}

	private void confirmCancelation() {
		GameScene.show(new WndOptions(name(), TXT_WARNING, TXT_YES, TXT_NO) {
			@Override
			protected void onSelect(int index) {
				switch (index) {
					case 0:
						curUser.spendAndNext(TIME_TO_READ);
						identifiedByUse = false;
						break;
					case 1:
						GameScene.selectItem(itemSelector, mode, inventoryTitle);
						break;
				}
			}

			@Override
			public void onBackPressed() {
			}
		});
	}

	protected abstract void onItemSelected(Item item);

	protected static boolean identifiedByUse = false;
	protected static WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect(Item item) {
			if (item != null) {

				((InventoryScroll) curItem).onItemSelected(item);
				curUser.spendAndNext(TIME_TO_READ);

				Sample.INSTANCE.play(Assets.SND_READ);
				Invisibility.dispel();

			} else if (identifiedByUse && !((Scroll) curItem).ownedByBook) {

				((InventoryScroll) curItem).confirmCancelation();

			} else if (!((Scroll) curItem).ownedByBook) {

				curItem.collect(curUser.belongings.backpack);

			}
		}
	};
}
