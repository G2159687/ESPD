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
package com.github.epd.sprout.windows;

import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.ui.ItemSlot;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.ui.Window;
import com.github.epd.sprout.utils.Utils;

public class WndInfoItem extends Window {

	private static final float GAP = 2;

	private static final int WIDTH = 120;

	public WndInfoItem(Heap heap) {

		super();

		if (heap.type == Heap.Type.HEAP || heap.type == Heap.Type.FOR_SALE) {

			Item item = heap.peek();

			int color = TITLE_COLOR;
			if (item.levelKnown && item.level > 0) {
				color = ItemSlot.UPGRADED;
			} else if (item.levelKnown && item.level < 0) {
				color = ItemSlot.DEGRADED;
			}
			fillFields(item.image(), item.glowing(), color, item.toString(),
					item.info());

		} else {

			fillFields(heap.image(), heap.glowing(), TITLE_COLOR, heap.toString(), heap.info());

		}
	}

	public WndInfoItem(Item item) {

		super();

		int color = TITLE_COLOR;
		if (item.levelKnown && item.level > 0) {
			color = ItemSlot.UPGRADED;
		} else if (item.levelKnown && item.level < 0) {
			color = ItemSlot.DEGRADED;
		}

		fillFields(item.image(), item.glowing(), color, item.toString(),
				item.info());
	}

	private void fillFields(int image, ItemSprite.Glowing glowing,
	                        int titleColor, String title, String info) {

		IconTitle titlebar = new IconTitle();
		titlebar.icon(new ItemSprite(image, glowing));
		titlebar.label(Utils.capitalize(title), titleColor);
		titlebar.setRect(0, 0, WIDTH, 0);
		add(titlebar);

		RenderedTextMultiline txtInfo = PixelScene.renderMultiline(info, 6);
		txtInfo.maxWidth(WIDTH);
		txtInfo.setPos(titlebar.left(), titlebar.bottom() + GAP);
		add(txtInfo);

		resize(WIDTH, (int) (txtInfo.top() + txtInfo.height()));
	}
}
