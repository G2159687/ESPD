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
package com.github.epd.sprout.items.wands;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Strength;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.quest.DarkGold;
import com.github.epd.sprout.items.scrolls.ScrollOfUpgrade;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WandOfMagicMissile extends Wand {

	public static final String AC_DISENCHANT = Messages.get(WandOfMagicMissile.class, "ac_dis");

	private static final String TXT_SELECT_WAND = Messages.get(WandOfMagicMissile.class, "title");

	private static final String TXT_DISENCHANTED = Messages.get(WandOfMagicMissile.class, "dised");

	private static final float TIME_TO_DISENCHANT = 2f;

	private boolean disenchantEquipped;

	private float upgradeChance = 0.5f;

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.WAND_MAGIC_MISSILE;

		bones = false;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (level > 0) {
			actions.add(AC_DISENCHANT);
		}
		return actions;
	}

	@Override
	protected void onZap(Ballistica bolt) {

		Char ch = Actor.findChar(bolt.collisionPos);
		if (ch != null) {

			int level = level();
			int damage = Random.Int(level + 3, 6 + level * 2);
			if (Dungeon.hero.buff(Strength.class) != null) {
				damage *= (int) 4f;
				Buff.detach(Dungeon.hero, Strength.class);
			}
			ch.damage(damage, this);

			ch.sprite.burst(0xFFFFFFFF, level < 7 ? level / 2 + 2 : 5);
		}
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action.equals(AC_DISENCHANT)) {

			if (hero.belongings.weapon == this) {
				disenchantEquipped = true;
				hero.belongings.weapon = null;
				updateQuickslot();
			} else {
				disenchantEquipped = false;
				detach(hero.belongings.backpack);
			}

			curUser = hero;
			GameScene.selectItem(itemSelector, WndBag.Mode.WAND,
					TXT_SELECT_WAND);

		} else {

			super.execute(hero, action);

		}
	}

	@Override
	protected int initialCharges() {
		return 3;
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", 3 + level(), 6 + level() * 2);
	}

	private final WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect(Item item) {
			if (item != null) {

				Sample.INSTANCE.play(Assets.SND_EVOKE);
				ScrollOfUpgrade.upgrade(curUser);
				evoke(curUser);

				GLog.w(TXT_DISENCHANTED, item.name());

				Dungeon.quickslot.clearItem(WandOfMagicMissile.this);
				WandOfMagicMissile.this.updateQuickslot();

				DarkGold gold = Dungeon.hero.belongings.getItem(DarkGold.class);
				if (gold != null) {
					upgradeChance = (upgradeChance + (gold.quantity() * 0.01f));
				}

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
							GLog.w(Messages.get(WandOfMagicMissile.class, "notenough", item.name()));
							i = level;
						}
					}
					i++;
				}

				item.upgrade();
				curUser.spendAndNext(TIME_TO_DISENCHANT);

			} else {
				if (disenchantEquipped) {
					curUser.belongings.weapon = WandOfMagicMissile.this;
					WandOfMagicMissile.this.updateQuickslot();
				} else {
					collect(curUser.belongings.backpack);
				}
			}
		}
	};
}
