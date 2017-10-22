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

import com.github.epd.sprout.Challenges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.npcs.Ghost;
import com.github.epd.sprout.actors.mobs.npcs.Wandmaker;
import com.github.epd.sprout.items.AdamantWand;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.quest.CorpseDust;
import com.github.epd.sprout.items.wands.Wand;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.PixelScene;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.ui.NewRedButton;
import com.github.epd.sprout.ui.RenderedTextMultiline;
import com.github.epd.sprout.ui.Window;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;

public class WndWandmaker extends Window {

	private static final String TXT_MESSAGE = Messages.get(WndWandmaker.class, "msg");
	private static final String TXT_BATTLE = Messages.get(WndWandmaker.class, "battle");
	private static final String TXT_NON_BATTLE = Messages.get(WndWandmaker.class, "nonbattle");

	private static final String TXT_ADAMANT = Messages.get(WndWandmaker.class, "adamant");
	private static final String TXT_WOW = Messages.get(WndWandmaker.class, "wow");
	private static final String TXT_FARAWELL = Messages.get(WndWandmaker.class, "farewell");

	private static final int WIDTH = 120;
	private static final int BTN_HEIGHT = 20;
	private static final float GAP = 2;

	public WndWandmaker(final Wandmaker wandmaker, final Item item) {

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

		NewRedButton btnWand1 = new NewRedButton(Wandmaker.Quest.wand1.name()) {
			@Override
			protected void onClick() {
				selectReward(wandmaker, item, Wandmaker.Quest.wand1);
			}
		};
		btnWand1.setRect(0, message.top() + message.height() + GAP, WIDTH,
				BTN_HEIGHT);
		add(btnWand1);

		NewRedButton btnWand2 = new NewRedButton(Wandmaker.Quest.wand2.name()) {
			@Override
			protected void onClick() {
				selectReward(wandmaker, item, Wandmaker.Quest.wand2);
			}
		};
		btnWand2.setRect(0, btnWand1.bottom() + GAP, WIDTH, BTN_HEIGHT);
		add(btnWand2);

		resize(WIDTH, (int) btnWand2.bottom());
	}

	private void selectReward(Wandmaker wandmaker, Item item, Wand reward) {

		hide();

		if (item != null && item == Dungeon.hero.belongings.getItem(CorpseDust.class)) {
			if (!item.cursed) {
				wandmaker.yell(Messages.get(this, "uncursed"));
				Dungeon.level.drop(new AdamantWand(), wandmaker.pos).sprite.drop();
			}
		} else if (item != null && item == Dungeon.hero.belongings.getItem(Wandmaker.Rotberry.Seed.class)) {
			if (item.quantity() > 1) {
				wandmaker.yell("good");
				Dungeon.level.drop(new AdamantWand(), wandmaker.pos).sprite.drop();
			}
		}

		if (Dungeon.isChallenged(Challenges.NO_SCROLLS)){
			GLog.h(Messages.get(Ghost.class, "challenge"));
		}

		if (item != null)
		item.detach(Dungeon.hero.belongings.backpack);

		if (item != null)
			item.detachAll(Dungeon.hero.belongings.backpack);

		reward.identify();
		if (reward.doPickUp(Dungeon.hero)) {
			GLog.i(Messages.get(Hero.class, "have"), reward.name());
		} else {
			Dungeon.level.drop(reward, wandmaker.pos).sprite.drop();
		}

		wandmaker.yell(Utils.format(TXT_FARAWELL, Dungeon.hero.givenName()));
		wandmaker.destroy();
		wandmaker.sprite.die();
		Wandmaker.Quest.complete();
	}
}
