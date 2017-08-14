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

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.mobs.pets.PET;
import com.github.epd.sprout.items.rings.RingOfHaste;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.ui.NewRedButton;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.ui.Window;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;

public class WndPetHaste extends Window {
	
	//if people don't get it after this, I quit. I just quit.

	private static final String TXT_MESSAGE = Messages.get(WndPetHaste.class,"msg");
	
	private static final String TXT_YES = Messages.get(WndPetHaste.class,"yes");
	private static final String TXT_NO = Messages.get(WndPetHaste.class,"no");


	private static final int WIDTH = 120;
	private static final int BTN_HEIGHT = 20;
	private static final float GAP = 2;

	public WndPetHaste(final PET pet, final RingOfHaste ring) {

		super();

		IconTitle titlebar = new IconTitle();
		titlebar.icon(pet.sprite());
		titlebar.label(Utils.capitalize(pet.name));
		titlebar.setRect(0, 0, WIDTH, 0);
		add(titlebar);

		RenderedTextMultiline message = PixelScene
				.renderMultiline(TXT_MESSAGE, 6);
		message.maxWidth(WIDTH);
		message.setPos(0,titlebar.bottom() + GAP);
		add(message);

		NewRedButton btnBattle = new NewRedButton(TXT_YES) {
			@Override
			protected void onClick() {
				Dungeon.petHasteLevel=ring.level;
				GLog.p(Messages.get(WndPetHaste.class,"faster", pet.name));
				hide();
			}
		};
		btnBattle.setRect(0, message.top() + message.height() + GAP, WIDTH,
				BTN_HEIGHT);
		add(btnBattle);

		NewRedButton btnNonBattle = new NewRedButton(TXT_NO) {
			@Override
			protected void onClick() {
				hide();
			}
		};
		
		btnNonBattle.setRect(0, btnBattle.bottom() + GAP, WIDTH, BTN_HEIGHT);
		add(btnNonBattle);
		
		
		resize(WIDTH, (int) btnNonBattle.bottom());
	}

	
	
	
}
