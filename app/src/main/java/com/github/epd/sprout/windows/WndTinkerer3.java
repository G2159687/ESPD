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

import com.github.epd.sprout.actors.mobs.npcs.Tinkerer3;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.ui.NewRedButton;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.ui.Window;
import com.github.epd.sprout.utils.Utils;

public class WndTinkerer3 extends Window {

	private static final int WIDTH = 120;
	private static final int BTN_HEIGHT = 20;
	private static final float GAP = 2;

	public WndTinkerer3(final Tinkerer3 tinkerer, final Item item) {

		super();

		IconTitle titlebar = new IconTitle();
		titlebar.icon(new ItemSprite(item.image(), null));
		titlebar.label(Utils.capitalize(item.name()));
		titlebar.setRect(0, 0, WIDTH, 0);
		add(titlebar);

		RenderedTextMultiline message = PixelScene
				.renderMultiline("", 6);
		message.maxWidth(WIDTH);
		message.setPos(0, titlebar.bottom() + GAP);
		add(message);

		NewRedButton btnUpgrade = new NewRedButton("") {
			@Override
			protected void onClick() {
				selectUpgrade(tinkerer);
			}
		};
		btnUpgrade.setRect(0, message.top() + message.height() + GAP, WIDTH,
				BTN_HEIGHT);
		add(btnUpgrade);


		resize(WIDTH, (int) btnUpgrade.bottom());
	}

	private void selectUpgrade(Tinkerer3 tinkerer) {
		hide();
		tinkerer.destroy();
		tinkerer.sprite.die();
	}
}
