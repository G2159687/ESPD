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

import com.github.epd.sprout.actors.mobs.npcs.Tinkerer1;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.ui.NewRedButton;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.ui.Window;
import com.github.epd.sprout.utils.Utils;

public class WndTinkerer extends Window {

	private static final String TXT_MESSAGE = Messages.get(WndTinkerer.class, "msg");

	private static final String TXT_MESSAGE_WATER = Messages.get(WndTinkerer.class, "msgwater");


	private static final String TXT_MESSAGE_DRAW = Messages.get(WndDewDrawInfo.class, "msg1") + Messages.get(WndDewDrawInfo.class, "msg2") + Messages.get(WndDewDrawInfo.class, "msg3") + Messages.get(WndDewDrawInfo.class, "msg4");

	private static final String TXT_WATER = Messages.get(WndTinkerer.class, "water");
	private static final String TXT_DRAW = Messages.get(WndTinkerer.class, "draw");
	private static final String TXT_DRAW_INFO = Messages.get(WndTinkerer.class, "info");

	private static final String TXT_FARAWELL = Messages.get(WndTinkerer.class, "farewell");
	private static final String TXT_FARAWELL_DRAW = Messages.get(WndTinkerer.class, "farewelldraw");


	private static final int WIDTH = 120;
	private static final int BTN_HEIGHT = 20;
	private static final float GAP = 2;

	public WndTinkerer(final Tinkerer1 tinkerer, final Item item) {

		super();

		IconTitle titlebar = new IconTitle();
		titlebar.icon(new ItemSprite(item.image(), null));
		titlebar.label(Utils.capitalize(item.name()));
		titlebar.setRect(0, 0, WIDTH, 0);
		add(titlebar);

		RenderedTextMultiline message = PixelScene
				.renderMultiline(TXT_MESSAGE, 6);
		message.maxWidth(WIDTH);
		message.setPos(0, titlebar.bottom() + GAP);
		add(message);

		NewRedButton btnBattle = new NewRedButton(TXT_WATER) {
			@Override
			protected void onClick() {
				selectUpgrade(tinkerer, 1);
			}
		};
		btnBattle.setRect(0, message.top() + message.height() + GAP, WIDTH,
				BTN_HEIGHT);
		add(btnBattle);

		/*
	    BitmapTextMultiline message_draw = PixelScene
				.createMultiline(TXT_MESSAGE_DRAW, 6);
		message_draw.maxWidth = WIDTH;
		message_draw.measure();
		message_draw.y = btnBattle.bottom() + GAP;
		add(message_draw);
		*/

		NewRedButton btnNonBattle = new NewRedButton(TXT_DRAW) {
			@Override
			protected void onClick() {
				selectUpgrade(tinkerer, 2);
			}
		};

		btnNonBattle.setRect(0, btnBattle.bottom() + GAP, WIDTH, BTN_HEIGHT);
		add(btnNonBattle);

		NewRedButton btnNonBattle2 = new NewRedButton(TXT_DRAW_INFO) {
			@Override
			protected void onClick() {
				GameScene.show(new WndDewDrawInfo(item));
			}
		};
		btnNonBattle2.setRect(0, btnNonBattle.bottom() + GAP, WIDTH, BTN_HEIGHT);
		add(btnNonBattle2);

		resize(WIDTH, (int) btnNonBattle2.bottom());
	}

	private void selectUpgrade(Tinkerer1 tinkerer, int type) {

		hide();

		tinkerer.destroy();

		tinkerer.sprite.die();
	}
}
