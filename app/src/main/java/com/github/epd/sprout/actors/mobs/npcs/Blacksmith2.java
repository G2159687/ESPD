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
package com.github.epd.sprout.actors.mobs.npcs;

import com.github.epd.sprout.Challenges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.items.AdamantArmor;
import com.github.epd.sprout.items.AdamantRing;
import com.github.epd.sprout.items.AdamantWand;
import com.github.epd.sprout.items.AdamantWeapon;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.armor.Armor;
import com.github.epd.sprout.items.quest.DarkGold;
import com.github.epd.sprout.items.rings.Ring;
import com.github.epd.sprout.items.rings.RingOfHaste;
import com.github.epd.sprout.items.wands.Wand;
import com.github.epd.sprout.items.weapon.melee.MeleeWeapon;
import com.github.epd.sprout.items.weapon.missiles.Boomerang;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.BlacksmithSprite;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.windows.WndBlacksmith2;
import com.github.epd.sprout.windows.WndQuest;

public class Blacksmith2 extends NPC {

	{
		name = Messages.get(Blacksmith2.class, "name");
		spriteClass = BlacksmithSprite.class;

		properties.add(Property.IMMOVABLE);
	}


	@Override
	protected boolean act() {
		throwItem();
		return super.act();
	}

	@Override
	public boolean interact() {

		sprite.turnTo(pos, Dungeon.hero.pos);


		DarkGold gold = Dungeon.hero.belongings.getItem(DarkGold.class);
		if (!checkAdamant()) {
			tell(Messages.get(Blacksmith2.class, "two"));
		} else if (gold == null || gold.quantity() < (Dungeon.isChallenged(Challenges.NO_SCROLLS) ? 1 : 50)) {
			tell(Messages.get(Blacksmith2.class, "three"));
		} else if (checkAdamant() && gold != null && gold.quantity() > (Dungeon.isChallenged(Challenges.NO_SCROLLS) ? 1 : 49)) {
			GameScene.show(new WndBlacksmith2(this, Dungeon.hero));
		} else {
			tell(Messages.get(Blacksmith2.class, "two"));
		}
		return false;
	}

	public static String verify(Item item1, Item item2) {

		if (item1 == item2) {
			return Messages.get(Blacksmith.class, "sametwice");
		}

		if (!item1.isIdentified()) {
			return Messages.get(Blacksmith.class, "identify");
		}

		if (item1.cursed) {
			return Messages.get(Blacksmith.class, "cursed");
		}

		if (item1.reinforced) {
			return Messages.get(Blacksmith2.class, "reinforced");
		}

		if (item1.level < 0) {
			return Messages.get(Blacksmith.class, "junk");
		}

		if (!item1.isUpgradable() || item1 instanceof RingOfHaste) {
			return Messages.get(Blacksmith2.class, "cant");
		}

		if (item1 instanceof Armor && item2 instanceof AdamantArmor) {
			return null;
		}

		if (item1 instanceof MeleeWeapon && item2 instanceof AdamantWeapon) {
			return null;
		}

		if (item1 instanceof Boomerang && item2 instanceof AdamantWeapon) {
			return null;
		}

		if (item1 instanceof Wand && item2 instanceof AdamantWand) {
			return null;
		}

		if (item1 instanceof Ring && item2 instanceof AdamantRing) {
			return null;
		}

		return Messages.get(Blacksmith2.class, "nowork");

	}

	public static void upgrade(Item item1, Item item2) {

		item1.reinforced = true;
		if (!Dungeon.isChallenged(Challenges.NO_SCROLLS)) {
			item2.detach(Dungeon.hero.belongings.backpack);
		} else {
			GLog.h(Messages.get(Blacksmith2.class,"challenge"));
		}
		DarkGold gold = Dungeon.hero.belongings.getItem(DarkGold.class);
		if (gold != null && gold.quantity() > (Dungeon.isChallenged(Challenges.NO_SCROLLS) ? 1 : 49)) {
			gold.detach(Dungeon.hero.belongings.backpack, (Dungeon.isChallenged(Challenges.NO_SCROLLS) ? 1 : 50));
			if (!(Dungeon.hero.belongings.getItem(DarkGold.class).quantity() > 0)) {
				gold.detachAll(Dungeon.hero.belongings.backpack);
			}
		}
		GLog.p(Messages.get(Blacksmith2.class, "better"), item1.name());
		Dungeon.hero.spendAndNext(2f);
	}


	private void tell(String text) {
		GameScene.show(new WndQuest(this, text));
	}


	public static boolean checkAdamant() {
		AdamantArmor armor1 = Dungeon.hero.belongings.getItem(AdamantArmor.class);
		AdamantWeapon weapon1 = Dungeon.hero.belongings.getItem(AdamantWeapon.class);
		AdamantRing ring1 = Dungeon.hero.belongings.getItem(AdamantRing.class);
		AdamantWand wand1 = Dungeon.hero.belongings.getItem(AdamantWand.class);

		return armor1 != null || weapon1 != null || ring1 != null || wand1 != null;
	}


	@Override
	public int defenseSkill(Char enemy) {
		return 1000;
	}

	@Override
	public void damage(int dmg, Object src) {
	}

	@Override
	public void add(Buff buff) {
	}

	@Override
	public boolean reset() {
		return true;
	}

	@Override
	public String description() {
		return Messages.get(Blacksmith.class, "desc");
	}


}
