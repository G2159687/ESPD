
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.Challenges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Terror;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Gold;
import com.github.epd.sprout.items.consumables.Honeypot;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.artifacts.MasterThievesArmband;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.ThiefSprite;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Thief extends Mob {

	protected static final String TXT_STOLE = Messages.get(Thief.class, "stole");
	protected static final String TXT_CARRIES = Messages.get(Thief.class, "carries");
	protected static final String TXT_RATCHECK1 = "Spork is avail";
	protected static final String TXT_RATCHECK2 = "Spork is not avail";

	public Item item;

	{
		name = Messages.get(this, "name");
		spriteClass = ThiefSprite.class;

		HP = HT = 25 + (adj(0) * Random.NormalIntRange(3, 5));
		defenseSkill = 9 + adj(0);

		EXP = 5;

		loot = Generator.Category.BERRY;
		if (!Dungeon.isChallenged(Challenges.NO_HERBALISM)) {
			lootChance = 0.1f;
		} else {
			lootChance = 1f;
		}

		FLEEING = new Fleeing();

		properties.add(Property.UNDEAD);
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
		if (item != null) return (5 * super.speed()) / 6;
		else return super.speed();
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(1, 7 + adj(0));
	}

	@Override
	protected float attackDelay() {
		return 0.5f;
	}

	@Override
	public void die(Object cause) {

		super.die(cause);

		if (item != null) {
			Item.autocollect(item, pos);
		}

		if (!Dungeon.limitedDrops.armband.dropped() && Random.Float() < 0.05f){
			Dungeon.limitedDrops.armband.drop();
			Item.autocollect(new MasterThievesArmband(), pos);
		}
	}

	@Override
	protected Item createLoot() {
		if (!Dungeon.limitedDrops.armband.dropped()) {
			return super.createLoot();
		} else {
			if (!Dungeon.isChallenged(Challenges.NO_HERBALISM)) {
				return new Gold(Random.NormalIntRange(100, 250));
			} else {
				return new Gold(Random.NormalIntRange(1000, 2000));
			}
		}
	}

	@Override
	public int attackSkill(Char target) {
		return 12;
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

		Item item = hero.belongings.randomUnequipped();
		if (item != null && !item.unique && item.level < 1) {

			GLog.w(TXT_STOLE, this.name, item.name());
			Dungeon.quickslot.clearItem(item);
			item.updateQuickslot();

			if (item instanceof Honeypot) {
				this.item = ((Honeypot) item).shatter(this, this.pos);
				item.detach(hero.belongings.backpack);
			} else {
				this.item = item;
				if (item instanceof Honeypot.ShatteredPot)
					((Honeypot.ShatteredPot) item).setHolder(this);
				item.detachAll(hero.belongings.backpack);
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public String description() {
		String desc = Messages.get(this, "desc");

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
