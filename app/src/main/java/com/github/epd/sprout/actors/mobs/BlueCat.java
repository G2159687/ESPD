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
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Terror;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.items.Amulet;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Gold;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.artifacts.MasterThievesArmband;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.ThiefSprite;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class BlueCat extends Mob {

	protected static final String TXT_STOLE = Messages.get(BlueCat.class,"1");
	protected static final String TXT_CARRIES = Messages.get(BlueCat.class,"2");
	protected static final String TXT_RATCHECK1 = Messages.get(BlueCat.class,"3");
	protected static final String TXT_RATCHECK2 = Messages.get(BlueCat.class,"4");

	public Item item;

	{
		name = Messages.get(this,"name");
		spriteClass = ThiefSprite.class;

		HP = HT = 20+(adj(0)*Random.NormalIntRange(3, 5));
		defenseSkill = 8+adj(0);

		EXP = 5;
		
		loot = new MasterThievesArmband().identify();
		lootChance = 0.01f;
		
		lootOther = Generator.Category.BERRY;
		lootChanceOther = 1f; // by default, see die()

		FLEEING = new Fleeing();
	}

	private static final String ITEM = "item";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(ITEM, item);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		item = (Item) bundle.get(ITEM);
	}

	@Override
	public float speed() {
		if (item != null) return (5*super.speed())/6;
		else return super.speed();
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(1, 7+adj(0));
	}

	@Override
	protected float attackDelay() {
		return 0.5f;
	}

	@Override
	public void die(Object cause) {
		
		super.die(cause);

		if (item != null) {
			Dungeon.level.drop(item, pos).sprite.drop();
		}
	}

	@Override
	protected Item createLoot() {
		if (!Dungeon.limitedDrops.armband.dropped()) {
			Dungeon.limitedDrops.armband.drop();
			return super.createLoot();
		} else
			return new Gold(Random.NormalIntRange(100, 250));
	}

	@Override
	public int attackSkill(Char target) {
		return 120;
	}

	@Override
	public int dr() {
		return 3;
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		if (item == null && enemy instanceof Hero && steal((Hero) enemy)) {
			state = FLEEING;
		}

		return damage;
	}

	@Override
	public int defenseProc(Char enemy, int damage) {
		if (state == FLEEING) {
			Dungeon.level.drop(new Gold(), pos).sprite.drop();
		}

		return damage;
	}

	protected boolean steal(Hero hero) {

		Amulet item = hero.belongings.getItem(Amulet.class);
		if (item != null) {
			
			GLog.w(TXT_STOLE, this.name, item.name());
			Dungeon.quickslot.clearItem( item );
			item.updateQuickslot();
					
			this.item = item;
			item.detachAll(hero.belongings.backpack);
			

			return true;
		} else {
			return false;
		}
	}

	@Override
	public String description() {
		String desc = Messages.get(this,"desc");

		if (item != null) {
			desc += String.format(TXT_CARRIES, Utils.capitalize(this.name),
					item.name());
		}

		return desc;
	}

	private class Fleeing extends Mob.Fleeing {
		@Override
		protected void nowhereToRun() {
			if (buff(Terror.class) == null) {
				sprite.showStatus(CharSprite.NEGATIVE, TXT_RAGE);
				state = HUNTING;
			} else {
				super.nowhereToRun();
			}
		}
	}
}
