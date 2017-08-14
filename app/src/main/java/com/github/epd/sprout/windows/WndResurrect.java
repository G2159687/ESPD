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

import com.github.epd.sprout.Rankings;
import com.github.epd.sprout.Statistics;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.items.Ankh;
import com.github.epd.sprout.scenes.InterlevelScene;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.ui.NewRedButton;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.ui.Window;
import com.watabou.noosa.Game;

public class WndResurrect extends Window {

	private static final String TXT_MESSAGE = "You died, but you were given another chance to win this dungeon. Will you take it?";
	private static final String TXT_YES = "Yes, I will fight!";
	private static final String TXT_NO = "No, I give up";

	private static final int WIDTH = 120;
	private static final int BTN_HEIGHT = 20;
	private static final float GAP = 2;

	public static WndResurrect instance;
	public static Object causeOfDeath;

	public WndResurrect(final Ankh ankh, Object causeOfDeath) {

		super();

		instance = this;
		WndResurrect.causeOfDeath = causeOfDeath;

		IconTitle titlebar = new IconTitle();
		titlebar.icon(new ItemSprite(ankh.image(), null));
		titlebar.label(ankh.name());
		titlebar.setRect(0, 0, WIDTH, 0);
		add(titlebar);

		RenderedTextMultiline message = PixelScene
				.renderMultiline(TXT_MESSAGE, 6);
		message.maxWidth(WIDTH);
		message.setPos(0, titlebar.bottom() + GAP);
		add(message);

		NewRedButton btnYes = new NewRedButton(TXT_YES) {
			@Override
			protected void onClick() {
				hide();

				Statistics.ankhsUsed++;

				InterlevelScene.mode = InterlevelScene.Mode.RESURRECT;
				Game.switchScene(InterlevelScene.class);
			}
		};
		btnYes.setRect(0, message.top() + message.height() + GAP, WIDTH, BTN_HEIGHT);
		add(btnYes);

		NewRedButton btnNo = new NewRedButton(TXT_NO) {
			@Override
			protected void onClick() {
				hide();

				Rankings.INSTANCE.submit(false);
				Hero.reallyDie(WndResurrect.causeOfDeath);
			}
		};
		btnNo.setRect(0, btnYes.bottom() + GAP, WIDTH, BTN_HEIGHT);
		add(btnNo);

		resize(WIDTH, (int) btnNo.bottom());
	}

	@Override
	public void destroy() {
		super.destroy();
		instance = null;
	}

	@Override
	public void onBackPressed() {
	}
}
