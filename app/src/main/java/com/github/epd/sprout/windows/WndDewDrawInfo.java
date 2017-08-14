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

import com.github.epd.sprout.items.DewVial;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.ui.NewRedButton;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.ui.Window;
import com.github.epd.sprout.utils.Utils;

public class WndDewDrawInfo extends Window {
	
	//if people don't get it after this, I quit. I just quit.

	private static final String TXT_MESSAGE = Messages.get(WndDewDrawInfo.class,"msg1");
	
	private static final String TXT_MESSAGE2 = Messages.get(WndDewDrawInfo.class,"msg2");
	
	private static final String TXT_MESSAGE3 = Messages.get(WndDewDrawInfo.class,"msg3");

	private static final String TXT_MESSAGE4 = Messages.get(WndDewDrawInfo.class,"msg4");
	
	private static final String TXT_WATER = Messages.get(WndDewDrawInfo.class,"ok");


	private static final int WIDTH = 120;
	private static final int BTN_HEIGHT = 20;
	private static final float GAP = 2;

	public WndDewDrawInfo(final Item item) {

		super();
		
		Item dewvial = new DewVial();

		IconTitle titlebar = new IconTitle();
		titlebar.icon(new ItemSprite(dewvial.image(), null));
		titlebar.label(Messages.get(this,"title"));
		titlebar.setRect(0, 0, WIDTH, 0);
		add(titlebar);

		RenderedTextMultiline message = PixelScene.renderMultiline(TXT_MESSAGE, 6);
		message.maxWidth(WIDTH);
		message.setPos(0,titlebar.bottom() + GAP);
		add(message);

		RenderedTextMultiline message2 = PixelScene.renderMultiline(TXT_MESSAGE2, 6);
		message2.maxWidth(WIDTH);
		message2.setPos(0,message.top() + message.height() + GAP);
		add(message2);

		RenderedTextMultiline message3 = PixelScene.renderMultiline(TXT_MESSAGE3, 6);
		message3.maxWidth(WIDTH);
		message3.setPos(0, message2.top() + message2.height() + GAP);
		add(message3);
		
		RenderedTextMultiline message4 = PixelScene.renderMultiline(TXT_MESSAGE4, 6);
		message4.maxWidth(WIDTH);
		message4.setPos(0,message3.top() + message3.height() + GAP);
		add(message4);
		
		
		NewRedButton btnBattle = new NewRedButton(TXT_WATER) {
			@Override
			protected void onClick() {
				hide();
			}
		};
		btnBattle.setRect(0, message4.top() + message4.height() + GAP, WIDTH,
				BTN_HEIGHT);
		add(btnBattle);

		

		resize(WIDTH, (int) btnBattle.bottom());
	}

	
}
