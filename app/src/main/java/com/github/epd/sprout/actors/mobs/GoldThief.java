
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.Statistics;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Terror;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.items.teleporter.AncientCoin;
import com.github.epd.sprout.items.teleporter.CityKey;
import com.github.epd.sprout.items.Gold;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.artifacts.MasterThievesArmband;
import com.github.epd.sprout.items.weapon.missiles.Shuriken;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.GoldThiefSprite;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class GoldThief extends Mob {

	protected static final String TXT_STOLE = Messages.get(GoldThief.class, "steal");
	private static final String TXT_KILLCOUNT = Messages.get(GoldThief.class, "count");

	public Item item;

	{
		name = Messages.get(this, "name");
		spriteClass = GoldThiefSprite.class;

		HP = HT = 30 + Statistics.goldThievesKilled;
		defenseSkill = 26;

		EXP = 1;

		loot = new Shuriken(3);
		lootChance = 1f;

		//lootOther = new Cloudberry();
		//lootChanceOther = 0.1f; // by default, see die()

		FLEEING = new Fleeing();
	}

	private int goldtodrop = 0;

	private static final String GOLDTODROP = "goldtodrop";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(GOLDTODROP, goldtodrop);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		goldtodrop = bundle.getInt(GOLDTODROP);
	}

	@Override
	public float speed() {
		if (item != null) return (5 * super.speed()) / 6;
		else return super.speed();
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(30, 50);
	}

	@Override
	protected float attackDelay() {
		return 0.5f;
	}

	@Override
	public void die(Object cause) {

		if (Dungeon.depth < 27) {
			Item.autocollect(new CityKey(), pos);
			explodeDew(pos);
		} else {
			explodeDew(pos);
		}

		if (!Dungeon.limitedDrops.ancientcoin.dropped() && Statistics.goldThievesKilled > 30 && Random.Int(10) == 0) {
			Dungeon.limitedDrops.ancientcoin.drop();
			Item.autocollect(new AncientCoin(), pos);
		}

		if (!Dungeon.limitedDrops.ancientcoin.dropped() && Statistics.goldThievesKilled > 50) {
			Dungeon.limitedDrops.ancientcoin.drop();
			Item.autocollect(new AncientCoin(), pos);
		}

		if (!Dungeon.limitedDrops.armband.dropped() && Random.Float() < 0.05f && Statistics.goldThievesKilled > 50){
			Item.autocollect(new MasterThievesArmband().identify(), pos);
			Dungeon.limitedDrops.armband.drop();
		}

		if (!Dungeon.limitedDrops.armband.dropped() && Statistics.goldThievesKilled > 100){
			Item.autocollect(new MasterThievesArmband().identify(), pos);
			Dungeon.limitedDrops.armband.drop();
		}

		Statistics.goldThievesKilled++;
		GLog.h(TXT_KILLCOUNT, Statistics.goldThievesKilled);
		super.die(cause);

		if (item != null) {
			Item.autocollect(item, pos);
		}
	}

	// TODO: 为启用商店修改的情况添加更多种类的掉落物
	@Override
	protected Item createLoot() {
		Item item = new Gold(Random.NormalIntRange(goldtodrop + 50, goldtodrop + 100));
		if (ShatteredPixelDungeon.autocollect()) {
			Dungeon.gold += item.quantity;
			MasterThievesArmband.Thievery thievery = hero.buff(MasterThievesArmband.Thievery.class);
			if (thievery != null) thievery.collect(item.quantity);
		} else Dungeon.level.drop(item, Dungeon.hero.pos).sprite.drop();
		return item;
	}

	@Override
	public int attackSkill(Char target) {
		return 32;
	}

	@Override
	public int dr() {
		return 13 + Math.round((Statistics.goldThievesKilled + 1 / 10) + 1);
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


		Gold gold = new Gold(Random.Int(100, 500));
		if (gold.quantity() > 0) {
			goldtodrop = Math.min((gold.quantity() + 100), Dungeon.gold);
			Dungeon.gold -= goldtodrop;
			GLog.w(TXT_STOLE, this.name, gold.quantity());
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String description() {
		return Messages.get(this, "desc");
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
