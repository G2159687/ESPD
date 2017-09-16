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
package com.github.epd.sprout.items.weapon.melee;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.quest.DarkGold;
import com.github.epd.sprout.items.scrolls.ScrollOfUpgrade;
import com.github.epd.sprout.items.weapon.missiles.Boomerang;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ShortSword extends MeleeWeapon {

	public static final String AC_REFORGE = Messages.get(ShortSword.class, "ac_reforge");

	private static final String TXT_SELECT_WEAPON = Messages.get(ShortSword.class, "title");

	private static final String TXT_REFORGED = Messages.get(ShortSword.class, "reforged");
	private static final String TXT_NOT_BOOMERANG = Messages.get(ShortSword.class, "notboomerang");

	private static final float TIME_TO_REFORGE = 2f;

	private boolean equipped;

	private float upgradeChance = 0.5f;

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.SHORT_SWORD;

		bones = false;
	}

	public ShortSword() {
		super(1, 1f, 1f);

		STR = 11;
		MAX = 12;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (level > 0) {
			actions.add(AC_REFORGE);
		}
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action == AC_REFORGE) {

			if (hero.belongings.weapon == this) {
				equipped = true;
				hero.belongings.weapon = null;
			} else {
				equipped = false;
				detach(hero.belongings.backpack);
			}

			curUser = hero;

			GameScene.selectItem(itemSelector, WndBag.Mode.WEAPON,
					TXT_SELECT_WEAPON);

		} else {

			super.execute(hero, action);

		}
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	private final WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect(Item item) {
			DarkGold gold = Dungeon.hero.belongings.getItem(DarkGold.class);
			if (gold != null) {
				upgradeChance = (upgradeChance + (gold.quantity() * 0.01f));
			}
			if (item != null && !(item instanceof Boomerang)) {
				int i = 0;
				while (i < level) {
					if (i < 2) {
						Sample.INSTANCE.play(Assets.SND_EVOKE);
						ScrollOfUpgrade.upgrade(curUser);
						evoke(curUser);
						item.upgrade();
					} else if (Random.Float() < upgradeChance) {
						if (item.level < 15 || item.reinforced) {
							Sample.INSTANCE.play(Assets.SND_EVOKE);
							ScrollOfUpgrade.upgrade(curUser);
							evoke(curUser);
							item.upgrade();
							upgradeChance = Math.max(0.5f, upgradeChance - 0.1f);
						} else {
							GLog.w(Messages.get(ShortSword.class, "notenough"), item.name());
							i = level;
						}
					}
					i++;
				}

				curUser.spendAndNext(TIME_TO_REFORGE);

				GLog.w(TXT_REFORGED, item.name());

			} else {

				if (item instanceof Boomerang) {
					GLog.w(TXT_NOT_BOOMERANG);
				}

				if (equipped) {
					curUser.belongings.weapon = ShortSword.this;
				} else {
					collect(curUser.belongings.backpack);
				}
			}
		}
	};
}
